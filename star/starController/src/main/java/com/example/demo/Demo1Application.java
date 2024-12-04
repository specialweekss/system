package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication
public class Demo1Application {
    private static Process process;
	public static void main(String[] args) {
        // 注册关闭钩子
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            if (process != null && process.isAlive()) {
                process.destroy();
                System.out.println("Python script terminated on JVM shutdown.");
            }
        }));
        String dir = System.getProperty("user.dir");
		// Python 脚本的路径
		String pythonScriptPath = dir+"\\ali_model\\chat.py";
		// 构建命令
		String command = "py " + pythonScriptPath;
        try {
            process = Runtime.getRuntime().exec(command);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        SpringApplication.run(Demo1Application.class, args);
	}

}
