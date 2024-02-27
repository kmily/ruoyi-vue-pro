package cn.iocoder.yudao.module.steam.service.invpreview;

import java.util.*;
import cn.iocoder.yudao.module.steam.controller.admin.invpreview.vo.*;
import cn.iocoder.yudao.module.steam.dal.dataobject.invpreview.InvPreviewDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;

import javax.validation.Valid;

/**
 * 饰品在售预览 Service 接口
 *
 * @author LeeAm
 */
public interface InvPreviewService {

    /**
     * 创建饰品在售预览
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createInvPreview(@Valid InvPreviewSaveReqVO createReqVO);

    /**
     * 更新饰品在售预览
     *
     * @param updateReqVO 更新信息
     */
    void updateInvPreview(@Valid InvPreviewSaveReqVO updateReqVO);

    /**
     * 删除饰品在售预览
     *
     * @param id 编号
     */
    void deleteInvPreview(Long id);

    /**
     * 获得饰品在售预览
     *
     * @param id 编号
     * @return 饰品在售预览
     */
    InvPreviewDO getInvPreview(Long id);

    /**
     * 获得饰品在售预览分页
     *
     * @param pageReqVO 分页查询
     * @return 饰品在售预览分页
     */
    PageResult<InvPreviewDO> getInvPreviewPage(InvPreviewPageReqVO pageReqVO);

}