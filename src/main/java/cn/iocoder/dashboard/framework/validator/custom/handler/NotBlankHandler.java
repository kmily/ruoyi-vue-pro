package cn.iocoder.dashboard.framework.validator.custom.handler;

import cn.hutool.core.util.StrUtil;
import cn.iocoder.dashboard.framework.validator.custom.ValidateAnnotationHandler;

import javax.validation.constraints.NotBlank;

/**
 * 非空字符校验处理类
 *
 * @author zzf
 * @date 2021/4/15 9:21
 */
public class NotBlankHandler implements ValidateAnnotationHandler<NotBlank> {

    private static final String MSG = "格式错误或者没有有效字符!";

    @Override
    public Class<NotBlank> getAnnotation() {
        return NotBlank.class;
    }

    @Override
    public String validate(NotBlank validateAnnotation, Object fieldValue) {
        if(StrUtil.isBlankIfStr(fieldValue)) {
            return MSG;
        }
        return null;
    }

}
