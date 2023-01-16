package cn.iocoder.yudao.module.scan.controller.app.report.vo;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@ApiModel("扫描APP - 报告分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class AppReportPageReqVO extends PageParam {

    @ApiModelProperty(value = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

    @ApiModelProperty(value = "报告编号")
    private String code;

    @ApiModelProperty(value = "姓名", example = "王五")
    private String name;

    @ApiModelProperty(value = "性别")
    private Integer sex;

    @ApiModelProperty(value = "身高，厘米")
    private Integer height;

    @ApiModelProperty(value = "身体年龄，岁")
    private Integer age;

    @ApiModelProperty(value = "健康评分")
    private Integer healthScore;

    @ApiModelProperty(value = "高低肩：左右肩关节Y坐标差值")
    private Integer highLowShoulders;

    @ApiModelProperty(value = "头侧歪：头和脖子")
    private Integer headAskew;

    @ApiModelProperty(value = "左腿XOKD：X型 O型 K型 D型腿")
    private String leftLegKokd;

    @ApiModelProperty(value = "右腿XOKD：X型 O型 K型 D型腿")
    private String rightLegKokd;

    @ApiModelProperty(value = "长短腿：左右髋关节Y坐标差值")
    private Integer longShortLeg;

    @ApiModelProperty(value = "头部前倾：左耳和脖子")
    private Integer pokingChin;

    @ApiModelProperty(value = "骨盆前倾：脖子和左髋关节与右髋关节中点,左踝关节与右踝关节中点")
    private Integer pelvicAnteversion;

    @ApiModelProperty(value = "左膝盖分析：前曲 超伸")
    private String leftKneeAnalysis;

    @ApiModelProperty(value = "右膝盖分析：前曲 超伸")
    private String rightKneeAnalysis;

    @ApiModelProperty(value = "左圆肩：肩和肩峰")
    private String leftRoundShoulder;

    @ApiModelProperty(value = "右圆肩：肩和肩峰")
    private String rightRoundShoulder;

    @ApiModelProperty(value = "重心左右倾斜：人体重心和左踝关节与右踝关节中点")
    private String focusTiltLeftRight;

    @ApiModelProperty(value = "重心前后倾斜：人体重心和左踝关节与右踝关节中点")
    private String focusTiltForthBack;

    @ApiModelProperty(value = "体重")
    private Integer weight;

    @ApiModelProperty(value = "蛋白率")
    private Integer proteinRate;

    @ApiModelProperty(value = "骨量水分率")
    private Integer boneMassMositureContent;

    @ApiModelProperty(value = "脂肪率")
    private Integer fatPercentage;

    @ApiModelProperty(value = "肌肉率")
    private Integer musclePercentage;

    @ApiModelProperty(value = "骨格肌量")
    private Integer skeletalMuscleVolume;

    @ApiModelProperty(value = "BMI")
    private Integer bmi;

    @ApiModelProperty(value = "内脏脂肪等级")
    private Integer visceralFatGrade;

    @ApiModelProperty(value = "理想体重")
    private Integer idealWeight;

    @ApiModelProperty(value = "身体年龄")
    private Integer physicalAge;

    @ApiModelProperty(value = "基础代谢率")
    private Integer basalMetabolicRate;

    @ApiModelProperty(value = "胸围")
    private Integer bust;

    @ApiModelProperty(value = "腰围")
    private Integer waistline;

    @ApiModelProperty(value = "臀围")
    private Integer hipline;

    @ApiModelProperty(value = "左臀围")
    private Integer leftHipline;

    @ApiModelProperty(value = "右臀围")
    private Integer rightHipline;

    @ApiModelProperty(value = "左大腿围")
    private Integer leftThigCircumference;

    @ApiModelProperty(value = "右大腿围")
    private Integer rightThigCircumference;

    @ApiModelProperty(value = "左小腿围")
    private Integer leftCalfCircumference;

    @ApiModelProperty(value = "右小腿围")
    private Integer rightCalfCircumference;

    @ApiModelProperty(value = "模型文件id", example = "19515")
    private Integer modelFileId;

    @ApiModelProperty(value = "扫描用户id", example = "28752")
    private Long userId;

    @ApiModelProperty(value = "体型值")
    private String shapeValue;

    @ApiModelProperty(value = "体型id", example = "24000")
    private Long shapeId;

    @ApiModelProperty(value = "报告文件路径")
    private String reportFilePath;
}
