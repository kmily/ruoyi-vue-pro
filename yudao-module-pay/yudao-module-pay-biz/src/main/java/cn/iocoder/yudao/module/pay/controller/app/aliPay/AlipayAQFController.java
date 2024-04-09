package cn.iocoder.yudao.module.pay.controller.app.aliPay;


import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.pay.core.client.impl.PayClientFactoryImpl;
import cn.iocoder.yudao.framework.pay.core.client.impl.alipay.AlipaySafePayClient;
import cn.iocoder.yudao.framework.pay.core.enums.channel.PayChannelEnum;
import cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils;
import cn.iocoder.yudao.module.pay.dal.dataobject.channel.PayChannelDO;
import cn.iocoder.yudao.module.pay.dal.dataobject.order.SteamAlipayAqfJzbOrderDO;
import cn.iocoder.yudao.module.pay.dal.dataobject.order.SteamAlipayAqfTransferOrderDO;
import cn.iocoder.yudao.module.pay.dal.dataobject.sign.SteamAlipayAqfSignDO;
import cn.iocoder.yudao.module.pay.dal.mysql.order.SteamAlipayAqfJzbOrderMapper;
import cn.iocoder.yudao.module.pay.dal.mysql.order.SteamAlipayAqfTransferOrderDOMapper;
import cn.iocoder.yudao.module.pay.dal.mysql.sign.SteamAlipayAqfSignMapper;
import cn.iocoder.yudao.module.pay.service.channel.PayChannelService;
import com.alipay.api.AlipayApiException;
import com.alipay.api.response.*;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/alipay/aqf/")
public class AlipayAQFController {

    /**
     * 支付渠道Service
     */
    @Resource
    private PayChannelService channelService;

    @Resource
    SteamAlipayAqfSignMapper steamAlipayAqfSignMapper;

    @Resource
    SteamAlipayAqfJzbOrderMapper steamAlipayAqfJzbOrderMapper;

    @Resource
    SteamAlipayAqfTransferOrderDOMapper steamAlipayAqfTransferOrderDOMapper;

    private final PayClientFactoryImpl payClientFactory = new PayClientFactoryImpl();

    /**
     * 查询个人安全发签约成功的结果
     */
    @GetMapping("/userQuerySign")
    public CommonResult userQuerySign() throws AlipayApiException, JsonProcessingException, ParseException {
        SteamAlipayAqfSignDO steamAlipayAqfSignDO = steamAlipayAqfSignMapper.selectOne(new QueryWrapper<SteamAlipayAqfSignDO>()
                .eq("create_user_id", SecurityFrameworkUtils.getLoginUserId())
//                .eq("status", "NORMAL")
        );


        PayChannelDO channel = channelService.getChannel(58L);
        // 支付渠道构建
        payClientFactory.createOrUpdatePayClient(channel.getId(), PayChannelEnum.ALIPAY_AQF.getCode(), channel.getConfig());
        // 构建公共支付对象
        AlipaySafePayClient payClient = (AlipaySafePayClient) payClientFactory.getPayClient(channel.getId());
        // 执行签约
        AlipayUserAgreementQueryResponse response = payClient.alipayUserAgreementQuery(steamAlipayAqfSignDO.getExternalAgreementNo());
        if (response.isSuccess()){
            // json序列化对象
            ObjectMapper objectMapper=new ObjectMapper();
            // 获取Map
            Map map = objectMapper.readValue(response.getBody(), Map.class);
            // 签约成功的对象
            Map<String,String> steam =(Map<String,String>)map.get("alipay_user_agreement_query_response");
            // 在数据库中创建订单
            steamAlipayAqfSignMapper.update(new SteamAlipayAqfSignDO(
                            steam.get("status"),steam.get("sign_time"),steam.get("valid_time"),
                            steam.get("alipay_logon_id"),steam.get("invalid_time"),steam.get("agreement_no"))
                    ,new UpdateWrapper<SteamAlipayAqfSignDO>().eq("external_agreement_no",steamAlipayAqfSignDO.getExternalAgreementNo()));
            // 更新签约订单表数据
        }

        return CommonResult.success(steamAlipayAqfSignDO);
    }
    /**
     * 生成签约商户号方法
     * @return
     */
    private String generateContractNumber() {
        // 生成UUID作为签约号
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        // 取前32位作为签约号
        String contractNumber = uuid.substring(0, Math.min(uuid.length(), 32)).toUpperCase();
        return contractNumber;
    }

