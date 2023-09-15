package com.cn.bdth.service;

import com.cn.bdth.dto.DeleteBlogArticleDto;
import com.cn.bdth.dto.InsertBlogArticleDto;
import com.cn.bdth.dto.DeleteClassifyDto;
import com.cn.bdth.dto.DeleteArticleContentResource;
import com.cn.bdth.vo.AdminBlogArticleVo;
import com.cn.bdth.vo.AdminDrawingVo;
import com.cn.bdth.vo.AdminBlogClassifyVo;
import com.cn.bdth.vo.UploadResourceVo;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 管理员业务接口
 *
 * @author 时间海 @github dulaiduwang003
 * @version 1.0
 */
public interface AdminService {


    /**
     * 上传博客内容图片
     *
     * @param file the file
     * @return the string
     */
    UploadResourceVo uploadArticleContentImage(final MultipartFile file);


    /**
     * 删除博客内容资源
     * Deleted resource.
     *
     * @param dto the dto
     */
    void deleteArticleContentImage(final DeleteArticleContentResource dto);


    /**
     * 获取所有文章列表
     *
     * @return the blog article list
     */
    List<AdminBlogArticleVo> getBlogArticleList();


    /**
     * 设置或者取消 推荐文章
     *
     * @param seaBlogId the sea blog id
     */
    void setPopularArticles(final Long seaBlogId);


    /**
     * 撰写文章
     *
     * @param dto the dto
     */
    void writeArticles(final InsertBlogArticleDto dto);


    /**
     * 根据id删除某一篇文章
     *
     * @param dto the dto
     */
    void deleteArticlesBasedOnId(final DeleteBlogArticleDto dto);


    /**
     * 根据id删除某一个专题
     *
     * @param dto the dto
     */
    void deleteClassifyBasedOnTopicId(final DeleteClassifyDto dto);


    /**
     * 获取所有专栏集
     *
     * @return the all classifying
     */
    List<AdminBlogClassifyVo> getAllClassifyList();


    /**
     * 获取所有绘图作品。
     *
     * @return the all drawings
     */
    List<AdminDrawingVo> getAllDrawings();


    /**
     * 设置公共绘图 获取取消公共绘图
     *
     * @param seaImageId the sea image id
     */
    void setPublicDrawing(final Long seaImageId);

}
