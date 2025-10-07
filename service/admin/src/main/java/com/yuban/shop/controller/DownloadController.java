package com.yubansever.controller;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;
import java.net.URLEncoder;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
@RestController
@RequestMapping("")
@Tag(name = "文件下载接口", description = "图片文件下载等相关接口")
public class DownloadController {

    @Value("${upload.path}") // 从配置文件中读取上传路径
    private String downloadPath;

    @Operation(summary = "下载图片文件", description = "根据类型和文件名下载对应的图片文件")
    @RequestMapping("/images/{type}/{filename}")
    public void downloadimg(
            @Parameter(description = "图片类型：users(用户头像) 或 goods(商品图片)", required = true) @PathVariable String type,
            @Parameter(description = "文件名", required = true) @PathVariable String filename, 
            HttpServletRequest request,
            HttpServletResponse response){
        log.info("开始下载图片，类型：{}，文件名：{}", type, filename);
        
        // 获取文件路径
        Path filePath = Paths.get(downloadPath, "images", type, filename);
        File file = filePath.toFile();
        // 检查文件是否存在
        if (!file.exists()) {
            log.warn("文件不存在，路径：{}", filePath);
            response.setStatus(HttpServletResponse.SC_NOT_FOUND); // 返回 404 错误
            return ;
        }

        // 下载文件
        try {
            writeF(response, file);
            log.info("文件下载成功，路径：{}", filePath);
        } catch (Exception e) {
            log.error("文件下载失败，路径：{}", filePath, e); // 使用日志框架
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR); // 返回 500 错误
        }
    }

    public void writeF(HttpServletResponse res, java.io.File file)throws IOException{
        // 设置响应头
        res.setCharacterEncoding("UTF-8");
        res.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(file.getName(), "UTF-8"));
        res.addHeader("Content-Length", "" + file.length());
        res.setContentType("application/octet-stream");

        // 分块读取和写入文件
        try (InputStream inputStream = new BufferedInputStream(new FileInputStream(file));
             OutputStream outputStream = new BufferedOutputStream(res.getOutputStream())) {
            byte[] buffer = new byte[1024 * 1024 *8]; // 8MB 缓冲区
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            outputStream.flush();
        }
    }

}