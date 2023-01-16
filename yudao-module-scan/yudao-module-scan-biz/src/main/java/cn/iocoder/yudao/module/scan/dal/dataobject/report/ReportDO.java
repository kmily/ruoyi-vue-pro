package cn.iocoder.yudao.module.scan.dal.dataobject.report;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 报告 DO
 *
 * @author lyz
 */
@TableName("scan_report")
@KeySequence("scan_report_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReportDO extends BaseDO {

    /**
     * 报告编号
     */
    private String code;
    /**
     * 姓名
     */
    private String name;
    /**
     * 性别
     *
     * 枚举 {@link TODO system_user_sex 对应的类}
     */
    private Integer sex;
    /**
     * 身高，厘米
     */
    private Integer height;
    /**
     * 身体年龄，岁
     */
    private Integer age;
    /**
     * 健康评分
     */
    private Integer healthScore;
    /**
     * 高低肩：左右肩关节Y坐标差值
     */
    private Integer highLowShoulders;
    /**
     * 头侧歪：头和脖子
     */
    private Integer headAskew;
    /**
     * 左腿XOKD：X型 O型 K型 D型腿
     */
    private String leftLegKokd;
    /**
     * 右腿XOKD：X型 O型 K型 D型腿
     */
    private String rightLegKokd;
    /**
     * 长短腿：左右髋关节Y坐标差值
     */
    private Integer longShortLeg;
    /**
     * 头部前倾：左耳和脖子
     */
    private Integer pokingChin;
    /**
     * 骨盆前倾：脖子和左髋关节与右髋关节中点,左踝关节与右踝关节中点
     */
    private Integer pelvicAnteversion;
    /**
     * 左膝盖分析：前曲 超伸
     */
    private String leftKneeAnalysis;
    /**
     * 右膝盖分析：前曲 超伸
     */
    private String rightKneeAnalysis;
    /**
     * 左圆肩：肩和肩峰
     */
    private String leftRoundShoulder;
    /**
     * 右圆肩：肩和肩峰
     */
    private String rightRoundShoulder;
    /**
     * 重心左右倾斜：人体重心和左踝关节与右踝关节中点
     */
    private String focusTiltLeftRight;
    /**
     * 重心前后倾斜：人体重心和左踝关节与右踝关节中点
     */
    private String focusTiltForthBack;
    /**
     * 体重
     */
    private Integer weight;
    /**
     * 蛋白率
     */
    private Integer proteinRate;
    /**
     * 骨量水分率
     */
    private Integer boneMassMositureContent;
    /**
     * 脂肪率
     */
    private Integer fatPercentage;
    /**
     * 肌肉率
     */
    private Integer musclePercentage;
    /**
     * 骨格肌量
     */
    private Integer skeletalMuscleVolume;
    /**
     * BMI
     */
    private Integer bmi;
    /**
     * 内脏脂肪等级
     */
    private Integer visceralFatGrade;
    /**
     * 理想体重
     */
    private Integer idealWeight;
    /**
     * 身体年龄
     */
    private Integer physicalAge;
    /**
     * 基础代谢率
     */
    private Integer basalMetabolicRate;
    /**
     * 胸围
     */
    private Integer bust;
    /**
     * 腰围
     */
    private Integer waistline;
    /**
     * 臀围
     */
    private Integer hipline;
    /**
     * 左臀围
     */
    private Integer leftHipline;
    /**
     * 右臀围
     */
    private Integer rightHipline;
    /**
     * 左大腿围
     */
    private Integer leftThigCircumference;
    /**
     * 右大腿围
     */
    private Integer rightThigCircumference;
    /**
     * 左小腿围
     */
    private Integer leftCalfCircumference;
    /**
     * 右小腿围
     */
    private Integer rightCalfCircumference;
    /**
     * 模型文件id
     */
    private Integer modelFileId;
    /**
     * id
     */
    @TableId
    private Long id;
    /**
     * 扫描用户id
     */
    private Long userId;
    /**
     * 体型值
     */
    private String shapeValue;
    /**
     * 体型id
     */
    private Long shapeId;
    /**
     * 报告文件路径
     */
    private String reportFilePath;

}
