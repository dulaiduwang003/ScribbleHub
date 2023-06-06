package com.cn.app.superbot.model;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class GptAlphaModel {


    /**
     * The Prompt.
     */
    private String prompt;

    /**
     * The Model.
     */
    private String model = "image-alpha-001";


    /**
     * The Size.
     */
    private String size = "512x512";


    /**
     * The Num images.
     */
    private Integer num_images = 1;


    /**
     * The Response format.
     */
    private String response_format = "url";
}
