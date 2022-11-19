package cn.iocoder.yudao.module.yr.controller.admin.sys.dict.vo.data;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Date;

@ApiModel("管理后台 - 字典数据信息 Response VO")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class QgDictDataRespVO extends QgDictDataBaseVO {

    @ApiModelProperty(value = "字典数据编号", required = true, example = "1024")
    private Long id;

    @ApiModelProperty(value = "分类ID", required = true, example = "1024")
    private Long treeId;

    @ApiModelProperty(value = "创建时间", required = true, example = "时间戳格式")
    private Date createTime;

}
