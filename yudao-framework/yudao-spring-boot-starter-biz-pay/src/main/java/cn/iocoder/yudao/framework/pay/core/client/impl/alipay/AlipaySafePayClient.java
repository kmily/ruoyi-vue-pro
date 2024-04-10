package cn.iocoder.yudao.framework.pay.core.client.impl.alipay;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.pay.core.client.dto.order.PayOrderRespDTO;
import cn.iocoder.yudao.framework.pay.core.client.dto.order.PayOrderUnifiedReqDTO;
import cn.iocoder.yudao.framework.pay.core.enums.channel.PayChannelEnum;
import cn.iocoder.yudao.framework.pay.core.enums.order.PayOrderDisplayModeEnum;
import cn.iocoder.yudao.module.infra.service.config.ConfigService;
import com.alipay.api.AlipayApiException;
import com.alipay.api.domain.AlipayFundTransPagePayModel;
import com.alipay.api.domain.Participant;
import com.alipay.api.request.*;
import com.alipay.api.response.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

/**
 * 支付宝 安全发（服务商模式）
 * 文档地址 https://opendocs.alipay.com/open/01kwnr?pathHash=fc61691d
 * @author 最难风雨故人来
 */
@Slf4j
public class AlipaySafePayClient extends AbstractAlipayPayClient{


    @Resource
    private ConfigService configService;

    public AlipaySafePayClient(Long channelId, AlipayPayClientConfig config) {
        super(channelId, PayChannelEnum.ALIPAY_AQF.getCode(), config);
    }



