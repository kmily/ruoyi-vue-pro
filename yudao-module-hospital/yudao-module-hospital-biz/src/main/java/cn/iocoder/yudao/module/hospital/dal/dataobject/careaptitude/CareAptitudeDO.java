package cn.iocoder.yudao.module.hospital.dal.dataobject.careaptitude;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 医护资质 DO
 *
 * @author 芋道源码
 */
@TableName("hospital_care_aptitude")
@KeySequence("hospital_care_aptitude_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CareAptitudeDO extends BaseDO {

    /**
     * 编号
     */
    @TableId
    private Long id;
    /**
     * 资质编号
     */
    private Long aptitudeId;
    /**
     * 资质名称
     */
    private String aptitudeName;
    /**
     * 医护编号
     */
    private Long careId;
    /**
     * 证书正面
     */
    private String imageFront;
    /**
     * 证书反面
     */
    private String imageBack;
    /**
     * 最终审核人
     */
    private String checkName;
    /**
     * 最终审核时间
     */
    private LocalDateTime checkTime;
    /**
     * 最终审核状态
     */
    private String checkStatus;
    /**
     * 审核意见
     */
    private String opinion;

}
