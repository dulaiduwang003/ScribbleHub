package com.cn.bdth.service;

import com.cn.bdth.base.Page;
import com.cn.bdth.vo.*;

import java.util.List;

/**
 * 博客文章业务处理接口
 *
 * @author 时间海 @github dulaiduwang003
 * @version 1.0
 */
public interface BlogService {


    /**
     * 获取推荐博文（轮播图）
     *
     * @return the popular blog pages
     */
    List<PopularArticlesVo> getRecommendBlogs();


    /**
     * 按阅读量获取博客
     *
     * @param currentPage the current page
     * @return the blog read rank
     */
    Page<BlogRankClassifyVo> getBlogReadRank(final Long currentPage);


    /**
     * 根据类型渲染文章数据集
     *
     * @param isType the is type
     * @return the classified blog
     */
    List<BlogRankClassifyVo> getClassifyBlogOnType(final Long isType);


    /**
     * 根据专题id获取文章数据集合
     *
     * @param classifyId the classify id
     * @return the classified blog
     */
    BlogClassifyArticleVo getClassifyBlogOnId(final Long classifyId);


    /**
     * 根据ID获取文章 (查看文章)
     *
     * @param blogId the blog id
     * @return the blogs based on id
     */
    BlogArticleVo getBlogsBasedOnId(final Long blogId);


    /**
     * 根据关键词搜索文章
     *
     * @param keyWords the keywords
     * @return the list
     */
    List<BlogRankClassifyVo> searchForArticlesBasedOnKeywords(final String keyWords);


    /**
     * 随机获取每个类型下的前几篇文章
     *
     * @return the articles randomly
     */
    SearchRandomVo getArticlesRandomly();

}
