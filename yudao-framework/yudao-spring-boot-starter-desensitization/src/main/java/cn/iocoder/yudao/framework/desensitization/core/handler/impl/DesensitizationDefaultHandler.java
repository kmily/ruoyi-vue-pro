package cn.iocoder.yudao.framework.desensitization.core.handler.impl;

import cn.hutool.core.util.DesensitizedUtil;
import cn.hutool.core.util.StrUtil;
import cn.iocoder.yudao.framework.desensitization.core.annotation.Desensitization;
import cn.iocoder.yudao.framework.desensitization.core.handler.DesensitizationHandler;

/**
 * 默认脱敏处理器
 * 当regex为空时，使用cn.hutool.core.util.DesensitizedUtil内置的脱敏规则，需要指定注解上的type
 * 否则使用正则脱敏，需要指定注解上的regex
 *
 * @author VampireAchao
 * @since 2022/10/6 12:54
 */
public class DesensitizationDefaultHandler implements DesensitizationHandler {

    /**
     * 执行脱敏处理，返回脱敏后的字符串
     *
     * @param value           需要脱敏的值
     * @param desensitization 你的注解
     * @return 脱敏后的字符串
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