    private String getOrderTimeOut(){
        // 创建一个Date对象表示当前时间
        Date date = new Date();
        // 创建一个Calendar对象，并设置为当前时间
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        // 在Calendar对象中增加5分钟
        calendar.add(Calendar.MINUTE, 5);
        // 将Calendar对象转换回Date对象
        Date newDate = calendar.getTime();
        // 使用SimpleDateFormat格式化日期
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return sdf.format(newDate);
    }


    /**
     *付宝个人协议页面签约接口
     * 文档地址：https://opendocs.alipay.com/open/02r26e?pathHash=28766804
     * @return
     */
    @GetMapping("/userAgreementPageSign")
    @Transactional(rollbackFor = Exception.class)
    public Object userAgreementPageSign() throws AlipayApiException, JsonProcessingException {
        // 商户签约号
        String contractNumber = generateContractNumber();
        int userId = Objects.requireNonNull(SecurityFrameworkUtils.getLoginUserId()).intValue();
        // 在数据库中创建订单
        steamAlipayAqfSignMapper.delete(new QueryWrapper<SteamAlipayAqfSignDO>().eq("create_user_id",userId));
        steamAlipayAqfSignMapper.insertOrUpdate(
                new SteamAlipayAqfSignDO().setCreateUserId(userId).setExternalAgreementNo(contractNumber)
//                new SteamAlipayAqfSignDO(userId,userId,contractNumber)
        );
        // 拿到支付配置信息
        PayChannelDO channel = channelService.getChannel(58L);
        // 支付渠道构建
        payClientFactory.createOrUpdatePayClient(channel.getId(), PayChannelEnum.ALIPAY_AQF.getCode(), channel.getConfig());
        // 构建公共支付对象
        AlipaySafePayClient payClient = (AlipaySafePayClient) payClientFactory.getPayClient(channel.getId());
        // 执行签约
        return payClient.userAgreementPageSign(contractNumber);
    }


    /**
     * 支付宝个人代扣协议查询接口
     */
    @GetMapping("/alipayUserAgreementQuery")
    @Transactional(rollbackFor = Exception.class)
    public CommonResult  alipayUserAgreementQuery(@RequestParam String externalAgreementNo) throws AlipayApiException, JsonProcessingException, ParseException {
        // 拿到支付配置信息
        PayChannelDO channel = channelService.getChannel(58L);
        // 支付渠道构建
        payClientFactory.createOrUpdatePayClient(channel.getId(), PayChannelEnum.ALIPAY_AQF.getCode(), channel.getConfig());
        // 构建公共支付对象
        AlipaySafePayClient payClient = (AlipaySafePayClient) payClientFactory.getPayClient(channel.getId());
        // 执行签约
        AlipayUserAgreementQueryResponse response = payClient.alipayUserAgreementQuery(externalAgreementNo);
        if (response.isSuccess()){
            // json序列化对象
            ObjectMapper objectMapper=new ObjectMapper();
            // 获取Map
            Map map = objectMapper.readValue(response.getBody(), Map.class);
            // 签约成功的对象
            Map<String,String> steam =(Map<String,String>)map.get("alipay_user_agreement_query_response");
            // 在数据库中创建订单
            steamAlipayAqfSignMapper.update(new SteamAlipayAqfSignDO(
                    steam.get("status"),steam.get("sign_time"),steam.get("valid_time"),
                    steam.get("alipay_logon_id"),steam.get("invalid_time"),steam.get("agreement_no"))
                    ,new UpdateWrapper<SteamAlipayAqfSignDO>().eq("external_agreement_no",externalAgreementNo));
            // 更新签约订单表数据
        }
        return CommonResult.success(response);
    }

