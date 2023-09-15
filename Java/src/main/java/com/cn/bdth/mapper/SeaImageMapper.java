package com.cn.bdth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cn.bdth.entity.SeaImage;
import com.cn.bdth.vo.AdminDrawingVo;
import com.cn.bdth.vo.DrawingImageVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * 绘图资源映射
 *
 * @author 时间海 @github dulaiduwang003
 * @version 1.0
 */
@Mapper
public interface SeaImageMapper extends BaseMapper<SeaImage> {

    /**
     * 选择当前用户作品
     *
     * @return {@link List}<{@link DrawingImageVo}>
     */
    List<DrawingImageVo> getCurrentUserOpus(@Param("seaUserId") final Long seaUserId);

    /**
     * 获取公开作品
     *
     * @return {@link List}<{@link AdminDrawingVo}>
     */
    List<DrawingImageVo> getPublicOpus();

    /**
     * 获取所有作品
     *
     * @return {@link List}<{@link AdminDrawingVo}>
     */
    List<AdminDrawingVo> getAllDrawingOpus();
}
