package cn.iocoder.yudao.module.scan.controller.admin.appversion.vo;

import lombok.*;
import java.util.*;
import io.swagger.annotations.*;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import java.time.LocalDateTime;
import org.springframework.format.annotation.DateTimeFormat;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@ApiModel(value = "管理后台 - 应用版本记录 Excel 导出 Request VO", description = "参数和 AppVersionPageReqVO 是一致的")
@Data
public class AppVersionExportReqVO {

    @ApiModelProperty(value = "图片上传地址，有何用？", example = "https://www.iocoder.cn")
    private String fileUrl;

    @ApiModelProperty(value = "Apk公钥信息	")
    private String pubKey;

    @ApiModelProperty(value = "上线时间，新增记录时，")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] onlineTime;

    @ApiModelProperty(value = "下线时间，下线时更新，但可能用不到")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] offlineTime;

    @ApiModelProperty(value = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

    @ApiModelProperty(value = "0-pc 1-IOS 2-安卓", example = "1")
    private Integer sysType;

    @ApiModelProperty(value = "文件包名称，带版本号，带后缀名	", example = "张三")
    private String fileName;

    @ApiModelProperty(value = "版本号")
    private String verNo;

    @ApiModelProperty(value = "是否强制升级,0否1是")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private Boolean[] forceUpdate;

    @ApiModelProperty(value = "审核状态，0待审核，1审核通过，2审核不通过")
    private Integer auditState;

    @ApiModelProperty(value = "备注说明", example = "随便")
    private String remark;

    @ApiModelProperty(value = "版本描述信息", example = "随便")
    private String description;

    @ApiModelProperty(value = "软件包哈希值")
    private String packageHash;

    @ApiModelProperty(value = "版本状态0:正常1注销")
    private Boolean state;

    @ApiModelProperty(value = "版本序号")
    private Integer verCode;

}
