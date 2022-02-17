package cn.iocoder.yudao.framework.covert.handler.params;

import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.iocoder.yudao.framework.covert.annotation.LoadUser;
import com.fasterxml.jackson.databind.BeanProperty;

import java.util.Collections;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 多值String参数处理器
 * <p>
 * 如你的字段类型是String，且具备逗号分割的数据类型，此处理器将值分割为多个
 */
public class UserParamsHandler implements ParamsHandler {

    @Override
    public Object handleVal(Object params) {
        if (ObjectUtil.isEmpty(params)) {
            return null;
        }
        String paramsStr = String.valueOf(params);
        String[] userIds = paramsStr.split(",");
        return Stream.of(userIds).filter(NumberUtil::isLong).map(Long::valueOf).collect(Collectors.toList());
    }

    @Override
    public Object[] handleAnnotation(BeanProperty property) {
        // 用户注解值处理
        LoadUser loadDict = property.getAnnotation(LoadUser.class);
        if (loadDict != null) {
            return new Object[]{loadDict.batch()};
        }
        return null;
    }
}
