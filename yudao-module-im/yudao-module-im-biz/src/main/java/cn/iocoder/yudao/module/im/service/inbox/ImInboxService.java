package cn.iocoder.yudao.module.im.service.inbox;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.im.controller.admin.inbox.vo.ImInboxPageReqVO;
import cn.iocoder.yudao.module.im.controller.admin.inbox.vo.ImInboxSaveReqVO;
import cn.iocoder.yudao.module.im.dal.dataobject.inbox.ImInboxDO;
import jakarta.validation.Valid;

// TODO @hao：不用的方法，删除下哈；
/**
 * IM 收件箱 Service 接口
 *
 * @author 芋道源码
 */
public interface ImInboxService {

    /**
     * 创建收件箱
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createInbox(@Valid ImInboxSaveReqVO createReqVO);

    /**
     * 更新收件箱
     *
     * @param updateReqVO 更新信息
     */
    void updateInbox(@Valid ImInboxSaveReqVO updateReqVO);

    /**
     * 删除收件箱
     *
     * @param id 编号
     */
    void deleteInbox(Long id);

    /**
     * 获得收件箱
     *
     * @param id 编号
     * @return 收件箱
     */
    ImInboxDO getInbox(Long id);

    /**
     * 获得收件箱分页
     *
     * @param pageReqVO 分页查询
     * @return 收件箱分页
     */
    PageResult<ImInboxDO> getInboxPage(ImInboxPageReqVO pageReqVO);

}