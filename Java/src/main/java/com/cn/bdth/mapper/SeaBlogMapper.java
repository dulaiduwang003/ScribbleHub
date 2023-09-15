package com.cn.bdth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cn.bdth.base.Page;
import com.cn.bdth.entity.SeaBlog;
import com.cn.bdth.vo.AdminBlogArticleVo;
import com.cn.bdth.vo.BlogRankClassifyVo;
import com.cn.bdth.vo.PopularArticlesVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 文章 db映射
 *
 * @author 时间海 @github dulaiduwang003
 * @version 1.0
 */
@Mapper
public interface SeaBlogMapper extends BaseMapper<SeaBlog> {

    /**
     * 带有图像列表的阅读量博客列表。
     *
     * @param ids the ids
     * @return the list
     */
    List<BlogRankClassifyVo> selectBlogListWithImage(@Param("ids") List<Long> ids);


    /**
     * 带有图像文章列表
     *
     * @return the list
     */
    List<AdminBlogArticleVo> selectBlogImageList();

    /**
     * 获取专题下文章数量
     *
     * @return the long
     */
    Long countBlogspotClassifyId(Long seaClassifyId);


    /**
     * 获取推荐文章
     *
     * @return the list
     */
    List<PopularArticlesVo> getPopularArticles();

    /**
     * 选择带有图像页面的博客列表。
     *
     * @param ids     the ids
     * @return the page
     */
    default Page<BlogRankClassifyVo> selectBlogListWithImagePage(List<Long> ids) {
        List<BlogRankClassifyVo> list = selectBlogListWithImage(ids);
        //这里可以额外插入一些参数
        return new Page<BlogRankClassifyVo>().setList(list);
    }
}
