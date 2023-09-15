package com.cn.bdth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cn.bdth.common.AiConfig;
import com.cn.bdth.common.FunCommon;
import com.cn.bdth.constants.AiConstant;
import com.cn.bdth.dto.InsertDrawingPictureTaskDto;
import com.cn.bdth.dto.InsertDrawingTextTaskDto;
import com.cn.bdth.dto.InsertPublicationCommentDto;
import com.cn.bdth.dto.InsertPublicationReplyDto;
import com.cn.bdth.entity.*;
import com.cn.bdth.enums.FileEnum;
import com.cn.bdth.exceptions.BlogsException;
import com.cn.bdth.exceptions.CommentException;
import com.cn.bdth.exceptions.ExceptionMessages;
import com.cn.bdth.mapper.*;
import com.cn.bdth.model.PictureDrawingModel;
import com.cn.bdth.model.UserInfoModel;
import com.cn.bdth.service.FunctionService;
import com.cn.bdth.structure.DrawingStructure;
import com.cn.bdth.utils.*;
import com.cn.bdth.vo.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * 功能服务impl
 * The type Function service.
 *
 * @author 时间海 @github dulaiduwang003
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class FunctionServiceImpl implements FunctionService {

    private final SeaUserMapper userMapper;

    private final UploadUtil uploadUtil;

    private final SeaResourceMapper resourceMapper;

    private final SeaCommentMapper commentMapper;

    private final SeaBlogMapper blogMapper;

    private final SeaReplyMapper replyMapper;

    private final SeaImageMapper imageMapper;

    private final ImageUtils imageUtils;

    private final RedisTemplate<String, Object> redisTemplate;

    private final FunCommon funCommon;

    private final WeChatUtils weChatUtils;

    /**
     * 查看指定图片
     *
     * @param seaImageId the sea image id
     * @return the specified picture
     */
    @Override
    public PaintingDetailVo getsSpecifiedPicture(final Long seaImageId) {
        //获取图片
        final SeaImage seaImage = imageMapper.selectById(seaImageId);
        if (seaImage == null) {
            return null;
        }
        final PaintingDetailVo vo = new PaintingDetailVo();
        //Ai图
        final String generateUri;
        //原图
        final String originalUri;
        //判断生成的图具体来源
        if (seaImage.getOriginalId() == null) {
            generateUri = getsDrawingResourceGraph(seaImage.getGenerateId());
            vo.setDrawingText(new PaintingDetailVo.DrawingText().setGenerateImage(generateUri));
        } else {
            //获取原图
            originalUri = getsDrawingResourceGraph(seaImage.getOriginalId());
            //获取Ai生成后的图
            generateUri = getsDrawingResourceGraph(seaImage.getGenerateId());
            vo.setDrawingImage(new PaintingDetailVo.DrawingImage().setGenerateImage(generateUri).setOriginalImage(originalUri));
        }
        final UserInfoModel userInfoModel = userMapper.selectUserInfoBySeaUserId(seaImage.getSeaUserId());
        //填充用户信息 具体是谁生成的?
        vo.setUserName(userInfoModel.getUserName()).setCreatedTime(seaImage.getCreatedTime()).setPrompt(seaImage.getPrompt()).setAvatar(userInfoModel.getAvatar());
        return vo;
    }

    /**
     * 根据图片ID获取资源URI
     *
     * @param seaImageId the sea image id
     * @return the drawing resource graph
     */
    private String getsDrawingResourceGraph(final long seaImageId) {
        return resourceMapper.selectOne(new QueryWrapper<SeaResource>()
                .lambda().eq(SeaResource::getSeaResourceId, seaImageId)
                .select(SeaResource::getUri)).getUri();
    }

    /**
     * 获取当前登录用户作品
     *
     * @return List<DrawingImageVo>
     */
    @Override
    public List<DrawingImageVo> getsCurrentUserOpus() {
        return imageMapper.getCurrentUserOpus(UserUtils.getCurrentLoginId());
    }

    /**
     * 获取公开(鉴赏)作品
     *
     * @return List<DrawingImageVo>
     */
    @Override
    public List<DrawingImageVo> getPublicDrawingOpus() {
        return imageMapper.getPublicOpus();
    }

    /**
     * 获取当前用户登录信息
     *
     * @return the current user info
     */
    @Override
    public UserInfoVo getCurrentUserInfo() {
        final Long currentLoginId = UserUtils.getCurrentLoginId();
        return BeanUtils.copyClassProperTies(
                //获取当前登录用户 头像 以及 权限
                userMapper.selectUserInfoBySeaUserId(currentLoginId), UserInfoVo.class).setType(UserUtils.getCurrentRole()
        );
    }

    /**
     * 发布绘图任务(图生图)
     *
     * @param dto the dto
     * @return Long id
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long publishDrawingImageTask(final InsertDrawingPictureTaskDto dto) {
        //获取绘图文件夹
        final String dec = FileEnum.PAINTING.getDec();
        //上传原图
        final Pair<String, String> pair = uploadUtil.uploadFile(dto.getImages(), dec);
        //写入这条资源
        final SeaResource seaResource = new SeaResource()
                .setType(dec)
                .setUri(pair.getRight())
                .setTarget(pair.getLeft());
        resourceMapper.insert(seaResource);
        final SeaImage seaImage = new SeaImage()
                .setPrompt(dto.getPrompt())
                .setSeaUserId(UserUtils.getCurrentLoginId())
                .setOriginalId(seaResource.getSeaResourceId());
        //初始化图片任务
        imageMapper.insert(seaImage);
        final PictureDrawingModel pictureDrawingModel = InsertDrawingPictureTaskDto.convertToPictureImgModel(dto);
        //将图片转为BASE64 仅支持 BASE64图生图
        final String imageToBase64 = imageUtils.convertImageToBase64(pair.getLeft());
        pictureDrawingModel
                .setOpenId(UserUtils.getCurrentOpenId())
                .setSeaImageId(seaImage.getSeaImageId())
                .setInit_images(List.of(imageToBase64));
        //提交任务到队列中
        redisTemplate.opsForList().leftPush(AiConstant.DRAWING_TASK_QUEUE, new DrawingStructure().setIsType(AiConstant.DRAWING_IMAGE_TYPE.intValue()).setPictureDrawingModel(pictureDrawingModel));
        return seaImage.getSeaImageId();
    }


    /**
     * 发布绘图任务 (文生图)
     *
     * @param dto the dto
     * @return the long
     */
    @Override
    public Long publishDrawingTextTask(final InsertDrawingTextTaskDto dto) {
        final SeaImage seaImage = new SeaImage()
                .setPrompt(dto.getPrompt())
                .setSeaUserId(UserUtils.getCurrentLoginId());
        //初始化图片任务
        imageMapper.insert(seaImage);

        final PictureDrawingModel model = BeanUtils.copyClassProperTies(dto, PictureDrawingModel.class);
        model.setOpenId(UserUtils.getCurrentOpenId());
        model.setSeaImageId(seaImage.getSeaImageId());
        //提交任务到队列中
        redisTemplate.opsForList().leftPush(AiConstant.DRAWING_TASK_QUEUE, new DrawingStructure().setIsType(AiConstant.DRAWING_TEXT_TYPE.intValue()).setPictureDrawingModel(model));
        return seaImage.getSeaImageId();
    }

    /**
     * 检查绘图是否成功
     *
     * @param seaImageId the sea image id
     * @return the boolean
     */
    @Override
    public boolean drawingWasSuccessful(final Long seaImageId) {
        return imageMapper.selectCount(new QueryWrapper<SeaImage>()
                .lambda()
                .eq(SeaImage::getSeaImageId, seaImageId)
                .eq(SeaImage::getSeaUserId, UserUtils.getCurrentLoginId())
                .isNotNull(SeaImage::getGenerateId)
        ) > 0;
    }

    /**
     * 上传头像
     * Upload file long.
     *
     * @param file the file
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void userUploadAvatar(final MultipartFile file) {
        final String dec = FileEnum.AVATAR.getDec();
        //上传头像
        final Pair<String, String> pari = uploadUtil.uploadFile(file, dec);
        //获取用户原有头像
        final Long currentLoginId = UserUtils.getCurrentLoginId();

        final UserInfoModel userInfoModel = userMapper.selectUserInfoBySeaUserId(currentLoginId);
        //新头像数据
        final SeaResource seaResource = new SeaResource()
                .setType(dec)
                .setUri(pari.getRight())
                .setTarget(pari.getLeft());
        resourceMapper.insert(seaResource);
        //修改用户头像
        userMapper.updateById(new SeaUser().setSeaResourceId(seaResource.getSeaResourceId()).setSeaUserId(currentLoginId));
        if (userInfoModel.getAvatar() != null) {
            //删除原有头像
            uploadUtil.deletedFile(userInfoModel.getTarget());
            resourceMapper.deleteById(userInfoModel.getSeaResourceId());
        }
    }


    /**
     * 检测绘图服务是否开启
     *
     * @return the boolean
     */
    @Override
    public boolean detectDrawingServiceIsTurnedOn() {
        final AiConfig aiConfig = funCommon.currentConfigurationServer();
        return NetUtils.checkUrlConnectivity(aiConfig.getSdUrl() + AiConstant.DRAWING_TEXT);
    }

    /**
     * 用户更新名称
     *
     * @param name the name
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void userUpdateNickName(final String name) {
        weChatUtils.filterText(name,UserUtils.getCurrentOpenId());
        //获取当前登录用户ID
        final Long currentLoginId = UserUtils.getCurrentLoginId();
        userMapper.updateById(new SeaUser()
                .setSeaUserId(currentLoginId)
                .setUserName(name)
        );
    }

    /**
     * 发表评论
     *
     * @param dto the dto
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void publicationComment(final InsertPublicationCommentDto dto) {
        weChatUtils.filterText(dto.getCommentContent(), UserUtils.getCurrentOpenId());
        if (blogMapper.selectCount(new QueryWrapper<SeaBlog>()
                .lambda()
                .eq(SeaBlog::getSeaBlogId, dto.getSeaBlogId())
        ) <= 0) {
            throw new BlogsException(ExceptionMessages.BLOG_NOT_EXIST);
        }
        final SeaComment seaComment = BeanUtils.copyClassProperTies(dto, SeaComment.class);
        //设置发表人信息
        seaComment.setSeaUserId(UserUtils.getCurrentLoginId());
        commentMapper.insert(seaComment);
    }

    /**
     * 发表回复
     *
     * @param dto the dto
     */
    @Override
    public void publicationReply(final InsertPublicationReplyDto dto) {
        weChatUtils.filterText(dto.getReplyContent(), UserUtils.getCurrentOpenId());
        final Long commentExist = commentMapper.selectCount(new QueryWrapper<SeaComment>()
                .lambda()
                .eq(SeaComment::getSeaCommentId, dto.getSeaCommentId())
        );
        if (commentExist <= 0) {
            throw new CommentException(ExceptionMessages.COMMENT_NOT_EXIST);
        }
        final SeaReply seaReply = BeanUtils.copyClassProperTies(dto, SeaReply.class);
        if (seaReply.getReciprocityId() != null) {
            final Long replyExist = replyMapper.selectCount(new QueryWrapper<SeaReply>()
                    .lambda()
                    .eq(SeaReply::getSeaReplyId, seaReply.getReciprocityId())
            );
            if (replyExist <= 0) {
                throw new CommentException(ExceptionMessages.REPLY_NOT_EXIST);
            }
        }

        replyMapper.insert(seaReply.setSeaUserId(UserUtils.getCurrentLoginId()));
    }

    /**
     * 获取评论
     *
     * @param seaBlogId the sea blog id
     * @param loginId   登录id
     * @return the comments based on blog id
     */
    @Override
    public List<BlogCommentVo> getCommentsBasedOnBlogId(final Long seaBlogId, final Long loginId) {
        //获取该文章下的所有评论
        return commentMapper.selectList(new QueryWrapper<SeaComment>()
                .lambda()
                .eq(SeaComment::getSeaBlogId, seaBlogId)
                .select(SeaComment::getCommentContent, SeaComment::getCreatedTime, SeaComment::getSeaCommentId, SeaComment::getSeaUserId)
        ).stream().parallel().map(c -> {
            final BlogCommentVo vo = BeanUtils.copyClassProperTies(c, BlogCommentVo.class);
            //开辟异步处理评论用户头像
            CompletableFuture<UserInfoModel> userInfoFuture = CompletableFuture.supplyAsync(() -> userMapper.selectUserInfoBySeaUserId(c.getSeaUserId()));
            try {
                UserInfoModel userInfoModel = userInfoFuture.get();
                vo.setUserName(userInfoModel.getUserName())
                        .setAvatar(userInfoModel.getAvatar());
            } catch (InterruptedException | ExecutionException e) {
                log.error("异步获取用户头像失败 原因:{} 位置:{}", e.getMessage(), e.getClass());
                e.printStackTrace();
            }
            //是否登录 如果用户登录了 并且这条评论属于他 则要显示可删除
            if (Objects.equals(c.getSeaUserId(), loginId)) {
                vo.setIsDeleted(true);
            }
            //计算该评论下 是否有回复数据
            if (replyMapper.selectCount(new QueryWrapper<SeaReply>().lambda().eq(SeaReply::getSeaCommentId, c.getSeaCommentId())) > 0) {
                vo.setIsSmall(true);
            }
            return vo;
        }).toList();
    }

    /**
     * 基于评论id获取回复列表
     *
     * @param seaCommentId the sea comment id
     * @param loginId      the login id
     * @return the reply based on comment id
     */
    @Override
    public List<BlogReplyVo> getReplyBasedOnCommentId(final Long seaCommentId, final Long loginId) {
        return replyMapper.selectList(new QueryWrapper<SeaReply>()
                        .lambda()
                        .eq(SeaReply::getSeaCommentId, seaCommentId)
                        .select(SeaReply::getSeaReplyId, SeaReply::getCreatedTime, SeaReply::getReciprocityId, SeaReply::getSeaUserId, SeaReply::getReplyContent)
                ).stream()
                .parallel()
                .map(r -> {
                    final BlogReplyVo blogReplyVo = BeanUtils.copyClassProperTies(r, BlogReplyVo.class);
                    // 是否登录 如果用户登录了 并且这条评论属于他 则要显示可删除
                    Optional.ofNullable(loginId)
                            .filter(id -> Objects.equals(r.getSeaUserId(), id))
                            .ifPresent(id -> blogReplyVo.setIsDeleted(true));
                    // 设置评论用户头像昵称数据
                    Optional.ofNullable(userMapper.selectUserInfoBySeaUserId(r.getSeaUserId()))
                            .ifPresent(userInfoModel -> blogReplyVo
                                    .setUserName(userInfoModel.getUserName())
                                    .setAvatar(userInfoModel.getAvatar()));
                    return blogReplyVo;
                }).toList();
    }

    /**
     * 删除评论
     *
     * @param seaCommentId the sea comment id
     */
    @Override
    public void deleteCommentsBasedOnId(final Long seaCommentId) {
        if (commentMapper.selectCount(new QueryWrapper<SeaComment>()
                .lambda().eq(SeaComment::getSeaCommentId, seaCommentId)
                .eq(SeaComment::getSeaUserId, UserUtils.getCurrentLoginId())
        ) <= 0) {
            throw new CommentException(ExceptionMessages.COMMENT_NOT_EXIST);
        }
        //删除评论和回复
        commentMapper.deleteById(seaCommentId);
        replyMapper.delete(new QueryWrapper<SeaReply>()
                .lambda().eq(SeaReply::getSeaCommentId, seaCommentId)
        );
    }

    /**
     * 删除回复
     *
     * @param seaReplyId the sea reply id
     */
    @Override
    public void deleteReplyBasedOnReplyId(final Long seaReplyId) {
        final Long isExist = replyMapper.selectCount(new QueryWrapper<SeaReply>()
                .lambda().eq(SeaReply::getSeaReplyId, seaReplyId)
                .eq(SeaReply::getSeaUserId, UserUtils.getCurrentLoginId())
        );
        if (isExist <= 0) {
            throw new CommentException(ExceptionMessages.REPLY_NOT_EXIST);
        }
        //删除被其他人回复的数据
        replyMapper.delete(new QueryWrapper<SeaReply>()
                .lambda().eq(SeaReply::getReciprocityId, seaReplyId)
        );
        replyMapper.deleteById(seaReplyId);

    }
}
