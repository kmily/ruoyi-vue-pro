package cn.iocoder.yudao.module.member.dal.dataobject.serverperson;

import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.*;

import java.time.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 被服务人 DO
 *
 * @author 芋道源码
 */
@TableName(value = "member_server_person", autoResultMap = true)
@KeySequence("member_server_person_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ServerPersonDO extends BaseDO {

    /**
     * ID
     */
    @TableId
    private Long id;
    /**
     * 用户编号
     */
    private Long memberId;
    /**
     * 被服务人姓名
     */
    private String name;
    /**
     * 身份证号
     */
    private String idCard;
    /**
     * 联系方式
     */
    private String mobile;
    /**
     * 出生日期
     */
    private LocalDate birthday;
    /**
     * 用户性别
     *
     * 枚举 {@link TODO system_user_sex 对应的类}
     */
    private Byte sex;
    /**
     * 紧急联系人
     */
    private String critical;
    /**
     * 是否实名 NO-未实名，YES-实名
     */
    private String realname;
    /**
     * 医保卡正面
     */
    private String medicalCardFront;
    /**
     * 医保卡反面
     */
    private String medicalCardBack;
    /**
     * 病历资料路径 [xxx, xxx]
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<String> medicalRecord;
    /**
     * 特殊情况路径[xxx，xxxx]
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<String> special;
    /**
     * 状态
     */
    private String status;
    /**
     * 最终审核人
     */
    private String checkName;
    /**
     * 最终审核时间
     */
    private LocalDateTime checkTime;
    /**
     * 审核意见
     */
    private String opinion;

}
