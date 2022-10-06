package cn.iocoder.yudao.framework.desensitization.core.handler.impl;

import cn.hutool.core.util.DesensitizedUtil;
import cn.hutool.core.util.StrUtil;
import cn.iocoder.yudao.framework.desensitization.core.annotation.Desensitization;
import cn.iocoder.yudao.framework.desensitization.core.handler.DesensitizationHandler;

/**
 * @author VampireAchao
 * @since 2022/10/6 12:54
 */
public class DesensitizationDefaultHandler implements DesensitizationHandler {

    /**
     * apply the desensitization
     *
     * @param value           origin value
     * @param desensitization annotation
     * @return after desensitization
     */
    @Override
    public String apply(String value, Desensitization desensitization) {
        String regex = desensitization.regex();
        if (StrUtil.isBlank(regex)) {
            return DesensitizedUtil.desensitized(value, desensitization.type());
        }
        return value.replaceAll(desensitization.regex(), "*");
    }
}
