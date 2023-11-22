package cn.iocoder.yudao.module.hospital.dal.dataobject.medicalcare;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 医护收藏 DO
 *
 * @author 芋道源码
 */
@TableName("hospital_care_favorite")
@KeySequence("hospital_care_favorite_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CareFavoriteDO extends BaseDO {

    /**
     * 收藏编号
     */
    @TableId
    private Long id;
    /**
     * 会员编号
     */
    private Long memberId;
    /**
     * 医护编号
     */
    private Long careId;

}
