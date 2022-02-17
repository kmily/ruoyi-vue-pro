package cn.iocoder.yudao.framework.covert.annotation;

import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;

import java.lang.annotation.*;


/**
 * 翻译用户ID
 *
 * @author 625
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
@Inherited
@JacksonAnnotationsInside
@Load(bean = "dictDataServiceImpl", method = "getDictDataFromCache", cacheSecond = 30)
public @interface LoadDict {

    /**
     * 字典值
     *
     * @return
     */
    String value();
}
