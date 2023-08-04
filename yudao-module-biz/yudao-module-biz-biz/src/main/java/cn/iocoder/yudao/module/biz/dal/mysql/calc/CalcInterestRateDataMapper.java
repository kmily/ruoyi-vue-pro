package cn.iocoder.yudao.module.biz.dal.mysql.calc;

import java.math.BigDecimal;
import java.util.*;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.biz.controller.admin.calc.dto.ExecProcessDataDTO;
import cn.iocoder.yudao.module.biz.dal.dataobject.calc.CalcInterestRateDataDO;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.biz.controller.admin.calc.vo.*;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import org.apache.ibatis.annotations.Param;

/**
 * 利率数据 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface CalcInterestRateDataMapper extends BaseMapperX<CalcInterestRateDataDO> {


    /**
     * 查询利息
     */
    List<CalcInterestRateDataDO> selectAll();

    /**
     * 保存执行数据
     */
    Integer insertExecProcessDataBatch(@Param("list") List<ExecProcessDataDTO> dataList);

    Integer insertExecProcessDataMonthBatch(@Param("list") List<ExecProcessDataDTO> dataList);

    List<SectionIndexVO> selectSectionListByProcessAndYearType(@Param("processId") String processId);

    List<SectionIndexVO> selectSectionListByFixRate(@Param("processId") String processId);

    List<SectionIndexVO> selectSectionList4FxByFixRate(@Param("processId") String processId);

    List<SectionIndexVO> selectSectionListByFixMonthRate(@Param("processId") String processId);

    BigDecimal selectTotalAmountByProcessId(String processId);

    default PageResult<CalcInterestRateDataDO> selectPage(CalcInterestRateDataPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<CalcInterestRateDataDO>()
                .betweenIfPresent(CalcInterestRateDataDO::getStartDate, reqVO.getStartDate())
                .eqIfPresent(CalcInterestRateDataDO::getRateHalfYear, reqVO.getRateHalfYear())
                .eqIfPresent(CalcInterestRateDataDO::getRateOneYear, reqVO.getRateOneYear())
                .eqIfPresent(CalcInterestRateDataDO::getRateThreeYear, reqVO.getRateThreeYear())
                .eqIfPresent(CalcInterestRateDataDO::getRateFiveYear, reqVO.getRateFiveYear())
                .eqIfPresent(CalcInterestRateDataDO::getRateOverFiveYear, reqVO.getRateOverFiveYear())
                .betweenIfPresent(CalcInterestRateDataDO::getCreateTime, reqVO.getCreateTime())
                .orderByAsc(CalcInterestRateDataDO::getStartDate));
    }

    default List<CalcInterestRateDataDO> selectList(CalcInterestRateDataExportReqVO reqVO) {
        return selectList(new LambdaQueryWrapperX<CalcInterestRateDataDO>()
                .betweenIfPresent(CalcInterestRateDataDO::getStartDate, reqVO.getStartDate())
                .eqIfPresent(CalcInterestRateDataDO::getRateHalfYear, reqVO.getRateHalfYear())
                .eqIfPresent(CalcInterestRateDataDO::getRateOneYear, reqVO.getRateOneYear())
                .eqIfPresent(CalcInterestRateDataDO::getRateThreeYear, reqVO.getRateThreeYear())
                .eqIfPresent(CalcInterestRateDataDO::getRateFiveYear, reqVO.getRateFiveYear())
                .eqIfPresent(CalcInterestRateDataDO::getRateOverFiveYear, reqVO.getRateOverFiveYear())
                .betweenIfPresent(CalcInterestRateDataDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(CalcInterestRateDataDO::getId));
    }

}
