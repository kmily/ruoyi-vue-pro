package cn.iocoder.yudao.module.steam.service.youyoutemplate;

import java.util.*;
import javax.validation.*;
import cn.iocoder.yudao.module.steam.controller.admin.youyoutemplate.vo.*;
import cn.iocoder.yudao.module.steam.dal.dataobject.youyoutemplate.YouyouTemplateDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;

/**
 * 悠悠商品模板 Service 接口
 *
 * @author 管理员
 */
public interface YouyouTemplateService {

    /**
     * 创建悠悠商品模板
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Integer createYouyouTemplate(@Valid YouyouTemplateSaveReqVO createReqVO);

    /**
     * 更新悠悠商品模板
     *
     * @param updateReqVO 更新信息
     */
    void updateYouyouTemplate(@Valid YouyouTemplateSaveReqVO updateReqVO);

    /**
     * 删除悠悠商品模板
     *
     * @param id 编号
     */
    void deleteYouyouTemplate(Integer id);

    /**
     * 获得悠悠商品模板
     *
     * @param id 编号
     * @return 悠悠商品模板
     */
    YouyouTemplateDO getYouyouTemplate(Integer id);

    /**
     * 获得悠悠商品模板分页
     *
     * @param pageReqVO 分页查询
     * @return 悠悠商品模板分页
     */
    PageResult<YouyouTemplateDO> getYouyouTemplatePage(YouyouTemplatePageReqVO pageReqVO);

}