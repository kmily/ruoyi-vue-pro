package cn.iocoder.yudao.module.scan.dal.dataobject.appversion;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 应用版本记录 DO
 *
 * @author lyz
 */
@TableName("scan_app_version")
@KeySequence("scan_app_version_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppVersionDO extends BaseDO {

    /**
     * 图片上传地址，有何用？
     */
    private String fileUrl;
    /**
     * Apk公钥信息	
     */
    private String pubKey;
    /**
     * 上线时间，新增记录时，
     */
    private LocalDateTime onlineTime;
    /**
     * 下线时间，下线时更新，但可能用不到
     */
    private LocalDateTime offlineTime;
    /**
     * id
     */
    @TableId
    private Long id;
    /**
     * 0-pc 1-IOS 2-安卓
     */
    private Integer sysType;
    /**
     * 文件包名称，带版本号，带后缀名	
     */
    private String fileName;
    /**
     * 版本号
     */
    private String verNo;
    /**
     * 是否强制升级,0否1是
     */
    private Boolean forceUpdate;
    /**
     * 审核状态，0待审核，1审核通过，2审核不通过
     */
    private Integer auditState;
    /**
     * 备注说明
     */
    private String remark;
    /**
     * 版本描述信息
     */
    private String description;
    /**
     * 软件包哈希值
     */
    private String packageHash;
    /**
     * 版本状态0:正常1注销
     */
    private Boolean state;
    /**
     * 版本序号
     */
    private Integer verCode;

}
