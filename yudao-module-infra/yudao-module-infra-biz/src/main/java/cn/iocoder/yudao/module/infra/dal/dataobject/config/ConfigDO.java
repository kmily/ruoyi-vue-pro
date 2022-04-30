package cn.iocoder.yudao.module.infra.dal.dataobject.config;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import cn.iocoder.yudao.module.infra.enums.config.ConfigTypeEnum;
import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 参数配置表
 * @author 芋道源码
 */
@TableName(value = "INFRA_CONFIG", autoResultMap = true)
@KeySequence(value = "SEQ_INFRA_CONFIG",dbType = DbType.ORACLE)
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ConfigDO extends BaseDO {

    /**
     * 参数主键
     */
    @TableId
    private Long id;
    /**
     * 参数分组
     */
    @TableField("\"GROUP\"")
    private String group;
    /**
     * 参数名称
     */
    private String name;
    /**
     * 参数键名
     */
    @TableField("CONFIG_KEY")
    private String key;
    /**
     * 参数键值
     */
    private String value;
    /**
     * 参数类型
     * <p>
     * 枚举 {@link ConfigTypeEnum}
     */
    private Integer type;
    /**
     * 是否敏感
     * <p>
     * 对于敏感配置，需要管理权限才能查看
     */
    private Boolean sensitive;
    /**
     * 备注
     */
    private String remark;

}
