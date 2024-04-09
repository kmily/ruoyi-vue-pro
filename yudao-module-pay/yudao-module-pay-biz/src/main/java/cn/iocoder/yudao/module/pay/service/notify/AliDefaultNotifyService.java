package cn.iocoder.yudao.module.pay.service.notify;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.iocoder.yudao.framework.common.exception.ServiceException;
import cn.iocoder.yudao.framework.pay.core.client.PayClientFactory;
import cn.iocoder.yudao.framework.pay.core.client.dto.order.PayOrderRespDTO;
import cn.iocoder.yudao.framework.pay.core.client.impl.PayClientFactoryImpl;
import cn.iocoder.yudao.framework.pay.core.client.impl.alipay.AlipaySafePayClient;
import cn.iocoder.yudao.framework.pay.core.enums.channel.PayChannelEnum;
import cn.iocoder.yudao.framework.pay.core.enums.order.PayOrderStatusRespEnum;
import cn.iocoder.yudao.module.infra.dal.dataobject.config.ConfigDO;
import cn.iocoder.yudao.module.infra.service.config.ConfigService;
import cn.iocoder.yudao.module.pay.dal.dataobject.channel.PayChannelDO;
import cn.iocoder.yudao.module.pay.dal.dataobject.sign.SteamAlipayAqfSignDO;
import cn.iocoder.yudao.module.pay.dal.dataobject.sign.SteamAlipayIsvDO;
import cn.iocoder.yudao.module.pay.dal.mysql.order.PayOrderMapper;
import cn.iocoder.yudao.module.pay.dal.mysql.sign.SteamAlipayAqfSignMapper;
import cn.iocoder.yudao.module.pay.dal.mysql.sign.SteamAlipayIsvMapper;
import cn.iocoder.yudao.module.pay.dal.mysql.wallet.PayWalletMapper;
import cn.iocoder.yudao.module.pay.dal.mysql.wallet.PayWalletRechargeMapper;
import cn.iocoder.yudao.module.pay.service.channel.PayChannelService;
import cn.iocoder.yudao.module.pay.service.order.PayOrderService;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.domain.AlipayFundTransUniTransferModel;
import com.alipay.api.domain.Participant;
import com.alipay.api.request.AlipayFundTransUniTransferRequest;
import com.alipay.api.response.AlipayFundTransUniTransferResponse;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.Map;
import java.util.Objects;

/**
 * 支付宝回调相关
 */
@Service
@Valid
@Slf4j
public class AliDefaultNotifyService {

    @Resource
    SteamAlipayAqfSignMapper steamAlipayAqfSignMapper;
    @Resource
    PayOrderMapper payOrderMapper;
    @Resource
    PayWalletMapper payWalletMapper;

    @Resource
    PayWalletRechargeMapper payWalletRechargeMapper;
    @Resource
    PayOrderService payOrderService;
    @Resource
    private PayChannelService channelService;
    @Resource
    private RedissonClient redissonClient;
    @Resource
    private SteamAlipayIsvMapper steamAlipayIsvMapper;
    @Autowired
    private DataSourceTransactionManager transactionManager;
    @Autowired
    private TransactionDefinition transactionDefinition;
    @Resource
    private ConfigService configService;

    public void notifyDefault(Long channelId, Map<String, String> params) throws AlipayApiException {
        log.info("chanelId:{},params:{}", channelId, params);

        if (Objects.equals(params.get("personal_product_code"), "FUND_SAFT_SIGN_WITHHOLDING_P")
                && Objects.equals(params.get("sign_scene"), "INDUSTRY|SATF_ACC")) {
            safePaySigned(channelId, params);
        }



        if (Objects.equals("open_app_auth_notify", params.get("msg_method"))
                || Objects.equals("open_app_auth_notify", params.get("notify_type"))) {
            batchPaySigned(channelId, params);
        }

        if (Objects.equals(params.get("msg_method"), "alipay.fund.batch.order.changed")) {
            JSONObject bizContent = JSONUtil.parseObj(params.get("biz_content"));
            if (Objects.equals(bizContent.getStr("product_code"), "BATCH_API_TO_ACC")
                    && Objects.equals(bizContent.getStr("biz_scene"), "MESSAGE_BATCH_PAY")) {
                batchPayed(channelId, params);
            }

        }

        if (Objects.equals(params.get("msg_method"), "alipay.fund.trans.order.changed")) {
            JSONObject bizContent = JSONUtil.parseObj(params.get("biz_content"));
            if (Objects.equals(bizContent.getStr("product_code"), "FUND_ACCOUNT_BOOK")
                    && Objects.equals(bizContent.getStr("biz_scene"), "SATF_DEPOSIT")) {
                accountBookChanged(channelId, params);
            }

            if (Objects.equals(bizContent.getStr("product_code"), "SINGLE_TRANSFER_NO_PWD")
                    && Objects.equals(bizContent.getStr("biz_scene"), "ENTRUST_TRANSFER")) {
                accountBookPayed(channelId, params);
            }

        }
    }


    public void batchPayed(Long channelId, Map<String, String> params) {
        JSONObject bizContent = JSONUtil.parseObj(params.get("biz_content"));


        PayOrderRespDTO notify = new PayOrderRespDTO();
        notify.setOutTradeNo(bizContent.getStr("out_batch_no"));
        notify.setRawData(params);
        if (Objects.equals("SUCCESS", bizContent.get("batch_status"))) {
            notify.setStatus(PayOrderStatusRespEnum.SUCCESS.getStatus());
        } else {
            notify.setStatus(PayOrderStatusRespEnum.WAITING.getStatus());
        }

        payOrderService.notifyOrder(channelId, notify);

    }


