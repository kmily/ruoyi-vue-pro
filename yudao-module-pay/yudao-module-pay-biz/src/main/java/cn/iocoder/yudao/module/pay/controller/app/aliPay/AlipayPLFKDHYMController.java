package cn.iocoder.yudao.module.pay.controller.app.aliPay;


import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.pay.core.client.impl.PayClientFactoryImpl;
import cn.iocoder.yudao.framework.pay.core.client.impl.alipay.AlipayFundBatchPayClient;
import cn.iocoder.yudao.framework.pay.core.enums.channel.PayChannelEnum;
import cn.iocoder.yudao.framework.security.core.LoginUser;
import cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils;
import cn.iocoder.yudao.module.pay.dal.dataobject.channel.PayChannelDO;
import cn.iocoder.yudao.module.pay.dal.dataobject.sign.SteamAlipayIsvDO;
import cn.iocoder.yudao.module.pay.dal.mysql.sign.SteamAlipayIsvMapper;
import cn.iocoder.yudao.module.pay.service.channel.PayChannelService;
import com.alipay.api.AlipayApiException;
import com.alipay.api.response.AlipayOpenInviteOrderCreateResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.Objects;


/**
 * 批量付款到户有密
 */

@RestController
@RequestMapping("/alipay/plfkdhym/")
public class AlipayPLFKDHYMController {

    /**
     * 支付渠道Service
     */
    @Resource
    private PayChannelService channelService;

    @Resource
    private SteamAlipayIsvMapper steamAlipayIsvMapper;


    private final PayClientFactoryImpl payClientFactory = new PayClientFactoryImpl();
    /**
     * 查询个人签约记录
     * @return
     */
    @GetMapping("/userQueryIsvSign")
    public CommonResult userQueryIsvSign(){
        List<SteamAlipayIsvDO> steamAlipayIsvDOS = steamAlipayIsvMapper.selectList(new LambdaQueryWrapperX<SteamAlipayIsvDO>()
                .eq(SteamAlipayIsvDO::getSystemUserId, SecurityFrameworkUtils.getLoginUserId())
                .isNotNull(SteamAlipayIsvDO::getAppAuthToken)
                .ne(SteamAlipayIsvDO::getAppAuthToken, "")
                .orderByDesc(SteamAlipayIsvDO::getId)
        );
        if(steamAlipayIsvDOS.size()>0){
            return CommonResult.success(steamAlipayIsvDOS.get(0));
        }else{
            return CommonResult.success(Collections.emptyList());
        }
    }
    /**
     *支付宝个ISV页面签约接口
     * 文档地址：https://opendocs.alipay.com/open/02r26e?pathHash=28766804
     * @return
     */
    @GetMapping("/isvPageSign")
    public CommonResult userAgreementPageSign(SteamAlipayIsvDO steamAlipayIsvDO) throws AlipayApiException, JsonProcessingException {
        LoginUser loginUser = SecurityFrameworkUtils.getLoginUser();
        if(Objects.isNull(loginUser)){
            return CommonResult.error(-1,"请重新登录");
        }
        // json序列化对象
        ObjectMapper objectMapper=new ObjectMapper();
        // 构建参数
        ObjectNode jsonNode =(ObjectNode)objectMapper.valueToTree(steamAlipayIsvDO);
        // 拿到支付配置信息
        PayChannelDO channel = channelService.getChannel(61L);
        // 支付渠道构建
        payClientFactory.createOrUpdatePayClient(channel.getId(), PayChannelEnum.ALIPAY_PLFKDYMZH.getCode(), channel.getConfig());
        // 构建公共支付对象
        AlipayFundBatchPayClient payClient = (AlipayFundBatchPayClient) payClientFactory.getPayClient(channel.getId());
        // 执行签约
        AlipayOpenInviteOrderCreateResponse response = payClient.isvPageSign(jsonNode);
        SteamAlipayIsvDO isvDO=null;
        // 执行成功
        if (response.isSuccess()){
            // 租户id
//            jsonNode.put("tenantId",SecurityFrameworkUtils.getLoginUserId());
            // 用户ID
            jsonNode.put("systemUserId",SecurityFrameworkUtils.getLoginUserId());
            // 用户类型
            jsonNode.put("systemUserType",SecurityFrameworkUtils.getLoginUser().getUserType());
            isvDO=objectMapper.convertValue(jsonNode, SteamAlipayIsvDO.class);
            // 将签约订单记录持久化到数据库
            steamAlipayIsvMapper.delete(new LambdaQueryWrapperX<SteamAlipayIsvDO>()
                    .eqIfPresent(SteamAlipayIsvDO::getSystemUserId,loginUser.getId())
                    .eqIfPresent(SteamAlipayIsvDO::getSystemUserType,loginUser.getUserType())
            );
            steamAlipayIsvMapper.insertOrUpdate(isvDO);
        }
        return CommonResult.success(response.getBody());
    }

