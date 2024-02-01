package cn.iocoder.yudao.module.steam.service.selrarity;

import java.util.*;
import javax.validation.*;
import cn.iocoder.yudao.module.steam.controller.admin.selrarity.vo.*;
import cn.iocoder.yudao.module.steam.dal.dataobject.selrarity.SelRarityDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;

/**
 * 品质选择 Service 接口
 *
 * @author lgm
 */
public interface SelRarityService {

    /**
     * 创建品质选择
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createSelRarity(@Valid SelRaritySaveReqVO createReqVO);

    /**
     * 更新品质选择
     *
     * @param updateReqVO 更新信息
     */
    void updateSelRarity(@Valid SelRaritySaveReqVO updateReqVO);

    /**
     * 删除品质选择
     *
     * @param id 编号
     */
    void deleteSelRarity(Long id);

    /**
     * 获得品质选择
     *
     * @param id 编号
     * @return 品质选择
     */
    SelRarityDO getSelRarity(Long id);

    /**
     * 获得品质选择分页
     *
     * @param pageReqVO 分页查询
     * @return 品质选择分页
     */
    PageResult<SelRarityDO> getSelRarityPage(SelRarityPageReqVO pageReqVO);

}