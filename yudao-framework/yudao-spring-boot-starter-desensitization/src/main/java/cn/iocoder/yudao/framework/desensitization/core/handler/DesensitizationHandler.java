package cn.iocoder.yudao.framework.desensitization.core.handler;


import cn.iocoder.yudao.framework.desensitization.core.annotation.Desensitization;

import java.util.function.BiFunction;

/**
 * @author VampireAchao
 * @since 2022/10/6 12:52
 */
public interface DesensitizationHandler extends BiFunction<String, Desensitization, String> {

    /**
     * apply the desensitization
     *
     * @param value           origin value
     * @param desensitization annotation
     * @return after desensitization
     */
    @Override
    String apply(String value, Desensitization desensitization);
}