    /**
     * 协议解约接口
     */
    @GetMapping("/alipayUserAgreementUnsign")
    public CommonResult alipayUserAgreementUnsign(String externalAgreementNo) throws AlipayApiException, JsonProcessingException {
        // 拿到支付配置信息
        PayChannelDO channel = channelService.getChannel(58L);
        // 支付渠道构建
        payClientFactory.createOrUpdatePayClient(channel.getId(), PayChannelEnum.ALIPAY_AQF.getCode(), channel.getConfig());
        // 构建公共支付对象
        AlipaySafePayClient payClient = (AlipaySafePayClient) payClientFactory.getPayClient(channel.getId());
        // 执行签约
        return payClient.alipayUserAgreementUnsign(externalAgreementNo);
    }


    /**
     * 资金记账本开通
     */
    @GetMapping("/alipayFundAccountbookCreate")
    @Transactional(rollbackFor = Exception.class)
    public CommonResult alipayFundAccountbookCreate(@RequestParam String agreementNo) throws AlipayApiException, JsonProcessingException {
        // 拿到支付配置信息
        PayChannelDO channel = channelService.getChannel(58L);
        // 支付渠道构建
        payClientFactory.createOrUpdatePayClient(channel.getId(), PayChannelEnum.ALIPAY_AQF.getCode(), channel.getConfig());
        // 构建公共支付对象
        AlipaySafePayClient payClient = (AlipaySafePayClient) payClientFactory.getPayClient(channel.getId());
        // 执行签约
        AlipayFundAccountbookCreateResponse response = payClient.alipayFundAccountbookCreate(SecurityFrameworkUtils.getLoginUserId().toString(), agreementNo);
        if (response.isSuccess()){
            // json序列化对象
            ObjectMapper objectMapper=new ObjectMapper();
            // 获取Map
            Map map = objectMapper.readValue(response.getBody(), Map.class);
            // 签约成功的对象
            Map<String,String> steam =(Map<String,String>)map.get("alipay_fund_accountbook_create_response");
            // 根据协议号修改
            steamAlipayAqfSignMapper.update(new SteamAlipayAqfSignDO(1,steam.get("account_book_id")),
                    new UpdateWrapper<SteamAlipayAqfSignDO>()
                        .eq("agreement_no", agreementNo));
        }
        return CommonResult.success(response.getBody());
    }


    /**
     * 记账本查询
     */
        @GetMapping("/alipayFundAccountbookQuery")
    public CommonResult alipayFundAccountbookQuery(@RequestParam String agreementNo,
                                                   @RequestParam String accountBookId
    ) throws AlipayApiException, JsonProcessingException {
        // 拿到支付配置信息
        PayChannelDO channel = channelService.getChannel(58L);
        // 支付渠道构建
        payClientFactory.createOrUpdatePayClient(channel.getId(), PayChannelEnum.ALIPAY_AQF.getCode(), channel.getConfig());
        // 构建公共支付对象
        AlipaySafePayClient payClient = (AlipaySafePayClient) payClientFactory.getPayClient(channel.getId());
        // 执行签约
        return payClient.alipayFundAccountbookQuery(accountBookId,SecurityFrameworkUtils.getLoginUserId().toString(),agreementNo);
    }


    /**
     * 给记账本充值
     * @return
     */
    @GetMapping("/alipayFundTransPagePay")
    public CommonResult alipayFundTransPagePay(@RequestParam String transAmount,@RequestParam String agreementNo,@RequestParam String accountBookId) throws AlipayApiException, JsonProcessingException, ParseException {
        // 记账本充值商户订单号
        String contractNumber = generateContractNumber();
        // 订单超时时间
        String orderTimeOut = getOrderTimeOut();
        // 拿到支付配置信息
        PayChannelDO channel = channelService.getChannel(58L);
        // 支付渠道构建
        payClientFactory.createOrUpdatePayClient(channel.getId(), PayChannelEnum.ALIPAY_AQF.getCode(), channel.getConfig());
        // 构建公共支付对象
        AlipaySafePayClient payClient = (AlipaySafePayClient) payClientFactory.getPayClient(channel.getId());
        // 执行签约
        AlipayFundTransPagePayResponse response = payClient.alipayFundTransPagePay(transAmount, agreementNo, accountBookId,contractNumber,orderTimeOut);
        if (response.isSuccess()){
            // 保存数据库
            steamAlipayAqfJzbOrderMapper.insertOrUpdate(new SteamAlipayAqfJzbOrderDO(
                    Objects.requireNonNull(SecurityFrameworkUtils.getLoginUserId()).intValue(),
                    contractNumber,transAmount,"记账本充值","记账本充值",
                    orderTimeOut,accountBookId,agreementNo,SecurityFrameworkUtils.getLoginUserId().intValue()
            ));
        }
        // 构建返回
        return CommonResult.success(response.getBody());
    }

