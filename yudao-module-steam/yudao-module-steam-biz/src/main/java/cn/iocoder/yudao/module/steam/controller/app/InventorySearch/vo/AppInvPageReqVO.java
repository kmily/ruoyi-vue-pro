package cn.iocoder.yudao.module.steam.controller.app.InventorySearch.vo;

import cn.iocoder.yudao.module.steam.controller.admin.inv.vo.InvPageReqVO;
import cn.iocoder.yudao.module.steam.service.steam.C5ItemInfo;
import cn.iocoder.yudao.module.steam.service.steam.InventoryDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
public class AppInvPageReqVO extends InvPageReqVO {

    private String iconUrl;
    private String marketName;
    private String marketHashName;
    private String classid;
    private String steamId;
    private Integer status;
    private Integer transferStatus;
    private Long id;
    private String instanceid;
    private Integer userType;
    private List<InventoryDto.DescriptionsDTOX.TagsDTO> tags;
    private Integer tradeable;
    private Integer price;
    // 库存ID
    private List<Long> InvId;
    // 合并数量
    private Long mergeNum;


    // 分类查询字段
    /**
     * 类别选择
     */
    private String selQuality;
    /**
     * 收藏品选择
     */
    private String selItemset;
    /**
     * 武器选择
     */
    private String selWeapon;
    /**
     * 外观选择
     */
    private String selExterior;
    /**
     * 品质选择
     */
    private String selRarity;
    /**
     * 类型选择
     */
    private String selType;

    private C5ItemInfo itemInfo;
    private String assetList;

}
