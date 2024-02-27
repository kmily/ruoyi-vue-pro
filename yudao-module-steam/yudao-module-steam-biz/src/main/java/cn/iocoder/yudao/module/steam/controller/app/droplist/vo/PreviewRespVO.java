package cn.iocoder.yudao.module.steam.controller.app.droplist.vo;

import cn.iocoder.yudao.module.steam.dal.dataobject.invpreview.InvPreviewDO;
import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Schema(description = " ResponseVO")
@Data
@ExcelIgnoreUnannotated
public class PreviewRespVO extends InvPreviewDO {

    @Schema(description = "饰品在售预览")
    List<cn.iocoder.yudao.module.steam.dal.dataobject.invpreview.InvPreviewDO> preview;

}