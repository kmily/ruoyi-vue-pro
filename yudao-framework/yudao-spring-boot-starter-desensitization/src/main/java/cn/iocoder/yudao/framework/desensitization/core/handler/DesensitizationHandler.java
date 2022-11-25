package cn.iocoder.yudao.framework.desensitization.core.handler;


import cn.iocoder.yudao.framework.desensitization.core.annotation.Desensitization;

import java.util.function.BiFunction;

/**
 * 脱敏处理器，当字段包含脱敏注解，且当前值不为空时执行
 *
 * @author VampireAchao
 * @since 2022/10/6 12:52
 */
public interface DesensitizationHandler extends BiFunction<String, Desensitization, String> {

    /**
     * 执行脱敏处理，返回脱敏后的字符串
     *
     * @param value           需要脱敏的值
     * @param desensitization 你的注解
     * @return 脱敏后的字符串
     */
    @Override
    String apply(String value, Desensitization desensitization);
}
