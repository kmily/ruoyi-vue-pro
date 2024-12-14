package cn.iocoder.yudao.framework.datapermission.core.rule.uni;

import cn.iocoder.yudao.framework.datapermission.core.rule.DataPermissionRule;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * <pre>
 * -.-----......../-.-..----.-...-/--------...--.-./--..---.-..---./---..---...-..-/--.--...-.-----
 * 数据权限规则接口
 * --.-/-.-..------.---/--------...--.-./.----/-----/...--/..---/-..../----./....-/--.../-..../-----
 * </pre>
 *
 * @author 山野羡民
 */
public interface UniDataPermissionRule extends DataPermissionRule {

    /**
     * 添加数据权限过滤的数据编号列
     *
     * @param entityClass 实体类
     * @param columnName  列名
     * @param columnAlias 列别名，用于指定的数据编号不同于列名的情况，如Dept表的id对应的数据编号别名为dept_id
     */
    void addDataColumn(Class<? extends BaseDO> entityClass, String columnName, String columnAlias);

    /**
     * 添加数据权限过滤的数据编号列
     *
     * @param entityClass 实体类
     * @param columnName  列名
     */
    default void addDataColumn(Class<? extends BaseDO> entityClass, String columnName) {
        addDataColumn(entityClass, columnName, columnName);
    }

    /**
     * 添加数据权限过滤的数据编号列
     *
     * @param tableName   表名
     * @param columnName  列名
     * @param columnAlias 列别名，用于指定的数据编号不同于列名的情况，如Dept表的id对应的数据编号别名为dept_id
     */
    void addDataColumn(String tableName, String columnName, String columnAlias);

    /**
     * 添加数据权限过滤的数据编号列
     *
     * @param tableName  表名
     * @param columnName 列名
     */
    default void addDataColumn(String tableName, String columnName) {
        addDataColumn(tableName, columnName, columnName);
    }

    /**
     * 添加数据权限过滤的用户编号列
     *
     * @param entityClass 实体类
     * @param columnName  列名
     */
    void addUserColumn(Class<? extends BaseDO> entityClass, String columnName);

    /**
     * 添加数据权限过滤的用户编号列
     *
     * @param tableName  表名
     * @param columnName 列名
     */
    void addUserColumn(String tableName, String columnName);

}
