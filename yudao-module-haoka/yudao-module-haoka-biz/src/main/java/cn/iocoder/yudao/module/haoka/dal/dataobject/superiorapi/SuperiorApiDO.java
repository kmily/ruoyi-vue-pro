package cn.iocoder.yudao.module.haoka.dal.dataobject.superiorapi;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 上游API接口 DO
 *
 * @author 芋道源码
 */
@TableName("haoka_superior_api")
@KeySequence("haoka_superior_api_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SuperiorApiDO extends BaseDO {

    /**
     * ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
    /**
     * 名字
     */
    private String name;
    /**
     * 是否有选号功能
     *
     * 枚举 {@link TODO infra_boolean_string 对应的类}
     */
    private Boolean enableSelectNum;
    /**
     * 异常订单处理方式
     *
     * 枚举 {@link TODO abnormal_order_handle_method 对应的类}
     */
    private Integer abnormalOrderHandleMethod;
    /**
     * 接口状态
     *
     * 枚举 {@link TODO haoka_superior_api_status 对应的类}
     */
    private Integer status;
    /**
     * 发布日期
     */
    private LocalDateTime publishTime;
    /**
     * API文档
     */
    private String apiDoc;
    /**
     * 是否已配置开发
     *
     * 枚举 {@link TODO infra_boolean_string 对应的类}
     */
    private Boolean isDevConfined;
    /**
     * 是否已配置产品
     *
     * 枚举 {@link TODO infra_boolean_string 对应的类}
     */
    private Boolean isSkuConfined;
    /**
     * 部门ID
     */
    private Long deptId;

}
