package cn.iocoder.yudao.module.scan.controller.app.appversion.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

/**
* 应用版本记录 Base VO，提供给添加、修改、详细的子 VO 使用
* 如果子 VO 存在差异的字段，请不要添加到这里，影响 Swagger 文档生成
*/
@Data
public class AppAppVersionBaseVO {


    @ApiModelProperty(value = "图片上传地址，有何用？", example = "https://www.iocoder.cn")
    private String fileUrl;

    @ApiModelProperty(value = "Apk公钥信息	")
    private String pubKey;

    @ApiModelProperty(value = "上线时间，新增记录时，")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime onlineTime;

    @ApiModelProperty(value = "下线时间，下线时更新，但可能用不到")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime offlineTime;

    @ApiModelProperty(value = "0-pc 1-IOS 2-安卓", example = "1")
    private Integer sysType;

    @ApiModelProperty(value = "文件包名称，带版本号，带后缀名	", example = "张三")
    private String fileName;

    @ApiModelProperty(value = "版本号")
    private String verNo;

    @ApiModelProperty(value = "是否强制升级,0否1是")
    private Boolean forceUpdate;

    @ApiModelProperty(value = "审核状态，0待审核，1审核通过，2审核不通过", required = true)
    @NotNull(message = "审核状态，0待审核，1审核通过，2审核不通过不能为空")
    private Integer auditState;

    @ApiModelProperty(value = "备注说明", example = "随便")
    private String remark;

    @ApiModelProperty(value = "版本描述信息", example = "随便")
    private String description;

    @ApiModelProperty(value = "软件包哈希值")
    private String packageHash;

    @ApiModelProperty(value = "版本状态0:正常1注销", required = true)
    @NotNull(message = "版本状态0:正常1注销不能为空")
    private Boolean state;

    @ApiModelProperty(value = "版本序号", required = true)
    @NotNull(message = "版本序号不能为空")
    private Integer verCode;


}
