package cn.iocoder.yudao.module.system.controller.admin.organizationpackage.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.*;

@Schema(description = "管理后台 - 机构套餐创建 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class OrganizationPackageCreateReqVO extends OrganizationPackageBaseVO {

}
