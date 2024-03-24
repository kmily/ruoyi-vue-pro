package cn.iocoder.yudao.module.steam.controller.app.vo.UUCommondity;

import com.alibaba.excel.annotation.ExcelProperty;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;


/**
 * UU查询列表请求参数
 */

@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class ApiUUCommodityReqVO implements Serializable {

    @Schema(description = "templateId和hashName二选一必传递，同时传入以templateId为准，如传入不可为0", example = "14652")
    private String templateId;

    @Schema(description = "templateId和hashName二选一必传递，同时传入以templateId为准，如传入不可为空", example = "14652")
    private String templateHashName;


    @Schema(description = "每页查询数量", example = "默认50，最大200")
    @ExcelProperty("每页查询数量")
    private Integer pageSize;

    @Schema(description = "页码", example = "最大50")
    @ExcelProperty("页码")
    private Integer page;

    @Schema(description = "如需查询，则最小磨损度和最大磨损度都需传入，且最小磨损度需≤最大磨损度")
    @ExcelProperty("最小磨损度")
    private String abradeStartInterval;

    @Schema(description = "如需查询，则最小磨损度和最大磨损度都需传入，且最小磨损度需≤最大磨损度")
    @ExcelProperty("最大磨损度 ")
    private String abradeEndInterval;

    @Schema(description = "1:P1, 2:P2, 3:P3, 4:P4, 5:绿宝石, 6:红宝石, 7:蓝宝石, 8:黑珍珠")
    @ExcelProperty("多普勒属性")
    private Integer dopplerProperty;

    @Schema(description = "如需查询，则最小值和最大值都需传入，且最小值需≤最大值")
    @ExcelProperty("渐变区间最小值（%）")
    private Integer fadeRangeMin;

    @Schema(description = "如需查询，则最小值和最大值都需传入，且最小值需≤最大值")
    @ExcelProperty("渐变区间最大值（%）")
    private Integer fadeRangeMax;

    @Schema(description = "101:T1、102:T2、103:T3、104:T4、105：单面全蓝")
    @ExcelProperty("淬火属性")
    private Integer hardeningProperty;

    @Schema(description = "0：更新时间倒序； 1：价格升序； 2：价格降序。")
    @ExcelProperty("排序方式")
    private Number sortType;
}
