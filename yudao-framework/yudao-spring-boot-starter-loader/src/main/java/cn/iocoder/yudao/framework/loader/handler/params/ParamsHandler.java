package cn.iocoder.yudao.framework.loader.handler.params;

import cn.hutool.core.convert.Convert;
import cn.hutool.crypto.SecureUtil;
import cn.iocoder.yudao.framework.loader.bo.AnnotationsResult;
import com.fasterxml.jackson.databind.BeanProperty;
import org.apache.commons.lang3.StringUtils;

/**
 * 参数处理类
 */
public interface ParamsHandler {

    /**
     * 处理注解对象的值
     *
     * @param val
     * @return
     */
    Object handleVal(Object val);

    /**
     * 处理注解上的参数
     *
     * @param property
     * @return
     */
    AnnotationsResult handleAnnotation(BeanProperty property);

    /**
     * 获取缓存的key
     *
     * @param val           当前值
     * @param annotationVal 注解值
     * @return
     */
    default String getCacheKey(Object val, Object[] annotationVal) {
        if (annotationVal == null) {
            return val.toString();
        }
        StringBuilder key = new StringBuilder(val.toString());
        for (Object subVal : annotationVal) {
            key.append(subVal);
        }
        return key.toString();
    }
}
