package cn.iocoder.yudao.module.haoka.service.superiorapi;

import java.util.*;
import jakarta.validation.*;
import cn.iocoder.yudao.module.haoka.controller.admin.superiorapi.vo.*;
import cn.iocoder.yudao.module.haoka.dal.dataobject.superiorapi.SuperiorApiDO;
import cn.iocoder.yudao.module.haoka.dal.dataobject.superiorapi.SuperiorApiDevConfigDO;
import cn.iocoder.yudao.module.haoka.dal.dataobject.superiorapi.SuperiorApiSkuConfigDO;
import cn.iocoder.yudao.module.haoka.dal.dataobject.superiorproductconfig.SuperiorProductConfigDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;

/**
 * 上游API接口 Service 接口
 *
 * @author 芋道源码
 */
public interface SuperiorApiService {

    /**
     * 创建上游API接口
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createSuperiorApi(@Valid SuperiorApiSaveReqVO createReqVO);

    /**
     * 更新上游API接口
     *
     * @param updateReqVO 更新信息
     */
    void updateSuperiorApi(@Valid SuperiorApiSaveReqVO updateReqVO);

    /**
     * 删除上游API接口
     *
     * @param id 编号
     */
    void deleteSuperiorApi(Long id);

    /**
     * 获得上游API接口
     *
     * @param id 编号
     * @return 上游API接口
     */
    SuperiorApiDO getSuperiorApi(Long id);

    /**
     * 获得上游API接口分页
     *
     * @param pageReqVO 分页查询
     * @return 上游API接口分页
     */
    PageResult<SuperiorApiDO> getSuperiorApiPage(SuperiorApiPageReqVO pageReqVO);

    // ==================== 子表（上游API接口开发配置） ====================

    /**
     * 获得上游API接口开发配置分页
     *
     * @param pageReqVO 分页查询
     * @param haokaSuperiorApiId ID
     * @return 上游API接口开发配置分页
     */
    PageResult<SuperiorApiDevConfigDO> getSuperiorApiDevConfigPage(PageParam pageReqVO, Long haokaSuperiorApiId);

    /**
     * 创建上游API接口开发配置
     *
     * @param superiorApiDevConfig 创建信息
     * @return 编号
     */
    Long createSuperiorApiDevConfig(@Valid SuperiorApiDevConfigDO superiorApiDevConfig);

    /**
     * 更新上游API接口开发配置
     *
     * @param superiorApiDevConfig 更新信息
     */
    void updateSuperiorApiDevConfig(@Valid SuperiorApiDevConfigDO superiorApiDevConfig);

    /**
     * 删除上游API接口开发配置
     *
     * @param id 编号
     */
    void deleteSuperiorApiDevConfig(Long id);

	/**
	 * 获得上游API接口开发配置
	 *
	 * @param id 编号
     * @return 上游API接口开发配置
	 */
    SuperiorApiDevConfigDO getSuperiorApiDevConfig(Long id);

    // ==================== 子表（上游API接口SKU要求配置） ====================

    /**
     * 获得上游API接口SKU要求配置分页
     *
     * @param pageReqVO 分页查询
     * @param haokaSuperiorApiId ID
     * @return 上游API接口SKU要求配置分页
     */
    PageResult<SuperiorApiSkuConfigDO> getSuperiorApiSkuConfigPage(PageParam pageReqVO, Long haokaSuperiorApiId);

    /**
     * 创建上游API接口SKU要求配置
     *
     * @param superiorApiSkuConfig 创建信息
     * @return 编号
     */
    Long createSuperiorApiSkuConfig(@Valid SuperiorApiSkuConfigDO superiorApiSkuConfig);

    /**
     * 更新上游API接口SKU要求配置
     *
     * @param superiorApiSkuConfig 更新信息
     */
    void updateSuperiorApiSkuConfig(@Valid SuperiorApiSkuConfigDO superiorApiSkuConfig);

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

    // ==================== 子表（产品对接上游配置） ====================

    /**
     * 获得产品对接上游配置分页
     *
     * @param pageReqVO 分页查询
     * @param haokaSuperiorApiId ID
     * @return 产品对接上游配置分页
     */
    PageResult<SuperiorProductConfigDO> getSuperiorProductConfigPage(PageParam pageReqVO, Long haokaSuperiorApiId);

    /**
     * 创建产品对接上游配置
     *
     * @param superiorProductConfig 创建信息
     * @return 编号
     */
    Long createSuperiorProductConfig(@Valid SuperiorProductConfigDO superiorProductConfig);

    /**
     * 更新产品对接上游配置
     *
     * @param superiorProductConfig 更新信息
     */
    void updateSuperiorProductConfig(@Valid SuperiorProductConfigDO superiorProductConfig);

    /**
     * 删除产品对接上游配置
     *
     * @param id 编号
     */
    void deleteSuperiorProductConfig(Long id);

	/**
	 * 获得产品对接上游配置
	 *
	 * @param id 编号
     * @return 产品对接上游配置
	 */
    SuperiorProductConfigDO getSuperiorProductConfig(Long id);

}