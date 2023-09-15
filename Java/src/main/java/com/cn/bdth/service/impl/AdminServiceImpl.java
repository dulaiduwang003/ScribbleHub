package com.cn.bdth.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cn.bdth.constants.BlogConstant;
import com.cn.bdth.dto.DeleteBlogArticleDto;
import com.cn.bdth.dto.InsertBlogArticleDto;
import com.cn.bdth.dto.DeleteClassifyDto;
import com.cn.bdth.dto.DeleteArticleContentResource;
import com.cn.bdth.entity.*;
import com.cn.bdth.enums.FileEnum;
import com.cn.bdth.exceptions.BlogsException;
import com.cn.bdth.exceptions.ExceptionMessages;
import com.cn.bdth.mapper.*;
import com.cn.bdth.service.AdminService;
import com.cn.bdth.utils.*;
import com.cn.bdth.vo.AdminDrawingVo;
import com.cn.bdth.vo.AdminBlogArticleVo;
import com.cn.bdth.vo.AdminBlogClassifyVo;
import com.cn.bdth.vo.UploadResourceVo;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 管理员业务
 *
 * @author 时间海 @github dulaiduwang003
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {


    private final SeaBlogMapper blogMapper;

    private final SeaResourceMapper resourceMapper;

    private final UploadUtil uploadUtil;

    private final SeaClassifyMapper classifyMapper;

    private final RedisUtils redisUtils;

    private final SeaCommentMapper commentMapper;

    private final SeaReplyMapper replyMapper;

    private final SeaImageMapper imageMapper;


    /**
     * 获取所有文章
     *
     * @return List<AdminBlogArticleVo> 文章视图
     */
    @Override
    public List<AdminBlogArticleVo> getBlogArticleList() {
        return blogMapper.selectBlogImageList();
    }

    /**
     * 上传博客内容图片
     * 用于在写文章时 内容附带的附件 视频或图片
     *
     * @param file the file 文件
     * @return the string 文件信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public UploadResourceVo uploadArticleContentImage(final MultipartFile file) {
        //存放于 BLOG文件夹
        final String dec = FileEnum.BLOG.getDec();
        //物理上传
        final Pair<String, String> stringStringPair = uploadUtil.uploadFile(file, dec);
        //写入资源表
        final SeaResource seaResource = new SeaResource()
                .setType(dec)
                .setUri(stringStringPair.getRight())
                .setTarget(stringStringPair.getLeft());
        resourceMapper.insert(seaResource);
        //返回上传后的数据
        return BeanUtils.copyClassProperTies(seaResource, UploadResourceVo.class);
    }

    /**
     * 删除资源（用户如果处理博客编辑时上传了资源 但是 中途未提交）
     *
     * @param dto the dto
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteArticleContentImage(final DeleteArticleContentResource dto) {
        //是否为单路由？ 如果 单路由则选 遍历删除资源 (存在于内容)
        if (dto.getIsUrl()) {
            //删除资源
            final SeaResource seaResource = resourceMapper.selectOne(
                    new QueryWrapper<SeaResource>()
                            .lambda()
                            .eq(SeaResource::getUri, dto.getUri())
                            .select(SeaResource::getTarget, SeaResource::getSeaResourceId)
            );
            //物理删除
            deleteSeaResource(seaResource);
        } else {
            //获取上传记录ID集
            final List<SeaResource> seaResourceList = resourceMapper.selectBatchIds(dto.getSeaResourceIdList());
            seaResourceList.parallelStream().forEach(this::deleteSeaResource);

        }
    }

    /**
     * 设置 推荐?不推荐 文章7
     *
     * @param seaBlogId the sea blog id
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void setPopularArticles(final Long seaBlogId) {
        final SeaBlog seaBlog = blogMapper.selectById(seaBlogId);
        //检查文章是否存在?
        Optional.ofNullable(seaBlog).ifPresentOrElse((b -> {
            //设置 推荐? 不推荐文章
            b.setIsRecommend(b.getIsRecommend() == 1 ? 0L : 1L);
            blogMapper.updateById(seaBlog);
        }), () -> {
            //博客不存在
            throw new BlogsException(ExceptionMessages.BLOG_NOT_EXIST);
        });

    }

    /**
     * 撰写文章
     *
     * @param dto the dto
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void writeArticles(final InsertBlogArticleDto dto) {
        //获取文件夹目录
        final String dec = FileEnum.BLOG.getDec();
        //上传资源到服务器
        final Pair<String, String> pair = uploadUtil.uploadFile(dto.getFile(), dec);
        final SeaResource seaResource = new SeaResource()
                .setType(dec)
                .setUri(pair.getRight())
                .setTarget(pair.getLeft());
        resourceMapper.insert(seaResource);
        final SeaBlog seaBlog = InsertBlogArticleDto.convertToSeaBlog(dto);
        //获取 这篇博客内容中 所使用的 资源ID
        final String fileResourceIds = dto.getFileResourceIds();
        if (StringUtils.isNotBlank(fileResourceIds)) {
            //转为LIST
            final List<Long> collect = Arrays.stream(fileResourceIds.split(","))
                    .map(Long::parseLong)
                    .collect(Collectors.toList());
            seaBlog.setFileResourceIds(JSON.toJSONString(collect));
        }
        //获取摘要 超出100字则 截取100字
        String summary = seaBlog.getSummary().substring(0, Math.min(seaBlog.getSummary().length(), 100));
        final Long seaResourceId = seaResource.getSeaResourceId();
        //上传
        seaBlog.setSummary(summary)
                .setSeaResourceId(seaResourceId)
                .setSeaUserId(UserUtils.getCurrentLoginId());
        blogMapper.insert(seaBlog);
        //设置阅读量 且 seaBLOG回填ID
        redisUtils.zsetIncrementScore(BlogConstant.READING, seaBlog.getSeaBlogId(), 0D);
    }

    /**
     * Deleted blog.
     * 删除文章
     *
     * @param dto the dto
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteArticlesBasedOnId(final DeleteBlogArticleDto dto) {
        final SeaBlog seaBlog = blogMapper.selectOne(new QueryWrapper<SeaBlog>()
                .lambda()
                .eq(SeaBlog::getSeaBlogId, dto.getSeaBlogId()));
        final Long seaResourceId = seaBlog.getSeaResourceId();
        final SeaResource seaResource = resourceMapper.selectById(seaResourceId);
        final String target = seaResource.getTarget();
        // 删除实际所在路径
        uploadUtil.deletedFile(target);
        // 删除文章封面
        resourceMapper.deleteById(seaResourceId);
        final String json = seaBlog.getFileResourceIds();
        //获取文章评论数据
        commentMapper
                .selectList(new QueryWrapper<SeaComment>()
                        .lambda()
                        .eq(SeaComment::getSeaBlogId, seaBlog.getSeaBlogId())
                        .select(SeaComment::getSeaCommentId)
                ).stream().parallel().forEach(c -> {
                    replyMapper.delete(new QueryWrapper<SeaReply>()
                            .lambda().eq(SeaReply::getSeaCommentId, c.getSeaCommentId())
                    );
                    commentMapper.deleteById(c.getSeaCommentId());
                });

        blogMapper.deleteById(dto.getSeaBlogId());
        //删除文章中的图片
        if (StringUtils.isNotBlank(json)) {
            //获取文章中所使用的图片
            final List<Long> fileResourceIds = JSON.parseObject(json, new TypeReference<>() {
            });
            final List<SeaResource> seaResources = resourceMapper.selectBatchIds(fileResourceIds);
            //删除物理文件
            for (SeaResource sa : seaResources) {
                uploadUtil.deletedFile(sa.getTarget());
            }
            //删除服务器资源
            resourceMapper.deleteBatchIds(fileResourceIds);
        }
        //清除阅读量
        redisUtils.zsetDel(BlogConstant.READING, seaBlog.getSeaBlogId());
    }

    /**
     * 删除资源
     * Delete sea resource.
     *
     * @param seaResource the sea resource
     */
    private void deleteSeaResource(final SeaResource seaResource) {
        //物理删除资源
        uploadUtil.deletedFile(seaResource.getTarget());
        //删除mysql db中的row数据
        resourceMapper.deleteById(seaResource.getSeaResourceId());
    }

    /**
     * 删除专栏
     *
     * @param dto the dto
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteClassifyBasedOnTopicId(final DeleteClassifyDto dto) {
        //查看专栏下是否还有文章
        if (blogMapper.selectCount(new QueryWrapper<SeaBlog>()
                .lambda()
                .eq(SeaBlog::getSeaClassifyId, dto.getSeaClassifyId())) > 0) {
            //不允许删除
            throw new BlogsException(ExceptionMessages.BLOG_EXIST);
        }
        //获取专栏信息
        final SeaClassify seaClassify = classifyMapper.selectById(dto.getSeaClassifyId());
        //获取资源坐标
        final SeaResource seaResource = resourceMapper.selectById(seaClassify.getSeaResourceId());
        //删除专栏
        classifyMapper.deleteById(dto.getSeaClassifyId());
        //物理删除 专题封面
        uploadUtil.deletedFile(seaResource.getTarget());
        //删除资源
        resourceMapper.deleteById(seaResource.getSeaResourceId());
    }

    /**
     * 获取所有专栏数据集
     *
     * @return the all classifying
     */
    @Override
    public List<AdminBlogClassifyVo> getAllClassifyList() {
        return classifyMapper.connectResourceBasedAll();
    }

    /**
     * 获取所有绘图数据集
     *
     * @return the all classifying
     */
    @Override
    public List<AdminDrawingVo> getAllDrawings() {
        return imageMapper.getAllDrawingOpus();
    }

    /**
     * 设置 公开作品 或 取消公开作品
     *
     * @param seaImageId id
     */
    @Override
    public void setPublicDrawing(final Long seaImageId) {
        final SeaImage seaImage = imageMapper.selectById(seaImageId);
        Optional.ofNullable(seaImage).ifPresentOrElse((b -> {
            //设置 推荐?未 推荐文章
            b.setIsPublic(b.getIsPublic() == 1 ? 0L : 1L);
            imageMapper.updateById(seaImage);
        }), () -> {
            throw new BlogsException(ExceptionMessages.IMAGE_NOT_EXIST);
        });
    }


}
