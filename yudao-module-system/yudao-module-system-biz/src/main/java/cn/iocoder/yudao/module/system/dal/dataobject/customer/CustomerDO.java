package cn.iocoder.yudao.module.system.dal.dataobject.customer;

import com.sun.xml.bind.v2.TODO;
import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 客户管理 DO
 *
 * @author 管理员
 */
@TableName("oa_customer")
@KeySequence("oa_customer_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDO extends BaseDO {

    /**
     * 联系人
     */
    private String contactName;
    /**
     * 联系电话
     */
    private String contactPhone;
    /**
     * 详细地址
     */
    private String address;
    /**
     * 开户行
     */
    private String bankName;
    /**
     * 账户
     */
    private String bankAccount;
    /**
     * 税号
     */
    private String taxNumber;
    /**
     * 创建者
     */
    private String createBy;
    /**
     * 更新者
     */
    private String updateBy;
    /**
     * id
     */
    @TableId
    private Long id;
    /**
     * 名称
     */
    private String customerName;
    /**
     * 类型
     *
     * 枚举 {@link TODO oa_customer_type 对应的类}
     */
    private String customerType;
    /**
     * 省
     */
    private String province;
    /**
     * 市
     */
    private String city;
    /**
     * 区/县
     */
    private String district;
    /**
     * 备注
     */
    private String remark;

}
