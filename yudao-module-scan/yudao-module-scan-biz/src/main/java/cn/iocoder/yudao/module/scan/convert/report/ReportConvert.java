package cn.iocoder.yudao.module.scan.convert.report;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;

import cn.iocoder.yudao.module.scan.controller.app.report.vo.AppReportCreateReqVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import cn.iocoder.yudao.module.scan.controller.admin.report.vo.*;
import cn.iocoder.yudao.module.scan.controller.app.report.vo.*;
import cn.iocoder.yudao.module.scan.dal.dataobject.report.ReportDO;

/**
 * 报告 Convert
 *
 * @author lyz
 */
@Mapper
public interface ReportConvert {

    ReportConvert INSTANCE = Mappers.getMapper(ReportConvert.class);

    ReportDO convert(ReportCreateReqVO bean);

    ReportDO convert(ReportUpdateReqVO bean);

    ReportRespVO convert(ReportDO bean);

    List<ReportRespVO> convertList(List<ReportDO> list);

    PageResult<ReportRespVO> convertPage(PageResult<ReportDO> page);

    PageResult<AppReportRespVO> convertPage02(PageResult<ReportDO> page);

    List<ReportExcelVO> convertList02(List<ReportDO> list);

    ReportCreateReqVO convert(AppReportCreateReqVO bean);
    AppReportRespVO convert02(ReportDO bean);
    ReportPageReqVO convert(AppReportPageReqVO bean);
}
