package cn.iocoder.yudao.module.steam.service.youyoudetails;

import java.util.*;
import cn.iocoder.yudao.module.steam.controller.admin.youyoudetails.vo.*;
import cn.iocoder.yudao.module.steam.dal.dataobject.youyoudetails.YouyouDetailsDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;

import javax.validation.Valid;

/**
 * 用户查询明细 Service 接口
 *
 * @author 管理员
 */
public interface YouyouDetailsService {

    /**
     * 创建用户查询明细
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Integer createYouyouDetails(@Valid YouyouDetailsSaveReqVO createReqVO);

    /**
     * 更新用户查询明细
     *
     * @param updateReqVO 更新信息
     */
    void updateYouyouDetails(@Valid YouyouDetailsSaveReqVO updateReqVO);

    /**
     * 删除用户查询明细
     *
     * @param id 编号
     */
    void deleteYouyouDetails(Integer id);

    /**
     * 获得用户查询明细
     *
     * @param id 编号
     * @return 用户查询明细
     */
    YouyouDetailsDO getYouyouDetails(Integer id);

    /**
     * 获得用户查询明细分页
     *
     * @param pageReqVO 分页查询
     * @return 用户查询明细分页
     */
    PageResult<YouyouDetailsDO> getYouyouDetailsPage(YouyouDetailsPageReqVO pageReqVO);
}