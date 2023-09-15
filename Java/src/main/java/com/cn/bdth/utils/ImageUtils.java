package com.cn.bdth.utils;

import com.cn.bdth.exceptions.ExceptionMessages;
import com.cn.bdth.exceptions.UploadException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.Base64;

/**
 * 图像工具类
 *
 * @author 时间海 @github dulaiduwang003
 * @version 1.0
 */
@SuppressWarnings("all")
@Component
public class ImageUtils {


    @Value("${file.path}")
    private String path;

    public String convertImageToBase64(final String imagePath) {

        try {
            BufferedImage image = ImageIO.read(new File(path + imagePath));
            ByteArrayOutputStream base = new ByteArrayOutputStream();
            ImageIO.write(image, getImageFormat(imagePath), base);
            byte[] imageBytes = base.toByteArray();
            String base64String = Base64.getEncoder().encodeToString(imageBytes);
            return "data:image/png;base64," + base64String;
        } catch (Exception e) {
            throw new UploadException(ExceptionMessages.UPLOAD_INVERT);
        }
    }

    private String getImageFormat(String imagePath) {
        String extension = imagePath.substring(imagePath.lastIndexOf(".") + 1);
        return extension.equalsIgnoreCase("jpg") ? "jpeg" : extension;
    }


}
