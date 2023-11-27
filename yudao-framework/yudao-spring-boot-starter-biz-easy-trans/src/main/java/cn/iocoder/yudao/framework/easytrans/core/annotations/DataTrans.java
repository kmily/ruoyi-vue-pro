package cn.iocoder.yudao.framework.easytrans.core.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 数据翻译注解用于字段指定翻译相关参数
 * @author HUIHUI
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface DataTrans {

    /**
     * 类型：指定处理器类型
     */
    String type();

    String ref();

}
