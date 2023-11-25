package cn.iocoder.yudao.framework.common.util;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.Locale;

/**
 * @Description 多语言工具
 * @Author 张家余
 * @CreateDate 2023/6/30  15:22
 */
public class LangUtils {
    /**
     * 获取当前语言，默认中文（zh）
     * @return
     */
    public static String getLang(){
        return ObjectUtil.isNotNull(LocaleContextHolder.getLocale()) ? LocaleContextHolder.getLocale().getLanguage() : getDefaultLang();
    }

    /**
     * 获取默认语言（中文）
     * @return
     */
    public static String getDefaultLang(){
        return Locale.SIMPLIFIED_CHINESE.getLanguage();
    }

    /**
     * 将错误码拼接上当前语言（code_lang）
     * @return
     */
    public static String addSuffix(Integer errorCode){
        return errorCode + StrUtil.UNDERLINE + getLang();
    }
}
