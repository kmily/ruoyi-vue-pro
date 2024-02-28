package cn.iocoder.yudao.module.steam.service.selling;

import java.util.*;
import javax.validation.*;
import cn.iocoder.yudao.module.steam.controller.admin.selling.vo.*;
import cn.iocoder.yudao.module.steam.dal.dataobject.selling.SellingDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;

/**
 * 在售饰品 Service 接口
 *
 * @author 管理员
 */
public interface SellingService {

    /**
     * 创建在售饰品
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createSelling(@Valid SellingSaveReqVO createReqVO);

    /**
     * 更新在售饰品
     *
     * @param updateReqVO 更新信息
     */
    void updateSelling(@Valid SellingSaveReqVO updateReqVO);

    /**
     * 删除在售饰品
     *
     * @param id 编号
     */
    void deleteSelling(Long id);

    /**
     * 获得在售饰品
     *
     * @param id 编号
     * @return 在售饰品
     */
    SellingDO getSelling(Long id);

    /**
     * 获得在售饰品分页
     *
     * @param pageReqVO 分页查询
     * @return 在售饰品分页
     */
    PageResult<SellingDO> getSellingPage(SellingPageReqVO pageReqVO);

}