package com.yuban.Controller;

import com.yuban.pojo.ImgData;
import com.yuban.pojo.Result;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/upload")

public class UploadController {

    @PostMapping("/images")
    public Result uploadimg(@RequestParam MultipartFile img){
//            文件检测

        System.out.println(img);
        if (img.isEmpty()){
            return Result.error("图片上传失败");
        }

//        重命名
        String originalimgName=img.getOriginalFilename();
        if (originalimgName==null)return Result.error("上传文件为空!");

        String uuid = UUID.randomUUID().toString().replace("-","");
        String fileName=uuid+originalimgName;

//        获取路径
        ApplicationHome applicationHome =new ApplicationHome(this.getClass());
        String temp2 =applicationHome.getDir().getParentFile().getParentFile().getAbsolutePath()+"\\src\\main\\resources\\datas\\images\\";
        String path = temp2+fileName;

        try{
            img.transferTo(new File(path));
        }catch (IOException e){
            e.printStackTrace();
        }
        path=path.replaceAll("\\\\","/");
        return  Result.success(new ImgData(fileName,originalimgName,fileName));
    }
}
