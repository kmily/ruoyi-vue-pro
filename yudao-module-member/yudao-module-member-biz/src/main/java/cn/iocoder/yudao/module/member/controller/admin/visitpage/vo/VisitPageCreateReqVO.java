package cn.iocoder.yudao.module.member.controller.admin.visitpage.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.*;

@Schema(description = "管理后台 - 页面访问数据创建 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class VisitPageCreateReqVO extends VisitPageBaseVO {



    public String key(){
        return String.format("visit-page:%d", this.getUserId());
    }

}
