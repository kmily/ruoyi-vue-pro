package cn.iocoder.yudao.module.steam.service.selquality;

import java.util.*;

import cn.iocoder.yudao.module.steam.controller.admin.selquality.vo.*;
import cn.iocoder.yudao.module.steam.dal.dataobject.selquality.SelQualityDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;

import javax.validation.Valid;

/**
 * 类别选择 Service 接口
 *
 * @author 芋道源码
 */
public interface SelQualityService {

    /**
     * 创建类别选择
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createSelQuality(@Valid SelQualitySaveReqVO createReqVO);

    /**
     * 更新类别选择
     *
     * @param updateReqVO 更新信息
     */
    void updateSelQuality(@Valid SelQualitySaveReqVO updateReqVO);

    /**
     * 删除类别选择
     *
     * @param id 编号
     */
    void deleteSelQuality(Long id);

    /**
     * 获得类别选择
     *
     * @param id 编号
     * @return 类别选择
     */
    SelQualityDO getSelQuality(Long id);

    /**
     * 获得类别选择分页
     *
     * @param pageReqVO 分页查询
     * @return 类别选择分页
     */
    PageResult<SelQualityDO> getSelQualityPage(SelQualityPageReqVO pageReqVO);

}