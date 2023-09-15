package com.cn.bdth.api;

import com.cn.bdth.dto.*;
import com.cn.bdth.exceptions.CommentException;
import com.cn.bdth.exceptions.UploadException;
import com.cn.bdth.exceptions.ViolationsException;
import com.cn.bdth.msg.Result;
import com.cn.bdth.service.FunctionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * 小程序 (功能性接口) 非公开
 *
 * @author 时间海 @github dulaiduwang003
 * @version 1.0
 */
@Slf4j
@RestController
@RequestMapping("/function")
@RequiredArgsConstructor
public class FunctionApi {

    private final FunctionService functionService;

    /**
     * 查看指定图片
     */
    @GetMapping(value = "/drawing/get/picture", name = "根据图片id查看指定绘图", produces = MediaType.APPLICATION_JSON_VALUE)
    public Result viewDrawingAccordingToId(@RequestParam Long seaImageId) {
        return Result.data(
                functionService.getsSpecifiedPicture(seaImageId)
        );
    }


    /**
     * 发布图生图任务
     *
     * @param dto the dto
     * @return the result
     */
    @PostMapping(value = "/drawing/task/fig/picture", name = "根据用户上传的图片来进行二次创作", consumes = "multipart/form-data")
    public Result addDrawingImageTaskQueue(@Valid final InsertDrawingPictureTaskDto dto) {
        try {
            return Result.data(
                    functionService.publishDrawingImageTask(dto)
            );
        } catch (UploadException e) {
            log.error("创建图生图任务失败 原因:{}  位置:{}", e.getMessage(), e.getClass());
            return Result.error(e.getMessage());
        }
    }

    /**
     * 添加文字生图任务
     * Add task.
     *
     * @param dto the dto
     * @return the result
     */
    @PostMapping(value = "/drawing/task/text/picture", name = "根据用户描述来进行绘画传作", produces = MediaType.APPLICATION_JSON_VALUE)
    public Result addDrawingTextTaskQueue(@Validated @RequestBody final InsertDrawingTextTaskDto dto) {
        return Result.data(
                functionService.publishDrawingTextTask(dto)
        );
    }

    /**
     * 检查绘图结果
     *
     * @param seaImageId the sea image id
     * @return the result
     */
    @GetMapping(value = "/drawing/monitoring", name = "根据绘图ID检查绘图结果", produces = MediaType.APPLICATION_JSON_VALUE)
    public Result monitoringDrawingSuccess(@RequestParam final Long seaImageId) {
        return Result.data(functionService.drawingWasSuccessful(seaImageId));
    }


    /**
     * 检查绘图服务是否正常开启
     *
     * @return the result
     */
    @GetMapping(value = "/drawing/detect", name = "检查绘图服务是否正常开启", produces = MediaType.APPLICATION_JSON_VALUE)
    public Result detectDrawingServiceIsTurnedOn() {
        return Result.data(functionService.detectDrawingServiceIsTurnedOn());
    }


    /**
     * 获取当前登录用户的作品
     *
     * @return the current user opus
     */
    @GetMapping(value = "/drawing/user/opus", name = "获取当前登录用户作品集", produces = MediaType.APPLICATION_JSON_VALUE)
    public Result getsCurrentUserOpus() {
        return Result.data(functionService.getsCurrentUserOpus());
    }


    /**
     * 获取小程序优秀作品
     *
     * @return the current user opus
     */
    @GetMapping(value = "/drawing/public/opus", name = "获取小程序优秀作品集", produces = MediaType.APPLICATION_JSON_VALUE)
    public Result getsPublicOpus() {
        return Result.data(functionService.getPublicDrawingOpus());
    }


    /**
     * 获取当前登录用户信息
     *
     * @return the current user info
     */
    @GetMapping(value = "/user/current/info", name = "获取当前登录用户信息(头像,昵称....)", produces = MediaType.APPLICATION_JSON_VALUE)
    public Result getCurrentUserInfo() {
        return Result.data(functionService.getCurrentUserInfo());
    }


    /**
     * 用户更新头像
     *
     * @param avatar the avatar
     * @return the result
     */
    @PostMapping(value = "/user/upload/avatar", name = "用户更新自己的头像", consumes = "multipart/form-data")
    public Result userUploadAvatar(final MultipartFile avatar) {
        try {
            functionService.userUploadAvatar(avatar);
            return Result.ok();
        } catch (UploadException e) {
            log.error("更新头像失败 原因:{}  位置:{}", e.getMessage(), e.getClass());
            return Result.error(e.getMessage(), e.getCode());
        }
    }


    /**
     * 用户发表评论
     *
     * @param dto the dto
     * @return the result
     */
    @PostMapping(value = "/blog/publication/comment", name = "用户发表自己的见解", produces = MediaType.APPLICATION_JSON_VALUE)
    public Result publicationComment(@RequestBody @Validated final InsertPublicationCommentDto dto) {
        try {
            functionService.publicationComment(dto);
            return Result.ok();
        } catch (CommentException | ViolationsException e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 用户回复评论
     *
     * @param dto the dto
     * @return the result
     */
    @PostMapping(value = "/blog/publication/reply", name = "用户回复某条评论", produces = MediaType.APPLICATION_JSON_VALUE)
    public Result publicationReply(@RequestBody @Validated final InsertPublicationReplyDto dto) {
        try {
            functionService.publicationReply(dto);
            return Result.ok();
        } catch (CommentException | ViolationsException e) {
            return Result.error(e.getMessage());
        }
    }


    /**
     * 删除评论
     *
     * @param dto the dto
     * @return the result
     */
    @PostMapping(value = "/blog/delete/comment", name = "用户选择删除某条评论", produces = MediaType.APPLICATION_JSON_VALUE)
    public Result deleteComment(@RequestBody @Validated final DeleteCommentDto dto) {
        try {
            functionService.deleteCommentsBasedOnId(dto.getSeaCommentId());
            return Result.ok();
        } catch (CommentException e) {
            log.error("删除评论失败 原因:{}  位置:{}", e.getMessage(), e.getClass());
            return Result.error(e.getMessage(), e.getCode());
        }

    }


    /**
     * 用户删除回复
     *
     * @param dto the dto
     * @return the result
     */
    @PostMapping(value = "/blog/delete/reply", name = "用户选择删除某条回复", produces = MediaType.APPLICATION_JSON_VALUE)
    public Result deleteReply(@RequestBody @Validated final DeleteReplyDto dto) {
        try {
            functionService.deleteReplyBasedOnReplyId(dto.getSeaReplyId());
            return Result.ok();
        } catch (CommentException e) {
            log.error("删除回复失败 原因:{}  位置:{}", e.getMessage(), e.getClass());
            return Result.error(e.getMessage(), e.getCode());
        }

    }


    /**
     * 更新名称
     *
     * @param dto the dto
     * @return the result
     */
    @PostMapping(value = "/user/update/name", name = "用户更新自己的名称", produces = MediaType.APPLICATION_JSON_VALUE)
    public Result userUpdateNickName(@RequestBody @Validated final UpdateNickNameDto dto) {
        try {
            //修改昵称
            functionService.userUpdateNickName(dto.getUserName());
            return Result.ok();
        } catch (ViolationsException e) {
            return Result.error(e.getMessage());
        }
    }

}
