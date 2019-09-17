package com.business.start.utils;

import com.business.start.domain.NewsImage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

@Slf4j
public class ImageUtils {

    /**
     * 图片下载
     *
     * @param url
     */
    public static void imageDownload(String url, String targetDir , String fileName) {
        try {
            String s = targetDir + fileName;
            mkDir(targetDir);
            File file = new File(s);
            if(!file.exists()) {
                file.createNewFile();
                System.out.println("创建文件成功");
            }
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<byte[]> rsp = restTemplate.getForEntity(url, byte[].class);
            Files.write(Paths.get(s), Objects.requireNonNull(rsp.getBody(), "未获取到下载文件"));
        } catch (IOException e) {
            log.error("图片下载异常", e);
        } catch (Exception e1){
            log.error("图片下载异常2", e1);
        }
    }

    public static NewsImage getImageInfoToUrl(String url){
        try {
            int i = url.indexOf("?");
            url = url.substring(0,i);
            int last = url.lastIndexOf("/");
            String imageName = url.substring(last, i);
            int suffix = imageName.indexOf(".");
            String suffixName = imageName.substring(suffix);
            NewsImage build = NewsImage.builder()
                    .iamgeOldName(imageName)
                    .iamgeSuffix(suffixName)
                    .build();
            return build ;
        } catch (Exception e) {
           log.error("解析图片url异常",e);
        }
        return null ;
    }

    public static void mkDir(String file){
        File dir = new File(file);
        if(!dir.exists()){
            dir.mkdirs();
        }
    }

    public static void main(String[] args) {
        mkDir("E:/image/2019/9/17/asf.jpg");
//        imageDownload("http://static.1sapp.com/lw/img/2019/09/17/02b25e60216bbf35efd8122735bef6a0.jpeg","E:/image/2019/9/17/") ;
    }

}
