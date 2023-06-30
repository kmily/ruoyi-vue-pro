package cn.iocoder.yudao.framework.errorcode.core.loader;

import cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil;

/**
 * 错误码加载器
 *
 * 注意，错误码最终加载到 {@link ServiceExceptionUtil} 的 MESSAGES 变量中！
 *
 * @author dlyan
 */
public interface ErrorCodeLoader {

    /**
     * 添加错误码
     *
     * @param codeLang 错误码的编号_多语言
     * @param msg 错误码的提示
     */
    default void putErrorCode(String codeLang, String msg) {
        ServiceExceptionUtil.put(codeLang, msg);
    }

}
