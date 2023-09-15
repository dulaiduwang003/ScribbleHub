package com.cn.bdth.service;


import com.cn.bdth.dto.InsertClassifyDto;
import com.cn.bdth.vo.BlogClassifyDropVo;
import com.cn.bdth.vo.ClassifyMarqueeVo;

import java.util.List;

/**
 * 专题业务处理接口
 *
 * @author 时间海 @github dulaiduwang003
 * @version 1.0
 */
public interface ClassifyService {

    /**
     * 新增专题
     *
     * @param insertClassifyDto to classify dto
     */
    void insertClassify(final InsertClassifyDto insertClassifyDto);


    /**
     * 获取专题
     *
     * @return the classified list
     */
    List<BlogClassifyDropVo> getClassifyDrop();


    /**
     * 获取所有专栏 (走马灯)
     *
     * @return the classify marquee list
     */
    List<ClassifyMarqueeVo> getClassifyMarqueeList();


}
