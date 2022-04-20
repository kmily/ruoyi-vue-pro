package cn.iocoder.yudao.framework.loader.annotation;

import cn.iocoder.yudao.framework.loader.handler.params.UserParamsHandler;
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
@Load(bean = "adminUserService", method = "loadUsers", paramsHandler = UserParamsHandler.class)
public @interface LoadUser {

    /**
     * 回显到字段, 填写了已填写的为准, 否则自动填充$+被翻译字段
     *
     * @return
     */
    String field() default "";

    /**
     * 是否批量
     * 如果批量返回集合, 否则返回对象
     *
     * @return
     */
    boolean batch() default false;

}
