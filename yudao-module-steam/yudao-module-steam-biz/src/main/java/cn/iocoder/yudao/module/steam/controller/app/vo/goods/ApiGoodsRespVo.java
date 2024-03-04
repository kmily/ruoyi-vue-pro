package cn.iocoder.yudao.module.steam.controller.app.vo.goods;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.models.security.SecurityScheme;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 指定商品购买入参
 */
@Schema(description = "开发平台 -  开发平台接口")
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ApiGoodsRespVo implements Serializable {

    @Schema(description = "商品模版ID", example = "14652")
    private String templateId;

    @Schema(description = "模板hashname", example = "14652")
    private String templateHashName;

    @Schema(description = "每页查询数量", example = "默认50，最大200")
    private String pageSize;

    @Schema(description = "页码", example = "页码最大50")
    private String page;

    @Schema(description = "最小磨损度", example = "如需查询，则最小磨损度和最大磨损度都需传入，且最小磨损度需≤最大磨损度")
    private String abradeStartInterval;

    @Schema(description = "最大磨损度 ", example = "如需查询，则最小磨损度和最大磨损度都需传入，且最小磨损度需≤最大磨损度")
    private String abradeEndInterval;

    @Schema(description = "多普勒属性", example = "1:P1, 2:P2, 3:P3, 4:P4, 5:绿宝石, 6:红宝石, 7:蓝宝石, 8:黑珍珠")
    private Integer dopplerProperty;

    @Schema(description = "渐变区间最小值（%）", example = "如需查询，则最小值和最大值都需传入，且最小值需≤最大值")
    private Integer fadeRangeMin;

    @Schema(description = "渐变区间最大值（%）", example = "如需查询，则最小值和最大值都需传入，且最小值需≤最大值")
    private Integer fadeRangeMax;

    @Schema(description = "淬火属性", example = "101:T1、102:T2、103:T3、104:T4、105：单面全蓝")
    private Integer hardeningProperty;

    @Schema(description = "排序方式", example = "0：更新时间倒序； 1：价格升序； 2：价格降序。")
    private Number sortType;




}
