package cn.iocoder.yudao.module.hospital.dal.dataobject.medicalcare;

import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 医护管理 DO
 *
 * @author 芋道源码
 */
@TableName(value = "hospital_medical_care", autoResultMap = true)
@KeySequence("hospital_medical_care_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MedicalCareDO extends BaseDO {

    /**
     * 编号
     */
    @TableId
    private Long id;
    /**
     * 组织编号
     */
    private Long orgId;

    /**
     * 会员编号
     */
    private Long memberId;

    /**
     * 姓名
     */
    private String name;

    /**
     * 身份证号
     */
    private String idCard;
    /**
     * 身份证图片[正,反]
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<String> cardPath;
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
     * 职称
     */
    private Byte title;
    /**
     * 所属机构
     */
    private String organization;
    /**
     * 服务人数
     */
    private Integer quantity;
    /**
     * 服务时长，单位分钟
     */
    private Integer timeLength;
    /**
     * 从业时间
     */
    private LocalDateTime practiceTime;
    /**
     * 主要擅长
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private Set<String> genius;
    /**
     * 是否完善 NO-未实名，YES-实名
     */
    private String perfect;
    /**
     * 是否资质认证 NO-未实名，YES-实名
     */
    private String aptitude;
    /**
     * 个人简介
     */
    private String introduction;
    /**
     * 最终审核状态
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
    /**
     * 是否是店主
     */
    private Boolean shopkeeper;
    /**
     * 是否是超级管理员 超级管理员/普通管理员
     */
    private Boolean isSuper;
    /**
     * 是否可以提现
     */
    private Byte cashOut;

    /**
     * 邮箱
     */
    private String email;
    /**
     * 来源 来源 0-自册, 1-后台添加
     */
    private String source;

    /**
     * 头像路径
     */
    private String avatar;
}
