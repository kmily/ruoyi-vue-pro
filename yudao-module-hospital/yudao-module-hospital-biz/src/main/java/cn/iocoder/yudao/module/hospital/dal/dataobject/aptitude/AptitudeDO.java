package cn.iocoder.yudao.module.hospital.dal.dataobject.aptitude;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 资质信息 DO
 *
 * @author 芋道源码
 */
@TableName("hospital_aptitude")
@KeySequence("hospital_aptitude_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AptitudeDO extends BaseDO {

    /**
     * 收藏编号
     */
    @TableId
    private Long id;
    /**
     * 资质名称
     */
    private String name;
    /**
     * 资质图标
     */
    private String picUrl;
    /**
     * 状态（0正常 1停用）
     */
    private Byte status;
    /**
     * 备注
     */
    private String remark;

}
