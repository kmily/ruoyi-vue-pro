package cn.iocoder.yudao.module.member.controller.app.room.vo;

import lombok.*;

import java.time.LocalDateTime;


/**
 * 用户房间 Excel VO
 *
 * @author 芋道源码
 */
@Data
public class RoomExcelVO {

    //@ExcelProperty("自增编号")
    private Long id;

    //@ExcelProperty("用户ID")
    private Long userId;

   // @ExcelProperty("家庭ID")
    private Long familyId;

   // @ExcelProperty("房间名称")
    private String name;
   // @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}
