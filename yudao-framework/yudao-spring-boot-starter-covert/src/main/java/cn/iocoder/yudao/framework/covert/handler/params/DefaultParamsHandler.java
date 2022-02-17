package cn.iocoder.yudao.framework.covert.handler.params;

/**
 * 单值long参数处理器
 * <p>
 * 如你的字段类型是Long，且直接就是参数，不需要处理，用这个
 */
public class DefaultParamsHandler implements ParamsHandler<Object> {

    @Override
    public Object handle(Object params) {
        return params;
    }

    @Override
    public String getCacheKey(Object params) {
        return String.valueOf(params);
    }
}
