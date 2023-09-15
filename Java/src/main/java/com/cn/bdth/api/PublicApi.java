package com.cn.bdth.api;

import com.cn.bdth.msg.Result;
import com.cn.bdth.service.BlogService;
import com.cn.bdth.service.ClassifyService;
import com.cn.bdth.service.FunctionService;
import com.cn.bdth.utils.UserUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 小程序 (功能性接口) 公开
 *
 * @author 时间海 @github dulaiduwang003
 * @version 1.0
 */
@Slf4j
@RestController
@RequestMapping("/public")
@RequiredArgsConstructor
public class PublicApi {


    private final BlogService blogService;

    private final ClassifyService classifyService;

    private final FunctionService functionService;


    /**
     * 获取推荐文章
     *
     * @return the result
     */
    @GetMapping(value = "/blog/popular/article", name = "用于渲染首页热门文章", produces = MediaType.APPLICATION_JSON_VALUE)
    public Result getListOfPopularArticles() {
        return Result.data(blogService.getRecommendBlogs());
    }

    /**
     * 获取专栏走马灯(如 Springboot vue  等)
     *
     * @return the result
     */
    @GetMapping(value = "/blog/marquee/classify", name = "用于渲染首页专题走马灯", produces = MediaType.APPLICATION_JSON_VALUE)
    public Result getCollectionOfColumnMarquees() {
        return Result.data(classifyService.getClassifyMarqueeList());
    }


    /**
     * 首页文章分页功能
     *
     * @param currentPage the current page
     * @return the result
     */
    @GetMapping(value = "/blog/page/article/{currentPage}", name = "用于小程序用户手指滑动时分页处理", produces = MediaType.APPLICATION_JSON_VALUE)
    public Result blogPagination(@PathVariable final Long currentPage) {
        return Result.data(blogService.getBlogReadRank(currentPage));
    }


    /**
     * 获取基于主题类型的文章。
     *
     * @param isType the is type
     * @return the articles based on topic type
     */
    @GetMapping(value = "/blog/topic/article/{isType}", name = "根据类型获取文章数据集", produces = MediaType.APPLICATION_JSON_VALUE)
    public Result getArticlesBasedOnTopicType(@PathVariable final Long isType) {
        return Result.data(blogService.getClassifyBlogOnType(isType));
    }

    /**
     * 根据专题渲染文章列
     *
     * @param classifyId the classify id
     * @return the articles based on featured id
     */
    @GetMapping(value = "/blog/classify/article/{classifyId}", name = "根据专题主键渲染文章列", produces = MediaType.APPLICATION_JSON_VALUE)
    public Result getArticlesBasedOnFeaturedId(@PathVariable final Long classifyId) {
        return Result.data(blogService.getClassifyBlogOnId(classifyId));
    }


    /**
     * 获取指定文章
     *
     * @param blogId the blog id
     * @return the article based on id
     */
    @GetMapping(value = "/blog/view/article/{blogId}", name = "根据文章id获取文章(用户查看文章)", produces = MediaType.APPLICATION_JSON_VALUE)
    public Result getArticleBasedOnId(@PathVariable final Long blogId) {
        return Result.data(blogService.getBlogsBasedOnId(blogId));
    }

    /**
     * 获取博客下的评论
     *
     * @param blogId the blog id
     * @return the comments based on article id
     */
    @GetMapping(value = "/blog/article/comment/{blogId}", name = "根据文章id获取评论数据集", produces = MediaType.APPLICATION_JSON_VALUE)
    public Result getCommentsBasedOnArticleId(@PathVariable Long blogId) {
        return Result.data(functionService.getCommentsBasedOnBlogId(blogId, UserUtils.isLoginReturnId()));

    }

    /**
     * 获取评论下的回复数据
     *
     * @param commentId the comment id
     * @return the reply based on the comment id
     */
    @GetMapping(value = "/blog/comment/reply/{commentId}", name = "根据评论id获取回复数据集", produces = MediaType.APPLICATION_JSON_VALUE)
    public Result getReplyBasedOnTheCommentId(@PathVariable Long commentId) {
        return Result.data(functionService.getReplyBasedOnCommentId(commentId, UserUtils.isLoginReturnId()));

    }

    /**
     * 根据关键字搜索文章
     *
     * @param keyWord the key word
     * @return the result
     */
    @GetMapping(value = "/search/article", name = "搜索文章", produces = MediaType.APPLICATION_JSON_VALUE)
    public Result searchForArticlesBasedOnKeywords(final String keyWord) {
        return Result.data(blogService.searchForArticlesBasedOnKeywords(keyWord));
    }


    /**
     * 随机获取数据结果。
     *
     * @return the result
     */
    @GetMapping(value = "/search/random/article", name = "搜索页面随机数据集", produces = MediaType.APPLICATION_JSON_VALUE)
    public Result fetchDataRandomly() {
        return Result.data(blogService.getArticlesRandomly());
    }

}
