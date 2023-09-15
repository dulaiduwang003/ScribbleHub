package com.cn.bdth.task;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cn.bdth.entity.SeaImage;
import com.cn.bdth.entity.SeaResource;
import com.cn.bdth.mapper.SeaImageMapper;
import com.cn.bdth.mapper.SeaResourceMapper;
import com.cn.bdth.utils.UploadUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;


/**
 * 绘图任务定时任务
 *
 * @author 时间海 @github dulaiduwang003
 * @version 1.0
 */
@Component
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class DrawingTaskTimer {

    private final SeaImageMapper imageMapper;

    private final SeaResourceMapper resourceMapper;

    private final UploadUtil uploadUtil;

    @Scheduled(cron = "0 0 0 * * ?")
    public void executeTask() {
        imageMapper.selectList(new QueryWrapper<SeaImage>()
                .lambda()
                .eq(SeaImage::getIsPublic, 0)
                .le(SeaImage::getCreatedTime, LocalDateTime.now().minusDays(1))
                .select(SeaImage::getSeaImageId)
        ).forEach(i -> {
            imageMapper.deleteById(i.getSeaImageId());
            deleteResourceAndImage(i.getGenerateId());
            deleteResourceAndImage(i.getOriginalId());
        });

    }

    private void deleteResourceAndImage(final Long resourceId) {
        if (resourceId != null) {
            final String target = resourceMapper.selectOne(new QueryWrapper<SeaResource>()
                    .lambda()
                    .eq(SeaResource::getSeaResourceId, resourceId)
                    .select(SeaResource::getTarget)
            ).getTarget();
            resourceMapper.deleteById(resourceId);
            uploadUtil.deletedFile(target);
        }
    }

}
