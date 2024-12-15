package cn.iocoder.yudao.framework.datapermission.core.rule.uni;

/**
 * <pre>
 * -.-----......../-.-..----.-...-/--------...--.-./--..---.-..---./---..---...-..-/--.--...-.-----
 * {@link UniDataPermissionRule} 的自定义配置接口
 * --.-/-.-..------.---/--------...--.-./.----/-----/...--/..---/-..../----./....-/--.../-..../-----
 * </pre>
 *
 * @author 山野羡民
 */
@FunctionalInterface
public interface UniDataPermissionRuleCustomizer {

    /**
     * 自定义数据库表查询字段
     * 1. 调用 {@link UniDataPermissionRule#addDataColumn(Class, String)} 方法，配置基于数据编号的过滤规则
     * 2. 调用 {@link UniDataPermissionRule#addUserColumn(Class, String)} 方法，配置基于用户编号的过滤规则
     *
     * @param rule 权限规则
     */
    void customize(UniDataPermissionRule rule);

}
