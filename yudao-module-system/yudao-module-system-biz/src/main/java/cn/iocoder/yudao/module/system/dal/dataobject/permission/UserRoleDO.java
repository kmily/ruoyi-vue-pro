package cn.iocoder.yudao.module.system.dal.dataobject.permission;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 用户和角色关联
 *
 * @author ruoyi
 */
@TableName(value = "SYSTEM_USER_ROLE", autoResultMap = true)
@KeySequence(value = "SEQ_SYSTEM_USER_ROLE",dbType = DbType.ORACLE)
@Data
@EqualsAndHashCode(callSuper = true)
public class UserRoleDO extends BaseDO {

    /**
     * 自增主键
     */
    @TableId
    private Long id;
    /**
     * 用户 ID
     */
    private Long userId;
    /**
     * 角色 ID
     */
    private Long roleId;

}