    /**
     * 记账本充值订单查询
     */
    @GetMapping("/alipayFundTransCommonQuery")
    public CommonResult alipayFundTransCommonQuery(String order,String bizSene,String productCode) throws AlipayApiException, JsonProcessingException {
        // 记账本充值商户订单号
        String contractNumber = generateContractNumber();
        // 订单超时时间
        String orderTimeOut = getOrderTimeOut();
        // 拿到支付配置信息
        PayChannelDO channel = channelService.getChannel(58L);
        // 支付渠道构建
        payClientFactory.createOrUpdatePayClient(channel.getId(), PayChannelEnum.ALIPAY_AQF.getCode(), channel.getConfig());
        // 构建公共支付对象
        AlipaySafePayClient payClient = (AlipaySafePayClient) payClientFactory.getPayClient(channel.getId());
        // 执行签约
        AlipayFundTransCommonQueryResponse response = payClient.alipayFundTransCommonQuery(order,bizSene,productCode);
        if (response.isSuccess()){

        }
        // 构建返回
        return CommonResult.success(response.getBody());
    }




    /**
     * 基于记账本调拨、转账
     */
    @PostMapping("/alipayFundTransUniTransfer")
    // 开启事务
    @Transactional(rollbackFor = Exception.class)
    public CommonResult alipayFundTransUniTransfer(@RequestBody SteamAlipayAqfTransferOrderDO steamAlipayAqfTransferOrderDO) throws AlipayApiException, JsonProcessingException {
        // json序列化对象
        ObjectMapper objectMapper=new ObjectMapper();
        // 构建参数
        ObjectNode jsonNode =(ObjectNode)objectMapper.valueToTree(steamAlipayAqfTransferOrderDO);
        // 传入用户id
        jsonNode.put("createUserId",SecurityFrameworkUtils.getLoginUserId());
        // 拿到支付配置信息
        PayChannelDO channel = channelService.getChannel(58L);
        // 支付渠道构建
        payClientFactory.createOrUpdatePayClient(channel.getId(), PayChannelEnum.ALIPAY_AQF.getCode(), channel.getConfig());
        // 构建公共支付对象
        AlipaySafePayClient payClient = (AlipaySafePayClient) payClientFactory.getPayClient(channel.getId());
        // 执行
        AlipayFundTransUniTransferResponse response = payClient.alipayFundTransUniTransfer(jsonNode);
        SteamAlipayAqfTransferOrderDO transferOrderDO=null;
        // 当订单创建成功后，将数据持久化到数据库
        if (response.isSuccess()){
            // 转账状态成功
            jsonNode.put("status",1);
            // 因为支付模块pay没有依赖与订单DTO模块，所有这里采用引用传参的方式获取值，减少模块之间的相互依赖
             transferOrderDO = objectMapper.convertValue(jsonNode, SteamAlipayAqfTransferOrderDO.class);
        }else{
            transferOrderDO = objectMapper.convertValue(jsonNode, SteamAlipayAqfTransferOrderDO.class);
            // 支付失败的原因
            transferOrderDO.setPayErro(response.getSubMsg());
        }
        // 将订单数据持久到数据库
        steamAlipayAqfTransferOrderDOMapper.insertOrUpdate(transferOrderDO);
        // 执行签约
        return CommonResult.success(response);
    }









}