    /**
     * ISV签约授权结果查询
     */
    @GetMapping("/alipayOpenInviteOrderQuery")
    public CommonResult alipayOpenInviteOrderQuery(@RequestParam String isvBizId) throws AlipayApiException, JsonProcessingException {
        // 拿到支付配置信息
        PayChannelDO channel = channelService.getChannel(61L);
        // 支付渠道构建
        payClientFactory.createOrUpdatePayClient(channel.getId(), PayChannelEnum.ALIPAY_PLFKDYMZH.getCode(), channel.getConfig());
        // 构建公共支付对象
        AlipayFundBatchPayClient payClient = (AlipayFundBatchPayClient) payClientFactory.getPayClient(channel.getId());
        // 查询签约状态
        return payClient.alipayOpenInviteOrderQuery(isvBizId);
    }

    /**
     * ISV调用该接口创建批量付款单据
     */
    @GetMapping("/alipayFundBatchCreate")
    public CommonResult alipayFundBatchCreate(@RequestParam String isvBizId) throws AlipayApiException, JsonProcessingException {
        // 拿到支付配置信息
        PayChannelDO channel = channelService.getChannel(61L);
        // 支付渠道构建
        payClientFactory.createOrUpdatePayClient(channel.getId(), PayChannelEnum.ALIPAY_PLFKDYMZH.getCode(), channel.getConfig());
        // 构建公共支付对象
        AlipayFundBatchPayClient payClient = (AlipayFundBatchPayClient) payClientFactory.getPayClient(channel.getId());
        // 创建批量付款单据
        return payClient.alipayFundBatchCreate(isvBizId);
    }

    /**
     * ISV. 批次支付接口 PC支付
     */
    @GetMapping("/alipayFundTransPagePay")
    public CommonResult alipayFundTransPagePay(@RequestParam String orderId) throws AlipayApiException, JsonProcessingException {
        // 拿到支付配置信息
        PayChannelDO channel = channelService.getChannel(61L);
        // 支付渠道构建
        payClientFactory.createOrUpdatePayClient(channel.getId(), PayChannelEnum.ALIPAY_PLFKDYMZH.getCode(), channel.getConfig());
        // 构建公共支付对象
        AlipayFundBatchPayClient payClient = (AlipayFundBatchPayClient) payClientFactory.getPayClient(channel.getId());
        // 创建批量付款单据
        return payClient.alipayFundTransPagePay(orderId);
    }

    /**
     * 批次查询接口（alipay.fund.batch.detail.query）
     */
    @GetMapping("/alipayFundBatchDetailQuery")
    public CommonResult alipayFundBatchDetailQuery(@RequestParam String outBatchNo) throws AlipayApiException, JsonProcessingException {
        // 拿到支付配置信息
        PayChannelDO channel = channelService.getChannel(61L);
        // 支付渠道构建
        payClientFactory.createOrUpdatePayClient(channel.getId(), PayChannelEnum.ALIPAY_PLFKDYMZH.getCode(), channel.getConfig());
        // 构建公共支付对象
        AlipayFundBatchPayClient payClient = (AlipayFundBatchPayClient) payClientFactory.getPayClient(channel.getId());
        // 创建批量付款单据
        return payClient.alipayFundBatchDetailQuery(outBatchNo);
    }
}
