package cn.iocoder.yudao.module.scan.controller.admin.appversion.vo;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import io.swagger.annotations.*;

import com.alibaba.excel.annotation.ExcelProperty;

/**
 * 应用版本记录 Excel VO
 *
 * @author lyz
 */
@Data
public class AppVersionExcelVO {

    @ExcelProperty("图片上传地址，有何用？")
    private String fileUrl;

    @ExcelProperty("Apk公钥信息	")
    private String pubKey;

    @ExcelProperty("上线时间，新增记录时，")
    private LocalDateTime onlineTime;

    @ExcelProperty("下线时间，下线时更新，但可能用不到")
    private LocalDateTime offlineTime;

    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

    @ExcelProperty("id")
    private Long id;

    @ExcelProperty("0-pc 1-IOS 2-安卓")
    private Integer sysType;

    @ExcelProperty("文件包名称，带版本号，带后缀名	")
    private String fileName;

    @ExcelProperty("版本号")
    private String verNo;

    @ExcelProperty("是否强制升级,0否1是")
    private Boolean forceUpdate;

    @ExcelProperty("审核状态，0待审核，1审核通过，2审核不通过")
    private Integer auditState;

    @ExcelProperty("备注说明")
    private String remark;

    @ExcelProperty("版本描述信息")
    private String description;

    @ExcelProperty("软件包哈希值")
    private String packageHash;

    @ExcelProperty("版本状态0:正常1注销")
    private Boolean state;

    @ExcelProperty("版本序号")
    private Integer verCode;

}