    public void batchPaySigned(Long channelId, Map<String, String> params) {
        JSONObject bizContent = JSONUtil.parseObj(params.get("biz_content"));
        String outBizNo = bizContent.getJSONObject("notify_context").getJSONObject("trigger_context").getStr("out_biz_no");

        SteamAlipayIsvDO steamAlipayIsvDO = steamAlipayIsvMapper.selectOne("isv_biz_id", outBizNo);

        if ((!ObjectUtil.isEmpty(steamAlipayIsvDO)) && (ObjectUtil.isEmpty(steamAlipayIsvDO.getAppAuthToken()))) {
            steamAlipayIsvMapper.update(new UpdateWrapper<SteamAlipayIsvDO>()
                    .eq("isv_biz_id", outBizNo)
                    .set("app_auth_token", bizContent.getJSONObject("detail").getStr("app_auth_token"))
                    .set("app_id", bizContent.getJSONObject("detail").getStr("app_id"))
                    .set("auth_app_id", bizContent.getJSONObject("detail").getStr("auth_app_id"))
                    .set("user_id", bizContent.getJSONObject("detail").getStr("user_id"))
            );
        }
    }

    public void accountBookPayed(Long channelId, Map<String, String> params) {
        JSONObject bizContent = JSONUtil.parseObj(params.get("biz_content"));
        PayOrderRespDTO notify = new PayOrderRespDTO();
        notify.setOutTradeNo(bizContent.getStr("out_biz_no"));
        notify.setRawData(params);
        if (Objects.equals("SUCCESS", bizContent.getStr("status"))) {
            notify.setStatus(PayOrderStatusRespEnum.SUCCESS.getStatus());
        } else {
            notify.setStatus(PayOrderStatusRespEnum.WAITING.getStatus());
        }

        payOrderService.notifyOrder(channelId, notify);

    }


    public void accountBookChanged(Long channelId, Map<String, String> params) throws AlipayApiException {

        JSONObject bizContent = JSONUtil.parseObj(params.get("biz_content"));

        String orderId = bizContent.getStr("out_biz_no");
        if (Objects.equals(bizContent.get("status"), "SUCCESS")) {
            PayChannelDO channel = channelService.getChannel(58L);
            // 支付渠道构建
            PayClientFactory payClientFactory = new PayClientFactoryImpl();

            payClientFactory.createOrUpdatePayClient(channel.getId(), PayChannelEnum.ALIPAY_AQF.getCode(), channel.getConfig());
            // 构建公共支付对象
            AlipaySafePayClient payClient = (AlipaySafePayClient) payClientFactory.getPayClient(channel.getId());

            AlipayClient client=payClient.getClient();
            AlipayFundTransUniTransferModel model = new AlipayFundTransUniTransferModel();

            model.setOutBizNo(orderId);
            model.setOrderTitle(orderId + ":记账本支付");
            model.setTransAmount(bizContent.getStr("trans_amount"));
            model.setProductCode("SINGLE_TRANSFER_NO_PWD");
            model.setBizScene("ENTRUST_TRANSFER");
            model.setBusinessParams("{\"payer_show_name_use_alias\":\"true\"}");

            Participant payer = new Participant();
            Participant payee = new Participant();

            payer.setIdentityType("ACCOUNT_BOOK_ID");

            JSONObject businessParams = JSONUtil.parseObj(bizContent.get("passback_params"));

            payer.setIdentity(businessParams.getStr("account_book_id"));
            payer.setExtInfo("{\"agreement_no\":\"" + businessParams.get("book_agreement_no") + "\"}");

            ConfigDO configAlipayName = configService.getConfigByKey("alipay.name");
            ConfigDO configAlipayUserId = configService.getConfigByKey("alipay.ALIPAY_USER_ID");
            payee.setIdentity(configAlipayUserId.getValue());
            payee.setIdentityType("ALIPAY_USER_ID");
            payee.setName(configAlipayName.getValue());

            model.setPayeeInfo(payee);
            model.setPayerInfo(payer);


            AlipayFundTransUniTransferRequest request = new AlipayFundTransUniTransferRequest();

            request.setBizModel(model);
            AlipayFundTransUniTransferResponse response;

            response = client.certificateExecute(request);
            log.info("支付宝 账本支付：{}", response.getBody());

        } else {

        }
    }

    public void safePaySigned(Long channelId, Map<String, String> params) {
        String externalAgreementNo = params.get("external_agreement_no");
        if (Objects.equals(params.get("status"), "NORMAL")) {

            if (!ObjectUtil.isEmpty(externalAgreementNo)) {
                SteamAlipayAqfSignDO steamAlipayAqfSignDO = new SteamAlipayAqfSignDO();
                steamAlipayAqfSignDO.setAlipayLogonId(params.get("alipay_login_id"));
//                steamAlipayAqfSignDO.setSignTime(params.get("sign_time"));
                steamAlipayAqfSignDO.setStatus(params.get("status"));
                steamAlipayAqfSignDO.setAgreementNo(params.get("agreement_no"));
//                steamAlipayAqfSignDO.setValidTime(params.get("valid_time"));
                steamAlipayAqfSignMapper.update(steamAlipayAqfSignDO, new UpdateWrapper<SteamAlipayAqfSignDO>()
                        .eq("external_agreement_no", externalAgreementNo));

            }
        } else {
            SteamAlipayAqfSignDO steamAlipayAqfSignDO = new SteamAlipayAqfSignDO();
            steamAlipayAqfSignDO.setStatus(params.get("status"));
            steamAlipayAqfSignMapper.update(steamAlipayAqfSignDO, new UpdateWrapper<SteamAlipayAqfSignDO>()
                    .eq("external_agreement_no", externalAgreementNo));
            throw new ServiceException(500, "fail");
        }
    }


}
