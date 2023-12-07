package cn.iocoder.yudao.module.hospital.dal.dataobject.carebankcard;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 提现银行卡 DO
 *
 * @author 管理人
 */
@TableName("hospital_care_bank_card")
@KeySequence("hospital_care_bank_card_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CareBankCardDO extends BaseDO {

    /**
     * ID
     */
    @TableId
    private Long id;
    /**
     * 用户编号
     */
    private Long careId;
    /**
     * 持卡人
     */
    private String name;
    /**
     * 卡号
     */
    private String cardNo;
    /**
     * 开户行
     */
    private String bank;
    /**
     * 身份证
     */
    private String idCard;
    /**
     * 预留手机号
     */
    private String mobile;
    /**
     * 是否默认
     */
    private Boolean defaultStatus;

}
