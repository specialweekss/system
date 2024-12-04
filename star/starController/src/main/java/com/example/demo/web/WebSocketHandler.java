package com.example.demo.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class WebSocketHandler extends TextWebSocketHandler {

    private final Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessions.put(session.getId(), session);
        System.out.println("WebSocket connection established: " + session.getId());
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws IOException {
        String payload = message.getPayload();
        Message msg = objectMapper.readValue(payload, Message.class);
        System.out.println("Received message: " + payload);
        if("userId".equals(msg.getType())){
            String userId = msg.getFrom();
            // 更新用户会话映射
            sessions.remove(session.getId());
            sessions.put(userId, session);
            System.out.println("User " + userId + " connected.");
        }
        else if ("offer".equals(msg.getType()) || "answer".equals(msg.getType())) {
            WebSocketSession targetSession = sessions.get(msg.getTo());
            if (targetSession != null && targetSession.isOpen()) {
                targetSession.sendMessage(new TextMessage(objectMapper.writeValueAsString(msg)));
                System.out.println("Sent message to: " + msg.getTo());
            } else {
                WebSocketSession fromSession = sessions.get(msg.getFrom());
                fromSession.sendMessage(new TextMessage(objectMapper.writeValueAsString(msg)));
                System.out.println("Target session not found or closed: " + msg.getTo());
            }
        } else if ("candidate".equals(msg.getType())) {
            System.out.println(msg.getCandidate());
            WebSocketSession targetSession = sessions.get(msg.getTo());
            if (targetSession != null && targetSession.isOpen()) {
                targetSession.sendMessage(new TextMessage(objectMapper.writeValueAsString(msg)));
                System.out.println("Sent candidate to: " + msg.getTo());
            } else {
                System.out.println("Target session not found or closed: " + msg.getTo());
            }
        }else if("cancel".equals(msg.getType())){
            WebSocketSession targetSession = sessions.get(msg.getTo());
            if (targetSession != null && targetSession.isOpen()) {
                targetSession.sendMessage(new TextMessage(objectMapper.writeValueAsString(msg)));
                System.out.println("Sent cancel to: " + msg.getTo());
            } else {
                System.out.println("Target session not found or closed: " + msg.getTo());
            }
        }
        else {
            System.out.println("Unknown message type: " + msg.getType());
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        sessions.remove(session.getId());
        System.out.println("WebSocket connection closed: " + session.getId());
    }

    // 消息类
    @Data
    public static class Message {
        private String to;
        private String type;
        private String sdp;
        private String from;
        private String candidate;
    }
}