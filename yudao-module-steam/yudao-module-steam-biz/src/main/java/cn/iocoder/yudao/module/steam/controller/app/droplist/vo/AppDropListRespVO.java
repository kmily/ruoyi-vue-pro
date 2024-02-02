package cn.iocoder.yudao.module.steam.controller.app.droplist.vo;

import cn.iocoder.yudao.module.steam.dal.dataobject.selexterior.SelExteriorDO;
import cn.iocoder.yudao.module.steam.dal.dataobject.selitemset.SelItemsetDO;
import cn.iocoder.yudao.module.steam.dal.dataobject.selquality.SelQualityDO;
import cn.iocoder.yudao.module.steam.dal.dataobject.selrarity.SelRarityDO;
import cn.iocoder.yudao.module.steam.dal.dataobject.seltype.SelTypeDO;
import cn.iocoder.yudao.module.steam.dal.dataobject.seltype.SelWeaponDO;
import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Schema(description = " ResponseVO")
@Data
@ExcelIgnoreUnannotated
public class AppDropListRespVO {

    // 类型选择
    @Schema(description = "类型选择")
    List<SelTypeDO> type;

    // 外观选择
    @Schema(description = "外观选择")
    List<SelExteriorDO> exterior;

    // 收藏品选择
    @Schema(description = "收藏品选择")
    List<SelItemsetDO> itemset;

    // 类别选择
    @Schema(description = "类别选择")
    List<SelQualityDO> quality;

    // 品质选择
    @Schema(description = "品质选择")
    List<SelRarityDO> rarity;

    // 武器选择
    @Schema(description = "武器选择")
    List<cn.iocoder.yudao.module.steam.dal.dataobject.seltype.SelWeaponDO> weapon;



}