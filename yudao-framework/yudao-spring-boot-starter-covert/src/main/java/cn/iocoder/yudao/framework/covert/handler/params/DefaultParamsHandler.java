package cn.iocoder.yudao.framework.covert.handler.params;

import com.fasterxml.jackson.databind.BeanProperty;

/**
 * 单值long参数处理器
 * <p>
 * 如你的字段类型是Long，且直接就是参数，不需要处理，用这个
 */
public class DefaultParamsHandler implements ParamsHandler {

    @Override
    public Object handleVal(Object val) {
        return val;
    }

    @Override
    public Object[] handleAnnotation(BeanProperty property) {
        return null;
    }
}
