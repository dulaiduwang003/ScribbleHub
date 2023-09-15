package com.cn.bdth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cn.bdth.entity.SeaClassify;
import com.cn.bdth.model.ClassifyCoverModel;
import com.cn.bdth.vo.AdminBlogClassifyVo;
import com.cn.bdth.vo.ClassifyMarqueeVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * 专题映射
 *
 * @author 时间海 @github dulaiduwang003
 */
@Mapper
public interface SeaClassifyMapper extends BaseMapper<SeaClassify> {

    /**
     * 带有图像列表的博客列表。
     *
     * @param seaClassifyId the sea classifying id
     * @return the list
     */
    ClassifyCoverModel connectResourceBasedOnClassifyId(@Param("seaClassifyId") Long seaClassifyId);


    /**
     * 所有资源博客类集合
     *
     * @return the blog classifies admin vo
     */

    List<AdminBlogClassifyVo> connectResourceBasedAll();


    /**
     * 专题走马灯
     *
     * @return the blog classifies admin vo
     */
    List<ClassifyMarqueeVo> getClassifyMarqueeList();
}
