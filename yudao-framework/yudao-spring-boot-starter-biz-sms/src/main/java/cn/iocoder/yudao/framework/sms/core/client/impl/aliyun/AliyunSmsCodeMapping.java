package cn.iocoder.yudao.framework.sms.core.client.impl.aliyun;

import cn.iocoder.yudao.framework.common.exception.ErrorCode;
import cn.iocoder.yudao.framework.common.exception.enums.GlobalErrorCodeConstants;
import cn.iocoder.yudao.framework.sms.core.client.SmsCodeMapping;
import cn.iocoder.yudao.framework.sms.core.enums.SmsFrameworkErrorCodeConstants;

/**
 * 阿里云的 SmsCodeMapping 实现类
 *
 * 参见 https://help.aliyun.com/document_detail/101346.htm 文档
 *
 * @author 芋道源码
 */
public class AliyunSmsCodeMapping implements SmsCodeMapping {

    @Override
    public ErrorCode apply(String apiCode) {
        return switch (apiCode) {
            case "OK" -> GlobalErrorCodeConstants.SUCCESS;
            case "isv.ACCOUNT_NOT_EXISTS", "isv.ACCOUNT_ABNORMAL", "MissingAccessKeyId" ->
                    SmsFrameworkErrorCodeConstants.SMS_ACCOUNT_INVALID;
            case "isp.RAM_PERMISSION_DENY" -> SmsFrameworkErrorCodeConstants.SMS_PERMISSION_DENY;
            case "isv.INVALID_JSON_PARAM", "isv.INVALID_PARAMETERS" ->
                    SmsFrameworkErrorCodeConstants.SMS_API_PARAM_ERROR;
            case "isv.BUSINESS_LIMIT_CONTROL" -> SmsFrameworkErrorCodeConstants.SMS_SEND_BUSINESS_LIMIT_CONTROL;
            case "isv.DAY_LIMIT_CONTROL" -> SmsFrameworkErrorCodeConstants.SMS_SEND_DAY_LIMIT_CONTROL;
            case "isv.SMS_CONTENT_ILLEGAL" -> SmsFrameworkErrorCodeConstants.SMS_SEND_CONTENT_INVALID;
            case "isv.SMS_TEMPLATE_ILLEGAL" -> SmsFrameworkErrorCodeConstants.SMS_TEMPLATE_INVALID;
            case "isv.SMS_SIGNATURE_ILLEGAL", "isv.SIGN_NAME_ILLEGAL", "isv.SMS_SIGN_ILLEGAL" ->
                    SmsFrameworkErrorCodeConstants.SMS_SIGN_INVALID;
            case "isv.AMOUNT_NOT_ENOUGH", "isv.OUT_OF_SERVICE" ->
                    SmsFrameworkErrorCodeConstants.SMS_ACCOUNT_MONEY_NOT_ENOUGH;
            case "isv.MOBILE_NUMBER_ILLEGAL" -> SmsFrameworkErrorCodeConstants.SMS_MOBILE_INVALID;
            case "isv.TEMPLATE_MISSING_PARAMETERS" -> SmsFrameworkErrorCodeConstants.SMS_TEMPLATE_PARAM_ERROR;
            default -> SmsFrameworkErrorCodeConstants.SMS_UNKNOWN;
        };
    }

}
