package com.example.demo.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.demo.other.Result;
import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
public class AnswerController {
    @CrossOrigin
    @GetMapping("/getAnswer")
    public Result getAnswer(String back_ground,String question) throws IOException, InterruptedException {
        if(back_ground==null||question==null){
            return Result.fail("缺少参数");
        }
        // 创建HttpClient实例
        HttpClient client = HttpClient.newHttpClient();
        // 创建HttpRequest实例
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:5000/get_response?back_ground="+back_ground+"&question="+question))
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();
        // 发送请求并接收响应
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        JSONObject object = JSONObject.parseObject(response.body());
        String answer ;
        answer = object.getString("response");
        if(answer == null)
        {
            answer = object.getString("error");
        }
        Pattern pattern = Pattern.compile("(\\\\u(\\p{XDigit}{4}))");
        Matcher matcher = pattern.matcher(answer);
        char ch;
        while (matcher.find()) {
            ch = (char) Integer.parseInt(matcher.group(2), 16);
            answer = response.body().replace(matcher.group(1), ch + "");
        }
        return Result.success(answer);
    }
}
