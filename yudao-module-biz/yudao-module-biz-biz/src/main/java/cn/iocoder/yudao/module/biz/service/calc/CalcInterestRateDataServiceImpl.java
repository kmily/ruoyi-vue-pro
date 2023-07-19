package cn.iocoder.yudao.module.biz.service.calc;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.biz.controller.admin.calc.vo.*;
import cn.iocoder.yudao.module.biz.convert.calc.CalcInterestRateDataConvert;
import cn.iocoder.yudao.module.biz.dal.dataobject.calc.CalcInterestRateDataDO;
import cn.iocoder.yudao.module.biz.dal.mysql.calc.CalcInterestRateDataMapper;
import cn.iocoder.yudao.module.biz.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collection;
import java.util.Date;
import java.util.List;


/**
 * 利率数据 Service 实现类
 *
 * @author 芋道源码
 */
@Slf4j
@Service
@Validated
public class CalcInterestRateDataServiceImpl implements CalcInterestRateDataService {

    /**
     * LPR开始时间
     */
    private final static Date LPR_START_DATE = DateUtil.paseDate(DateUtil.DATE_FORMAT_NORMAL, "2019-10-08");
    /**
     * 罚息利率规定变更时间
     */
    private final static Date FX_DATE = DateUtil.paseDate(DateUtil.DATE_FORMAT_NORMAL, "2014-08-01");
    private final static BigDecimal FX_RATE = new BigDecimal("0.000175");

    @Resource
    private CalcInterestRateDataMapper calcInterestRateDataMapper;

    @Override
    public CalcInterestRateExecResVO execCalcInterestData(CalcInterestRateExecParamVO execVO) {
        CalcInterestRateExecResVO vo = new CalcInterestRateExecResVO();

        if (execVO.getCalcType() == 1) {
            execLx(execVO);
        } else if (execVO.getCalcType() == 2) {
            execFx(execVO);
        } else if (execVO.getCalcType() == 3) {
            execZxf(execVO);
        }

        return vo;
    }

    //获取所在年度天数
    private Integer getDaysThisYear() {

        return 0;
    }

    private CalcInterestRateExecResVO execLx(CalcInterestRateExecParamVO execVO) {
        List<CalcInterestRateDataDO> allRate = calcInterestRateDataMapper.selectAll();
        //计算开始结束时间差
        if (execVO.getLxRateType() == 1) {//1约定利率
            //计算日期差
            int days = DateUtil.dateIntervalDay(execVO.getLxStartDate(), execVO.getLxEndDate()) + 1;
            BigDecimal lxAmount =
                    execVO.getLxFixRate()
                            .divide(new BigDecimal(365))
                            .divide(new BigDecimal(100))
                            .multiply(new BigDecimal(days))
                            .multiply(execVO.getLxAmount())
                            .setScale(2, RoundingMode.HALF_DOWN);
            log.info(lxAmount.toString());
        } else if (execVO.getLxRateType() == 2) {//2中国人民银行同期贷款基准利率与LPR自动分段

        } else if (execVO.getLxRateType() == 3) {//3全国银行间同业拆借中心公布的贷款市场报价利率(LPR)

        }
        return null;
    }

    private CalcInterestRateExecResVO execFx(CalcInterestRateExecParamVO execVO) {

        return null;
    }

    private CalcInterestRateExecResVO execZxf(CalcInterestRateExecParamVO execVO) {

        return null;
    }


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
