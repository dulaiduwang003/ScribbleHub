package com.cn.bdth.utils;


import com.cn.bdth.exceptions.ExceptionMessages;
import com.cn.bdth.exceptions.UploadException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Base64;
import java.util.UUID;


/**
 * 文件上传
 *
 * @author 时间海 @github dulaiduwang003
 * @version 1.0
 */
@Component
@SuppressWarnings("all")
@Slf4j

public class UploadUtil {

    @Value(value = "${file.path}")
    private String path;

    public Pair<String, String> uploadFile(final MultipartFile file, final String path) {
        String uri = "";
        UUID uuid = UUID.randomUUID();
        String originalFileName = file.getOriginalFilename();
        String fileSuffix = originalFileName.substring(originalFileName.lastIndexOf('.'));
        //文件实际存储路径
        final String subpath = path + "/" + uuid + fileSuffix;
        File dest = new File(this.path + subpath);
        if (!dest.getParentFile().exists()) {
            //创建文件夹
            dest.getParentFile().mkdirs();
        }
        try {
            file.transferTo(dest);
        } catch (IOException e) {
            log.error("上传文件失败 原因:{} 位置类:{}",e.getMessage(),e.getClass());
            throw new UploadException(ExceptionMessages.UPLOAD_ERR);
        }
        uri = "/upload/" + uuid + fileSuffix;

        return Pair.of(subpath, uri);
    }


    public Pair<String, String> downloadImage(final String base64, String path) throws IOException {
        try {
            // 解码base64字符串为字节数组
            byte[] decodedBytes = Base64.getDecoder().decode(base64);
            // 生成随机的图片文件名
            String fileName = UUID.randomUUID().toString() + ".jpg";
            // 拼接文件路径
            String folderPath = this.path + path;
            File folder = new File(folderPath);
            if (!folder.exists()) {
                folder.mkdirs(); // 创建文件夹
            }
            String filePath = folderPath + "/" + fileName;
            // 将字节数组保存为图片文件
            Path _path = Path.of(filePath);
            Files.write(_path, decodedBytes, StandardOpenOption.CREATE);
            return Pair.of((path + "/" + fileName), ("/upload/" + fileName));
        } catch (Exception e) {
            throw new UploadException(ExceptionMessages.UPLOAD_DOWNLOAD);
        }
    }

    public void deletedFile(final String path) {
        File file = new File(this.path + path);
        // 判断是否为视频文件
        try {
            FileUtils.forceDelete(file);
        } catch (IOException e) {
            e.printStackTrace();
            throw new UploadException(ExceptionMessages.UPLOAD_DELETE);
        }
    }

    private boolean isVideoFile(File file) {
        String fileName = file.getName();
        String fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1);
        return fileExtension.equalsIgnoreCase("mp4");
    }

}
