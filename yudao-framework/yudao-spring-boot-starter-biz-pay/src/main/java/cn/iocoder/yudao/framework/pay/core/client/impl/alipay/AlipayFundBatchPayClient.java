package cn.iocoder.yudao.framework.pay.core.client.impl.alipay;

import cn.iocoder.yudao.framework.common.exception.ServiceException;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.pay.core.client.dto.order.PayOrderRespDTO;
import cn.iocoder.yudao.framework.pay.core.client.dto.order.PayOrderUnifiedReqDTO;
import cn.iocoder.yudao.framework.pay.core.enums.channel.PayChannelEnum;
import cn.iocoder.yudao.framework.pay.core.enums.order.PayOrderDisplayModeEnum;
import cn.iocoder.yudao.module.infra.service.config.ConfigService;
import com.alipay.api.AlipayApiException;
import com.alipay.api.domain.AlipayFundBatchCreateModel;
import com.alipay.api.domain.AlipayFundTransPagePayModel;
import com.alipay.api.domain.Participant;
import com.alipay.api.domain.TransOrderDetail;
import com.alipay.api.request.*;
import com.alipay.api.response.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.*;

import static cn.iocoder.yudao.framework.pay.core.client.impl.alipay.AlipayPayClientConfig.MODE_CERTIFICATE;

/**
 * 支付宝 安全发（服务商模式）
 * 文档地址 https://opendocs.alipay.com/open/01kwnr?pathHash=fc61691d
 *
 * @author 最难风雨故人来
 */

@Slf4j
public class AlipayFundBatchPayClient extends AbstractAlipayPayClient {
    @Resource
    private ConfigService configService;
    public AlipayFundBatchPayClient(Long channelId, AlipayPayClientConfig config) {
        super(channelId, PayChannelEnum.ALIPAY_PLFKDYMZH.getCode(), config);
    }

    @Override
    protected PayOrderRespDTO doUnifiedOrder(PayOrderUnifiedReqDTO reqDTO) throws Exception {

        AlipayFundBatchCreateResponse createResponse = batchCreate(reqDTO);

        if (!Objects.equals(createResponse.getCode(), "10000")) {
            throw new ServiceException(Integer.parseInt(createResponse.getCode()), createResponse.getSubMsg());
        }


        String batchTransId = createResponse.getBatchTransId();

        // 1.1 构建 AlipayTradePrecreateModel 请求
        AlipayFundTransPagePayModel model = new AlipayFundTransPagePayModel();
        // ① 通用的参数
        model.setOutBizNo(reqDTO.getOutTradeNo());
        model.setTransAmount(formatAmount(reqDTO.getPrice()));
        model.setProductCode("BATCH_API_TO_ACC");
        model.setBizScene("MESSAGE_BATCH_PAY");
        model.setOrderTitle(reqDTO.getSubject());
        model.setOrderId(batchTransId);
        // ② 个性化的参数【无】


        // ③ 支付宝扫码支付只有一种展示，考虑到前端可能希望二维码扫描后，手机打开
        String displayMode = PayOrderDisplayModeEnum.FORM.getMode();

        // 1.2 构建 AlipayTradePrecreateRequest 请求
        AlipayFundTransPagePayRequest request = new AlipayFundTransPagePayRequest();
        request.setBizModel(model);
        request.setNotifyUrl(reqDTO.getNotifyUrl());
        request.setReturnUrl(reqDTO.getReturnUrl());
        request.putOtherTextParam("app_auth_token", reqDTO.getChannelExtras().get("app_auth_token"));
        // 2.1 执行请求
        AlipayFundTransPagePayResponse response;
//        if (Objects.equals(config.getMode(), MODE_CERTIFICATE)) {
//            // 证书模式
//            response = client.certificateExecute(request);
//        } else {
//            response = client.execute(request);
//        }
        response = client.pageExecute(request);
        // 2.2 处理结果
        if (!response.isSuccess()) {
            return buildClosedPayOrderRespDTO(reqDTO, response);
        }
        return PayOrderRespDTO.waitingOf(displayMode, response.getBody(),
                reqDTO.getOutTradeNo(), response);
    }

