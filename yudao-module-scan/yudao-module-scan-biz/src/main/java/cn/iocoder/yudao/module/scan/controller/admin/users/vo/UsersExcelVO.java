package cn.iocoder.yudao.module.scan.controller.admin.users.vo;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import io.swagger.annotations.*;

import com.alibaba.excel.annotation.ExcelProperty;

/**
 * 扫描用户 Excel VO
 *
 * @author lyz
 */
@Data
public class UsersExcelVO {

    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

    @ExcelProperty("姓名")
    private String name;

    @ExcelProperty("年龄")
    private Integer age;

    @ExcelProperty("性别")
    private Integer sex;

    @ExcelProperty("手机号")
    private String phone;

    @ExcelProperty("设备id")
    private Integer deviceId;

    @ExcelProperty("id")
    private Long id;

}
