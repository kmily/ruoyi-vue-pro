package cn.iocoder.yudao.framework.covert.handler.params;

/**
 * 参数处理类
 */
public interface ParamsHandler<T> {

    T handle(Object params);

    String getCacheKey(Object params);
}
