package cn.iocoder.yudao.framework.datapermission.core.rule.uni;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.util.Set;

/**
 * <pre>
 * -.-----......../-.-..----.-...-/--------...--.-./--..---.-..---./---..---...-..-/--.--...-.-----
 * 数据权限范围自定义接口
 * --.-/-.-..------.---/--------...--.-./.----/-----/...--/..---/-..../----./....-/--.../-..../-----
 * </pre>
 *
 * @author 山野羡民
 * @see UniDataPermissionScopeMap
 * @since 2024/11/15
 */
public interface UniDataPermissionScope {

    /**
     * 获取数据权限范围键名，如：shop_id
     */
    @Nonnull
    String getScopeKey();

    /**
     * 获取数据权限范围键值，如：[1,2]
     */
    @Nullable
    Set<Long> getScopeValues();

}
