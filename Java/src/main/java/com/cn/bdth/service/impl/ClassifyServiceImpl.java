package com.cn.bdth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cn.bdth.dto.InsertClassifyDto;
import com.cn.bdth.entity.SeaClassify;
import com.cn.bdth.entity.SeaResource;
import com.cn.bdth.enums.FileEnum;
import com.cn.bdth.mapper.SeaBlogMapper;
import com.cn.bdth.mapper.SeaClassifyMapper;
import com.cn.bdth.mapper.SeaResourceMapper;
import com.cn.bdth.service.ClassifyService;
import com.cn.bdth.utils.UploadUtil;
import com.cn.bdth.vo.BlogClassifyDropVo;
import com.cn.bdth.vo.ClassifyMarqueeVo;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 专栏文章业务处理类
 *
 * @author 时间海 @github dulaiduwang003
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
public class ClassifyServiceImpl implements ClassifyService {

    private final SeaClassifyMapper classifyMapper;
    private final UploadUtil uploadUtil;
    private final SeaResourceMapper resourceMapper;
    private final SeaBlogMapper blogMapper;

    /**
     * 添加专栏.
     *
     * @param insertClassifyDto dto
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insertClassify(final InsertClassifyDto insertClassifyDto) {
        final String type = FileEnum.COVER.getDec();
        //上传文件
        final Pair<String, String> pair = uploadUtil.uploadFile(insertClassifyDto.getFile(), type);
        //写入 mdb
        final SeaResource seaResource =
                new SeaResource().setType(type).setUri(pair.getRight()).setTarget(pair.getLeft());
        resourceMapper.insert(seaResource);
        //写入 mdb
        classifyMapper.insert(new SeaClassify()
                .setIsType(insertClassifyDto.getIsType())
                .setClassifyName(insertClassifyDto.getClassifyName())
                .setSeaResourceId(seaResource.getSeaResourceId()));
    }


    /**
     * 获取专栏数据集
     *
     * @return the classified list
     */
    @Override
    public List<BlogClassifyDropVo> getClassifyDrop() {
        final List<SeaClassify> seaClassifies = classifyMapper.selectList(new QueryWrapper<SeaClassify>()
                .lambda().select(SeaClassify::getClassifyName, SeaClassify::getSeaClassifyId)
        );
        return com.cn.bdth.utils.BeanUtils.copyArrayProperTies(seaClassifies, BlogClassifyDropVo.class);
    }

    /**
     * 获取专题走马灯
     *
     * @return the classify marquee list
     */
    @Override
    public List<ClassifyMarqueeVo> getClassifyMarqueeList() {
        //获取所有专题
        final List<ClassifyMarqueeVo> classifyMarqueeVos = classifyMapper.getClassifyMarqueeList();
        classifyMarqueeVos.stream().parallel().forEach((c) -> {
            //回填文章数量
            c.setArticles(blogMapper.countBlogspotClassifyId(c.getSeaClassifyId()));
        });
        return classifyMarqueeVos;
    }
}
