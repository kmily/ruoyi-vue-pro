package cn.iocoder.yudao.module.crm.util;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.extra.spring.SpringUtil;
import cn.iocoder.yudao.module.crm.dal.dataobject.permission.CrmPermissionDO;
import cn.iocoder.yudao.module.crm.enums.common.CrmBizTypeEnum;
import cn.iocoder.yudao.module.crm.enums.common.CrmSceneTypeEnum;
import cn.iocoder.yudao.module.crm.enums.permission.CrmPermissionLevelEnum;
import cn.iocoder.yudao.module.crm.enums.permission.CrmPermissionRoleCodeEnum;
import cn.iocoder.yudao.module.system.api.permission.PermissionApi;
import cn.iocoder.yudao.module.system.api.user.AdminUserApi;
import cn.iocoder.yudao.module.system.api.user.dto.AdminUserRespDTO;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.github.yulichang.autoconfigure.MybatisPlusJoinProperties;
import com.github.yulichang.wrapper.MPJLambdaWrapper;

import java.util.Collection;
import java.util.List;

import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.convertSet;

/**
 * CRM 查询工具类
 *
 * @author HUIHUI
 */
public class CrmQueryWrapperUtils {

    /**
     * 构造 CRM 数据类型数据分页查询条件
     *
     * @param query     连表查询对象
     * @param bizType   数据类型 {@link CrmBizTypeEnum}
     * @param bizId     数据编号
     * @param userId    用户编号
     * @param sceneType 场景类型
     * @param pool      公海
     */
    public static <T extends MPJLambdaWrapper<?>, S> void appendPermissionCondition(T query, Integer bizType, SFunction<S, ?> bizId,
                                                                                    Long userId, Integer sceneType, Boolean pool) {
        final String ownerUserIdField = SingletonManager.getMybatisPlusJoinProperties().getTableAlias() + ".owner_user_id";
        // 1. 构建数据权限连表条件
        if (ObjUtil.notEqual(validateAdminUser(userId), Boolean.TRUE) && ObjUtil.notEqual(pool, Boolean.TRUE)) { // 管理员，公海不需要数据权限
            query.innerJoin(CrmPermissionDO.class, on -> on.eq(CrmPermissionDO::getBizType, bizType)
                    .eq(CrmPermissionDO::getBizId, bizId) // 只能使用 SFunction 如果传 id 解析出来的 sql 不对
                    .eq(CrmPermissionDO::getUserId, userId));
        }
        // 2.1 场景一：我负责的数据
        if (CrmSceneTypeEnum.isOwner(sceneType)) {
            query.eq(ownerUserIdField, userId);
        }
        // 2.2 场景二：我参与的数据
        if (CrmSceneTypeEnum.isInvolved(sceneType)) {
            query.ne(ownerUserIdField, userId)
                    .in(CrmPermissionDO::getLevel, CrmPermissionLevelEnum.READ.getLevel(), CrmPermissionLevelEnum.WRITE.getLevel());
        }
        // 2.3 场景三：下属负责的数据
        if (CrmSceneTypeEnum.isSubordinate(sceneType)) {
            List<AdminUserRespDTO> subordinateUsers = SingletonManager.getAdminUserApi().getUserListBySubordinate(userId);
            if (CollUtil.isEmpty(subordinateUsers)) {
                query.eq(ownerUserIdField, -1); // 不返回任何结果
            } else {
                query.in(ownerUserIdField, convertSet(subordinateUsers, AdminUserRespDTO::getId));
            }
        }

        // 3. 拼接公海的查询条件
        if (ObjUtil.equal(pool, Boolean.TRUE)) { // 情况一：公海
            query.isNull(ownerUserIdField);
        } else { // 情况二：不是公海
            query.isNotNull(ownerUserIdField);
        }
    }

    /**
     * 构造 CRM 数据类型批量数据查询条件
     *
     * @param query   连表查询对象
     * @param bizType 数据类型 {@link CrmBizTypeEnum}
     * @param bizIds  数据编号
     * @param userId  用户编号
     */
    public static <T extends MPJLambdaWrapper<?>> void appendPermissionCondition(T query, Integer bizType, Collection<Long> bizIds, Long userId) {
        if (ObjUtil.equal(validateAdminUser(userId), Boolean.TRUE)) {// 管理员不需要数据权限
            return;
        }

        query.innerJoin(CrmPermissionDO.class, on ->
                on.eq(CrmPermissionDO::getBizType, bizType).in(CrmPermissionDO::getBizId, bizIds)
                        .in(CollUtil.isNotEmpty(bizIds), CrmPermissionDO::getUserId, userId));
    }

    /**
     * 校验用户是否是 CRM 管理员
     *
     * @param userId 用户编号
     * @return 是/否
     */
    private static boolean validateAdminUser(Long userId) {
        return SingletonManager.getPermissionApi().hasAnyRoles(userId, CrmPermissionRoleCodeEnum.CRM_ADMIN.getCode());
    }

    /**
     * 静态内部类实现单例获取
     *
     * @author HUIHUI
     */
    private static class SingletonManager {

        private static final AdminUserApi ADMIN_USER_API = SpringUtil.getBean(AdminUserApi.class);
        private static final PermissionApi PERMISSION_API = SpringUtil.getBean(PermissionApi.class);
        private static final MybatisPlusJoinProperties MYBATIS_PLUS_JOIN_PROPERTIES = SpringUtil.getBean(MybatisPlusJoinProperties.class);

        public static AdminUserApi getAdminUserApi() {
            return ADMIN_USER_API;
        }

        public static PermissionApi getPermissionApi() {
            return PERMISSION_API;
        }

        public static MybatisPlusJoinProperties getMybatisPlusJoinProperties() {
            return MYBATIS_PLUS_JOIN_PROPERTIES;
        }

    }

}
