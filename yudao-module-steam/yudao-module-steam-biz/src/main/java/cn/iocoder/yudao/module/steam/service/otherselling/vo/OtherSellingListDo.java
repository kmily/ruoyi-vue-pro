package cn.iocoder.yudao.module.steam.service.otherselling.vo;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import lombok.ToString;

@Schema(description = "管理后台 - 其他平台在售分页 Request VO")
@Data
@ToString(callSuper = true)
public class OtherSellingListDo extends PageParam {
    @Schema(description = "csgoid", example = "14673")
    private Integer appid;


    @Schema(description = "出售价格单价分", example = "1948")
    private Integer price;


    @Schema(description = "图片地址", example = "https://www.iocoder.cn")
    private String iconUrl;

    @Schema(description = "商品名称", example = "张三")
    private String marketName;

    @Schema(description = "类别选择")
    private String selQuality;

    @Schema(description = "外观选择")
    private String selExterior;

    @Schema(description = "品质选择")
    private String selRarity;

    @Schema(description = "类型选择", example = "2")
    private String selType;

    @Schema(description = "marketHashName", example = "赵六")
    private String marketHashName;

    @Schema(description = "出售用户名字", example = "李四")
    private String sellingUserName;

    @Schema(description = "出售用户头像")
    private String sellingAvator;

    @Schema(description = "出售平台id")
    private Integer platformIdentity;
}