    private AlipayFundBatchCreateResponse batchCreate(PayOrderUnifiedReqDTO reqDTO) throws AlipayApiException {


        AlipayFundBatchCreateModel model = new AlipayFundBatchCreateModel();
        model.setOutBatchNo(reqDTO.getOutTradeNo());
        model.setTotalTransAmount(formatAmount(reqDTO.getPrice()));
        model.setTotalCount("1");
        model.setProductCode("BATCH_API_TO_ACC");
        model.setBizScene("MESSAGE_BATCH_PAY");
        model.setOrderTitle(reqDTO.getSubject());

        TransOrderDetail detail = new TransOrderDetail();
        detail.setOutBizNo(reqDTO.getOutTradeNo());
        detail.setTransAmount(formatAmount(reqDTO.getPrice()));
        Participant payee = new Participant();


        payee.setIdentity(reqDTO.getChannelExtras().get("configAlipayUserId"));
        payee.setIdentityType("ALIPAY_USER_ID");
        payee.setName(reqDTO.getChannelExtras().get("configAlipayName"));
//        payee.setIdentity("18008389230@163.com");
//        payee.setIdentityType("ALIPAY_LOGON_ID");
//        payee.setName("重庆辉耀慧光网络科技有限公司");
        detail.setPayeeInfo(payee);

        model.setTransOrderList(new ArrayList<>());

        model.getTransOrderList().add(detail);

        AlipayFundBatchCreateRequest request = new AlipayFundBatchCreateRequest();

        request.setBizModel(model);
        request.setNotifyUrl(reqDTO.getNotifyUrl());
        request.setReturnUrl(reqDTO.getReturnUrl());
        request.putOtherTextParam("app_auth_token", reqDTO.getChannelExtras().get("app_auth_token"));

        AlipayFundBatchCreateResponse response;

        if (Objects.equals(config.getMode(), MODE_CERTIFICATE)) {
            // 证书模式
            response = client.certificateExecute(request);
        } else {
            response = client.execute(request);
        }
        return response;
    }

    /**
     * 付宝个ISV页面签约接口
     */
    public AlipayOpenInviteOrderCreateResponse isvPageSign(ObjectNode jsonNode) throws AlipayApiException, JsonProcessingException {
        // 构建ISV签约的订单号
        String contractNumber = generateContractNumber();
        System.err.println("构建ISV签约的订单号:" + contractNumber);
        // 构建签约页面对象
        AlipayOpenInviteOrderCreateRequest request = new AlipayOpenInviteOrderCreateRequest();
        // 构建参数对象
        ObjectMapper objectMapper = new ObjectMapper();
        // 构建节点
        ObjectNode bizContent = objectMapper.createObjectNode();
        // isv业务系统的申请单id
        bizContent.put("isv_biz_id", contractNumber);
        jsonNode.put("isvBizId", contractNumber);
        // isv签约回调地址页面
        bizContent.put("isv_return_url", jsonNode.get("isvReturnUrl").textValue());
        // 存储
        request.setBizContent(objectMapper.writeValueAsString(bizContent));
        // 执行签约
        AlipayOpenInviteOrderCreateResponse response = null;
        try {
            response = client.pageExecute(request);
        } catch (Exception e) {
            throw new RuntimeException("ISV签约失败!");
        }
        //获取需提交的form表单
        return response;
    }

