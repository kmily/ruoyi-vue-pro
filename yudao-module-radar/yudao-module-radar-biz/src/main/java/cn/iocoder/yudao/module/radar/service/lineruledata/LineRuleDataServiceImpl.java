package cn.iocoder.yudao.module.radar.service.lineruledata;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.iocoder.yudao.module.radar.controller.app.lineruledata.vo.*;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.stream.Collectors;

import cn.iocoder.yudao.module.radar.controller.admin.lineruledata.vo.*;
import cn.iocoder.yudao.module.radar.dal.dataobject.lineruledata.LineRuleDataDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;

import cn.iocoder.yudao.module.radar.convert.lineruledata.LineRuleDataConvert;
import cn.iocoder.yudao.module.radar.dal.mysql.lineruledata.LineRuleDataMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
//import static cn.iocoder.yudao.module.radar.enums.ErrorCodeConstants.*;

/**
 * 绊线数据 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class LineRuleDataServiceImpl implements LineRuleDataService {

    @Resource
    private LineRuleDataMapper lineRuleDataMapper;

    @Override
    public Long createLineRuleData(LineRuleDataCreateReqVO createReqVO) {
        // 插入
        LineRuleDataDO lineRuleData = LineRuleDataConvert.INSTANCE.convert(createReqVO);
        lineRuleDataMapper.insert(lineRuleData);
        // 返回
        return lineRuleData.getId();
    }

    @Override
    public void updateLineRuleData(LineRuleDataUpdateReqVO updateReqVO) {
        // 校验存在
        validateLineRuleDataExists(updateReqVO.getId());
        // 更新
        LineRuleDataDO updateObj = LineRuleDataConvert.INSTANCE.convert(updateReqVO);
        lineRuleDataMapper.updateById(updateObj);
    }

    @Override
    public void deleteLineRuleData(Long id) {
        // 校验存在
        validateLineRuleDataExists(id);
        // 删除
        lineRuleDataMapper.deleteById(id);
    }

    private void validateLineRuleDataExists(Long id) {
        if (lineRuleDataMapper.selectById(id) == null) {
            //throw exception(LINE_RULE_DATA_NOT_EXISTS);
        }
    }

    @Override
    public LineRuleDataDO getLineRuleData(Long id) {
        return lineRuleDataMapper.selectById(id);
    }

    @Override
    public List<LineRuleDataDO> getLineRuleDataList(Collection<Long> ids) {
        return lineRuleDataMapper.selectBatchIds(ids);
    }

    @Override
    public PageResult<LineRuleDataDO> getLineRuleDataPage(LineRuleDataPageReqVO pageReqVO) {
        return lineRuleDataMapper.selectPage(pageReqVO);
    }

    @Override
    public List<LineRuleDataDO> getLineRuleDataList(LineRuleDataExportReqVO exportReqVO) {
        return lineRuleDataMapper.selectList(exportReqVO);
    }

    @Override
    public List<AppLineRuleDataResVO> getLineRuleDataList(Long deviceId, String  startDate, String endDate) {

        String start = startDate + " 00:00:00";
        String end = endDate + " 23:59:59";
        List<LineRuleDataDO> list = lineRuleDataMapper.selectList(deviceId, start, end);
        if(CollUtil.isEmpty(list)){
            return new ArrayList<>();
        }
        Map<String, List<LineRuleDataDO>> dateToDataMap = list.stream().collect(Collectors.groupingBy(item -> LocalDateTimeUtil.format(item.getCreateTime(), "yyyy-MM-dd")));
        List<AppLineRuleDataResVO> resVOS = new ArrayList<>();
        dateToDataMap.forEach((date, ruleDataList) -> {
            AppLineRuleDataResVO resVO = new AppLineRuleDataResVO();
            resVO.setDate(date);
            List<AppLineRuleDataInfoVO> infoVOS = ruleDataList.stream().map(AppLineRuleDataInfoVO::new).collect(Collectors.toList());
            Long dateTime = null;
            List<AppLineRuleDataInfoVO> collect = infoVOS.stream().filter(AppLineRuleDataInfoVO::getGoOut).collect(Collectors.toList());
            if(CollUtil.isNotEmpty(collect)){
                 dateTime = collect.stream().map(item -> item.getCreateTime().toInstant(ZoneOffset.of("+8")).toEpochMilli())
                        .max((o1, o2) -> (int)(o2 - o1)).orElse(null);
            }

            resVO.setRuleDataVO(new AppLineRuleDataVO().setDetails(infoVOS).setLastGoOut(dateTime));

            resVOS.add(resVO);
        });
        return resVOS;
    }

    @Override
    public PageResult<AppLineRuleDataInfoVO> queryEnterAndLeaveDetail(AppEntryLeaveDetailPageReqVO reqVO) {
        String queryDate = reqVO.getQueryDate();
        String start = queryDate + " 00:00:00";
        String end = queryDate + " 23:59:59";

       List<LineRuleDataDO> list =  lineRuleDataMapper.selectList(reqVO.getDeviceId(), start, end);

       List<AppLineRuleDataInfoVO> splitList = new ArrayList<>();

       list.forEach(item -> {
           Integer enter = item.getEnter();
           Integer goOut = item.getGoOut();
           if(enter > 0){
                splitList.add(new AppLineRuleDataInfoVO().setCreateTime(item.getCreateTime()).setEnter(true));
           }
           if(goOut > 0){
               splitList.add(new AppLineRuleDataInfoVO().setCreateTime(item.getCreateTime()).setGoOut(true));
           }
       });

        Integer pageNo = reqVO.getPageNo();
        Integer pageSize = reqVO.getPageSize();

        int startIndex = (pageNo - 1) * pageSize;
        int endIndex = startIndex + pageSize;
        List<AppLineRuleDataInfoVO> infoVOS = null;
        if(startIndex >= splitList.size()){
            infoVOS = new ArrayList<>();
        }else{
            infoVOS = splitList.subList(startIndex, Math.min(endIndex, splitList.size()));
        }
        return new PageResult<>(infoVOS, (long) splitList.size());
    }

}
