package cn.iocoder.yudao.module.member.controller.admin.user.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * @author whycode
 * @title: UserStatisticsExcelVO
 * @projectName ruoyi-vue-pro
 * @description: TODO
 * @date 2023/8/3015:33
 */
@Data
public class UserStatisticsExcelVO {
    @ExcelProperty("日期")
    private String month;
    @ExcelProperty("新增用户")
    private Long quantity;

}
