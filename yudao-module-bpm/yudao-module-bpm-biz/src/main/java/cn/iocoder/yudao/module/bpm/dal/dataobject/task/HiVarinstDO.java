package cn.iocoder.yudao.module.bpm.dal.dataobject.task;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.util.Date;

// TODO @ke：具体什么 DO 哈
/**
 * DO
 *
 * @author 芋道
 */
@TableName("act_hi_varinst")
@Data
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HiVarinstDO {

    /**
     *
     */
    @TableId(value = "ID_", type = IdType.NONE)
    private String id;
    /**
     *
     */
    @TableField("REV_")
    private Integer rev;
    /**
     *
     */
    @TableField("PROC_INST_ID_")
    private String procInstId;
    /**
     *
     */
    @TableField("EXECUTION_ID_")
    private String executionId;
    /**
     *
     */
    @TableField("TASK_ID_")
    private String taskId;
    /**
     *
     */
    @TableField("NAME_")
    private String name;
    /**
     *
     */
    @TableField("VAR_TYPE_")
    private String varType;
    /**
     *
     */
    @TableField("SCOPE_ID_")
    private String scopeId;
    /**
     *
     */
    @TableField("SUB_SCOPE_ID_")
    private String subScopeId;
    /**
     *
     */
    @TableField("SCOPE_TYPE_")
    private String scopeType;
    /**
     *
     */
    @TableField("BYTEARRAY_ID_")
    private String bytearrayId;
    /**
     *
     */
    @TableField("DOUBLE_")
    private Double doubleValue;
    /**
     *
     */
    @TableField("LONG_")
    private Long longValue;
    /**
     *
     */
    @TableField("TEXT_")
    private String text;
    /**
     *
     */
    @TableField("TEXT2_")
    private String text2;

    /**
     *
     */
    @TableField("CREATE_TIME_")
    private Date createTime;
    /**
     *
     */
    @TableField("LAST_UPDATED_TIME_")
    private Date lastUpdatedTime;

}
