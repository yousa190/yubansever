package com.yuban.Controller;


import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.ibatis.jdbc.Null;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;
import java.net.URLEncoder;

@RestController
@RequestMapping("")
public class DownloadController {

    @RequestMapping("/images/{filename}")
    public void downloadimg(@PathVariable String filename, HttpServletRequest request,HttpServletResponse response){
        //        获取路径
        ApplicationHome applicationHome =new ApplicationHome(this.getClass());
        String temp2 =applicationHome.getDir().getParentFile().getParentFile().getAbsolutePath()+"\\src\\main\\resources\\datas\\images\\";


        try{
            writeF(response, new java.io.File(temp2+filename));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void writeF(HttpServletResponse res, java.io.File file){
        FileInputStream f=null;
        InputStream bf= null;
        try{
            f=new FileInputStream(file);
            bf=new BufferedInputStream(f);
            byte[] bytes=new byte[bf.available()];
            bf.read(bytes);
            bf.close();
            // 清空response
            res.reset();
            // 设置response的Header
            res.setCharacterEncoding("UTF-8");
            //Content-Disposition的作用：告知浏览器以何种方式显示响应返回的文件，用浏览器打开还是以附件的形式下载到本地保存
            //attachment表示以附件方式下载   inline表示在线打开   "Content-Disposition: inline; filename=文件名.mp3"
            // filename表示文件的默认名称，因为网络传输只支持URL编码的相关支付，因此需要将文件名URL编码后进行传输,前端收到后需要反编码才能获取到真正的名称
            res.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(file.getName(), "UTF-8"));
            // 告知浏览器文件的大小
            res.addHeader("Content-Length", "" + file.length());
            OutputStream outputStream = new BufferedOutputStream(res.getOutputStream());
            res.setContentType("application/octet-stream");
            outputStream.write(bytes);
            outputStream.flush();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
