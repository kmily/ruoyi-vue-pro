package cn.iocoder.yudao.module.hospital.dal.dataobject.organization;

import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 组织机构 DO
 *
 * @author 芋道源码
 */
@TableName(value = "hospital_organization", autoResultMap = true)
@KeySequence("hospital_organization_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrganizationDO extends BaseDO {

    /**
     * ID
     */
    @TableId
    private Long id;
    /**
     * 会员Id
     */
    private Long userId;
    /**
     * 会员名称
     */
    private String userName;
    /**
     * 是否自营
     */
    private Boolean selfOperated;
    /**
     * 是否允许员工提现
     */
    private Byte cashOut;
    /**
     * 机构状态
     */
    private String disable;
    /**
     * 机构logo
     */
    private String logo;
    /**
     * 机构名称
     */
    private String name;
    /**
     * 机构地址
     */
    private String addressDetail;
    /**
     * 地址id
     */
    private String addressIdPath;
    /**
     * 地址名称
     */
    private String addressPath;
    /**
     * 经纬度
     */
    private String center;
    /**
     * 机构简介
     */
    private String introduction;
    /**
     * 物流评分
     */
    private Double deliveryScore;
    /**
     * 描述评分
     */
    private Double descriptionScore;
    /**
     * 服务评分
     */
    private Double serviceScore;
    /**
     * 员工数量
     */
    private Integer staffNum;
    /**
     * 商品数量
     */
    private Integer goodsNum;
    /**
     * 收藏数量
     */
    private Integer collectionNum;
    /**
     * 宣传图册
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<String> images;
    /**
     * 联系方式
     */
    private String mobile;
    /**
     * 邮箱
     */
    private String email;

    /**
     * 审核人
     */
    private String checkName;

    /**
     * 审核时间
     */
    private LocalDateTime checkTime;

    /**
     * 审核状态
     */
    private String checkStatus;

    /**
     * 审核意见
     */
    private String opinion;

}
