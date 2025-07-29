package com.yuban.shop.controller;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
public class DownloadController {

    @Value("${upload.path}") // 从配置文件中读取上传路径
    private String downloadPath;

    @RequestMapping("/images/{type}/{filename}")
    public void downloadimg(@PathVariable String type,@PathVariable String filename, HttpServletRequest request,HttpServletResponse response){
        // 获取文件路径
        Path filePath = Paths.get(downloadPath, "images", type, filename);
        File file = filePath.toFile();
        // 检查文件是否存在
        if (!file.exists()) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND); // 返回 404 错误
            return ;
        }

        // 下载文件
        try {
            writeF(response, file);
        } catch (Exception e) {
            log.error("文件下载失败", e); // 使用日志框架
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
