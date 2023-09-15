package com.cn.bdth.service;

import com.cn.bdth.dto.InsertDrawingPictureTaskDto;
import com.cn.bdth.dto.InsertDrawingTextTaskDto;
import com.cn.bdth.dto.InsertPublicationCommentDto;
import com.cn.bdth.dto.InsertPublicationReplyDto;
import com.cn.bdth.vo.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 功能性业务处理接口
 *
 * @author 时间海 @github dulaiduwang003
 * @version 1.0
 */
public interface FunctionService {


    /**
     * 根据id获取指定图片数据
     *
     * @param seaImageId the sea image id
     * @return the specified picture
     */
    PaintingDetailVo getsSpecifiedPicture(final Long seaImageId);

    /**
     * 获取当前登录用户的作品集
     *
     * @return the current user opus
     */
    List<DrawingImageVo> getsCurrentUserOpus();


    /**
     * 获取当前小程序中 公开的作品
     *
     * @return List<DrawingImageVo>
     */
    List<DrawingImageVo> getPublicDrawingOpus();


    /**
     * 获取当前登录用户信息
     * Gets user info.
     *
     * @return the user info
     */
    UserInfoVo getCurrentUserInfo();


    /**
     * 遥测绘图是否成功
     *
     * @param seaImageId the sea image id
     * @return the boolean
     */
    boolean drawingWasSuccessful(final Long seaImageId);

    /**
     * 用户修改头像
     *
     * @param file the file
     */
    void userUploadAvatar(final MultipartFile file);


    /**
     * 遥测SD绘图服务是否正常开启
     *
     * @return the boolean
     */
    boolean detectDrawingServiceIsTurnedOn();


    /**
     * 用户修改昵称
     *
     * @param name the name
     */
    void userUpdateNickName(final String name);


    /**
     * 发表评论
     *
     * @param dto the dto
     */
    void publicationComment(final InsertPublicationCommentDto dto);


    /**
     * 回复评论
     *
     * @param dto the dto
     */
    void publicationReply(final InsertPublicationReplyDto dto);


    /**
     * 根据id获取评论表
     *
     * @param seaBlogId the sea blog id
     * @param loginId   the login id
     * @return the comments based on blog id
     */
    List<BlogCommentVo> getCommentsBasedOnBlogId(final Long seaBlogId, final Long loginId);


    /**
     * 获取回复数据
     *
     * @param seaCommentId the sea comment id
     * @param loginId      the login id
     * @return the reply based on comment id
     */
    List<BlogReplyVo> getReplyBasedOnCommentId(final Long seaCommentId, Long loginId);


    /**
     * 删除评论
     *
     * @param seaCommentId the sea comment id
     */
    void deleteCommentsBasedOnId(final Long seaCommentId);


    /**
     * 删除回复
     *
     * @param seaReplyId the sea reply id
     */
    void deleteReplyBasedOnReplyId(final Long seaReplyId);


    /**
     * 发布图生图任务到队列中
     *
     * @param dto the dto
     * @return the long
     */
    Long publishDrawingImageTask(final InsertDrawingPictureTaskDto dto);


    /**
     * 发布文生图任务到队列中
     *
     * @param dto the dto
     * @return the long
     */
    Long publishDrawingTextTask(final InsertDrawingTextTaskDto dto);
}
