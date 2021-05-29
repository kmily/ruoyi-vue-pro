package cn.iocoder.dashboard.framework.validator.custom.handler;

import cn.hutool.core.util.ObjectUtil;
import cn.iocoder.dashboard.framework.validator.custom.ValidateAnnotationHandler;

import javax.validation.constraints.NotNull;

/**
 * 必传校验处理类
 *
 * @author zzf
 * @date 2021/4/15 9:21
 */
public class NotNullHandler implements ValidateAnnotationHandler<NotNull> {

    private static final String MSG = "必传!";

    @Override
    public Class<NotNull> getAnnotation() {
        return NotNull.class;
    }

    @Override
    public String validate(NotNull validateAnnotation, Object fieldValue) {
        if (ObjectUtil.isNull(fieldValue)) {
            return MSG;
        }
        return null;
    }
}
