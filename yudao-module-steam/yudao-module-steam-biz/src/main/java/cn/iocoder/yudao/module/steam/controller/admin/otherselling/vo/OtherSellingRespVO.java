package cn.iocoder.yudao.module.steam.controller.admin.otherselling.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.util.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;

@Schema(description = "管理后台 - 其他平台在售 Response VO")
@Data
@ExcelIgnoreUnannotated
public class OtherSellingRespVO {

    @Schema(description = "id", requiredMode = Schema.RequiredMode.REQUIRED, example = "22881")
    @ExcelProperty("id")
    private Integer id;

    @Schema(description = "csgoid", example = "14673")
    @ExcelProperty("csgoid")
    private Integer appid;

    @Schema(description = "资产id(饰品唯一)", example = "457")
    @ExcelProperty("资产id(饰品唯一)")
    private String assetid;

    @Schema(description = "classid", example = "19441")
    @ExcelProperty("classid")
    private String classid;

    @Schema(description = "instanceid", example = "17732")
    @ExcelProperty("instanceid")
    private String instanceid;

    @Schema(description = "amount")
    @ExcelProperty("amount")
    private String amount;

    @Schema(description = "创建时间")
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

    @Schema(description = "状态", example = "1")
    @ExcelProperty("状态")
    private Integer status;

    @Schema(description = "出售价格单价分", example = "1948")
    @ExcelProperty("出售价格单价分")
    private Integer price;

    @Schema(description = "发货状态(0代表未出售，1代表出售中，2代表已出售 )", example = "2")
    @ExcelProperty("发货状态(0代表未出售，1代表出售中，2代表已出售 )")
    private Integer transferStatus;

    @Schema(description = "contextid", example = "18890")
    @ExcelProperty("contextid")
    private String contextid;

    @Schema(description = "图片地址", example = "https://www.iocoder.cn")
    @ExcelProperty("图片地址")
    private String iconUrl;

    @Schema(description = "商品名称", example = "张三")
    @ExcelProperty("商品名称")
    private String marketName;

    @Schema(description = "类别选择")
    @ExcelProperty("类别选择")
    private String selQuality;

    @Schema(description = "收藏品选择")
    @ExcelProperty("收藏品选择")
    private String selItemset;

    @Schema(description = "武器选择")
    @ExcelProperty("武器选择")
    private String selWeapon;

    @Schema(description = "外观选择")
    @ExcelProperty("外观选择")
    private String selExterior;

    @Schema(description = "品质选择")
    @ExcelProperty("品质选择")
    private String selRarity;

    @Schema(description = "类型选择", example = "2")
    @ExcelProperty("类型选择")
    private String selType;

    @Schema(description = "marketHashName", example = "赵六")
    @ExcelProperty("marketHashName")
    private String marketHashName;

    @Schema(description = "在售展示权重")
    @ExcelProperty("在售展示权重")
    private Integer displayWeight;

    @Schema(description = "itemInfo")
    @ExcelProperty("itemInfo")
    private String itemInfo;

    @Schema(description = "short_name", example = "赵六")
    @ExcelProperty("short_name")
    private String shortName;

    @Schema(description = "出售用户名字", example = "李四")
    @ExcelProperty("出售用户名字")
    private String sellingUserName;

    @Schema(description = "出售用户头像")
    @ExcelProperty("出售用户头像")
    private String sellingAvator;

    @Schema(description = "出售用户id", example = "21029")
    @ExcelProperty("出售用户id")
    private Integer sellingUserId;

    @Schema(description = "出售平台id")
    @ExcelProperty("出售平台id")
    private Integer platformIdentity;

    @Schema(description = "steamId", example = "15026")
    @ExcelProperty("steamId")
    private String steamId;

}