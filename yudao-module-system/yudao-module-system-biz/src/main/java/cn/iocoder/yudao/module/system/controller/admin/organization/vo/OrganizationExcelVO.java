package cn.iocoder.yudao.module.system.controller.admin.organization.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 组织机构 Excel VO
 *
 * @author 芋道源码
 */
@Data
public class OrganizationExcelVO {

    @ExcelProperty("ID")
    private Long id;

    @ExcelProperty("会员Id")
    private String userId;

    @ExcelProperty("会员名称")
    private String userName;

    @ExcelProperty("是否自营")
    private Boolean selfOperated;

    @ExcelProperty("是否允许员工提现")
    private Byte cashOut;

    @ExcelProperty("机构状态")
    private String disable;

    @ExcelProperty("机构logo")
    private String logo;

    @ExcelProperty("机构名称")
    private String name;

    @ExcelProperty("机构地址")
    private String addressDetail;

    @ExcelProperty("地址id")
    private String addressIdPath;

    @ExcelProperty("地址名称")
    private String addressPath;

    @ExcelProperty("经纬度")
    private String center;

    @ExcelProperty("机构简介")
    private String introduction;

    @ExcelProperty("物流评分")
    private Double deliveryScore;

    @ExcelProperty("描述评分")
    private Double descriptionScore;

    @ExcelProperty("服务评分")
    private Double serviceScore;

    @ExcelProperty("员工数量")
    private Integer staffNum;

    @ExcelProperty("商品数量")
    private Integer goodsNum;

    @ExcelProperty("收藏数量")
    private Integer collectionNum;

    @ExcelProperty("宣传图册")
    private List<String> images;

    @ExcelProperty("联系方式")
    private String mobile;

    @ExcelProperty("邮箱")
    private String email;

    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}
