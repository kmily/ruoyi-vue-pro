package cn.iocoder.yudao.module.steam.controller.admin.selitemset.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;

@Schema(description = "管理后台 - 收藏品选择列表 Request VO")
@Data
public class SelItemsetListReqVO {

    @Schema(description = "父级编号", example = "15350")
    private Long parentId;

    @Schema(description = "英文名称", example = "张三")
    private String internalName;

    @Schema(description = "中文名称", example = "赵六")
    private String localizedTagName;

}