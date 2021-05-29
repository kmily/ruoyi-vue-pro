package cn.iocoder.dashboard.framework.validator.custom.handler;

import cn.iocoder.dashboard.framework.validator.custom.ValidateAnnotationHandler;
import org.slf4j.helpers.MessageFormatter;

import javax.validation.constraints.Min;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

/**
 * 最小值校验处理类
 *
 * @author zzf
 * @date 2021/4/15 9:21
 */
public class MinHandler implements ValidateAnnotationHandler<Min> {

    private static final String MSG = "的值必须大于{}!";

    @Override
    public Class<Min> getAnnotation() {
        return Min.class;
    }

    @Override
    public String validate(Min validateAnnotation, Object fieldValue) {
        if (Objects.isNull(fieldValue)) {
            return null;
        }

        long value = validateAnnotation.value();
        boolean valid;
        if (fieldValue instanceof Integer) {
            valid = value < (Integer) fieldValue;
        } else if (fieldValue instanceof BigDecimal | fieldValue instanceof Double | fieldValue instanceof Short) {
            valid = new BigDecimal(String.valueOf(value)).compareTo(new BigDecimal(String.valueOf(fieldValue))) < 0;
        } else if (fieldValue instanceof Long) {
            valid = value < (Long) fieldValue;
        } else if (fieldValue instanceof Date) {
            valid = value < ((Date) fieldValue).getTime();
        } else {
            valid = true;
        }
        if (!valid) {
            MessageFormatter.format(MSG, value).getMessage();
        }
        return null;
    }

}
