package cn.iocoder.yudao.module.im.service.group;

import jakarta.validation.*;
import cn.iocoder.yudao.module.im.controller.admin.group.vo.*;
import cn.iocoder.yudao.module.im.dal.dataobject.group.ImGroupDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;

// TODO @hao：前缀 IM
/**
 * 群 Service 接口
 *
 * @author 芋道源码
 */
public interface GroupService {

    /**
     * 创建群
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createGroup(@Valid ImGroupSaveReqVO createReqVO);

    /**
     * 更新群
     *
     * @param updateReqVO 更新信息
     */
    void updateGroup(@Valid ImGroupSaveReqVO updateReqVO);

    /**
     * 删除群
     *
     * @param id 编号
     */
    void deleteGroup(Long id);

    /**
     * 获得群
     *
     * @param id 编号
     * @return 群
     */
    ImGroupDO getGroup(Long id);

    /**
     * 获得群分页
     *
     * @param pageReqVO 分页查询
     * @return 群分页
     */
    PageResult<ImGroupDO> getGroupPage(ImGroupPageReqVO pageReqVO);

}