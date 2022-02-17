package cn.iocoder.yudao.framework.covert.handler.params;

import cn.hutool.core.util.ObjectUtil;

/**
 * 多值String参数处理器
 * <p>
 * 如你的字段类型是String，且具备逗号分割的数据类型，此处理器将值分割为多个
 */
public class LongParamsHandler implements ParamsHandler<Long> {

    @Override
    public Long handle(Object params) {
        if (ObjectUtil.isEmpty(params)) {
            return null;
        }
        String paramsStr = String.valueOf(params);
        return Long.valueOf(paramsStr);
    }

    @Override
    public String getCacheKey(Object params) {
        return String.valueOf(params);
    }
}
