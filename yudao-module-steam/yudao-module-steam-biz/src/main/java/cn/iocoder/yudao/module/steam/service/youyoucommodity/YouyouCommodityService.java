package cn.iocoder.yudao.module.steam.service.youyoucommodity;

import java.util.*;
import javax.validation.*;
import cn.iocoder.yudao.module.steam.controller.admin.youyoucommodity.vo.*;
import cn.iocoder.yudao.module.steam.dal.dataobject.youyoucommodity.YouyouCommodityDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;

/**
 * 悠悠商品列表 Service 接口
 *
 * @author 管理员
 */
public interface YouyouCommodityService {

    /**
     * 创建悠悠商品列表
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Integer createYouyouCommodity(@Valid YouyouCommoditySaveReqVO createReqVO);

    /**
     * 更新悠悠商品列表
     *
     * @param updateReqVO 更新信息
     */
    void updateYouyouCommodity(@Valid YouyouCommoditySaveReqVO updateReqVO);

    /**
     * 删除悠悠商品列表
     *
     * @param id 编号
     */
    void deleteYouyouCommodity(Integer id);

    /**
     * 获得悠悠商品列表
     *
     * @param id 编号
     * @return 悠悠商品列表
     */
    YouyouCommodityDO getYouyouCommodity(Integer id);

    /**
     * 获得悠悠商品列表分页
     *
     * @param pageReqVO 分页查询
     * @return 悠悠商品列表分页
     */
    PageResult<YouyouCommodityDO> getYouyouCommodityPage(YouyouCommodityPageReqVO pageReqVO);

}