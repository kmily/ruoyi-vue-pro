package cn.iocoder.yudao.module.radar.service.lineruledata;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;

import java.util.*;
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

}
