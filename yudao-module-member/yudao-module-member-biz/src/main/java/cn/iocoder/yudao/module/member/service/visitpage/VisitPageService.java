package cn.iocoder.yudao.module.member.service.visitpage;

import java.util.*;
import javax.validation.*;
import cn.iocoder.yudao.module.member.controller.admin.visitpage.vo.*;
import cn.iocoder.yudao.module.member.dal.dataobject.visitpage.VisitPageDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;

/**
 * 页面访问数据 Service 接口
 *
 * @author 和尘同光
 */
public interface VisitPageService {

    /**
     * 创建页面访问数据
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createVisitPage(@Valid VisitPageCreateReqVO createReqVO);

    /**
     * 更新页面访问数据
     *
     * @param updateReqVO 更新信息
     */
    void updateVisitPage(@Valid VisitPageUpdateReqVO updateReqVO);

    /**
     * 删除页面访问数据
     *
     * @param id 编号
     */
    void deleteVisitPage(Long id);

    /**
     * 获得页面访问数据
     *
     * @param id 编号
     * @return 页面访问数据
     */
    VisitPageDO getVisitPage(Long id);

    /**
     * 获得页面访问数据列表
     *
     * @param ids 编号
     * @return 页面访问数据列表
     */
    List<VisitPageDO> getVisitPageList(Collection<Long> ids);

    /**
     * 获得页面访问数据分页
     *
     * @param pageReqVO 分页查询
     * @return 页面访问数据分页
     */
    PageResult<VisitPageDO> getVisitPagePage(VisitPagePageReqVO pageReqVO);

    /**
     * 获得页面访问数据列表, 用于 Excel 导出
     *
     * @param exportReqVO 查询条件
     * @return 页面访问数据列表
     */
    List<VisitPageDO> getVisitPageList(VisitPageExportReqVO exportReqVO);


    /**
     * 退出页面
     * @param id
     */
    void exit(Long id);
}
