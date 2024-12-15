package cn.iocoder.yudao.module.haoka.service.superiorapi;

import java.util.*;
import jakarta.validation.*;
import cn.iocoder.yudao.module.haoka.controller.admin.superiorapi.vo.*;
import cn.iocoder.yudao.module.haoka.dal.dataobject.superiorapi.SuperiorApiDevConfigDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;

/**
 * 上游API接口开发配置 Service 接口
 *
 * @author 芋道源码
 */
public interface SuperiorApiDevConfigService {

    /**
     * 创建上游API接口开发配置
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createSuperiorApiDevConfig(@Valid SuperiorApiDevConfigSaveReqVO createReqVO);

    /**
     * 更新上游API接口开发配置
     *
     * @param updateReqVO 更新信息
     */
    void updateSuperiorApiDevConfig(@Valid SuperiorApiDevConfigSaveReqVO updateReqVO);

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

    /**
     * 获得上游API接口开发配置分页
     *
     * @param pageReqVO 分页查询
     * @return 上游API接口开发配置分页
     */
    PageResult<SuperiorApiDevConfigDO> getSuperiorApiDevConfigPage(SuperiorApiDevConfigPageReqVO pageReqVO);

}