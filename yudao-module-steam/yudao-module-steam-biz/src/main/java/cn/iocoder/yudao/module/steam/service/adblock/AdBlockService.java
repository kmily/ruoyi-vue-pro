package cn.iocoder.yudao.module.steam.service.adblock;

import java.util.*;
import javax.validation.*;
import cn.iocoder.yudao.module.steam.controller.admin.adblock.vo.*;
import cn.iocoder.yudao.module.steam.dal.dataobject.adblock.AdBlockDO;
import cn.iocoder.yudao.module.steam.dal.dataobject.ad.AdDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;

/**
 * 广告位 Service 接口
 *
 * @author 管理员
 */
public interface AdBlockService {

    /**
     * 创建广告位
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createAdBlock(@Valid AdBlockSaveReqVO createReqVO);

    /**
     * 更新广告位
     *
     * @param updateReqVO 更新信息
     */
    void updateAdBlock(@Valid AdBlockSaveReqVO updateReqVO);

    /**
     * 删除广告位
     *
     * @param id 编号
     */
    void deleteAdBlock(Long id);

    /**
     * 获得广告位
     *
     * @param id 编号
     * @return 广告位
     */
    AdBlockDO getAdBlock(Long id);

    /**
     * 获得广告位分页
     *
     * @param pageReqVO 分页查询
     * @return 广告位分页
     */
    PageResult<AdBlockDO> getAdBlockPage(AdBlockPageReqVO pageReqVO);

    // ==================== 子表（广告） ====================

    /**
     * 获得广告列表
     *
     * @param blockId adID
     * @return 广告列表
     */
    List<AdDO> getAdListByBlockId(Long blockId);

}