package com.yuban.shop.controller;

import com.yuban.shop.pojo.entity.ImgData;
import com.yuban.shop.pojo.entity.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.UUID;

@RestController
@RequestMapping("/upload")
@Tag(name = "文件上传接口", description = "图片上传、文件管理等相关接口")

public class UploadController {

    @Value("${upload.path}") // 从配置文件中读取上传路径
    private String uploadPath;
    @Operation(summary = "上传图片", description = "上传图片文件，支持用户头像和商品图片")
    @PostMapping("/images")
    public Result uploadimg(
            @Parameter(description = "图片文件", required = true) @RequestParam MultipartFile img,
            @Parameter(description = "图片类型：users(用户头像) 或 goods(商品图片)", required = true) @RequestParam String type){
        // 文件检测
        if (img.isEmpty()) {
            return Result.error("图片上传失败：文件为空");
        }


        // 文件类型检查
        String contentType = img.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            return Result.error("图片上传失败：文件类型不支持");
        }


        // 重命名
        String originalimgName = img.getOriginalFilename();
        if (originalimgName == null) {
            return Result.error("图片上传失败：文件名为空");
        }


        String uuid = UUID.randomUUID().toString().replace("-","");
        String fileName = uuid + StringUtils.cleanPath(originalimgName);

//        获取路径
        if (Objects.equals(type, "users") || Objects.equals(type, "goods")) {
            Path path = Paths.get(uploadPath +"\\images\\"+ type, fileName);
            try{
                img.transferTo(path.toFile());
            }catch (IOException e){
                e.printStackTrace();
            }
            return  Result.success(new ImgData(type+"/"+fileName,originalimgName,originalimgName));
        }
        else {
            return Result.error("图片保存错误:类型错误");
        }
    }
}
