package com.example.demo.controller;

import com.example.demo.other.Result;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
public class AudioController {
    @CrossOrigin
    @PostMapping("/upload")
    public Result handleFileUpload(MultipartFile file) {
        try {
            byte[] bytes = file.getBytes();
            Path uploadPath = Paths.get("audio");

            // 检查并创建目标目录
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            Path filePath = uploadPath.resolve(file.getOriginalFilename());
            Files.write(filePath, bytes);
            return Result.success();
        } catch (IOException e) {
            e.printStackTrace();
            return Result.fail();
        }
    }
}
