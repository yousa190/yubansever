package com.yuban.Controller;

import com.yuban.pojo.ImgData;
import com.yuban.pojo.Result;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.UUID;

@RestController
@RequestMapping("/upload")

public class UploadController {

    @Value("${upload.path}") // 从配置文件中读取上传路径
    private String uploadPath;
    @PostMapping("/images")
    public Result uploadimg(@RequestParam MultipartFile img,@RequestParam String type){
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
