package cn.iocoder.yudao.module.steam.service.youyougoodslist;

import java.util.*;
import javax.validation.*;
import cn.iocoder.yudao.module.steam.controller.admin.youyougoodslist.vo.*;
import cn.iocoder.yudao.module.steam.dal.dataobject.youyougoodslist.YouyouGoodslistDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;

/**
 * 查询商品列 Service 接口
 *
 * @author 管理员
 */
public interface YouyouGoodslistService {

    /**
     * 创建查询商品列
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Integer createYouyouGoodslist(@Valid YouyouGoodslistSaveReqVO createReqVO);

    /**
     * 更新查询商品列
     *
     * @param updateReqVO 更新信息
     */
    void updateYouyouGoodslist(@Valid YouyouGoodslistSaveReqVO updateReqVO);

    /**
     * 删除查询商品列
     *
     * @param id 编号
     */
    void deleteYouyouGoodslist(Integer id);

    /**
     * 获得查询商品列
     *
     * @param id 编号
     * @return 查询商品列
     */
    YouyouGoodslistDO getYouyouGoodslist(Integer id);

    /**
     * 获得查询商品列分页
     *
     * @param pageReqVO 分页查询
     * @return 查询商品列分页
     */
    PageResult<YouyouGoodslistDO> getYouyouGoodslistPage(YouyouGoodslistPageReqVO pageReqVO);

}