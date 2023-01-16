package cn.iocoder.yudao.module.scan.controller.admin.report.vo;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import io.swagger.annotations.*;
import javax.validation.constraints.*;

/**
 * 报告 Base VO，提供给添加、修改、详细的子 VO 使用
 * 如果子 VO 存在差异的字段，请不要添加到这里，影响 Swagger 文档生成
 */
@Data
public class ReportBaseVO {

    @ApiModelProperty(value = "报告编号", required = true)
    @NotNull(message = "报告编号不能为空")
    private String code;

    @ApiModelProperty(value = "姓名", example = "王五")
    private String name;

    @ApiModelProperty(value = "性别")
    private Integer sex;

    @ApiModelProperty(value = "身高，厘米")
    private Integer height;

    @ApiModelProperty(value = "身体年龄，岁")
    private Integer age;

    @ApiModelProperty(value = "健康评分", required = true)
    @NotNull(message = "健康评分不能为空")
    private Integer healthScore;

    @ApiModelProperty(value = "高低肩：左右肩关节Y坐标差值", required = true)
    @NotNull(message = "高低肩：左右肩关节Y坐标差值不能为空")
    private Integer highLowShoulders;

    @ApiModelProperty(value = "头侧歪：头和脖子", required = true)
    @NotNull(message = "头侧歪：头和脖子不能为空")
    private Integer headAskew;

    @ApiModelProperty(value = "左腿XOKD：X型 O型 K型 D型腿", required = true)
    @NotNull(message = "左腿XOKD：X型 O型 K型 D型腿不能为空")
    private String leftLegKokd;

    @ApiModelProperty(value = "右腿XOKD：X型 O型 K型 D型腿", required = true)
    @NotNull(message = "右腿XOKD：X型 O型 K型 D型腿不能为空")
    private String rightLegKokd;

    @ApiModelProperty(value = "长短腿：左右髋关节Y坐标差值", required = true)
    @NotNull(message = "长短腿：左右髋关节Y坐标差值不能为空")
    private Integer longShortLeg;

    @ApiModelProperty(value = "头部前倾：左耳和脖子", required = true)
    @NotNull(message = "头部前倾：左耳和脖子不能为空")
    private Integer pokingChin;

    @ApiModelProperty(value = "骨盆前倾：脖子和左髋关节与右髋关节中点,左踝关节与右踝关节中点", required = true)
    @NotNull(message = "骨盆前倾：脖子和左髋关节与右髋关节中点,左踝关节与右踝关节中点不能为空")
    private Integer pelvicAnteversion;

    @ApiModelProperty(value = "左膝盖分析：前曲 超伸", required = true)
    @NotNull(message = "左膝盖分析：前曲 超伸不能为空")
    private String leftKneeAnalysis;

    @ApiModelProperty(value = "右膝盖分析：前曲 超伸", required = true)
    @NotNull(message = "右膝盖分析：前曲 超伸不能为空")
    private String rightKneeAnalysis;

    @ApiModelProperty(value = "左圆肩：肩和肩峰", required = true)
    @NotNull(message = "左圆肩：肩和肩峰不能为空")
    private String leftRoundShoulder;

    @ApiModelProperty(value = "右圆肩：肩和肩峰", required = true)
    @NotNull(message = "右圆肩：肩和肩峰不能为空")
    private String rightRoundShoulder;

    @ApiModelProperty(value = "重心左右倾斜：人体重心和左踝关节与右踝关节中点", required = true)
    @NotNull(message = "重心左右倾斜：人体重心和左踝关节与右踝关节中点不能为空")
    private String focusTiltLeftRight;

    @ApiModelProperty(value = "重心前后倾斜：人体重心和左踝关节与右踝关节中点", required = true)
    @NotNull(message = "重心前后倾斜：人体重心和左踝关节与右踝关节中点不能为空")
    private String focusTiltForthBack;

    @ApiModelProperty(value = "体重", required = true)
    @NotNull(message = "体重不能为空")
    private Integer weight;

    @ApiModelProperty(value = "蛋白率", required = true)
    @NotNull(message = "蛋白率不能为空")
    private Integer proteinRate;

    @ApiModelProperty(value = "骨量水分率", required = true)
    @NotNull(message = "骨量水分率不能为空")
    private Integer boneMassMositureContent;

    @ApiModelProperty(value = "脂肪率", required = true)
    @NotNull(message = "脂肪率不能为空")
    private Integer fatPercentage;

    @ApiModelProperty(value = "肌肉率", required = true)
    @NotNull(message = "肌肉率不能为空")
    private Integer musclePercentage;

    @ApiModelProperty(value = "骨格肌量", required = true)
    @NotNull(message = "骨格肌量不能为空")
    private Integer skeletalMuscleVolume;

    @ApiModelProperty(value = "BMI", required = true)
    @NotNull(message = "BMI不能为空")
    private Integer bmi;

    @ApiModelProperty(value = "内脏脂肪等级", required = true)
    @NotNull(message = "内脏脂肪等级不能为空")
    private Integer visceralFatGrade;

    @ApiModelProperty(value = "理想体重", required = true)
    @NotNull(message = "理想体重不能为空")
    private Integer idealWeight;

    @ApiModelProperty(value = "身体年龄", required = true)
    @NotNull(message = "身体年龄不能为空")
    private Integer physicalAge;

    @ApiModelProperty(value = "基础代谢率", required = true)
    @NotNull(message = "基础代谢率不能为空")
    private Integer basalMetabolicRate;

    @ApiModelProperty(value = "胸围", required = true)
    @NotNull(message = "胸围不能为空")
    private Integer bust;

    @ApiModelProperty(value = "腰围", required = true)
    @NotNull(message = "腰围不能为空")
    private Integer waistline;

    @ApiModelProperty(value = "臀围", required = true)
    @NotNull(message = "臀围不能为空")
    private Integer hipline;

    @ApiModelProperty(value = "左臀围", required = true)
    @NotNull(message = "左臀围不能为空")
    private Integer leftHipline;

    @ApiModelProperty(value = "右臀围", required = true)
    @NotNull(message = "右臀围不能为空")
    private Integer rightHipline;

    @ApiModelProperty(value = "左大腿围", required = true)
    @NotNull(message = "左大腿围不能为空")
    private Integer leftThigCircumference;

    @ApiModelProperty(value = "右大腿围", required = true)
    @NotNull(message = "右大腿围不能为空")
    private Integer rightThigCircumference;

    @ApiModelProperty(value = "左小腿围", required = true)
    @NotNull(message = "左小腿围不能为空")
    private Integer leftCalfCircumference;

    @ApiModelProperty(value = "右小腿围", required = true)
    @NotNull(message = "右小腿围不能为空")
    private Integer rightCalfCircumference;

    @ApiModelProperty(value = "模型文件id", required = true, example = "19515")
    @NotNull(message = "模型文件id不能为空")
    private Integer modelFileId;

    @ApiModelProperty(value = "扫描用户id", required = true, example = "28752")
    @NotNull(message = "扫描用户id不能为空")
    private Long userId;

    @ApiModelProperty(value = "体型值", required = true)
    @NotNull(message = "体型值不能为空")
    private String shapeValue;

    @ApiModelProperty(value = "体型id", required = true, example = "24000")
    @NotNull(message = "体型id不能为空")
    private Long shapeId;

    @ApiModelProperty(value = "报告文件路径", required = true)
    @NotNull(message = "报告文件路径不能为空")
    private String reportFilePath;

}
