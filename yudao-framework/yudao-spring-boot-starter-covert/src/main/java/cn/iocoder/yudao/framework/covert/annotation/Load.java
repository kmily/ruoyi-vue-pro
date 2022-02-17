package cn.iocoder.yudao.framework.covert.annotation;

import cn.iocoder.yudao.framework.covert.handler.params.DefaultParamsHandler;
import cn.iocoder.yudao.framework.covert.handler.params.ParamsHandler;
import cn.iocoder.yudao.framework.covert.handler.rsp.DefaultResponseHandler;
import cn.iocoder.yudao.framework.covert.handler.rsp.ResponseHandler;
import cn.iocoder.yudao.framework.covert.serializer.LoadSerializer;
import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.lang.annotation.*;

/**
 * 翻译
 *
 * @author 625
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Inherited
@JacksonAnnotationsInside
@JsonSerialize(using = LoadSerializer.class)
public @interface Load {

    /**
     * spring操作的BeanName
     *
     * @return
     */
    String bean();

    /**
     * bean方法
     *
     * @return
     */
    String method() default "load";

    /**
     * 缓存时间
     *
     * @return
     */
    int cacheSecond() default 600;

    /**
     * 额外参数
     * <p>
     * 参数格式，多个参数，按照反射参数占位符传递
     *
     * @return
     */
    String[] args() default {};

    /**
     * 参数处理器
     *
     * @return
     */
    Class<? extends ParamsHandler> paramsHandler() default DefaultParamsHandler.class;

    /**
     * 返回结果处理类
     *
     * @return
     */
    Class<? extends ResponseHandler> responseHandler() default DefaultResponseHandler.class;
}
