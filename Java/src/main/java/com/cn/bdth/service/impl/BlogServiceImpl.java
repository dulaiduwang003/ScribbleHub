package com.cn.bdth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cn.bdth.base.Page;
import com.cn.bdth.constants.BlogConstant;
import com.cn.bdth.entity.SeaBlog;
import com.cn.bdth.entity.SeaClassify;
import com.cn.bdth.entity.SeaResource;
import com.cn.bdth.mapper.SeaBlogMapper;
import com.cn.bdth.mapper.SeaClassifyMapper;
import com.cn.bdth.mapper.SeaResourceMapper;
import com.cn.bdth.mapper.SeaUserMapper;
import com.cn.bdth.model.ClassifyCoverModel;
import com.cn.bdth.model.UserInfoModel;
import com.cn.bdth.service.BlogService;
import com.cn.bdth.utils.BeanUtils;
import com.cn.bdth.utils.RedisUtils;
import com.cn.bdth.vo.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 博客文章业务处理类
 *
 * @author 时间海 @github dulaiduwang003
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
public class BlogServiceImpl implements BlogService {

    private final SeaBlogMapper blogMapper;

    private final RedisUtils redisUtils;

    private final SeaUserMapper userMapper;

    private final SeaClassifyMapper classifyMapper;

    private final SeaResourceMapper resourceMapper;

    /**
     * 获取博客 分页 (按阅读量自增)
     *
     * @param pageNum the page num
     * @return the blog read rank
     */
    @Override
    public Page<BlogRankClassifyVo> getBlogReadRank(final Long pageNum) {
        //获取博客排行榜
        final Set<Object> blogIds = redisUtils.zsetReverseRange(BlogConstant.READING, (pageNum - 1) * 10, pageNum * 10 - 1);
        // 如果没有博客ID，则返回空结果
        if (blogIds == null || blogIds.size() == 0) {
            return new Page<BlogRankClassifyVo>().setPageNum(pageNum);
        }
        // 将博客ID转换为Long类型
        final List<Long> ids = blogIds.stream().map(b -> Long.parseLong(b.toString())).toList();


        final Page<BlogRankClassifyVo> blogReadRankVoPage = blogMapper.selectBlogListWithImagePage(ids);
        //设置用户信息 以及阅读量
        List<CompletableFuture<BlogRankClassifyVo>> futures = blogReadRankVoPage.getList().stream()
                .map(b -> CompletableFuture.supplyAsync(() -> {
                    //回填文章用户信息
                    UserInfoModel userInfo = userMapper.selectUserInfoBySeaUserId(b.getSeaUserId());
                    b.setAvatar(userInfo.getAvatar()).setUserName(userInfo.getUserName());
                    //设置阅读量
                    Double source = redisUtils.zsetSetGetSource(BlogConstant.READING, b.getSeaBlogId());
                    b.setReading(source);
                    return b;
                })).toList();

        List<BlogRankClassifyVo> sortedBlogs = futures.stream()
                .map(CompletableFuture::join)
                //根据阅读量排序
                .sorted(Comparator.comparingDouble(BlogRankClassifyVo::getReading).reversed())
                .collect(Collectors.toList());

        blogReadRankVoPage.setList(sortedBlogs);
        return blogReadRankVoPage;
    }

    /**
     * 根据专栏id获取文章数据集
     *
     * @param classifyId the classify id
     * @return the classify blog on id
     */
    @Override
    public BlogClassifyArticleVo getClassifyBlogOnId(final Long classifyId) {
        final ClassifyCoverModel classifyCoverModel = classifyMapper.connectResourceBasedOnClassifyId(classifyId);
        if (classifyCoverModel == null) {
            return null;
        }
        final List<SeaBlog> seaBlogs = blogMapper.selectList(new QueryWrapper<SeaBlog>()
                .lambda().eq(SeaBlog::getSeaClassifyId, classifyId)
                .select(SeaBlog::getTitle,
                        SeaBlog::getSeaBlogId,
                        SeaBlog::getSeaUserId,
                        SeaBlog::getCreatedTime,
                        SeaBlog::getSummary,
                        SeaBlog::getSeaResourceId
                )
        );
        final BlogClassifyArticleVo vo = BeanUtils.copyClassProperTies(classifyCoverModel, BlogClassifyArticleVo.class);
        vo.setList(getBlogRankClassifyVoList(seaBlogs));
        return vo;
    }

    /**
     * 根据类型获取文章数据集
     *
     * @param isType the is type
     * @return the classified blog
     */
    @Override
    public List<BlogRankClassifyVo> getClassifyBlogOnType(final Long isType) {
        Set<Long> classifyIds = classifyMapper.selectList(new QueryWrapper<SeaClassify>()
                .lambda().eq(SeaClassify::getIsType, isType)
                .select(SeaClassify::getSeaClassifyId)
        ).stream().map(SeaClassify::getSeaClassifyId).collect(Collectors.toSet());
        if (classifyIds.isEmpty()) {
            return null;
        }
        //批量查询
        final List<SeaBlog> seaBlogs = blogMapper.selectList(new QueryWrapper<SeaBlog>()
                .lambda().in(SeaBlog::getSeaClassifyId, classifyIds)
                .select(SeaBlog::getTitle,
                        SeaBlog::getSeaBlogId,
                        SeaBlog::getSeaUserId,
                        SeaBlog::getCreatedTime,
                        SeaBlog::getSummary,
                        SeaBlog::getSeaResourceId
                )
        );
        return getBlogRankClassifyVoList(seaBlogs);
    }