    /**
     * ISV签约状态查询
     */
    public CommonResult alipayOpenInviteOrderQuery(String isvBizId) throws AlipayApiException, JsonProcessingException {
        // 构建签约页面对象
        AlipayOpenInviteOrderQueryRequest request = new AlipayOpenInviteOrderQueryRequest();
        // 构建参数对象
        ObjectMapper objectMapper = new ObjectMapper();
        // 构建节点
        ObjectNode bizContent = objectMapper.createObjectNode();
        // isv业务系统单据编号
        bizContent.put("isv_biz_id", isvBizId);
        // 存储
        request.setBizContent(objectMapper.writeValueAsString(bizContent));
        // 构建
        AlipayOpenInviteOrderQueryResponse response;
        try {
            response = client.certificateExecute(request);
        } catch (Exception e) {
            return CommonResult.error(500, "查询ISV签约状态失败!");
        }
        //获取需提交的form表单
        return CommonResult.success(response.getBody());
    }


    /**
     * 生成签约商户号方法
     *
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
     * ISV调用该接口创建批量付款单据
     */
    public CommonResult alipayFundBatchCreate(String transAmount) throws AlipayApiException, JsonProcessingException {
        // 支付订单
        BigDecimal bigDecimal = new BigDecimal(transAmount);
        // 取值精确到小数点后两位
        BigDecimal roundedNumber = bigDecimal.setScale(2, RoundingMode.HALF_UP);
        // 构建ISV签约的订单号
        String contractNumber = generateContractNumber();
        // 商户端明细外部订单号
        String transOrder = generateContractNumber();
        System.err.println("构建ISV签约的订单号:" + contractNumber);
        // 构建批量付款对象
        AlipayFundBatchCreateRequest request = new AlipayFundBatchCreateRequest();
        //设置app_auth_token
        request.putOtherTextParam("app_auth_token", "202404BBe6e4b543a59242a1ad8d0026bba17D73");
        // 构建参数对象
        ObjectMapper objectMapper = new ObjectMapper();
        // 构建节点
        ObjectNode bizContent = objectMapper.createObjectNode();
        // 商户端批次的唯一订单
        bizContent.put("out_batch_no", contractNumber);
        // 批次总金额，单位为元，精确到小数点后两位，取值范围[1,9999999999999.99]。
        bizContent.put("total_trans_amount", bigDecimal);
        // 批次总笔数。有几个收款方就填入多少个批次数量
        bizContent.put("total_count", 1);
        //销售产品码，BATCH_API_TO_ACC（固定值）。
        bizContent.put("product_code", "BATCH_API_TO_ACC");
        // 业务场景，MESSAGE_BATCH_PAY（固定值）。
        bizContent.put("biz_scene", "MESSAGE_BATCH_PAY");
        // 订单标题
        bizContent.put("order_title", "代发");
        // 业务备注
        bizContent.put("remark", "pay");
        // 绝对超时时间，格式为yyyy-MM-dd HH:mm。
        bizContent.put("time_expire", getOrderTimeOut());
        // 创建 TransOrderDetail 数组
        ArrayNode transOrderDetailArray = objectMapper.createArrayNode();
        // 收款信息列表
        ObjectNode transOrderList = objectMapper.createObjectNode();
        // 商户端明细外部订单号，同一批次下，明细单号需要唯一
        transOrderList.put("out_biz_no", transOrder);
        // 明细金额，单位为元（最小1元），精确到小数点后两位，取值范围[1,9999999999999.99]
        transOrderList.put("trans_amount", bigDecimal);
        // 转账备注，收款方如果是支付宝账号，会展示在收款方账单里。
        transOrderList.put("remark", "转账");
        //收款方信息
        ObjectNode payeeInfo = objectMapper.createObjectNode();
        //收款方账号，传入支付宝账号或者支付宝 uid。
        payeeInfo.put("identity", "2088741838367375");
        //当 identity 传入支付宝账号时，identity_type 传 ALIPAY_LOGON_ID；
        //当identity传入支付宝 uid 时，identity_type 传 ALIPAY_USER_ID；
        //当identity传入支付宝订单号时，identity_type 传 ALIPAY_TRADE_NO
        payeeInfo.put("identity_type", "ALIPAY_USER_ID");
        //参与方真实姓名，如果非空，将校验收款支付宝账号姓名一致性。
        payeeInfo.put("name", "重庆辉耀慧光网络科技有限公司");
        // 存入
        transOrderList.put("payee_info", payeeInfo);
        // 存入数组中
        transOrderDetailArray.add(transOrderList);
        // 将多个付款方信息进行存储
        bizContent.put("trans_order_list", transOrderDetailArray);
        // 存储所有参数信息
        request.setBizContent(objectMapper.writeValueAsString(bizContent));
        AlipayFundBatchCreateResponse response;
        try {
            response = client.certificateExecute(request);
        } catch (Exception e) {
            return CommonResult.error(500, "创建批量付款单据失败!");
        }
        //获取需提交的form表单
        return CommonResult.success(response.getBody());
    }


