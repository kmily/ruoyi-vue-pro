package cn.iocoder.yudao.module.biz.service.calc;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import cn.iocoder.yudao.framework.common.pojo.PageResult;

import java.util.*;
import cn.iocoder.yudao.module.biz.controller.admin.calc.vo.*;
import cn.iocoder.yudao.module.biz.dal.dataobject.calc.CalcInterestRateDataDO;
import cn.iocoder.yudao.module.biz.convert.calc.CalcInterestRateDataConvert;
import cn.iocoder.yudao.module.biz.dal.mysql.calc.CalcInterestRateDataMapper;


/**
 * 利率数据 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class CalcInterestRateDataServiceImpl implements CalcInterestRateDataService {

    @Resource
    private CalcInterestRateDataMapper calcInterestRateDataMapper;

    @Override
    public Integer createCalcInterestRateData(CalcInterestRateDataCreateReqVO createReqVO) {
        // 插入
        CalcInterestRateDataDO calcInterestRateData = CalcInterestRateDataConvert.INSTANCE.convert(createReqVO);
        calcInterestRateDataMapper.insert(calcInterestRateData);
        // 返回
        return calcInterestRateData.getId();
    }

    @Override
    public void updateCalcInterestRateData(CalcInterestRateDataUpdateReqVO updateReqVO) {
        // 校验存在
        validateCalcInterestRateDataExists(updateReqVO.getId());
        // 更新
        CalcInterestRateDataDO updateObj = CalcInterestRateDataConvert.INSTANCE.convert(updateReqVO);
        calcInterestRateDataMapper.updateById(updateObj);
    }

    @Override
    public void deleteCalcInterestRateData(Integer id) {
        // 校验存在
        validateCalcInterestRateDataExists(id);
        // 删除
        calcInterestRateDataMapper.deleteById(id);
    }

    private void validateCalcInterestRateDataExists(Integer id) {
        if (calcInterestRateDataMapper.selectById(id) == null) {
//            throw exception(CALC_INTEREST_RATE_DATA_NOT_EXISTS);
        }
    }

    @Override
    public CalcInterestRateDataDO getCalcInterestRateData(Integer id) {
        return calcInterestRateDataMapper.selectById(id);
    }

    @Override
    public List<CalcInterestRateDataDO> getCalcInterestRateDataList(Collection<Integer> ids) {
        return calcInterestRateDataMapper.selectBatchIds(ids);
    }

    @Override
    public PageResult<CalcInterestRateDataDO> getCalcInterestRateDataPage(CalcInterestRateDataPageReqVO pageReqVO) {
        return calcInterestRateDataMapper.selectPage(pageReqVO);
    }

    @Override
    public List<CalcInterestRateDataDO> getCalcInterestRateDataList(CalcInterestRateDataExportReqVO exportReqVO) {
        return calcInterestRateDataMapper.selectList(exportReqVO);
    }

}
