package cn.iocoder.yudao.module.infra.controller.admin.demo.demo04.vo;

import cn.iocoder.yudao.framework.datatranslation.core.annotations.DataTrans;
import cn.iocoder.yudao.framework.excel.core.annotations.DictFormat;
import cn.iocoder.yudao.framework.excel.core.convert.DictConvert;
import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

@Schema(description = "管理后台 - 学生 Response VO")
@Data
@ExcelIgnoreUnannotated
public class Demo04StudentRespVO {

    @Schema(description = "编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "8525")
    @ExcelProperty("编号")
    private Long id;

    @Schema(description = "名字", requiredMode = Schema.RequiredMode.REQUIRED, example = "芋艿")
    @ExcelProperty("名字")
    private String name;

    @Schema(description = "性别", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty(value = "性别", converter = DictConvert.class)
    @DictFormat("system_user_sex") // TODO 代码优化：建议设置到对应的 DictTypeConstants 枚举类中
    private Integer sex;

    @Schema(description = "出生日期", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("出生日期")
    private LocalDateTime birthday;

    @Schema(description = "简介", requiredMode = Schema.RequiredMode.REQUIRED, example = "随便")
    @ExcelProperty("简介")
    private String description;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

    /**
     * 学生班级
     */
    //@Trans(type = TransType.SIMPLE, target = Demo04GradeDO.class, fields = "name", ref = "gradeName")
    private Long gradeId;

    private String gradeName;

    // TODO puhui999: 1、定义一个父接口 DataTranslationHandler-一个单查询接口-一个批量查询接口(统一返回 Map)，需要翻译的数据api实现这个接口
    // TODO puhui999: 2、定义一个属性注解 DataTrans-
    /**
     * 学生课程
     */
    // TODO puhui999: 系统自动查表作为数据源-翻译正常
    //@Trans(type = TransType.SIMPLE, target = Demo04CourseDO.class, fields = "name", ref = "courseNames")
    //private Set<Long> courseIds;

    // TODO puhui999: 自定义数据源翻译-问题：只显示第一个
    //@Trans(type = TransType.AUTO_TRANS, key = "demo04course", fields = "name", ref = "courseNames")
    //@Trans(type = TransType.AUTO_TRANS, key = "demo04course")
    @DataTrans(type = "Demo04CourseTrans", fields = "name", resultMapping = "courseNames")
    private Set<Long> courseIds;

    //private Set<String> courseNames; TODO 收不到值

    private String courseNames;

}