    /**
     * 批次支付接口： PC支付
     * ISV调用该页面接口跳转至支付确认页（PC版本），商户进行支付验证后完成支付
     */
    public CommonResult alipayFundTransPagePay(String orderId) throws JsonProcessingException {
        // 构建入参对象
        AlipayFundTransPagePayRequest request = new AlipayFundTransPagePayRequest();
        // 支付宝应用授权码
        request.putOtherTextParam("app_auth_token", "202404BBe6e4b543a59242a1ad8d0026bba17D73");
        // 构建json对象
        ObjectMapper objectMapper = new ObjectMapper();
        // 构建bizContentjson对象
        ObjectNode bizContent = objectMapper.createObjectNode();
        // 支付宝批次订单号。(当前场景，可以取alipay.fund.batch.create接口返回值中的batch_trans_id的值）
        bizContent.put("order_id", orderId);
        //销售产品码，BATCH_API_TO_ACC（固定值）。
        bizContent.put("product_code", "BATCH_API_TO_ACC");
        //业务场景，MESSAGE_BATCH_PAY（固定值）。
        bizContent.put("biz_scene", "MESSAGE_BATCH_PAY");
        // 入参
        request.setBizContent(objectMapper.writeValueAsString(bizContent));
        // 构建执行对象
        AlipayFundTransPagePayResponse response;
        try {
            response = client.pageExecute(request);
        } catch (Exception e) {
            return CommonResult.error(500, "创建批量付款单据失败!");
        }
        //获取需提交的form表单
        return CommonResult.success(response.getBody());
    }

    /**
     * 批次查询接口（alipay.fund.batch.detail.query）
     */
    public CommonResult alipayFundBatchDetailQuery(String outBatchNo) throws JsonProcessingException {
        // 构建入参对象
        AlipayFundBatchDetailQueryRequest request = new AlipayFundBatchDetailQueryRequest();
        // 支付宝应用授权码
        request.putOtherTextParam("app_auth_token", "202404BBe6e4b543a59242a1ad8d0026bba17D73");
        // 构建入参对象
        ObjectMapper objectMapper = new ObjectMapper();
        // 构建bizContent
        ObjectNode bizContent = objectMapper.createObjectNode();
        //商户的批次号。
        bizContent.put("out_batch_no", outBatchNo);
        //销售产品码，BATCH_API_TO_ACC（固定值）。
        bizContent.put("product_code", "BATCH_API_TO_ACC");
        //业务场景，MESSAGE_BATCH_PAY（固定值）。
        bizContent.put("biz_scene", "MESSAGE_BATCH_PAY");
        // 传入
        request.setBizContent(objectMapper.writeValueAsString(bizContent));
        // 构建执行对象
        AlipayFundBatchDetailQueryResponse response;
        try {
            response = client.certificateExecute(request);
        } catch (Exception e) {
            return CommonResult.error(500, "创建批量付款单据失败!");
        }
        //获取需提交的form表单
        return CommonResult.success(response.getBody());
    }


    /**
     * 设置超时时间
     *
     * @return
     */
    private String getOrderTimeOut(int amount) {
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

    private String getOrderTimeOut() {
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


}
