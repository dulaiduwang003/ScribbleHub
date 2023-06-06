package com.cn.app.superbot.api;

import com.cn.app.superbot.dto.CreationsDto;
import com.cn.app.superbot.dto.MappingDto;
import com.cn.app.superbot.exception.GenerateImagesException;
import com.cn.app.superbot.exception.InsufficientAmountException;
import com.cn.app.superbot.msg.Result;
import com.cn.app.superbot.service.FunctionalityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * The type Gpt ability api.
 *
 * @author bdth
 */
@Slf4j
@RestController
@RequestMapping("/function")
@RequiredArgsConstructor
public class FunctionApi {


    /**
     * The Function service.
     */
    private final FunctionalityService functionalityService;


    /**
     * Generate images result.
     *
     * @param dto the dto
     * @return the result
     */
    @PostMapping(value = "/generate/picture", name = "生成绘图", produces = MediaType.APPLICATION_JSON_VALUE)
    public Result generateImages(@RequestBody @Validated MappingDto dto) {
        try {
            return Result.data(functionalityService.generateImage(dto));
        } catch (GenerateImagesException | InsufficientAmountException e) {
            return Result.error(e.getMessage());
        }
    }


    /**
     * Inspired creations result.
     *
     * @param dto the dto
     * @return the result
     */
    @PostMapping(value = "/inspiration/created", name = "灵感创作GPT", produces = MediaType.APPLICATION_JSON_VALUE)
    public Result inspiredCreations(@RequestBody @Validated CreationsDto dto) {
        try {
            return Result.data(functionalityService.originality(dto));
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error(e.getMessage());
        }
    }

}
