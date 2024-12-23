package cn.iocoder.yudao.module.haoka.service.superiorproductconfig;

import java.util.*;
import jakarta.validation.*;
import cn.iocoder.yudao.module.haoka.controller.admin.superiorproductconfig.vo.*;
import cn.iocoder.yudao.module.haoka.dal.dataobject.superiorproductconfig.SuperiorProductConfigDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;

/**
 * 产品对接上游配置 Service 接口
 *
 * @author 芋道源码
 */
public interface SuperiorProductConfigService {

    /**
     * 创建产品对接上游配置
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createSuperiorProductConfig(@Valid SuperiorProductConfigSaveReqVO createReqVO);

    /**
     * 更新产品对接上游配置
     *
     * @param updateReqVO 更新信息
     */
    void updateSuperiorProductConfig(@Valid SuperiorProductConfigSaveReqVO updateReqVO);

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

    /**
     * 获得产品对接上游配置分页
     *
     * @param pageReqVO 分页查询
     * @return 产品对接上游配置分页
     */
    PageResult<SuperiorProductConfigDO> getSuperiorProductConfigPage(SuperiorProductConfigPageReqVO pageReqVO);

}