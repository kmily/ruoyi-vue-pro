package cn.iocoder.yudao.module.biz.service.calc;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.biz.controller.admin.calc.dto.ExecProcessDataDTO;
import cn.iocoder.yudao.module.biz.controller.admin.calc.dto.MonthInfoDTO;
import cn.iocoder.yudao.module.biz.controller.admin.calc.dto.YearInfoDTO;
import cn.iocoder.yudao.module.biz.controller.admin.calc.vo.*;
import cn.iocoder.yudao.module.biz.convert.calc.CalcInterestRateDataConvert;
import cn.iocoder.yudao.module.biz.dal.dataobject.calc.CalcInterestRateDataDO;
import cn.iocoder.yudao.module.biz.dal.mysql.calc.CalcInterestRateDataMapper;
import cn.iocoder.yudao.module.biz.util.CodeUtil;
import cn.iocoder.yudao.module.biz.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
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
     * 罚息利率规定变更时间
     */
    private final static Date FX_DATE = DateUtil.paseDate("2014-08-01", DateUtil.DATE_FORMAT_NORMAL);
    private final static BigDecimal FX_RATE = new BigDecimal("0.000175");

    @Resource
    private CalcInterestRateDataMapper calcInterestRateDataMapper;

    @Override
    public CalcInterestRateExecResVO execCalcInterestLxData(CalcInterestRateExecLxParamVO paramVO) {
        String processId = CodeUtil.getUUID();
        paramVO.setProcessId(processId);
        return execLx(paramVO);
    }

    @Override
    public CalcInterestRateExecResVO execCalcInterestFxData(CalcInterestRateExecFxParamVO paramVO) {
        String processId = CodeUtil.getUUID();
        paramVO.setProcessId(processId);
        return execFx(paramVO);
    }

    @Override
    public CalcInterestRateExecResVO execCalcFeeData(CalcInterestRateExecFxParamVO execVO) {
        return null;
    }


    private List<YearInfoDTO> getYearsList(Date startDate, Date endDate) {
        List<YearInfoDTO> yearList = new ArrayList<>();
        List<Integer> yearVList = new ArrayList<>();
        while (startDate.compareTo(endDate) <= 0) {
            if (yearVList.contains(cn.hutool.core.date.DateUtil.year(startDate))) {
                startDate = DateUtil.addDays(startDate, 1);
                continue;
            }
            boolean isLeapYear = cn.hutool.core.date.DateUtil.isLeapYear(cn.hutool.core.date.DateUtil.year(startDate));
            yearList.add(new YearInfoDTO(cn.hutool.core.date.DateUtil.year(startDate), isLeapYear));
            yearVList.add(cn.hutool.core.date.DateUtil.year(startDate));
            startDate = DateUtil.addDays(startDate, 1);
        }
        return yearList;
    }

    //获取所在年度天数
    private Integer getDaysThisYear() {

        return 0;
    }

    /**
     * 处理利息+罚息
     */
    private CalcInterestRateExecResVO execLx(CalcInterestRateExecLxParamVO execVO) {
        CalcInterestRateExecResVO vo = new CalcInterestRateExecResVO();
        //计算开始结束时间差
        if (execVO.getRateType() == 1) {//1约定利率
            vo = getLxType1(execVO);
        } else if (execVO.getRateType() == 2) {//2中国人民银行同期贷款基准利率与LPR自动分段
            //计算日期区间,选择适用区间
            getLxType2(execVO);
        } else if (execVO.getRateType() == 3) {//3全国银行间同业拆借中心公布的贷款市场报价利率(LPR)

        }
        return vo;
    }

    private CalcInterestRateExecResVO execFx(CalcInterestRateExecFxParamVO execVO) {
        CalcInterestRateExecResVO vo = new CalcInterestRateExecResVO();
        //计算开始结束时间差
        if (execVO.getRateType() == 1) {//1约定利率
            vo = getFxType1(execVO);
        } else if (execVO.getRateType() == 2) {//2中国人民银行同期贷款基准利率与LPR自动分段
            //计算日期区间,选择适用区间
            getFxType2(execVO);
        } else if (execVO.getRateType() == 3) {//3全国银行间同业拆借中心公布的贷款市场报价利率(LPR)

        }
        return vo;
    }


    private CalcInterestRateExecResVO getFxType1(CalcInterestRateExecFxParamVO execVO) {
        CalcInterestRateExecResVO vo = new CalcInterestRateExecResVO();
        {
            //固定数值
            //计算日期差
            if (execVO.getFixSectionType() == 1) {
                //1-年
                Date startDate = execVO.getStartDate();
                Date endDate = execVO.getEndDate();
                BigDecimal dayRateValue = null;
                while (startDate.compareTo(endDate) <= 0) {
                    ExecProcessDataDTO execDataIndex = null;
                    if (startDate.compareTo(FX_DATE) < 0) {
                        //2014-08-01之前
                        dayRateValue = execVO.getFixRate()
                                .divide(new BigDecimal(100), 8, RoundingMode.HALF_UP)
                                .divide(new BigDecimal(365), 8, RoundingMode.HALF_UP);
                    } else {
                        //2014-08-01之后
                        dayRateValue = FX_RATE;
                    }
                    execDataIndex = new ExecProcessDataDTO(CodeUtil.getUUID(), execVO.getProcessId(), 0, startDate, dayRateValue, dayRateValue.multiply(execVO.getLeftAmount()));
                    calcInterestRateDataMapper.insertExecProcessData(execDataIndex);
                    startDate = DateUtil.addDays(startDate, 1);
                }
                vo.setSectionList(calcInterestRateDataMapper.selectSectionListByFixRate(execVO.getProcessId()));
                vo.setTotalAmount(calcInterestRateDataMapper.selectTotalAmountByProcessId(execVO.getProcessId()));
                vo.setProcessId(execVO.getProcessId());
            } else if (execVO.getFixSectionType() == 2) {
                //2-月
                //判断有多少个整月，然后最后不足一个月的有多少天
                Date startDate = execVO.getStartDate();
                Date endDate = execVO.getEndDate();
                List<MonthInfoDTO> monthList = getMonthList(startDate, endDate);
                while (startDate.compareTo(endDate) <= 0) {
                    BigDecimal dayRateValue = null;
                    /**
                     * 处理日利率
                     */
                    for (MonthInfoDTO monthIndex : monthList) {
                        if (startDate.compareTo(monthIndex.getMonthStartDate()) >= 0 && startDate.compareTo(monthIndex.getMonthEndDate()) <= 0) {
                            dayRateValue = execVO.getFixRate().divide(new BigDecimal(100)).divide(new BigDecimal(monthIndex.getDays()), 8, RoundingMode.HALF_UP);
                            break;
                        }
                    }
                    ExecProcessDataDTO execDataIndex = null;
                    if (startDate.compareTo(FX_DATE) >= 0) {
                        //2014-08-01之后
                        dayRateValue = FX_RATE;
                    }
                    execDataIndex = new ExecProcessDataDTO(CodeUtil.getUUID(), execVO.getProcessId(), 0, startDate, dayRateValue, dayRateValue.multiply(execVO.getLeftAmount()));
                    calcInterestRateDataMapper.insertExecProcessData(execDataIndex);
                    startDate = DateUtil.addDays(startDate, 1);
                }
                vo.setSectionList(calcInterestRateDataMapper.selectSectionListByFixMonthRate(execVO.getProcessId()));
                vo.setTotalAmount(calcInterestRateDataMapper.selectTotalAmountByProcessId(execVO.getProcessId()));
                vo.setProcessId(execVO.getProcessId());
            } else if (execVO.getFixSectionType() == 3) {
                //3-日
                Date startDate = execVO.getStartDate();
                Date endDate = execVO.getEndDate();
                BigDecimal dayRateValue = null;
                while (startDate.compareTo(endDate) <= 0) {
                    ExecProcessDataDTO execDataIndex = null;
                    if (startDate.compareTo(FX_DATE) < 0) {
                        //2014-08-01之前
                        dayRateValue = execVO.getFixRate().divide(new BigDecimal(100), 8, RoundingMode.HALF_UP);
                    } else {
                        //2014-08-01之后
                        dayRateValue = FX_RATE;
                    }
                    execDataIndex = new ExecProcessDataDTO(CodeUtil.getUUID(), execVO.getProcessId(), 0, startDate, dayRateValue, dayRateValue.multiply(execVO.getLeftAmount()));
                    calcInterestRateDataMapper.insertExecProcessData(execDataIndex);
                    startDate = DateUtil.addDays(startDate, 1);
                }
                vo.setSectionList(calcInterestRateDataMapper.selectSectionListByFixRate(execVO.getProcessId()));
                vo.setTotalAmount(calcInterestRateDataMapper.selectTotalAmountByProcessId(execVO.getProcessId()));
                vo.setProcessId(execVO.getProcessId());
            }

        }
        return vo;
    }


    private CalcInterestRateExecResVO getFxType2(CalcInterestRateExecFxParamVO execVO) {
        CalcInterestRateExecResVO vo = new CalcInterestRateExecResVO();
        //计算日期区间,选择适用区间
        String processId = execVO.getProcessId();
        Integer yearType = getYearType(execVO.getStartDate(), execVO.getEndDate());
        Date startDate = execVO.getStartDate();
        Date endDate = execVO.getEndDate();
        List<CalcInterestRateDataDO> allRateList = calcInterestRateDataMapper.selectAll();
        while (startDate.compareTo(endDate) <= 0) {
            BigDecimal suiteRateValue = null;
            if (startDate.compareTo(FX_DATE) < 0) {//2014-08-01之前
                CalcInterestRateDataDO suiteRate = getSuiteRate(allRateList, startDate);
                Integer yearDays = 365;
                if (yearType == 1) {
                    suiteRateValue = suiteRate.getRateHalfYear();
                } else if (yearType == 2) {
                    suiteRateValue = suiteRate.getRateOneYear();
                } else if (yearType == 3) {
                    suiteRateValue = suiteRate.getRateThreeYear();
                } else if (yearType == 4) {
                    suiteRateValue = suiteRate.getRateFiveYear();
                } else if (yearType == 5) {
                    suiteRateValue = suiteRate.getRateOverFiveYear();
                }
                suiteRateValue = suiteRateValue.divide(new BigDecimal(100), 8, RoundingMode.HALF_UP).divide(new BigDecimal(yearDays), 8, RoundingMode.HALF_UP);
                ExecProcessDataDTO execDataIndex = new ExecProcessDataDTO(CodeUtil.getUUID(), processId, suiteRate.getId(), startDate, suiteRateValue, suiteRateValue.multiply(execVO.getLeftAmount()));
                calcInterestRateDataMapper.insertExecProcessData(execDataIndex);
            } else {
                suiteRateValue = FX_RATE;
                ExecProcessDataDTO execDataIndex = new ExecProcessDataDTO(CodeUtil.getUUID(), processId, 0, startDate, suiteRateValue, suiteRateValue.multiply(execVO.getLeftAmount()));
                calcInterestRateDataMapper.insertExecProcessData(execDataIndex);
            }
            startDate = DateUtil.addDays(startDate, 1);
        }
        vo.setSectionList(calcInterestRateDataMapper.selectSectionListByProcessAndYearType(processId, yearType));
        vo.setTotalAmount(calcInterestRateDataMapper.selectTotalAmountByProcessId(processId));
        vo.setProcessId(processId);
        return vo;
    }


    private CalcInterestRateExecResVO getLxType1(CalcInterestRateExecLxParamVO execVO) {
        CalcInterestRateExecResVO vo = new CalcInterestRateExecResVO();
        if (execVO.getFixType() == 1) {
            //固定数值
            //计算日期差
            if (execVO.getFixSectionType() == 1) {
                //1-年
                Date startDate = execVO.getStartDate();
                Date endDate = execVO.getEndDate();
                BigDecimal dayRateValue = execVO.getFixRate()
                        .divide(new BigDecimal(100), 8, RoundingMode.HALF_UP)
                        .divide(new BigDecimal(365), 8, RoundingMode.HALF_UP);
                while (startDate.compareTo(endDate) <= 0) {
                    ExecProcessDataDTO execDataIndex = new ExecProcessDataDTO(CodeUtil.getUUID(), execVO.getProcessId(), 0, startDate, dayRateValue, dayRateValue.multiply(execVO.getLeftAmount()));
                    calcInterestRateDataMapper.insertExecProcessData(execDataIndex);
                    startDate = DateUtil.addDays(startDate, 1);
                }
                vo.setSectionList(calcInterestRateDataMapper.selectSectionListByFixRate(execVO.getProcessId()));
                vo.setTotalAmount(calcInterestRateDataMapper.selectTotalAmountByProcessId(execVO.getProcessId()));
                vo.setProcessId(execVO.getProcessId());
            } else if (execVO.getFixSectionType() == 2) {
                //2-月
                //判断有多少个整月，然后最后不足一个月的有多少天
                Date startDate = execVO.getStartDate();
                Date endDate = execVO.getEndDate();
                List<MonthInfoDTO> monthList = getMonthList(startDate, endDate);
                while (startDate.compareTo(endDate) <= 0) {
                    BigDecimal dayRateValue = null;
                    /**
                     * 处理日利率
                     */
                    for (MonthInfoDTO monthIndex : monthList) {
                        if (startDate.compareTo(monthIndex.getMonthStartDate()) >= 0 && startDate.compareTo(monthIndex.getMonthEndDate()) <= 0) {
                            dayRateValue = execVO.getFixRate().divide(new BigDecimal(100)).divide(new BigDecimal(monthIndex.getDays()), 8, RoundingMode.HALF_UP);
                            break;
                        }
                    }
                    ExecProcessDataDTO execDataIndex = new ExecProcessDataDTO(CodeUtil.getUUID(), execVO.getProcessId(), 0, startDate, dayRateValue, dayRateValue.multiply(execVO.getLeftAmount()));
                    calcInterestRateDataMapper.insertExecProcessData(execDataIndex);
                    startDate = DateUtil.addDays(startDate, 1);
                }
                vo.setSectionList(calcInterestRateDataMapper.selectSectionListByFixMonthRate(execVO.getProcessId()));
                vo.setTotalAmount(calcInterestRateDataMapper.selectTotalAmountByProcessId(execVO.getProcessId()));
                vo.setProcessId(execVO.getProcessId());
            } else if (execVO.getFixSectionType() == 3) {
                //3-日
                Date startDate = execVO.getStartDate();
                Date endDate = execVO.getEndDate();
                BigDecimal dayRateValue = execVO.getFixRate().divide(new BigDecimal(100), 8, RoundingMode.HALF_UP);
                while (startDate.compareTo(endDate) <= 0) {
                    ExecProcessDataDTO execDataIndex = new ExecProcessDataDTO(CodeUtil.getUUID(), execVO.getProcessId(), 0, startDate, dayRateValue, dayRateValue.multiply(execVO.getLeftAmount()));
                    calcInterestRateDataMapper.insertExecProcessData(execDataIndex);
                    startDate = DateUtil.addDays(startDate, 1);
                }
                vo.setSectionList(calcInterestRateDataMapper.selectSectionListByFixRate(execVO.getProcessId()));
                vo.setTotalAmount(calcInterestRateDataMapper.selectTotalAmountByProcessId(execVO.getProcessId()));
                vo.setProcessId(execVO.getProcessId());
            }

        } else if (execVO.getFixType() == 2) {
            List<CalcInterestRateDataDO> allRateList = calcInterestRateDataMapper.selectAll();
            //LPR倍数
            Date startDate = execVO.getStartDate();
            Date endDate = execVO.getEndDate();
            if (execVO.getFixLPRs().compareTo(new BigDecimal("4")) == 0 && DateUtil.format(startDate, DateUtil.DATE_FORMAT_NORMAL).compareTo(DateUtil.format(new Date("2019-01-01"), DateUtil.DATE_FORMAT_NORMAL)) > 0) {
                //2019年以前不可以选择LPR4倍数据
                throw new IllegalArgumentException("起始时间是2019年以前的计算，不可以选择4倍LPR！");
            }
            while (startDate.compareTo(endDate) <= 0) {
                CalcInterestRateDataDO suiteRate = getSuiteRate(allRateList, startDate);
                Integer yearDays = 365;
                BigDecimal suiteRateValue = null;
                BigDecimal suiteOneYearRateValue = null;
                Integer yearType = getYearType(execVO.getStartDate(), execVO.getEndDate());
                if (yearType == 1) {
                    suiteRateValue = suiteRate.getRateHalfYear();
                } else if (yearType == 2) {
                    suiteOneYearRateValue = suiteRate.getRateOneYear();
                    suiteRateValue = suiteRate.getRateOneYear();
                } else if (yearType == 3) {
                    suiteRateValue = suiteRate.getRateThreeYear();
                } else if (yearType == 4) {
                    suiteRateValue = suiteRate.getRateFiveYear();
                } else if (yearType == 5) {
                    suiteRateValue = suiteRate.getRateOverFiveYear();
                }
                if (execVO.getFixLPRs().compareTo(new BigDecimal("4")) == 0) {
                    suiteRateValue = suiteOneYearRateValue.multiply(execVO.getFixLPRs()).divide(new BigDecimal(100), 8, RoundingMode.HALF_UP).divide(new BigDecimal(yearDays), 8, RoundingMode.HALF_UP);
                } else {
                    suiteRateValue = suiteRateValue.multiply(execVO.getFixLPRs()).divide(new BigDecimal(100), 8, RoundingMode.HALF_UP).divide(new BigDecimal(yearDays), 8, RoundingMode.HALF_UP);
                }
                ExecProcessDataDTO execDataIndex = new ExecProcessDataDTO(CodeUtil.getUUID(), execVO.getProcessId(), suiteRate.getId(), startDate, suiteRateValue, suiteRateValue.multiply(execVO.getLeftAmount()));
                calcInterestRateDataMapper.insertExecProcessData(execDataIndex);
                startDate = DateUtil.addDays(startDate, 1);
            }
            vo.setSectionList(calcInterestRateDataMapper.selectSectionListByFixMonthRate(execVO.getProcessId()));
            vo.setTotalAmount(calcInterestRateDataMapper.selectTotalAmountByProcessId(execVO.getProcessId()));
            vo.setProcessId(execVO.getProcessId());

        }
        return vo;
    }

    private CalcInterestRateDataDO getSuiteRate(List<CalcInterestRateDataDO> allRateList, Date startDate) {
        CalcInterestRateDataDO suiteRate = null;
        for (CalcInterestRateDataDO index : allRateList) {
            if (index.getStartDate().compareTo(startDate) > 0) {
                break;
            }
            suiteRate = index;
        }
        return suiteRate;
    }

    private List<MonthInfoDTO> getMonthList(Date startDate, Date endDate) {
        List<MonthInfoDTO> monthList = new ArrayList<>();
        while (startDate.compareTo(endDate) <= 0) {
            Date end = DateUtil.addDays(cn.hutool.core.date.DateUtil.offsetMonth(startDate, 1), -1);
            Integer isFull = 1;
            Integer days = 0;
            if (end.compareTo(endDate) > 0) {
                isFull = 0;
                days = DateUtil.dateIntervalDay(startDate, end);
                end = endDate;
            } else {
                days = DateUtil.dateIntervalDay(startDate, end);
            }
            MonthInfoDTO monthInfoDTO = new MonthInfoDTO(startDate, end, isFull, days);
            if (!monthList.contains(monthInfoDTO)) {
                monthList.add(monthInfoDTO);
            }
            startDate = cn.hutool.core.date.DateUtil.offsetMonth(startDate, 1);//加一个月
        }
        return monthList;
    }

    private CalcInterestRateExecResVO getLxType2(CalcInterestRateExecLxParamVO execVO) {
        CalcInterestRateExecResVO vo = new CalcInterestRateExecResVO();
        Integer yearType = getYearType(execVO.getStartDate(), execVO.getEndDate());
        List<CalcInterestRateDataDO> allRateList = calcInterestRateDataMapper.selectAll();
        Date startDate = execVO.getStartDate();
        Date endDate = execVO.getEndDate();
        while (startDate.compareTo(endDate) <= 0) {
            CalcInterestRateDataDO suiteRate = getSuiteRate(allRateList, startDate);
            BigDecimal suiteRateValue = null;
            Integer yearDays = 365;
            if (yearType == 1) {
                suiteRateValue = suiteRate.getRateHalfYear();
            } else if (yearType == 2) {
                suiteRateValue = suiteRate.getRateOneYear();
            } else if (yearType == 3) {
                suiteRateValue = suiteRate.getRateThreeYear();
            } else if (yearType == 4) {
                suiteRateValue = suiteRate.getRateFiveYear();
            } else if (yearType == 5) {
                suiteRateValue = suiteRate.getRateOverFiveYear();
            }
            suiteRateValue = suiteRateValue.divide(new BigDecimal(100), 8, RoundingMode.HALF_UP).divide(new BigDecimal(yearDays), 8, RoundingMode.HALF_UP);
            ExecProcessDataDTO execDataIndex = new ExecProcessDataDTO(CodeUtil.getUUID(), execVO.getProcessId(), suiteRate.getId(), startDate, suiteRateValue, suiteRateValue.multiply(execVO.getLeftAmount()));
            calcInterestRateDataMapper.insertExecProcessData(execDataIndex);
            startDate = DateUtil.addDays(startDate, 1);
        }
        vo.setSectionList(calcInterestRateDataMapper.selectSectionListByProcessAndYearType(execVO.getProcessId(), yearType));
        vo.setTotalAmount(calcInterestRateDataMapper.selectTotalAmountByProcessId(execVO.getProcessId()));
        vo.setProcessId(execVO.getProcessId());
        return vo;
    }

    private Integer getYearType(Date startDate, Date endDate) {
        Integer yearType = 1;
        int diffDays = DateUtil.dateIntervalDay(startDate, endDate);
        //判断所属区间
        BigDecimal yt = new BigDecimal(diffDays).divide(new BigDecimal(365), 2, RoundingMode.HALF_UP);
        if (new BigDecimal("0.5").compareTo(yt) >= 0) {
            //半年期
            yearType = 1;
        } else if (new BigDecimal("0.5").compareTo(yt) < 0 && BigDecimal.ONE.compareTo(yt) >= 0) {
            //一年期
            yearType = 2;
        } else if (BigDecimal.ONE.compareTo(yt) < 0 && new BigDecimal("3").compareTo(yt) >= 0) {
            //三年期
            yearType = 3;
        } else if (new BigDecimal("3").compareTo(yt) < 0 && new BigDecimal("5").compareTo(yt) >= 0) {
            //三年到五年期
            yearType = 4;
        } else if (new BigDecimal("5").compareTo(yt) < 0) {
            //五年以上
            yearType = 5;
        }
        return yearType;
    }


    private CalcInterestRateExecResVO execZxf(CalcInterestRateExecFxParamVO execVO) {

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
