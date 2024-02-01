package cn.iocoder.yudao.module.steam.service.selexterior;

import java.util.*;
import cn.iocoder.yudao.module.steam.controller.admin.selexterior.vo.*;
import cn.iocoder.yudao.module.steam.dal.dataobject.selexterior.SelExteriorDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;

import javax.validation.Valid;

/**
 * 外观选择 Service 接口
 *
 * @author 芋道源码
 */
public interface SelExteriorService {

    /**
     * 创建外观选择
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createSelExterior(@Valid SelExteriorSaveReqVO createReqVO);

    /**
     * 更新外观选择
     *
     * @param updateReqVO 更新信息
     */
    void updateSelExterior(@Valid SelExteriorSaveReqVO updateReqVO);

    /**
     * 删除外观选择
     *
     * @param id 编号
     */
    void deleteSelExterior(Long id);

    /**
     * 获得外观选择
     *
     * @param id 编号
     * @return 外观选择
     */
    SelExteriorDO getSelExterior(Long id);

    /**
     * 获得外观选择分页
     *
     * @param pageReqVO 分页查询
     * @return 外观选择分页
     */
    PageResult<SelExteriorDO> getSelExteriorPage(SelExteriorPageReqVO pageReqVO);

}