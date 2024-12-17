package cn.iocoder.yudao.module.haoka.service.superiorapi;

import java.util.*;
import jakarta.validation.*;
import cn.iocoder.yudao.module.haoka.controller.admin.superiorapi.vo.*;
import cn.iocoder.yudao.module.haoka.dal.dataobject.superiorapi.SuperiorApiSkuConfigDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;

/**
 * 上游API接口SKU要求配置 Service 接口
 *
 * @author 芋道源码
 */
public interface SuperiorApiSkuConfigService {

    /**
     * 创建上游API接口SKU要求配置
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createSuperiorApiSkuConfig(@Valid SuperiorApiSkuConfigSaveReqVO createReqVO);

    /**
     * 更新上游API接口SKU要求配置
     *
     * @param updateReqVO 更新信息
     */
    void updateSuperiorApiSkuConfig(@Valid SuperiorApiSkuConfigSaveReqVO updateReqVO);

    /**
     * 删除上游API接口SKU要求配置
     *
     * @param id 编号
     */
    void deleteSuperiorApiSkuConfig(Long id);

    /**
     * 获得上游API接口SKU要求配置
     *
     * @param id 编号
     * @return 上游API接口SKU要求配置
     */
    SuperiorApiSkuConfigDO getSuperiorApiSkuConfig(Long id);

    /**
     * 获得上游API接口SKU要求配置分页
     *
     * @param pageReqVO 分页查询
     * @return 上游API接口SKU要求配置分页
     */
    PageResult<SuperiorApiSkuConfigDO> getSuperiorApiSkuConfigPage(SuperiorApiSkuConfigPageReqVO pageReqVO);

    List<SuperiorApiSkuConfigDO> getAllSuperiorApiSkuConfig(Long superiorApiId);
}
