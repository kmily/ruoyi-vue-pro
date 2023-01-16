package cn.iocoder.yudao.module.scan.service.report;

import java.util.*;
import javax.validation.*;
import cn.iocoder.yudao.module.scan.controller.admin.report.vo.*;
import cn.iocoder.yudao.module.scan.dal.dataobject.report.ReportDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;

/**
 * 报告 Service 接口
 *
 * @author lyz
 */
public interface ReportService {

    /**
     * 创建报告
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createReport(@Valid ReportCreateReqVO createReqVO);

    /**
     * 更新报告
     *
     * @param updateReqVO 更新信息
     */
    void updateReport(@Valid ReportUpdateReqVO updateReqVO);

    /**
     * 删除报告
     *
     * @param id 编号
     */
    void deleteReport(Long id);

    /**
     * 获得报告
     *
     * @param id 编号
     * @return 报告
     */
    ReportDO getReport(Long id);

    /**
     * 获得报告列表
     *
     * @param ids 编号
     * @return 报告列表
     */
    List<ReportDO> getReportList(Collection<Long> ids);

    /**
     * 获得报告分页
     *
     * @param pageReqVO 分页查询
     * @return 报告分页
     */
    PageResult<ReportDO> getReportPage(ReportPageReqVO pageReqVO);

    /**
     * 获得报告列表, 用于 Excel 导出
     *
     * @param exportReqVO 查询条件
     * @return 报告列表
     */
    List<ReportDO> getReportList(ReportExportReqVO exportReqVO);
    /**
     * 生成报告文件
     *
     * @param id 编号
     */
    String generateReportFile(Long id);
}
