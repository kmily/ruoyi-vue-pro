package cn.iocoder.yudao.module.system.dal.dataobject.area;

import lombok.*;

import java.math.BigDecimal;
import java.util.*;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 行政区域 DO
 *
 * @author 芋道源码
 */
@TableName("system_area")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AreaDO extends BaseDO {

    /**
     * 自增主键id
     */
    @TableId
    private Long id;
    /**
     * 数据版本
     */
    private String dataVersion;
    /**
     * 省直辖市编码
     */
    private String provinceCode;
    /**
     * 省直辖市
     */
    private String province;
    /**
     * 市编码
     */
    private String cityCode;
    /**
     * 市
     */
    private String city;
    /**
     * 区县编码
     */
    private String countyCode;
    /**
     * 区县
     */
    private String county;
    /**
     * 乡镇编码
     */
    private String townCode;
    /**
     * 乡镇街道
     */
    private String town;
    /**
     * 村社区编码
     */
    private String villageCode;
    /**
     * 村,社区
     */
    private String village;
    /**
     * 经度
     */
    private BigDecimal longitude;
    /**
     * 纬度
     */
    private BigDecimal latitude;
    /**
     * 使用标识 1使用 0不使用
     */
    private Integer useFlag;

}
