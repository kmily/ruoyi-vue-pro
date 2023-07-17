package cn.iocoder.yudao.module.biz.service.calc;

import java.util.*;
import javax.validation.*;
import cn.iocoder.yudao.module.biz.controller.admin.calc.vo.*;
import cn.iocoder.yudao.module.biz.dal.dataobject.calc.CalcInterestRateDataDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
/**
 * 利率数据 Service 接口
 *
 * @author 芋道源码
 */
public interface CalcInterestRateDataService {

    /**
     * 创建利率数据
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Integer createCalcInterestRateData(@Valid CalcInterestRateDataCreateReqVO createReqVO);

    /**
     * 更新利率数据
     *
     * @param updateReqVO 更新信息
     */
    void updateCalcInterestRateData(@Valid CalcInterestRateDataUpdateReqVO updateReqVO);

    /**
     * 删除利率数据
     *
     * @param id 编号
     */
    void deleteCalcInterestRateData(Integer id);

    /**
     * 获得利率数据
     *
     * @param id 编号
     * @return 利率数据
     */
    CalcInterestRateDataDO getCalcInterestRateData(Integer id);

    /**
     * 获得利率数据列表
     *
     * @param ids 编号
     * @return 利率数据列表
     */
    List<CalcInterestRateDataDO> getCalcInterestRateDataList(Collection<Integer> ids);

    /**
     * 获得利率数据分页
     *
     * @param pageReqVO 分页查询
     * @return 利率数据分页
     */
    PageResult<CalcInterestRateDataDO> getCalcInterestRateDataPage(CalcInterestRateDataPageReqVO pageReqVO);

    /**
     * 获得利率数据列表, 用于 Excel 导出
     *
     * @param exportReqVO 查询条件
     * @return 利率数据列表
     */
    List<CalcInterestRateDataDO> getCalcInterestRateDataList(CalcInterestRateDataExportReqVO exportReqVO);

}
