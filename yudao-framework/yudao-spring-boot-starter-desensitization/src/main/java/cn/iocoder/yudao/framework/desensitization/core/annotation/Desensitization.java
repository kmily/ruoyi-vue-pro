package cn.iocoder.yudao.framework.desensitization.core.annotation;

import cn.hutool.core.util.DesensitizedUtil;
import cn.iocoder.yudao.framework.desensitization.core.handler.DesensitizationHandler;
import cn.iocoder.yudao.framework.desensitization.core.handler.impl.DesensitizationDefaultHandler;

import java.lang.annotation.*;

/**
 * @author VampireAchao
 * @since 2022/10/5 22:03
 */
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Desensitization {

    DesensitizedUtil.DesensitizedType type() default DesensitizedUtil.DesensitizedType.MOBILE_PHONE;

    String regex() default "";

    Class<? extends DesensitizationHandler> handler() default DesensitizationDefaultHandler.class;
}
