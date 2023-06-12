package cn.iocoder.yudao.module.oa.dal.dataobject.customer;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 客户 DO
 *
 * @author 东海
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
     */
    private Byte customerType;
    /**
     * 联系人
     */
    private String contactName;
    /**
     * 联系电话
     */
    private String contactPhone;
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
     * 备注
     */
    private String remark;

}
