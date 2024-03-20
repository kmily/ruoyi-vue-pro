package cn.iocoder.yudao.module.steam.service.alipayisv;

import java.util.*;
import javax.validation.*;
import cn.iocoder.yudao.module.steam.controller.admin.alipayisv.vo.*;
import cn.iocoder.yudao.module.steam.dal.dataobject.alipayisv.AlipayIsvDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;

/**
 * 签约ISV用户 Service 接口
 *
 * @author 管理员
 */
public interface AlipayIsvService {

    /**
     * 创建签约ISV用户
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createAlipayIsv(@Valid AlipayIsvSaveReqVO createReqVO);

    /**
     * 更新签约ISV用户
     *
     * @param updateReqVO 更新信息
     */
    void updateAlipayIsv(@Valid AlipayIsvSaveReqVO updateReqVO);

    /**
     * 删除签约ISV用户
     *
     * @param id 编号
     */
    void deleteAlipayIsv(Long id);

    /**
     * 获得签约ISV用户
     *
     * @param id 编号
     * @return 签约ISV用户
     */
    AlipayIsvDO getAlipayIsv(Long id);

    /**
     * 获得签约ISV用户分页
     *
     * @param pageReqVO 分页查询
     * @return 签约ISV用户分页
     */
    PageResult<AlipayIsvDO> getAlipayIsvPage(AlipayIsvPageReqVO pageReqVO);

}