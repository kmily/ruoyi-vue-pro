package cn.iocoder.yudao.module.steam.controller.app.InventorySearch.vo;

import cn.iocoder.yudao.module.steam.controller.admin.inv.vo.InvPageReqVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class AppInvPageReqVO extends InvPageReqVO {

    private String iconUrl;
    private String marketName;
    private String classid;
    private String steamId;
    private Integer status;
    private Integer transferStatus;
    private Long id;
    private String instanceid;
    private Integer userType;



}
