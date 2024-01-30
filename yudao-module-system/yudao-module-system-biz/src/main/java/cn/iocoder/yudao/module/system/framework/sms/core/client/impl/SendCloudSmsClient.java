package cn.iocoder.yudao.module.system.framework.sms.core.client.impl;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.DigestUtil;
import cn.hutool.crypto.digest.HmacAlgorithm;
import cn.iocoder.yudao.framework.common.core.KeyValue;
import cn.iocoder.yudao.framework.common.util.json.JsonUtils;
import cn.iocoder.yudao.module.system.framework.sms.core.client.dto.SmsReceiveRespDTO;
import cn.iocoder.yudao.module.system.framework.sms.core.client.dto.SmsSendRespDTO;
import cn.iocoder.yudao.module.system.framework.sms.core.client.dto.SmsTemplateRespDTO;
import cn.iocoder.yudao.module.system.framework.sms.core.enums.SmsTemplateAuditStatusEnum;
import cn.iocoder.yudao.module.system.framework.sms.core.property.SmsChannelProperties;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;

import java.util.*;

/**
 * 腾讯云短信功能实现
 *
 * 参见 <a href="https://cloud.tencent.com/document/product/382/52077">文档</a>
 *
 * @author shiwp
 */
public class SendCloudSmsClient extends AbstractSmsClient {

    public SendCloudSmsClient(SmsChannelProperties properties) {
        super(properties);
        Assert.notEmpty(properties.getApiKey(), "apiKey 不能为空");
        Assert.notEmpty(properties.getApiSecret(), "apiSecret 不能为空");
    }

    @Override
    protected void doInit() {
    }

    @Override
    public SmsSendRespDTO sendSms(Long sendLogId, String mobile,
                                  String apiTemplateId, List<KeyValue<String, Object>> templateParams) throws Throwable {
        String url = "https://api.sendcloud.net/smsapi/send";
        // 填充参数
        Map<String, String> params = new HashMap<String, String>();
        params.put("smsUser", properties.getApiKey());
        params.put("templateId", "1");
        params.put("msgType", "0");
        params.put("phone", mobile);
        params.put("vars", "{\"code\":\"123455\"}");

        // 对参数进行排序
        Map<String, String> sortedMap = new TreeMap<String, String>(new Comparator<String>() {

            public int compare(String arg0, String arg1) {
                // 忽略大小写
                return arg0.compareToIgnoreCase(arg1);
            }
        });
        sortedMap.putAll(params);

        // 计算签名
        StringBuilder sb = new StringBuilder();
        sb.append(properties.getApiSecret()).append("&");
        for (String s : sortedMap.keySet()) {
            sb.append(String.format("%s=%s&", s, sortedMap.get(s)));
        }
        sb.append(properties.getApiSecret());
        String sig = DigestUtils.md5Hex(sb.toString());

        // 将所有参数和签名添加到post请求参数数组里
        List<NameValuePair> postparams = new ArrayList<NameValuePair>();
        for (String s : sortedMap.keySet()) {
            postparams.add(new BasicNameValuePair(s, sortedMap.get(s)));
        }
        postparams.add(new BasicNameValuePair("signature", sig));

        HttpPost httpPost = new HttpPost(url);
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(postparams, "utf8"));

            DefaultHttpClient httpclient = new DefaultHttpClient();
            httpclient.getParams().setIntParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 3000);
            httpclient.getParams().setIntParameter(CoreConnectionPNames.SO_TIMEOUT, 100000);
            HttpResponse response = httpclient.execute(httpPost);

            HttpEntity entity = response.getEntity();
            Map<?, ?> responseObj = JsonUtils.parseObject(EntityUtils.toString(response.getEntity()), Map.class);
            String errorCode = MapUtil.getStr(responseObj, "statusCode");
            return new SmsSendRespDTO().setSuccess(Objects.equals(errorCode, "200")).setSerialNo(StrUtil.uuid())
                .setApiCode(errorCode).setApiMsg(MapUtil.getStr(responseObj, "message"));
//            System.out.println(EntityUtils.toString(response.getEntity()));
        } catch (Exception e) {
            System.out.println(e.toString());
        } finally {
            httpPost.releaseConnection();
        }

        return null;
//        // 构建请求
//        String url = buildUrl("robot/send");
//        Map<String, Object> params = new HashMap<>();
//        params.put("msgtype", "text");
//        String content = String.format("【模拟短信】\n手机号：%s\n短信日志编号：%d\n模板参数：%s",
//                mobile, sendLogId, MapUtils.convertMap(templateParams));
//        params.put("text", MapUtil.builder().put("content", content).build());
//        // 执行请求
//        String responseText = HttpUtil.post(url, JsonUtils.toJsonString(params));
//        // 解析结果
//        Map<?, ?> responseObj = JsonUtils.parseObject(responseText, Map.class);
//        String errorCode = MapUtil.getStr(responseObj, "errcode");
//        return new SmsSendRespDTO().setSuccess(Objects.equals(errorCode, "0")).setSerialNo(StrUtil.uuid())
//                .setApiCode(errorCode).setApiMsg(MapUtil.getStr(responseObj, "errorMsg"));
    }

    /**
     * 构建请求地址
     *
     * 参见 <a href="https://developers.dingtalk.com/document/app/custom-robot-access/title-nfv-794-g71">文档</a>
     *
     * @param path 请求路径
     * @return 请求地址
     */
    @SuppressWarnings("SameParameterValue")
    private String buildUrl(String path) {
        // 生成 timestamp
        long timestamp = System.currentTimeMillis();
        // 生成 sign
        String secret = properties.getApiSecret();
        String stringToSign = timestamp + "\n" + secret;
        byte[] signData = DigestUtil.hmac(HmacAlgorithm.HmacSHA256, StrUtil.bytes(secret)).digest(stringToSign);
        String sign = Base64.encode(signData);
        // 构建最终 URL
        return String.format("https://api.sendcloud.net/smsapi/send%s?access_token=%s&timestamp=%d&sign=%s",
                path, properties.getApiKey(), timestamp, sign);
    }

    @Override
    public List<SmsReceiveRespDTO> parseSmsReceiveStatus(String text) {
        throw new UnsupportedOperationException("模拟短信客户端，暂时无需解析回调");
    }

    @Override
    public SmsTemplateRespDTO getSmsTemplate(String apiTemplateId) {
        return new SmsTemplateRespDTO().setId(apiTemplateId).setContent("")
                .setAuditStatus(SmsTemplateAuditStatusEnum.SUCCESS.getStatus()).setAuditReason("");
    }

}
