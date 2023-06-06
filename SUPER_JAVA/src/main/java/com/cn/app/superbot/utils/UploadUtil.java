package com.cn.app.superbot.utils;


import com.cn.app.superbot.comment.ChatComment;
import com.cn.app.superbot.constants.MsgConstants;
import com.cn.app.superbot.exception.CustomException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.Base64;
import java.util.UUID;


/**
 * The type Upload util.
 *
 * @author bdth
 * @email 2074055628 @qq.om
 */
@Component
@SuppressWarnings("all")
public class UploadUtil {

    /**
     * The Path.
     */
    @Value(value = "${file.path}")
    private String path;


    /**
     * Upload string.
     *
     * @param file the file
     * @return the string
     */
    public String upload(MultipartFile file) {
        String fileDownloadUri = "";
        UUID uuid = UUID.randomUUID();
        String originalFileName = file.getOriginalFilename();
        String fileSuffix = originalFileName.substring(originalFileName.lastIndexOf('.'));
        File dest = new File(path + uuid + fileSuffix);
        if (!dest.getParentFile().exists()) {
            dest.getParentFile().mkdirs();
        }
        try {

            file.transferTo(dest);
            fileDownloadUri = ChatComment.operateStructure.getAlipayCallbackUrl() + "/upload/" + uuid + fileSuffix;
        } catch (Exception e) {
            throw new CustomException(MsgConstants.UPLOAD_ERR_IMAGE, 500);
        }

        return fileDownloadUri;
    }

    /**
     * To images url string.
     *
     * @param base64 the base 64
     * @return the string
     */
    public String toImagesUrl(final String base64) {
        System.out.println(base64);
        byte[] bytes = Base64.getDecoder().decode(base64);
        UUID uuid = UUID.randomUUID();
        String fileName = uuid.toString() + ".jpg";
        File folder = new File(path);
        if (!folder.exists()) {
            try {
                folder.mkdir(); // 使用 mkdir() 方法
            } catch (SecurityException e) {
                throw new CustomException(MsgConstants.UPLOAD_ERR_IMAGE, 500);
            }
        }

        File file = new File(path, fileName);
        if (!file.exists()) {
            try {
                file.createNewFile(); // 使用 createNewFile() 方法创建文件
            } catch (IOException e) {
                throw new CustomException(MsgConstants.UPLOAD_ERR_IMAGE, 500);
            }
        }
        try {
            OutputStream outputStream = new FileOutputStream(file);
            outputStream.write(bytes);
            outputStream.flush();
            outputStream.close();
            return  ChatComment.operateStructure.getAlipayCallbackUrl() + "/upload/" + fileName;
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomException(MsgConstants.UPLOAD_ERR_IMAGE, 500);
        }
    }


    /**
     * Download image string.
     *
     * @param imageUrl the image url
     * @return the string
     * @throws IOException the io exception
     */
    public String downloadImage(String imageUrl) throws IOException {
        URL url = new URL(imageUrl);
        URLConnection connection = url.openConnection();
        connection.setRequestProperty("User-Agent", "Mozilla/5.0");
        String contentType = connection.getContentType();
        String extension = contentType.substring(contentType.lastIndexOf("/") + 1);
        // 使用UUID更改图片名称
        String fileName = UUID.randomUUID().toString() + "." + extension;
        File destinationFile = new File(path, fileName);
        // 判断文件夹是否存在，如果不存在则创建文件夹
        if (!destinationFile.getParentFile().exists()) {
            destinationFile.getParentFile().mkdirs();
        }
        InputStream inputStream = connection.getInputStream();
        byte[] buffer = new byte[2048];
        int length;
        try (FileOutputStream outputStream = new FileOutputStream(destinationFile)) {
            while ((length = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, length);
            }
        }
        return  ChatComment.operateStructure.getAlipayCallbackUrl() + "/upload/" + fileName;
    }


}
