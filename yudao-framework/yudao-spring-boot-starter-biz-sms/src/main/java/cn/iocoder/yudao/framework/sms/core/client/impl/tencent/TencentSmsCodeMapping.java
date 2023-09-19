package cn.iocoder.yudao.framework.sms.core.client.impl.tencent;

import cn.iocoder.yudao.framework.common.exception.ErrorCode;
import cn.iocoder.yudao.framework.common.exception.enums.GlobalErrorCodeConstants;
import cn.iocoder.yudao.framework.sms.core.client.SmsCodeMapping;

import static cn.iocoder.yudao.framework.sms.core.enums.SmsFrameworkErrorCodeConstants.*;

/**
 * 腾讯云的 SmsCodeMapping 实现类
 *
 * 参见 https://cloud.tencent.com/document/api/382/52075#.E5.85.AC.E5.85.B1.E9.94.99.E8.AF.AF.E7.A0.81
 *
 * @author : shiwp
 */
public class TencentSmsCodeMapping implements SmsCodeMapping {

    @Override
    public ErrorCode apply(String apiCode) {
        return switch (apiCode) {
            case TencentSmsClient.API_SUCCESS_CODE -> GlobalErrorCodeConstants.SUCCESS;
            case "FailedOperation.ContainSensitiveWord" -> SMS_SEND_CONTENT_INVALID;
            case "FailedOperation.JsonParseFail", "MissingParameter.EmptyPhoneNumberSet", "LimitExceeded.PhoneNumberCountLimit", "FailedOperation.FailResolvePacket" ->
                    GlobalErrorCodeConstants.BAD_REQUEST;
            case "FailedOperation.InsufficientBalanceInSmsPackage" -> SMS_ACCOUNT_MONEY_NOT_ENOUGH;
            case "FailedOperation.MarketingSendTimeConstraint" -> SMS_SEND_MARKET_LIMIT_CONTROL;
            case "FailedOperation.PhoneNumberInBlacklist" -> SMS_MOBILE_BLACK;
            case "FailedOperation.SignatureIncorrectOrUnapproved" -> SMS_SIGN_INVALID;
            case "FailedOperation.MissingTemplateToModify", "FailedOperation.TemplateIncorrectOrUnapproved" ->
                    SMS_TEMPLATE_INVALID;
            case "InvalidParameterValue.IncorrectPhoneNumber" -> SMS_MOBILE_INVALID;
            case "InvalidParameterValue.SdkAppIdNotExist" -> SMS_APP_ID_INVALID;
            case "InvalidParameterValue.TemplateParameterLengthLimit", "InvalidParameterValue.TemplateParameterFormatError" ->
                    SMS_TEMPLATE_PARAM_ERROR;
            case "LimitExceeded.PhoneNumberDailyLimit" -> SMS_SEND_DAY_LIMIT_CONTROL;
            case "LimitExceeded.PhoneNumberThirtySecondLimit", "LimitExceeded.PhoneNumberOneHourLimit" ->
                    SMS_SEND_BUSINESS_LIMIT_CONTROL;
            case "UnauthorizedOperation.RequestPermissionDeny", "FailedOperation.ForbidAddMarketingTemplates", "FailedOperation.NotEnterpriseCertification", "UnauthorizedOperation.IndividualUserMarketingSmsPermissionDeny" ->
                    SMS_PERMISSION_DENY;
            case "UnauthorizedOperation.RequestIpNotInWhitelist" -> SMS_IP_DENY;
            case "AuthFailure.SecretIdNotFound" -> SMS_ACCOUNT_INVALID;
        };
    }
}