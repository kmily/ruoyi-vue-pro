package cn.iocoder.yudao.module.steam.service.selitemset;

import java.util.*;
import javax.validation.*;
import cn.iocoder.yudao.module.steam.controller.admin.selitemset.vo.*;
import cn.iocoder.yudao.module.steam.dal.dataobject.selitemset.SelItemsetDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;

/**
 * 收藏品选择 Service 接口
 *
 * @author glzaboy
 */
public interface SelItemsetService {

    /**
     * 创建收藏品选择
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createSelItemset(@Valid SelItemsetSaveReqVO createReqVO);

    /**
     * 更新收藏品选择
     *
     * @param updateReqVO 更新信息
     */
    void updateSelItemset(@Valid SelItemsetSaveReqVO updateReqVO);

    /**
     * 删除收藏品选择
     *
     * @param id 编号
     */
    void deleteSelItemset(Long id);

    /**
     * 获得收藏品选择
     *
     * @param id 编号
     * @return 收藏品选择
     */
    SelItemsetDO getSelItemset(Long id);

    /**
     * 获得收藏品选择列表
     *
     * @param listReqVO 查询条件
     * @return 收藏品选择列表
     */
    List<SelItemsetDO> getSelItemsetList(SelItemsetListReqVO listReqVO);

}