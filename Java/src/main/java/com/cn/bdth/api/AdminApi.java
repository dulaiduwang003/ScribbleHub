package com.cn.bdth.api;

import com.cn.bdth.common.FunCommon;
import com.cn.bdth.dto.*;
import com.cn.bdth.exceptions.BlogsException;
import com.cn.bdth.exceptions.UploadException;
import com.cn.bdth.msg.Result;
import com.cn.bdth.service.AdminService;
import com.cn.bdth.service.ClassifyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * 管理员权限接口
 *
 * @author 时间海 @github dulaiduwang003
 * @version 1.0
 */
@Slf4j
@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminApi {

    private final AdminService adminService;

    private final ClassifyService classifyService;

    private final FunCommon funCommon;


    /**
     * 添加一篇文章
     *
     * @param insertBlogArticleDto the dto
     * @return the result
     */
    @PostMapping(value = "/blog/insert/article", name = "撰写文章", consumes = "multipart/form-data")
    public Result writeArticles(@Valid final InsertBlogArticleDto insertBlogArticleDto) {
        try {
            adminService.writeArticles(insertBlogArticleDto);
            return Result.ok();
        } catch (UploadException e) {
            return Result.error(e.getMessage(), e.getCode());
        }
    }

    /**
     * 删除文章
     *
     * @param dto the dto
     * @return the result
     */
    @PostMapping(value = "/blog/delete/article", name = "删除文章", produces = MediaType.APPLICATION_JSON_VALUE)
    public Result deleteArticlesBasedOnId(@Validated @RequestBody final DeleteBlogArticleDto dto) {
        try {
            adminService.deleteArticlesBasedOnId(dto);
            return Result.ok();
        } catch (UploadException e) {
            return Result.error(e.getMessage(), e.getCode());
        }
    }


    /**
     * 新增专题
     *
     * @param dto the dto
     * @return the result
     */
    @PostMapping(value = "/blog/insert/classify", name = "新增专题", consumes = "multipart/form-data")
    public Result insertClassify(@Valid final InsertClassifyDto dto) {
        try {
            classifyService.insertClassify(dto);
            return Result.ok();
        } catch (UploadException e) {
            return Result.error(e.getMessage(), e.getCode());
        }
    }

    /**
     * 获取专题列表
     *
     * @return the classified list
     */
    @GetMapping(value = "/blog/drop/classify", name = "获取专栏列表", produces = MediaType.APPLICATION_JSON_VALUE)
    public Result getClassifyDrop() {
        return Result.data(classifyService.getClassifyDrop());
    }


    /**
     * 上传博客内容图片 (写博客时 内容带文件)
     *
     * @param file the file
     * @return the result
     */
    @PostMapping(value = "/blog/upload/resource", name = "上传博客内容资源", consumes = "multipart/form-data")
    public Result uploadArticleContentImage(final MultipartFile file) {
        try {
            return Result.data(adminService.uploadArticleContentImage(file));
        } catch (UploadException e) {
            return Result.error(e.getMessage());
        }
    }


    /**
     * 删除文章内容中的资源
     *
     * @param dto the dto
     * @return the result
     */
    @PostMapping(value = "/blog/delete/resource", name = "删除文章内容中的图片", produces = MediaType.APPLICATION_JSON_VALUE)
    public Result deleteArticleContentImage(@RequestBody @Validated final DeleteArticleContentResource dto) {
        try {
            adminService.deleteArticleContentImage(dto);
            return Result.ok();
        } catch (UploadException e) {
            e.printStackTrace();
            return Result.error(e.getMessage());
        }
    }

    /**
     * 获取所有文章列表
     *
     * @return the all articles
     */
    @GetMapping(value = "/blog/all/article", name = "获取所有文章列表", produces = MediaType.APPLICATION_JSON_VALUE)
    public Result getAllBlogArticles() {
        return Result.data(adminService.getBlogArticleList());
    }


    /**
     * 设置或者取消推荐文章
     *
     * @param dto the dto
     * @return the recommendations
     */
    @PostMapping(value = "/blog/popular/article", name = "设置或者取消推荐文章", produces = MediaType.APPLICATION_JSON_VALUE)
    public Result setPopularArticles(@RequestBody @Validated final PutPopularArticleDto dto) {
        try {
            adminService.setPopularArticles(dto.getSeaBlogId());
            return Result.ok();
        } catch (BlogsException e) {
            log.error("设置文章推荐状态失败 原因:{}", e.getMessage());
            return Result.error(e.getMessage());
        }

    }


    /**
     * 删除专栏
     *
     * @param dto the dto
     * @return the result
     */
    @PostMapping(value = "/blog/delete/classify", name = "删除专栏", produces = MediaType.APPLICATION_JSON_VALUE)
    public Result deleteColumnsBasedOnId(@RequestBody @Validated final DeleteClassifyDto dto) {
        try {
            adminService.deleteClassifyBasedOnTopicId(dto);
            return Result.ok();
        } catch (BlogsException | UploadException e) {
            log.error("删除专栏失败 原因:{} 位置:{}", e.getMessage(), e.getClass());
            return Result.error(e.getMessage());
        }

    }

    /**
     * 获取所有专栏 图表
     *
     * @return the all classifying
     */
    @GetMapping(value = "/blog/all/classify", name = "获取所有专栏列表", produces = MediaType.APPLICATION_JSON_VALUE)
    public Result classifyList() {
        return Result.data(adminService.getAllClassifyList());

    }

    /**
     * 获取所有传作
     *
     * @return Result
     */
    @GetMapping(value = "/picture/all/creation", name = "获取所有创作(图片)", produces = MediaType.APPLICATION_JSON_VALUE)
    public Result getAllDrawings() {
        return Result.data(adminService.getAllDrawings());
    }

    /**
     * 设置公开作品
     *
     * @return Result
     */
    @PostMapping(value = "/drawing/set/public", name = "设置公开作品(鉴赏)", produces = MediaType.APPLICATION_JSON_VALUE)
    public Result setPublicDrawing(@RequestBody @Validated final PutPublicDrawingDto dto) {
        try {
            adminService.setPublicDrawing(dto.getSeaImageId());
            return Result.ok();
        } catch (BlogsException e) {
            log.error("设置公开作品失败 原因:{} 位置:{}", e.getMessage(), e.getClass());
            return Result.error(e.getMessage());
        }


    }

    /**
     * 获取BOT参数
     *
     * @return the bot configuration
     */
    @GetMapping(value = "/server/get/config", name = "获取BOT配置参数", produces = MediaType.APPLICATION_JSON_VALUE)
    public Result getBotConfiguration() {
        return Result.data(funCommon.currentConfigurationServer());

    }


    /**
     * 更新BOT参数
     *
     * @return the bot configuration
     */
    @PostMapping(value = "/server/put/config", name = "用于保存或更新BOT参数", produces = MediaType.APPLICATION_JSON_VALUE)
    public Result updateBotConfiguration(@RequestBody @Validated final AiDispositionConfigDto dto) {
        funCommon.update(dto);
        return Result.ok();
    }


}
