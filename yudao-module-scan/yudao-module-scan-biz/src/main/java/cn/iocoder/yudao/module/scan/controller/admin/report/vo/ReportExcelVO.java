package cn.iocoder.yudao.module.scan.controller.admin.report.vo;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import io.swagger.annotations.*;

import com.alibaba.excel.annotation.ExcelProperty;

/**
 * 报告 Excel VO
 *
 * @author lyz
 */
@Data
public class ReportExcelVO {

    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

    @ExcelProperty("报告编号")
    private String code;

    @ExcelProperty("姓名")
    private String name;

    @ExcelProperty("性别")
    private Integer sex;

    @ExcelProperty("身高，厘米")
    private Integer height;

    @ExcelProperty("身体年龄，岁")
    private Integer age;

    @ExcelProperty("健康评分")
    private Integer healthScore;

    @ExcelProperty("高低肩：左右肩关节Y坐标差值")
    private Integer highLowShoulders;

    @ExcelProperty("头侧歪：头和脖子")
    private Integer headAskew;

    @ExcelProperty("左腿XOKD：X型 O型 K型 D型腿")
    private String leftLegKokd;

    @ExcelProperty("右腿XOKD：X型 O型 K型 D型腿")
    private String rightLegKokd;

    @ExcelProperty("长短腿：左右髋关节Y坐标差值")
    private Integer longShortLeg;

    @ExcelProperty("头部前倾：左耳和脖子")
    private Integer pokingChin;

    @ExcelProperty("骨盆前倾：脖子和左髋关节与右髋关节中点,左踝关节与右踝关节中点")
    private Integer pelvicAnteversion;

    @ExcelProperty("左膝盖分析：前曲 超伸")
    private String leftKneeAnalysis;

    @ExcelProperty("右膝盖分析：前曲 超伸")
    private String rightKneeAnalysis;

    @ExcelProperty("左圆肩：肩和肩峰")
    private String leftRoundShoulder;

    @ExcelProperty("右圆肩：肩和肩峰")
    private String rightRoundShoulder;

    @ExcelProperty("重心左右倾斜：人体重心和左踝关节与右踝关节中点")
    private String focusTiltLeftRight;

    @ExcelProperty("重心前后倾斜：人体重心和左踝关节与右踝关节中点")
    private String focusTiltForthBack;

    @ExcelProperty("体重")
    private Integer weight;

    @ExcelProperty("蛋白率")
    private Integer proteinRate;

    @ExcelProperty("骨量水分率")
    private Integer boneMassMositureContent;

    @ExcelProperty("脂肪率")
    private Integer fatPercentage;

    @ExcelProperty("肌肉率")
    private Integer musclePercentage;

    @ExcelProperty("骨格肌量")
    private Integer skeletalMuscleVolume;

    @ExcelProperty("BMI")
    private Integer bmi;

    @ExcelProperty("内脏脂肪等级")
    private Integer visceralFatGrade;

    @ExcelProperty("理想体重")
    private Integer idealWeight;

    @ExcelProperty("身体年龄")
    private Integer physicalAge;

    @ExcelProperty("基础代谢率")
    private Integer basalMetabolicRate;

    @ExcelProperty("胸围")
    private Integer bust;

    @ExcelProperty("腰围")
    private Integer waistline;

    @ExcelProperty("臀围")
    private Integer hipline;

    @ExcelProperty("左臀围")
    private Integer leftHipline;

    @ExcelProperty("右臀围")
    private Integer rightHipline;

    @ExcelProperty("左大腿围")
    private Integer leftThigCircumference;

    @ExcelProperty("右大腿围")
    private Integer rightThigCircumference;

    @ExcelProperty("左小腿围")
    private Integer leftCalfCircumference;

    @ExcelProperty("右小腿围")
    private Integer rightCalfCircumference;

    @ExcelProperty("模型文件id")
    private Integer modelFileId;

    @ExcelProperty("报告文件id")
    private Integer reportFileId;

    @ExcelProperty("id")
    private Long id;

    @ExcelProperty("扫描用户id")
    private Long userId;

    @ExcelProperty("体型值")
    private String shapeValue;

    @ExcelProperty("体型id")
    private Long shapeId;

}
