package cn.iocoder.yudao.framework.datapermission.core.rule.uni;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ConcurrentHashSet;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.iocoder.yudao.framework.common.enums.UserTypeEnum;
import cn.iocoder.yudao.framework.common.util.collection.CollectionUtils;
import cn.iocoder.yudao.framework.common.util.json.JsonUtils;
import cn.iocoder.yudao.framework.datapermission.core.rule.DataPermissionRule;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import cn.iocoder.yudao.framework.mybatis.core.util.MyBatisUtils;
import cn.iocoder.yudao.framework.security.core.LoginUser;
import cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils;
import cn.iocoder.yudao.module.system.api.permission.PermissionApi;
import cn.iocoder.yudao.module.system.api.permission.dto.DeptDataPermissionRespDTO;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.expression.Alias;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.expression.NullValue;
import net.sf.jsqlparser.expression.operators.conditional.OrExpression;
import net.sf.jsqlparser.expression.operators.relational.EqualsTo;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;
import net.sf.jsqlparser.expression.operators.relational.InExpression;
import net.sf.jsqlparser.expression.operators.relational.ParenthesedExpressionList;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 通用的 {@link DataPermissionRule} 实现类
 * <p>
 * ！！！注意！！！
 * 1、使用 {@link UniDataPermissionRuleImpl#addDataColumn} 时，需要保证表中有指定的字段。
 * 2、删改之前，必须先调用 {@code XxxServiceImpl#validateXxxExists} 基于数据权限过滤确保数据存在 。
 *
 * @author 山野羡民
 */
@RequiredArgsConstructor
@Slf4j
public class UniDataPermissionRuleImpl implements UniDataPermissionRule {

    /**
     * LoginUser 的 Context 缓存 Key
     */
    public static final String CONTEXT_KEY = UniDataPermissionRuleImpl.class.getSimpleName();
    public static final String CONTEXT_KEY_SCOPE = UniDataPermissionRuleImpl.class.getSimpleName() + "#SCOPE";
    /**
     * WHERE 语句中添加 AND NULL
     */
    public static final Expression EXPRESSION_NULL = new NullValue();

    /**
     * 基于数据的表字段配置，key-表名，value-字段名。
     * 只支持一个数据权限字段，多字段共存方案可参考多租户的 tenant_id。
     * 比如，基于用户的数据编号字段是 user_id，基于部门的数据编号字段是 dept_id，基于店铺的数据编号字段是 shop_id，
     * dept_id 及 user_id 和其他自定义数据编号字段（如 shop_id）不能同时配置，默认自定义数据编号字段优先较高。
     */
    private final static Map<String, String> DATA_COLUMN_NAMES = new ConcurrentHashMap<>();
    private final static Map<String, String> DATA_COLUMN_ALIAS = new ConcurrentHashMap<>();
    /**
     * 基于用户的表字段配置，key-表名，value-字段名。
     * 一般情况下，每个表的数据编号字段是 user_id，通过该配置自定义，如 admin_user_id。
     */
    private final static Map<String, String> USER_COLUMN_NAMES = new ConcurrentHashMap<>();
    /**
     * 所有表名，是 {@link #DATA_COLUMN_NAMES} 和 {@link #USER_COLUMN_NAMES} 的合集
     */
    private final static Set<String> TABLE_NAMES = new ConcurrentHashSet<>();

    private final PermissionApi permissionApi;
    private final List<UniDataPermissionScope> permissionScopes;

    @Override
    public Set<String> getTableNames() {
        return TABLE_NAMES;
    }

    @Override
    public Expression getExpression(String tableName, Alias tableAlias) {
        // 只有有登录用户的情况下，才进行数据权限的处理
        LoginUser loginUser = SecurityFrameworkUtils.getLoginUser();
        if (loginUser == null) {
            log.debug("[getExpression][LoginUser为null 未登录的情况忽略数据权限处理]");
            return null;
        }

        // 只有管理员类型的用户，才进行自定义数据权限的处理，普通用户直接通过 user_id 进行数据权限过滤即可
        if (ObjectUtil.notEqual(loginUser.getUserType(), UserTypeEnum.ADMIN.getValue())) {
            log.debug("[getExpression][LoginUser({}) 登录的不是管理员类型的用户忽略数据权限处理]", JsonUtils.toJsonString(loginUser));
            return null;
        }

        // 自定义数据筛选条件，如：{"shop_id":[1,2]}
        UniDataPermissionScopeMap scopeMap = loginUser.getContext(CONTEXT_KEY_SCOPE, UniDataPermissionScopeMap.class);
        if (scopeMap == null) {
            scopeMap = new UniDataPermissionScopeMap();
            for (UniDataPermissionScope permissionScope : permissionScopes) {
                scopeMap.put(permissionScope.getScopeKey(), permissionScope.getScopeValues());
            }
            loginUser.setContext(CONTEXT_KEY_SCOPE, scopeMap);
        }
        Expression dataExpression = buildDataExpression(tableName, tableAlias, scopeMap);
        log.debug("[getExpression][LoginUser({}) 获取自定义数据权限为 {}]", JsonUtils.toJsonString(loginUser), JsonUtils.toJsonString(scopeMap));
        if (dataExpression != null) {
            return dataExpression;
        }

        // 获得部门数据权限
        DeptDataPermissionRespDTO deptDataPermission = loginUser.getContext(CONTEXT_KEY, DeptDataPermissionRespDTO.class);
        // 从上下文中拿不到，则调用逻辑进行获取
        if (deptDataPermission == null) {
            deptDataPermission = permissionApi.getDeptDataPermission(loginUser.getId());
            if (deptDataPermission == null) {
                throw new NullPointerException(String.format("LoginUser(%d) Table(%s/%s) 未返回部门数据权限",
                        loginUser.getId(), tableName, tableAlias.getName()));
            }
            // 添加到上下文中，避免重复计算
            loginUser.setContext(CONTEXT_KEY, deptDataPermission);
        }
        log.debug("[getExpression][LoginUser({}) 获取部门数据权限为 {}]", JsonUtils.toJsonString(loginUser), JsonUtils.toJsonString(deptDataPermission));

        // 情况一，如果是 ALL 可查看全部部门，则无需拼接条件
        if (deptDataPermission.getAll()) {
            return null;
        }

        // 情况二，即不能查看部门的数据，又不能查看自己的数据，则说明 100% 无权限
        if (CollUtil.isEmpty(deptDataPermission.getDeptIds())
                && Boolean.FALSE.equals(deptDataPermission.getSelf())) {
            return new EqualsTo(null, null); // WHERE null = null，可以保证返回的数据为空
        }

        // 情况三，拼接部门表和用户表的组合条件
        Expression deptExpression = buildDeptExpression(tableName, tableAlias, deptDataPermission.getDeptIds());
        Expression userExpression = buildUserExpression(tableName, tableAlias, deptDataPermission.getSelf(), loginUser.getId());
        if (deptExpression == null && userExpression == null) {
            // 芋艿：获得不到条件的时候，暂时不抛出异常，而是不返回数据
            log.warn("[getExpression][Table({}/{}) LoginUser({}) DeptDataPermission({}) 构建的查询条件为空]",
                    tableName, tableAlias, JsonUtils.toJsonString(loginUser), JsonUtils.toJsonString(deptDataPermission));
            return EXPRESSION_NULL;
        }
        if (deptExpression == null) {
            return userExpression;
        }
        if (userExpression == null) {
            return deptExpression;
        }
        // 目前，如果有指定数据 + 可查看自己，采用 OR 条件。即，WHERE (shop_id IN ? OR user_id = ?)
        return new ParenthesedExpressionList<>(new OrExpression(deptExpression, userExpression));
    }

    @Override
    public void addDataColumn(Class<? extends BaseDO> entityClass, String columnName, String columnAlias) {
        String tableName = getTableName(entityClass);
        addDataColumn(tableName, columnName, columnAlias);
    }

    @Override
    public void addDataColumn(String tableName, String columnName, String columnAlias) {
        if (CharSequenceUtil.isEmpty(tableName)) {
            return;
        }
        DATA_COLUMN_NAMES.put(tableName, columnName);
        DATA_COLUMN_ALIAS.put(tableName, columnAlias);
        TABLE_NAMES.add(tableName);

    }

    @Override
    public void addDataColumn(String tableName, String columnName) {
        addDataColumn(tableName, columnName, columnName);
    }

    @Override
    public void addUserColumn(Class<? extends BaseDO> entityClass, String columnName) {
        String tableName = getTableName(entityClass);
        addUserColumn(tableName, columnName);
    }

    @Override
    public void addUserColumn(String tableName, String columnName) {
        if (CharSequenceUtil.isEmpty(tableName)) {
            return;
        }
        USER_COLUMN_NAMES.put(tableName, columnName);
        TABLE_NAMES.add(tableName);
    }

    private Expression buildDataExpression(String tableName, Alias tableAlias, UniDataPermissionScopeMap scopeMap) {
        // 如果不存在配置，则无需作为条件
        String columnName = DATA_COLUMN_NAMES.get(tableName);
        String columnAlias = DATA_COLUMN_ALIAS.get(tableName);
        log.info("[buildDataExpression] tableName({}/{}) columnName({}) columnAlias({}) scopeMap({})", tableName, tableAlias, columnName, columnAlias, scopeMap);
        if (StrUtil.isEmpty(columnAlias) || MapUtil.isEmpty(scopeMap)) {
            return null;
        }
        Set<Long> emptySet = CollUtil.newHashSet();
        Set<Long> dataIds = scopeMap.getOrDefault(columnAlias, emptySet);
        // 过滤掉 dataIds 里的 null 值，如：{"shop_id":[null]}
        List<Long> dataIdList = dataIds.stream().filter(Objects::nonNull).toList();
        if (CollUtil.isEmpty(dataIdList)) {
            return null;
        }
        // 拼接条件
        if (dataIds.size() == 1) {
            return new EqualsTo(MyBatisUtils.buildColumn(tableName, tableAlias, columnName), new LongValue(dataIdList.get(0)));
        }
        return new InExpression(MyBatisUtils.buildColumn(tableName, tableAlias, columnName),
                // Parenthesis 的目的，是提供 (1,2,3) 的 () 左右括号
                new ParenthesedExpressionList<>(new ExpressionList<>(CollectionUtils.convertList(dataIdList, LongValue::new))));
    }

    private Expression buildDeptExpression(String tableName, Alias tableAlias, Set<Long> deptIds) {
        UniDataPermissionScopeMap scopeMap = new UniDataPermissionScopeMap();
        scopeMap.put("dept_id", deptIds);
        return buildDataExpression(tableName, tableAlias, scopeMap);
    }

    private Expression buildUserExpression(String tableName, Alias tableAlias, Boolean self, Long userId) {
        String columnName = USER_COLUMN_NAMES.get(tableName);
        log.info("[buildUserExpression] tableName({}/{}) columnName({})", tableName, tableAlias, columnName);
        // 如果不查看自己，则无需作为条件
        if (Boolean.FALSE.equals(self) || StrUtil.isEmpty(columnName)) {
            return null;
        }
        // 拼接条件
        return new EqualsTo(MyBatisUtils.buildColumn(tableName, tableAlias, columnName), new LongValue(userId));
    }

    private String getTableName(Class<? extends BaseDO> entityClass) {
        TableInfo tableInfo = TableInfoHelper.getTableInfo(entityClass);
        if (tableInfo == null) {
            log.warn("从实体类({})获取数据库表信息为 null", entityClass.getSimpleName());
            return null;
        }
        return tableInfo.getTableName();
    }

}
