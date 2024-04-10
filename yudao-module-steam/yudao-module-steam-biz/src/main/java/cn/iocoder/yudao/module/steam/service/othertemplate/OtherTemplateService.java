package cn.iocoder.yudao.module.steam.service.othertemplate;

import java.util.*;
import javax.validation.*;
import cn.iocoder.yudao.module.steam.controller.admin.othertemplate.vo.*;
import cn.iocoder.yudao.module.steam.dal.dataobject.othertemplate.OtherTemplateDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;

/**
 * 其他平台模板 Service 接口
 *
 * @author 管理员
 */
public interface OtherTemplateService {

    /**
     * 创建其他平台模板
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Integer createOtherTemplate(@Valid OtherTemplateSaveReqVO createReqVO);

    /**
     * 更新其他平台模板
     *
     * @param updateReqVO 更新信息
     */
    void updateOtherTemplate(@Valid OtherTemplateSaveReqVO updateReqVO);

    /**
     * 删除其他平台模板
     *
     * @param id 编号
     */
    void deleteOtherTemplate(Integer id);

    /**
     * 获得其他平台模板
     *
     * @param id 编号
     * @return 其他平台模板
     */
    OtherTemplateDO getOtherTemplate(Integer id);

    /**
     * 获得其他平台模板分页
     *
     * @param pageReqVO 分页查询
     * @return 其他平台模板分页
     */
    PageResult<OtherTemplateDO> getOtherTemplatePage(OtherTemplatePageReqVO pageReqVO);

}