package cn.iocoder.yudao.module.steam.service.otherselling;

import java.util.*;
import javax.validation.*;
import cn.iocoder.yudao.module.steam.controller.admin.otherselling.vo.*;
import cn.iocoder.yudao.module.steam.dal.dataobject.otherselling.OtherSellingDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;

/**
 * 其他平台在售 Service 接口
 *
 * @author 管理员
 */
public interface OtherSellingService {

    /**
     * 创建其他平台在售
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Integer createOtherSelling(@Valid OtherSellingSaveReqVO createReqVO);

    /**
     * 更新其他平台在售
     *
     * @param updateReqVO 更新信息
     */
    void updateOtherSelling(@Valid OtherSellingSaveReqVO updateReqVO);

    /**
     * 删除其他平台在售
     *
     * @param id 编号
     */
    void deleteOtherSelling(Integer id);

    /**
     * 获得其他平台在售
     *
     * @param id 编号
     * @return 其他平台在售
     */
    OtherSellingDO getOtherSelling(Integer id);

    /**
     * 获得其他平台在售分页
     *
     * @param pageReqVO 分页查询
     * @return 其他平台在售分页
     */
    PageResult<OtherSellingDO> getOtherSellingPage(OtherSellingPageReqVO pageReqVO);

}