package cn.iocoder.yudao.module.scan.dal.dataobject.device;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 设备 DO
 *
 * @author lyz
 */
@TableName("scan_device")
@KeySequence("scan_device_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeviceDO extends BaseDO {

    /**
     * 设备名称
     */
    private String name;
    /**
     * 设备编号
     */
    private String code;
    /**
     * 联系人
     */
    private String contact;
    /**
     * 联系电话
     */
    private String phone;
    /**
     * 激活序号
     */
    private Integer serialNo;
    /**
     * 省名称
     */
    private String provinceName;
    /**
     * 省编号
     */
    private String provinceCode;
    /**
     * 市名称
     */
    private String cityName;
    /**
     * 市编号
     */
    private String cityCode;
    /**
     * 区名称
     */
    private String areaName;
    /**
     * 区编号
     */
    private String areaCode;
    /**
     * 详细地址
     */
    private String address;
    /**
     * 设备管理密码
     */
    private String password;
    /**
     * id
     */
    @TableId
    private Long id;
    /**
     * 门店名称
     */
    private String storeName;

}
