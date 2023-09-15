package com.cn.bdth.dto;

import com.cn.bdth.model.PictureDrawingModel;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.web.multipart.MultipartFile;

/**
 * 添加图生图
 *
 * @author 时间海 @github dulaiduwang003
 * @version 1.0
 */
@Data
@Accessors(chain = true)
@SuppressWarnings("all")
public class InsertDrawingPictureTaskDto {

    @NotNull(message = "上传图片不能为空")
    private MultipartFile images;

    @NotBlank(message = "提示词不能为空")
    private String prompt;

    @NotNull(message = "图片宽度不能为空")
    private Long width;

    @NotNull(message = "图片高度不能为空")
    private Long height;


    @NotNull(message = "人类特征不能为空")
    private Integer restore_faces;

    @NotNull(message = "随机性不能为空")
    private Integer seed;
    public static PictureDrawingModel convertToPictureImgModel(InsertDrawingPictureTaskDto item) {
        if (item == null) {
            return null;
        }
        PictureDrawingModel result = new PictureDrawingModel();

        result.setPrompt(item.getPrompt());
        result.setWidth(item.getWidth());
        result.setHeight(item.getHeight());
//        result.setSeed(item.getSeed());
//        result.setRestore_faces(item.getRestore_faces() == 0);

        return result;
    }


}
