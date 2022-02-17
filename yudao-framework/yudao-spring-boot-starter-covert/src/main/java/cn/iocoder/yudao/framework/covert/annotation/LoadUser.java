package cn.iocoder.yudao.framework.covert.annotation;

import cn.iocoder.yudao.framework.covert.handler.params.LongParamsHandler;
import cn.iocoder.yudao.framework.covert.handler.params.StringParamsHandler;
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
@Load(bean = "adminUserService", method = "getUser", paramsHandler = LongParamsHandler.class)
public @interface LoadUser {

}