    @Override
    protected PayOrderRespDTO doUnifiedOrder(PayOrderUnifiedReqDTO reqDTO) throws Throwable {


        AlipayFundTransPagePayResponse response1;
        response1 = alipayFundTransPagePay(formatAmount(reqDTO.getPrice()),
                reqDTO.getChannelExtras().get("book_agreement_no"),
                reqDTO.getChannelExtras().get("account_book_id"),
                reqDTO.getOutTradeNo(),
                getOrderTimeOut());
        if(!response1.isSuccess()){
            return buildClosedPayOrderRespDTO(reqDTO, response1);
        }

        //转账开始
//        AlipayFundTransUniTransferModel model = new AlipayFundTransUniTransferModel();
//
//        model.setOutBizNo(reqDTO.getOutTradeNo());
//        model.setOrderTitle(reqDTO.getSubject());
//        model.setTransAmount(String.valueOf(reqDTO.getPrice()));
//        model.setProductCode("SINGLE_TRANSFER_NO_PWD");
//        model.setBizScene("ENTRUST_TRANSFER");
//        model.setBusinessParams("{\"payer_show_name_use_alias\":\"true\"}");
//
//        Participant payer = new Participant();
//        Participant payee = new Participant();
//
//        payer.setIdentityType("ACCOUNT_BOOK_ID");
//        payer.setIdentity(reqDTO.getChannelExtras().get("account_book_id"));
//        payer.setExtInfo("{\"agreement_no\":\"" + reqDTO.getChannelExtras().get("book_agreement_no") + "\"}");
//
//
//        payee.setIdentity(reqDTO.getChannelExtras().get("configAlipayUserId"));
//        payee.setIdentityType("ALIPAY_USER_ID");
//        payee.setName(reqDTO.getChannelExtras().get("configAlipayName"));
//
//        model.setPayeeInfo(payee);
//        model.setPayerInfo(payer);
//
        String displayMode = PayOrderDisplayModeEnum.FORM.getMode();
//
//
//        AlipayFundTransUniTransferRequest request = new AlipayFundTransUniTransferRequest();
//
//        request.setBizModel(model);
//
//        AlipayFundTransUniTransferResponse response;
//        if (Objects.equals(config.getMode(), MODE_CERTIFICATE)) {
//            // 证书模式
//            response = client.certificateExecute(request);
//        } else {
//            response = client.execute(request);
//        }

        if (!response1.isSuccess()) {
            return buildClosedPayOrderRespDTO(reqDTO, response1);
        }
        return PayOrderRespDTO.waitingOf(displayMode, response1.getBody(),
                reqDTO.getOutTradeNo(), response1);
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

    /**
     *  第一步用户授权协议签约
     * @return
     * @throws AlipayApiException
     */
    public CommonResult userAgreementPageSign(String contractNumber) throws AlipayApiException, JsonProcessingException {
        // 创建ObjectMapper实例
        ObjectMapper objectMapper = new ObjectMapper();
        // 构建参数对象
        AlipayUserAgreementPageSignRequest request = new AlipayUserAgreementPageSignRequest();
        // 构建json对象


        ObjectNode bizContent = objectMapper.createObjectNode();
        // 用户在商户网站的登录账号，用于在签约页面展示
        bizContent.put("external_logon_id", "");
        // 个人产品码，固定值。安全发-服务商模式（单笔转账到支付宝账户）场景传入：FUND_SAFT_SIGN_
        bizContent.put("personal_product_code", "FUND_SAFT_SIGN_WITHHOLDING_P");
        // 场景码，固定值。
        bizContent.put("sign_scene", "INDUSTRY|SATF_ACC");
        // 商户签约号，协议中标示用户的唯一签约号（自定义，确保在商户系统中唯一）。 格式规则：支持大写小写字母和数字，最长32位。
        bizContent.put("external_agreement_no", contractNumber);
        // 商家类型
        bizContent.put("third_party_type", "PARTNER");
        // 商家签约的产品码，固定值。安全发-服务商模式（单笔转账到支付宝账户）场景传入：FUND_SAFT_SIGN_
        bizContent.put("product_code", "FUND_SAFT_SIGN_WITHHOLDING");
        // 特殊参数 端类型：扫码授权，固定值，仅支持 QRCODE
        ObjectNode accessParamsNode = objectMapper.createObjectNode();
        //  1. ALIPAYAPP （钱包h5页面签约） 2. QRCODE(扫码签约) 3. QRCODEORSMS(扫码签约或者短信签约)
        accessParamsNode.put("channel", "QRCODE");
        // 将access_params节点添加到bizContent中
        bizContent.set("access_params", accessParamsNode);
        // 构建参数请求对象
        request.setBizContent(objectMapper.writeValueAsString(bizContent));
        //通过alipayClient调用API，获得对应的response类
        AlipayUserAgreementPageSignResponse response = null;
        try {
            response = client.pageExecute(request, "get");
        }catch (Exception e){
            return CommonResult.error(500,"构建签约环境失败!");
        }
        // 返回支付宝授权页面
        return CommonResult.success(response.getBody());
    }

    /**
     * 协议查询接口(查询签约是否成功)
     */
    public AlipayUserAgreementQueryResponse alipayUserAgreementQuery(String externalAgreementNo) throws AlipayApiException, JsonProcessingException {
        // 构建请求参数对象
        AlipayUserAgreementQueryRequest request = new AlipayUserAgreementQueryRequest();
        // 创建ObjectMapper实例
        ObjectMapper objectMapper = new ObjectMapper();
        // 构建json对象
        ObjectNode bizContent = objectMapper.createObjectNode();
        // 个人产品码，固定值。
        bizContent.put("personal_product_code","FUND_SAFT_SIGN_WITHHOLDING_P");
        // 场景码，固定值。
        bizContent.put("sign_scene","INDUSTRY|SATF_ACC");
        // 销售产品码，固定传入FUND_SAFT_SIGN_WITHHOLDING
        bizContent.put("product_code","FUND_SAFT_SIGN_WITHHOLDING");
        // 商户签约号，对应签约时候传入的值。本参数和支付宝协议号两者不能同时为空。 当本参数和支付宝协议号同时提供时，将用支付宝协议号进行查询，忽略本参数。
        bizContent.put("external_agreement_no",externalAgreementNo);
        // 存储
        request.setBizContent(objectMapper.writeValueAsString(bizContent));
        // 查询协议签约结果
        AlipayUserAgreementQueryResponse response = null;
        try{
            response =client.certificateExecute(request);
        }catch (Exception e){
            throw new RuntimeException("查询失败!");
        }
        return response;
    }

    /**
     * 协议解约接口
     */
    public CommonResult alipayUserAgreementUnsign(String externalAgreementNo) throws AlipayApiException, JsonProcessingException {
        // 构建请求参数对象
        AlipayUserAgreementUnsignRequest request = new AlipayUserAgreementUnsignRequest();
        // 创建ObjectMapper实例
        ObjectMapper objectMapper = new ObjectMapper();
        // 构建json对象
        ObjectNode bizContent = objectMapper.createObjectNode();
        // 个人产品码
        bizContent.put("personal_product_code","FUND_SAFT_SIGN_WITHHOLDING_P");
        // 场景码，商户签约号，对应签约时候传入的值。
        bizContent.put("sign_scene","INDUSTRY|SATF_ACC");
        // 商户签约号
        bizContent.put("external_agreement_no",externalAgreementNo);
        // 存储
        request.setBizContent(objectMapper.writeValueAsString(bizContent));
        // 执行解除协议
        AlipayUserAgreementUnsignResponse response = null;
        try{
            response = client.certificateExecute(request);
        }catch (Exception e){
            return CommonResult.error(500,"解除签约协议失败");
        }
        return CommonResult.success(response.getBody());

    }

    /**
     * 开通资金记账本
     * 说明： 商户只能给自己拥有的记账本充值，且不支持个人用户使用充值。
     */
    public AlipayFundAccountbookCreateResponse alipayFundAccountbookCreate(String userId,String agreementNo) throws AlipayApiException, JsonProcessingException {
        // 构建请求参数对象
        AlipayFundAccountbookCreateRequest request = new AlipayFundAccountbookCreateRequest();
        // 创建ObjectMapper实例
        ObjectMapper objectMapper = new ObjectMapper();
        // 构建json对象
        ObjectNode bizContent = objectMapper.createObjectNode();
        // 外部商户系统会员的唯一标识；记账本创建时候的幂等字段。
        bizContent.put("merchant_user_id",userId);
        // 外部商户用户类型，固定值。
        bizContent.put("merchant_user_type","BUSINESS_EMPLOYEE");
        // 资金记账本的业务场景，固定值。
        bizContent.put("scene_code","SATF_FUND_BOOK");
        // 可选参数，业务参数自定义
        ObjectNode accessParamsNode = objectMapper.createObjectNode();
        // 支付宝协议号
        accessParamsNode.put("agreement_no",agreementNo);
        bizContent.put("ext_info",accessParamsNode);
        // 存储
        request.setBizContent(objectMapper.writeValueAsString(bizContent));
        // 执行开通记账本
        AlipayFundAccountbookCreateResponse response = null;
        try{
            response = client.certificateExecute(request);
        }catch (Exception e){
            throw new RuntimeException("开通记账本失败");
        }
        return response;
    }

    /**
     * 记账本查询
     */
    public CommonResult alipayFundAccountbookQuery(String accountBookId,String userId,String agreementNo) throws AlipayApiException, JsonProcessingException {
        // 构建请求参数对象
        AlipayFundAccountbookQueryRequest request = new AlipayFundAccountbookQueryRequest();
        // 创建ObjectMapper实例
        ObjectMapper objectMapper = new ObjectMapper();
        // 构建json对象
        ObjectNode bizContent = objectMapper.createObjectNode();
        // 资金记账本id
        bizContent.put("account_book_id",accountBookId);
        // 资金记账本的开通场景码 固定值
        bizContent.put("scene_code","SATF_FUND_BOOK");
        //  商户会员的唯一标识，如果传入account_book_id此字段必传
        bizContent.put("merchant_user_id",userId);
        // 特殊配置 传递业务参数
        ObjectNode accessParamsNode = objectMapper.createObjectNode();
        // 支付宝协议号(企业支付宝签约成功后返回)
        accessParamsNode.put("agreement_no",agreementNo);
        bizContent.put("ext_info",accessParamsNode);
        // 存储配置
        request.setBizContent(objectMapper.writeValueAsString(bizContent));
        // 构建执行对象
        AlipayFundAccountbookQueryResponse response = null;
        try{
            response = client.certificateExecute(request);
        }catch (Exception e){
            return CommonResult.error(500,"记账本查询失败!");
        }
        return CommonResult.success(response);
    }


    /**
     * 给记账本充值
     */
    public AlipayFundTransPagePayResponse alipayFundTransPagePay(String transAmount,String agreementNo,String accountBookId,String contractNumber,String orderTimeOut) throws AlipayApiException, JsonProcessingException {
        // 支付订单

        // 构建请求参数对象
        AlipayFundTransPagePayRequest request = new AlipayFundTransPagePayRequest();

        AlipayFundTransPagePayModel model=new AlipayFundTransPagePayModel();

        model.setOutBizNo(contractNumber);
        model.setTransAmount(transAmount);

        model.setProductCode("FUND_ACCOUNT_BOOK");

        model.setBizScene("SATF_DEPOSIT");
        model.setRemark("记账本充值");

        model.setTimeExpire(orderTimeOut);
        model.setOrderTitle("记账本账户充值: "+transAmount+"元");
        Participant payee=new Participant();
        payee.setIdentityType("ACCOUNT_BOOK_ID");
        payee.setIdentity(accountBookId);
        payee.setExtInfo("{\"agreement_no\":\""+agreementNo+"\"}");

        model.setPayeeInfo(payee);
        model.setPassbackParams("{\"account_book_id\":\""+accountBookId+"\",\"book_agreement_no\":\""+agreementNo+"\"}");
        // 存储
        request.setBizModel(model);
        // 转账
        AlipayFundTransPagePayResponse response = null;
        try{
            response = client.pageExecute(request, "get");//通过alipayClient调用API，获得对应的response类
        }catch (Exception e){
            throw new RuntimeException("转账失败!");
        }
        return response;
    }



    /**
     * 设置超时时间
     * @return
     */
    private String getOrderTimeOut(int amount){
        // 创建一个Date对象表示当前时间
        Date date = new Date();
        // 创建一个Calendar对象，并设置为当前时间
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        // 在Calendar对象中增加5分钟
        calendar.add(Calendar.MINUTE, amount);
        // 将Calendar对象转换回Date对象
        Date newDate = calendar.getTime();
        // 使用SimpleDateFormat格式化日期
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return sdf.format(newDate);
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
     * 转账结果查询
     * 商户可通过该接口查询转账业务单据的状态，
     * 主要应用于统一转账接口(alipay.fund.trans.uni.transfer)、无线转账接口(alipay.fund.trans.app.pay)、单笔转账到支付宝账户接口（alipay.fund.trans.toaccount.transfer）
     * bizScene DIRECT_TRANSFER无密转账 PERSONAL_PAY充值记账本
     */
    public AlipayFundTransCommonQueryResponse alipayFundTransCommonQuery(String outBizNo,String bizScene,String productCode) throws AlipayApiException, JsonProcessingException {
        // 构造请求参数以调用接口
        AlipayFundTransCommonQueryRequest request = new AlipayFundTransCommonQueryRequest();
        // 参数对象
        ObjectMapper objectMapper=new ObjectMapper();
        // bizConent
        ObjectNode bizConent = objectMapper.createObjectNode();
        bizConent.put("product_code",productCode);
        bizConent.put("biz_scene",bizScene);
        bizConent.put("out_biz_no",outBizNo);
        request.setBizContent(objectMapper.writeValueAsString(bizConent));
        AlipayFundTransCommonQueryResponse response = null;
        try{
            response = client.certificateExecute(request, "get");//通过alipayClient调用API，获得对应的response类
        }catch (Exception e){
            throw new RuntimeException("转账失败!");
        }
        return response;

    }

    /**
     * 基于记账本调拨、转账
     */
    public AlipayFundTransUniTransferResponse alipayFundTransUniTransfer(ObjectNode objectNode) throws AlipayApiException, JsonProcessingException {
        // 记账本之间相互调拨商户订单号
        String contractNumber = generateContractNumber();
        objectNode.put("outBizNo",contractNumber);
        // 支付订单
        BigDecimal bigDecimal = new BigDecimal(objectNode.get("transAmount").toString());
        // 取值精确到小数点后两位
        BigDecimal roundedNumber = bigDecimal.setScale(2, RoundingMode.HALF_UP);
        // 构造请求参数以调用接口
        AlipayFundTransUniTransferRequest request = new AlipayFundTransUniTransferRequest();
        // 创建ObjectMapper实例
        ObjectMapper objectMapper = new ObjectMapper();
        // 构建json对象
        ObjectNode bizContent = objectMapper.createObjectNode();
        // 商户端的唯一订单号，对于同一笔转账请求，商户需保证该订单号唯一。
        bizContent.put("out_biz_no",contractNumber);
        // 订单总金额，单位为元，精确到小数点后两位，取值范围[0.01,100000000]
        bizContent.put("trans_amount",roundedNumber);
        // 产品码，固定值 SINGLE_TRANSFER_NO_PWD
        bizContent.put("product_code","SINGLE_TRANSFER_NO_PWD");
        objectNode.put("productCode","SINGLE_TRANSFER_NO_PWD");
        // 资金归集 ENTRUST_TRANSFER
        bizContent.put("biz_scene","ENTRUST_TRANSFER");
        // Participant类型，收款方信息
        ObjectNode payeeInfo = objectMapper.createObjectNode();
        // 固定值 ACCOUNT_BOOK_ID
        payeeInfo.put("identity_type","ALIPAY_USER_ID");
        objectNode.put("payeeInfoIdentityType","ACCOUNT_BOOK_ID");
        // 收款方记账本 id 的值 2088741838367375
        payeeInfo.put("identity",objectNode.get("payeeInfoIdentity").textValue());
        // 用户信息 重庆辉耀慧光网络科技有限公司
        payeeInfo.put("name",objectNode.get("payeeInfoName").textValue());
//        // 收款方账户信息。
//        //agreement_no 子户开通协议号。（必传）
//        ObjectNode extInfo = objectMapper.createObjectNode();
//        // 子户开通协议号
//        extInfo.put("agreement_no","20245130061233085335");
//        payeeInfo.put("ext_info",objectMapper.writeValueAsString(extInfo));
        // 收款方信息
        bizContent.put("payee_info",payeeInfo);
        // 转账业务的标题，用于在支付宝用户的账单里显示。
        bizContent.put("order_title",objectNode.get("orderTitle").textValue() );
        // 业务备注。
        //在单笔金额超过 50000 的时候必传。
        bizContent.put("remark",objectNode.get("remark").textValue());


        // Participant类型，付款方信息
        ObjectNode payerInfo = objectMapper.createObjectNode();
        // 参与方的标识类型，目前支持如下枚举：
        //ALIPAY_USER_ID：支付宝的会员 ID。
        //ALIPAY_LOGON_ID：支付宝登录号，支持邮箱和手机号格式。
        payerInfo.put("identity_type","ACCOUNT_BOOK_ID");
        objectNode.put("payerInfoIdentityType","ACCOUNT_BOOK_ID");
        //参与方的标识 ID。
        //当 identity_type = ALIPAY_USER_ID 时，填写支付宝用户 UID。示例值：2088123412341234。
        //当 identity_type = ALIPAY_LOGON_ID 时，填写支付宝登录号。示例值：186xxxxxxxx。
        // 记账本 id 的值
        payerInfo.put("identity",objectNode.get("payerInfoIdentity").textValue());
        //agreement_no 子户开通协议号。（必传）
        ObjectNode fkextInfo = objectMapper.createObjectNode();
        // 子户开通协议号
        fkextInfo.put("agreement_no",objectNode.get("extInfoAgreementNo"));
        payerInfo.put("ext_info",objectMapper.writeValueAsString(fkextInfo));
        bizContent.put("payer_info",payerInfo);
        // 存储配置
        request.setBizContent(objectMapper.writeValueAsString(bizContent));
        AlipayFundTransUniTransferResponse response = null;
        try{
            response = client.certificateExecute(request, "get");//通过alipayClient调用API，获得对应的response类
        }catch (Exception e){
            throw new RuntimeException("转账失败!");
        }
        return response;
    }

    /**
     * 基于记账本单笔代发到支付宝账户
     */
    public CommonResult alipayFundTransUniTransfers(String transAmount,String accountBookId,String agreementNo) throws AlipayApiException, JsonProcessingException {
        // 记账本之间相互调拨商户订单号
        String contractNumber = generateContractNumber();
        System.err.println("记账本充值商户订单号:"+contractNumber);
        // 构造请求参数以调用接口
        AlipayFundTransUniTransferRequest request = new AlipayFundTransUniTransferRequest();
        // 创建ObjectMapper实例
        ObjectMapper objectMapper = new ObjectMapper();
        // 构建json对象
        ObjectNode bizContent = objectMapper.createObjectNode();
        // 商户端的唯一订单号，对于同一笔转账请求，商户需保证该订单号唯一。
        bizContent.put("out_biz_no",contractNumber);
        // 订单总金额，单位为元，精确到小数点后两位，取值范围[0.01,100000000]
        bizContent.put("trans_amount",transAmount);
        // 产品码，固定值
        bizContent.put("product_code","SINGLE_TRANSFER_NO_PWD");
        // 资金归集
        bizContent.put("biz_scene","ENTRUST_ALLOCATION");
        // Participant类型，收款方信息
        ObjectNode payerInfo = objectMapper.createObjectNode();
        // 固定值 ACCOUNT_BOOK_ID
        payerInfo.put("identity_type","ACCOUNT_BOOK_ID");
        // 记账本 id 的值
        payerInfo.put("identity",accountBookId);
        // 收款方账户信息。
        //agreement_no 子户开通协议号。（必传）
        ObjectNode extInfo = objectMapper.createObjectNode();
        // 子户开通协议号
        extInfo.put("agreement_no",agreementNo);
        payerInfo.put("ext_info",objectMapper.writeValueAsString(extInfo));
        // 付款方信息
        bizContent.put("payer_info",payerInfo);
        // 转账业务的标题，用于在支付宝用户的账单里显示。
        bizContent.put("order_title","佳旭睿代发");
        // 业务备注。
        //在单笔金额超过 50000 的时候必传。
        bizContent.put("remark","佳旭睿代发");
        // Participant类型，收款方信息
        ObjectNode payeeInfo = objectMapper.createObjectNode();
        // 参与方的标识类型，目前支持如下枚举：
        //ALIPAY_USER_ID：支付宝的会员 ID。
        //ALIPAY_LOGON_ID：支付宝登录号，支持邮箱和手机号格式。
        payeeInfo.put("identity_type","ALIPAY_LOGON_ID");
        //参与方的标识 ID。
        //当 identity_type = ALIPAY_USER_ID 时，填写支付宝用户 UID。示例值：2088123412341234。
        //当 identity_type = ALIPAY_LOGON_ID 时，填写支付宝登录号。示例值：186xxxxxxxx。
        payeeInfo.put("identity","186xxxxxxxx");
        bizContent.put("payee_info",payeeInfo);
        AlipayFundTransUniTransferResponse response = null;
        try{
            response = client.certificateExecute(request, "get");//通过alipayClient调用API，获得对应的response类
        }catch (Exception e){
            return CommonResult.error(500,"转账失败!");
        }
        return CommonResult.success(response.getBody());
    }


}







