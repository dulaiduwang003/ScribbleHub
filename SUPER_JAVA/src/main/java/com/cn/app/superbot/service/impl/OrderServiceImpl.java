package com.cn.app.superbot.service.impl;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.AlipayConfig;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradePrecreateModel;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradePrecreateRequest;
import com.alipay.api.response.AlipayTradePrecreateResponse;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cn.app.superbot.comment.AlipayConfigSingleton;
import com.cn.app.superbot.comment.ChatComment;
import com.cn.app.superbot.comment.IdGenerator;
import com.cn.app.superbot.constants.MsgConstants;
import com.cn.app.superbot.constants.cache.LockCache;
import com.cn.app.superbot.entity.SuperOrder;
import com.cn.app.superbot.entity.SuperProduct;
import com.cn.app.superbot.entity.SuperUser;
import com.cn.app.superbot.exception.OrderException;
import com.cn.app.superbot.mapper.OrderMapper;
import com.cn.app.superbot.mapper.ProductMapper;
import com.cn.app.superbot.mapper.UserMapper;
import com.cn.app.superbot.queue.UnpaidOrderQueue;
import com.cn.app.superbot.service.OrderService;
import com.cn.app.superbot.structure.AlipayCacheStructure;
import com.cn.app.superbot.structure.OperateStructure;
import com.cn.app.superbot.utils.QRCodeGenerator;
import com.cn.app.superbot.utils.RedisLockHelper;
import com.cn.app.superbot.utils.RedisUtils;
import com.cn.app.superbot.utils.UserUtils;
import com.cn.app.superbot.vo.AdminOrderPageVo;
import com.cn.app.superbot.vo.AlipayPayCodeVo;
import com.cn.app.superbot.vo.UserOrderPageVo;
import com.cn.app.superbot.vo.UserPageVo;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * The type Order service.
 *
 * @author 欧渐风.
 * @email 2074055628 @qq.com.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {

    /**
     * The Redis lock helper.
     */
    private final RedisLockHelper lockHelper;


    /**
     * The Order mapper.
     */
    private final OrderMapper orderMapper;


    /**
     * The Unpaid order queue.
     */
    private final UnpaidOrderQueue unpaidOrderQueue;


    /**
     * The Product mapper.
     */
    private final ProductMapper productMapper;


    /**
     * The's generator.
     */
    private final IdGenerator idGenerator;


    /**
     * The User mapper.
     */
    private final UserMapper userMapper;


    /**
     * The Redis utils.
     */
    private final RedisUtils redisUtils;


    /**
     * Alipay qr code pay alipay pay code vo.
     *
     * @param productId the product id
     * @return the alipay pay code vo
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public AlipayPayCodeVo alipayQRCodePay(final Long productId) {
        final String timestamp = String.valueOf(System.currentTimeMillis());
        //当前登录用户ID
        final Long userId = UserUtils.userIdToLong();
        //锁前缀
        final String lockPrefix = LockCache.ORDER_USER + userId;
        //上锁
        final boolean lock = lockHelper.lock(lockPrefix, timestamp);
        try {
            if (!lock) {
                throw new RuntimeException(MsgConstants.PLACE_AN_ORDER_REPEATEDLY_ERR);
            }
             /*
                    判断用户是否下过同类型订单 防止用户刷订单
                    如果存在 则返回原有二维码
             */
            final String orderProduct = userId.toString() + productId;
            if (redisUtils.doesItExist(orderProduct)) {
                final AlipayCacheStructure cache = (AlipayCacheStructure) redisUtils.getValue(orderProduct);
                //生成BASE64图片给前端
                return new AlipayPayCodeVo()
                        .setId(cache.getId())
                        .setQrCode(QRCodeGenerator.generateQRCode(cache.getUrl()))
                        .setPrice(cache.getPrice())
                        .setName(cache.getName())
                        .setCreatedTime(cache.getCreatedTime());
            }
            //商品是否存在
            final SuperProduct superProduct = productMapper.selectOne(new QueryWrapper<SuperProduct>()
                    .lambda().eq(SuperProduct::getId, productId)
                    .select(SuperProduct::getFrequency, SuperProduct::getPrice, SuperProduct::getName)
            );

            if (superProduct == null) {
                throw new RuntimeException(MsgConstants.PRODUCT_NULL_ERR);
            }
            /*
            1. 生成ID ( 雪花算法生成 数字+英文 )
            2. 构建订单
            3. 判断用户是否存在未支付订单 防止用户刷接口
            4. 发送支付宝请求
             */
            final String orderNo = idGenerator.getOrderNo();
            final SuperOrder superOrder = new SuperOrder()
                    .setId(orderNo)
                    // 0 待支付 1已支付 2 已回收
                    .setState(0L)
                    .setProductPrice(superProduct.getPrice())
                    .setProductName(superProduct.getName())
                    .setProductId(productId)
                    .setFrequency(superProduct.getFrequency())
                    .setCreatedTime(LocalDateTime.now())
                    .setUserId(userId);
            orderMapper.insert(superOrder);
            //获取服务器运营配置 （支付宝）
            final AlipayConfig alipayConfig = AlipayConfigSingleton.INSTANCE.getAlipayConfig();
            //服务器内存中获取 运营配置
            final OperateStructure config = ChatComment.operateStructure;
            //支付宝APPID
            alipayConfig.setAppId(config.getAlipayAppid());
            //支付宝公钥
            alipayConfig.setAlipayPublicKey(config.getAlipayPublic());
            //支付宝私钥
            alipayConfig.setPrivateKey(config.getAlipayPrivate());
            //构建支付宝订单
            AlipayClient alipayClient = new DefaultAlipayClient(alipayConfig);
            //预构建请求
            AlipayTradePrecreateRequest request = new AlipayTradePrecreateRequest();
            AlipayTradePrecreateModel model = new AlipayTradePrecreateModel();
            //写入订单 这里直接用同DB订单
            model.setOutTradeNo(orderNo);
            //金额
            model.setTotalAmount(String.valueOf(superProduct.getPrice()));
            //商品名称
            model.setSubject(superProduct.getName());
            //5分钟过期
            model.setTimeoutExpress("5m");
            request.setBizModel(model);
            //支付宝回调地址
            request.setNotifyUrl(config.getAlipayCallbackUrl() + "/public/callback/order");
            AlipayTradePrecreateResponse response = alipayClient.execute(request);
            //是否构建成功？ 构建成功则 创建二维码
            if (response.isSuccess()) {
                final AlipayCacheStructure cache = new AlipayCacheStructure()
                        .setCreatedTime(superOrder.getCreatedTime())
                        .setName(superProduct.getName())
                        .setUrl(response.getQrCode())
                        .setPrice(superProduct.getPrice())
                        .setId(orderNo);
                //缓存订单数据
                redisUtils.setValueTimeout(userId.toString() + productId, cache, 300);
                //添加至 待支付 队列中
                unpaidOrderQueue.addOrder(orderNo);
                //生成BASE64图片给前端
                //返回base64编码支付二维码图片
                return new AlipayPayCodeVo()
                        .setId(cache.getId())
                        .setQrCode(QRCodeGenerator.generateQRCode(cache.getUrl()))
                        .setPrice(cache.getPrice())
                        .setName(cache.getName())
                        .setCreatedTime(cache.getCreatedTime());
            } else {
                log.error("创建订单失败 订单号:{}, 错误信息：{}", orderNo, response.getBody());
                throw new RuntimeException(MsgConstants.CREATED_ORDER_ERR);
            }
        } catch (Exception e) {
            throw new OrderException(e.getMessage(), 400);
        } finally {
            //释放
            lockHelper.unlock(lockPrefix, timestamp);
        }
    }

    /**
     * 处理支付宝业务回调
     * Alipay pullback.
     *
     * @param request the request
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String alipayPullback(final HttpServletRequest request) {
        Map<String, String> params = new HashMap<>();
        Map<String, String[]> requestParams = request.getParameterMap();
        for (String name : requestParams.keySet()) {
            String[] values = requestParams.get(name);
            StringBuilder valueStr = new StringBuilder();
            for (int i = 0; i < values.length; i++) {
                valueStr.append((i == values.length - 1) ? values[i] : values[i] + ",");
            }
            params.put(name, valueStr.toString());
        }
        // 调用SDK验证签名
        boolean signVerified;
        try {
            signVerified = AlipaySignature.rsaCheckV1(params, ChatComment.operateStructure.getAlipayPublic(), "UTF8", "RSA2");
        } catch (AlipayApiException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        // 验证成功
        if (signVerified) {
            // 交易状态
            String tradeStatus = request.getParameter("trade_status");
            // 支付成功
            if (tradeStatus.equals("TRADE_SUCCESS")) {
                final String outTradeNo = request.getParameter("out_trade_no");
                final SuperOrder superOrder = orderMapper.selectOne(new QueryWrapper<SuperOrder>()
                        .lambda()
                        .eq(SuperOrder::getId, outTradeNo)
                        .select(SuperOrder::getFrequency, SuperOrder::getUserId, SuperOrder::getProductId)
                );
                if (superOrder != null) {
                    orderMapper
                            .updateById(new SuperOrder()
                                    .setId(outTradeNo)
                                    //已支付
                                    .setState(1L)
                                    .setPayTime(LocalDateTime.now())
                            );
                    userMapper.update(null, new UpdateWrapper<SuperUser>()
                            .lambda()
                            .eq(SuperUser::getId, superOrder.getUserId())
                            .setSql("frequency = frequency +" + superOrder.getFrequency()));
                    redisUtils.delKey(superOrder.getUserId().toString() + superOrder.getProductId());
                }

                return "success";
            }
        } else {
            log.error("支付失败");
            return "fail";
        }
        return "fail";
    }

    /**
     * Payment status string.
     *
     * @param orderNo the order no
     * @return the string
     */
    @Override
    public String paymentStatus(final String orderNo) {
        final SuperOrder superOrder = orderMapper.selectOne(new QueryWrapper<SuperOrder>()
                .lambda()
                .eq(SuperOrder::getId, orderNo)
                .select(SuperOrder::getId, SuperOrder::getState)
        );
        if (superOrder != null) {
            if (superOrder.getState() == 0) {
                return MsgConstants.BE_PAID;
            } else if (superOrder.getState() == 1) {
                return MsgConstants.PAID;
            } else {
                return MsgConstants.IS_CLOSED;
            }
        } else {
            return MsgConstants.IS_CLOSED;
        }

    }

    /**
     * Gets order consume page.
     *
     * @param pageNum the page num
     * @return the order consumes page
     */
    @Override
    public IPage<UserOrderPageVo> getOrderConsumePage(final Integer pageNum) {
        final Long userId = UserUtils.userIdToLong();
        Page<SuperOrder> superOrderPage = orderMapper.selectPage(new Page<>(pageNum, 5), new QueryWrapper<SuperOrder>()
                .lambda().eq(SuperOrder::getUserId, userId)
                .select(
                        //订单号
                        SuperOrder::getId,
                        //订单创建时间
                        SuperOrder::getCreatedTime,
                        //订单状态
                        SuperOrder::getState,
                        //商品价格
                        SuperOrder::getProductPrice,
                        //商品名称
                        SuperOrder::getProductName
                ).orderByDesc(SuperOrder::getCreatedTime)
        );

        // 查询总金额
        Double totalAmount;
        try {
            totalAmount = (Double) orderMapper.selectObjs(
                    new QueryWrapper<SuperOrder>()
                            .eq("user_id", userId)
                            .eq("state", 1)
                            .select("sum(product_price)")
            ).stream().findFirst().orElseThrow();

        } catch (Exception e) {
            totalAmount = 0D;
        }
        // 转换分页数据
        // 将totalAmount转换为只保留2位小数的字符串
        DecimalFormat df = new DecimalFormat("#0.00");
        String formattedAmount = df.format(totalAmount);
        // 将字符串转换为Double类型
        Double totalProductPrice = Double.parseDouble(formattedAmount);
        return superOrderPage.convert(superOrder -> {
            UserOrderPageVo userOrderPageVo = new UserOrderPageVo();
            BeanUtils.copyProperties(superOrder, userOrderPageVo);
            return userOrderPageVo.setTotalAmount(totalProductPrice);
        });
    }

    /**
     * Queue order check.
     *
     * @param orderNo the order no
     */
    @Override
    public void queueOrderCheck(final String orderNo) {
        final SuperOrder superOrder = orderMapper.selectOne(new QueryWrapper<SuperOrder>()
                .lambda()
                .eq(SuperOrder::getId, orderNo)
                .eq(SuperOrder::getState, 1L)
                .select(SuperOrder::getId)
        );
        if (superOrder == null) {
            orderMapper.updateById(new SuperOrder()
                    .setId(orderNo)
                    .setState(2L)
                    .setReasonFailure(MsgConstants.ORDER_TIMEOUT_ERR)
            );
        }

    }

    /**
     * Gets orders.
     *
     * @param pageNum the page num
     * @return the orders
     */
    @Override
    public IPage<AdminOrderPageVo> getOrderPage(Integer pageNum) {
        Page<SuperOrder> superOrderPage = orderMapper.selectPage(new Page<>(pageNum, 5), new QueryWrapper<SuperOrder>()
                .lambda()
                .select(
                        //订单号
                        SuperOrder::getId,
                        //订单创建时间
                        SuperOrder::getCreatedTime,
                        SuperOrder::getReasonFailure,
                        SuperOrder::getPayTime,
                        //订单状态
                        SuperOrder::getState,
                        //商品价格
                        SuperOrder::getProductPrice,
                        //商品名称
                        SuperOrder::getProductName
                ).orderByDesc(SuperOrder::getCreatedTime)
        );
        // 查询总金额
        Double totalAmount;
        try {
            totalAmount = (Double) orderMapper.selectObjs(
                    new QueryWrapper<SuperOrder>()
                            .eq("state", 1)
                            .select("sum(product_price)")
            ).stream().findFirst().orElseThrow();

        } catch (Exception e) {
            totalAmount = 0D;
        }
        // 转换分页数据
        // 将totalAmount转换为只保留2位小数的字符串
        DecimalFormat df = new DecimalFormat("#0.00");
        String formattedAmount = df.format(totalAmount);
        // 将字符串转换为Double类型
        Double totalProductPrice = Double.parseDouble(formattedAmount);
        return superOrderPage.convert(superOrder -> {
            AdminOrderPageVo vo = new AdminOrderPageVo();
            BeanUtils.copyProperties(superOrder, vo);
            return vo.setTotalAmount(totalProductPrice);
        });
    }

    /**
     * Gets user page.
     *
     * @param pageNum the page num
     * @return the user page
     */
    @Override
    public IPage<UserPageVo> getUserPage(final Integer pageNum) {
        Page<SuperUser> superUserPage = userMapper.selectPage(new Page<>(pageNum, 5), new QueryWrapper<SuperUser>()
                .lambda()
                .select(
                        SuperUser::getId,
                        SuperUser::getCreatedTime,
                        SuperUser::getEmail,
                        SuperUser::getPassword
                ).orderByDesc(SuperUser::getCreatedTime)
        );
        return superUserPage.convert(s -> {
            UserPageVo vo = new UserPageVo();
            BeanUtils.copyProperties(s, vo);
            return vo;
        });
    }
}