    /**
     * 获取推荐文博客
     *
     * @return the popular blog list
     */
    @Override
    public List<PopularArticlesVo> getRecommendBlogs() {
        // 获取推荐博客 (轮播图)
        final List<PopularArticlesVo> popularArticlesVos = blogMapper.getPopularArticles();
        Map<Long, String> classifyNameMap = popularArticlesVos.stream()
                .map(PopularArticlesVo::getSeaClassifyId)
                .distinct()
                .collect(Collectors.toMap(
                        Function.identity(),
                        id -> classifyMapper.selectById(id).getClassifyName()
                ));
        popularArticlesVos.forEach(c -> c.setClassifyName(classifyNameMap.get(c.getSeaClassifyId())));
        return popularArticlesVos;
    }

    /**
     * 查看指定文章
     *
     * @param blogId the blog id
     * @return the blogs based on id
     */
    @Override
    public BlogArticleVo getBlogsBasedOnId(final Long blogId) {
        final SeaBlog seaBlog = blogMapper.selectOne(new QueryWrapper<SeaBlog>()
                .lambda().eq(SeaBlog::getSeaBlogId, blogId)
                .select(SeaBlog::getCreatedTime, SeaBlog::getContent, SeaBlog::getLabel, SeaBlog::getTitle, SeaBlog::getSeaClassifyId)
        );
        final BlogArticleVo blogArticleVo = BeanUtils.copyClassProperTies(seaBlog, BlogArticleVo.class);
        //获取专题
        final String classifyName = classifyMapper.selectOne(new QueryWrapper<SeaClassify>()
                .lambda().eq(SeaClassify::getSeaClassifyId, seaBlog.getSeaClassifyId())
                .select(SeaClassify::getClassifyName)
        ).getClassifyName();
        //获取数量
        final Long aLong = blogMapper.countBlogspotClassifyId(seaBlog.getSeaClassifyId());
        //阅读量自增
        redisUtils.zsetIncrementScore(BlogConstant.READING, blogId, 1D);
        return blogArticleVo.setClassifyName(classifyName)
                .setArticles(aLong);
    }

    /**
     * 装载用户头像数据
     *
     * @param seaBlogList the sea blog list
     * @return the blog rank classify vo list
     */
    private List<BlogRankClassifyVo> getBlogRankClassifyVoList(List<SeaBlog> seaBlogList) {
        return seaBlogList.parallelStream().map(b -> {
            final BlogRankClassifyVo vo = BeanUtils.copyClassProperTies(b, BlogRankClassifyVo.class);
            final String uri = resourceMapper.selectOne(new QueryWrapper<SeaResource>()
                    .lambda()
                    .eq(SeaResource::getSeaResourceId, b.getSeaResourceId())
                    .select(SeaResource::getUri)
            ).getUri();
            vo.setUri(uri);
            //填充用户头像
            final UserInfoModel userInfoModel = userMapper.selectUserInfoBySeaUserId(b.getSeaUserId());
            vo.setAvatar(userInfoModel.getAvatar())
                    .setUserName(userInfoModel.getUserName());
            //获取博客阅读量
            //设置阅读量
            Double source = redisUtils.zsetSetGetSource(BlogConstant.READING, b.getSeaBlogId());
            vo.setReading(source);
            return vo;
        }).collect(Collectors.toList());
    }

    /**
     * 搜索文章
     *
     * @param keyWords theEssential
     * @return the list
     */
    @Override
    public List<BlogRankClassifyVo> searchForArticlesBasedOnKeywords(final String keyWords) {
        final List<SeaBlog> seaBlogs = blogMapper.selectList(new QueryWrapper<SeaBlog>()
                .lambda()
                .like(SeaBlog::getTitle, keyWords)
                .or().like(SeaBlog::getSummary, keyWords)
                .or().like(SeaBlog::getLabel, keyWords)
                .select(SeaBlog::getTitle, SeaBlog::getSeaBlogId, SeaBlog::getSeaUserId, SeaBlog::getCreatedTime, SeaBlog::getSummary, SeaBlog::getSeaResourceId
                )
        );
        return getBlogRankClassifyVoList(seaBlogs);
    }


    /**
     * 随机读 4曲柄
     *
     * @return the articles randomly
     */
    @Override
    public SearchRandomVo getArticlesRandomly() {
        final SearchRandomVo vo = new SearchRandomVo();
        final Function<Integer, List<Long>> getSeaClassifyIds = isType -> classifyMapper.selectList(
                new QueryWrapper<SeaClassify>()
                        .lambda()
                        .eq(SeaClassify::getIsType, isType)
                        .select(SeaClassify::getSeaClassifyId)
        ).stream().map(SeaClassify::getSeaClassifyId).toList();

        final List<List<?>> seaBlogsList = Stream.of(0, 1, 2, 3)
                .parallel()
                .map(getSeaClassifyIds)
                .map(seaClassifyIds -> {
                    if (seaClassifyIds.isEmpty()) {
                        return new ArrayList<>();
                    } else {
                        return blogMapper.selectList(
                                new QueryWrapper<SeaBlog>()
                                        .in("sea_classify_id", seaClassifyIds)
                                        .orderByAsc("RAND()")
                                        .last("LIMIT 6")
                                        .select("sea_blog_id", "title")
                        );
                    }
                })
                .toList();
        vo.setFront(BeanUtils.copyArrayProperTies(seaBlogsList.get(0), SearchRandomVo.Collections.class));
        vo.setBackEnd(BeanUtils.copyArrayProperTies(seaBlogsList.get(1), SearchRandomVo.Collections.class));
        vo.setMiddleware(BeanUtils.copyArrayProperTies(seaBlogsList.get(2), SearchRandomVo.Collections.class));
        vo.setOther(BeanUtils.copyArrayProperTies(seaBlogsList.get(3), SearchRandomVo.Collections.class));

        return vo;
    }

}
