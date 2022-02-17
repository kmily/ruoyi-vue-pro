package cn.iocoder.yudao.framework.covert.annotation;

import cn.iocoder.yudao.framework.covert.handler.params.UserParamsHandler;
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
     * 是否批量
     * 如果批量返回集合, 否则返回对象
     *
     * @return
     */
    boolean batch() default false;

}
