package cn.iocoder.yudao.module.system.controller.admin.signreq.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;

import com.alibaba.excel.annotation.ExcelProperty;
import cn.iocoder.yudao.framework.excel.core.annotations.DictFormat;
import cn.iocoder.yudao.framework.excel.core.convert.DictConvert;


/**
 * 注册申请 Excel VO
 *
 * @author 芋道源码
 */
@Data
public class SignReqExcelVO {

    @ExcelProperty("申请id,主键(自增策略)")
    private Long id;

    @ExcelProperty("备注信息")
    private String remark;

    @ExcelProperty("处理状态 1.未绑定 2.已绑定 3.已拒绝")
    private String status;

    @ExcelProperty("提交时间")
    private LocalDateTime createTime;

    @ExcelProperty("openId")
    private String openId;

    @ExcelProperty("邮箱")
    private String email;

    @ExcelProperty("手机号码")
    private String phonenumber;

    @ExcelProperty("姓名")
    private String userName;

    @ExcelProperty(value = "性别(0男 1女 2未知）", converter = DictConvert.class)
    @DictFormat("system_user_sex") // TODO 代码优化：建议设置到对应的 XXXDictTypeConstants 枚举类中
    private String sex;

}
