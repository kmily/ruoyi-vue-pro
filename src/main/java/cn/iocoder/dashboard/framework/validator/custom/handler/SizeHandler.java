package cn.iocoder.dashboard.framework.validator.custom.handler;

import org.slf4j.helpers.MessageFormatter;

import javax.validation.constraints.Size;

/**
 * <p>长度校验处理类
 *
 * @author xyf
 * @date 2021/5/13
 */
public class SizeHandler implements ValidateAnnotationHandler<Size> {

    /**
     * 小于等于
     */
    private static final String VALUE_LT = "长度必须大于等于{}!";
    /**
     * 大于等于
     */
    private static final String VALUE_GT = "长度必须小于等于{}!";

    @Override
    public Class<Size> getAnnotation() {
        return Size.class;
    }

    @Override
    public String validate(Size validateAnnotation, Object fieldValue) {
        if (fieldValue.toString().length() > validateAnnotation.max()) {
            return MessageFormatter.format(VALUE_GT, fieldValue).getMessage();
        }

        if (fieldValue.toString().length() < validateAnnotation.min()) {
            return MessageFormatter.format(VALUE_LT, fieldValue).getMessage();
        }

        return null;
    }

}