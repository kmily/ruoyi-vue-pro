package cn.iocoder.yudao.framework.datatranslation.core.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * CRM 数据操作权限校验 AOP 注解
 *
 * @author HUIHUI
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface DataTranslation {

}
