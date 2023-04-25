package cn.iocoder.yudao.module.system.service.signreq;

import java.util.*;
import javax.validation.*;
import cn.iocoder.yudao.module.system.controller.admin.signreq.vo.*;
import cn.iocoder.yudao.module.system.dal.dataobject.signreq.SignReqDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;

/**
 * 注册申请 Service 接口
 *
 * @author 芋道源码
 */
public interface SignReqService {

    /**
     * 创建注册申请
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createSignReq(@Valid SignReqCreateReqVO createReqVO);

    /**
     * 更新注册申请
     *
     * @param updateReqVO 更新信息
     */
    void updateSignReq(@Valid SignReqUpdateReqVO updateReqVO);

    /**
     * 删除注册申请
     *
     * @param id 编号
     */
    void deleteSignReq(Long id);

    /**
     * 获得注册申请
     *
     * @param id 编号
     * @return 注册申请
     */
    SignReqDO getSignReq(Long id);

    /**
     * 获得注册申请列表
     *
     * @param ids 编号
     * @return 注册申请列表
     */
    List<SignReqDO> getSignReqList(Collection<Long> ids);

    /**
     * 获得注册申请分页
     *
     * @param pageReqVO 分页查询
     * @return 注册申请分页
     */
    PageResult<SignReqDO> getSignReqPage(SignReqPageReqVO pageReqVO);

    /**
     * 获得注册申请列表, 用于 Excel 导出
     *
     * @param exportReqVO 查询条件
     * @return 注册申请列表
     */
    List<SignReqDO> getSignReqList(SignReqExportReqVO exportReqVO);

}
