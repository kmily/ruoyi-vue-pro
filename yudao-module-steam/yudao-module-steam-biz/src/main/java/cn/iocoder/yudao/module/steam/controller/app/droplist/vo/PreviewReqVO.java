package cn.iocoder.yudao.module.steam.controller.app.droplist.vo;

import cn.iocoder.yudao.module.steam.dal.dataobject.invpreview.InvPreviewDO;
import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Schema(description = " PreviewReqVO")
@Data
@ExcelIgnoreUnannotated
public class PreviewReqVO {

    @Schema(description = "饰品在售预览")
    private String marketHashName;

}