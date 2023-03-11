package cn.iocoder.yudao.module.bpm.service.task;

import cn.iocoder.yudao.framework.tenant.core.db.dynamic.TenantDS;
import cn.iocoder.yudao.module.bpm.controller.admin.task.vo.activity.BpmActivityRespVO;
import cn.iocoder.yudao.module.bpm.convert.task.BpmActivityConvert;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.HistoryService;
import org.flowable.engine.history.HistoricActivityInstance;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.util.List;


/**
 * BPM 活动实例 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Slf4j
@Validated
@TenantDS // 工作流的 Service 必须添加 @TenantDS 注解。原因是，Flowable 使用事务，无法切换数据源，需要提使用 @TenantDS 切到它的数据源
public class BpmActivityServiceImpl implements BpmActivityService {

    @Resource
    private HistoryService historyService;

    @Override
    public List<BpmActivityRespVO> getActivityListByProcessInstanceId(String processInstanceId) {
        List<HistoricActivityInstance> activityList = historyService.createHistoricActivityInstanceQuery()
                .processInstanceId(processInstanceId).list();
        return BpmActivityConvert.INSTANCE.convertList(activityList);
    }

    @Override
    public List<HistoricActivityInstance> getHistoricActivityListByExecutionId(String executionId) {
        return historyService.createHistoricActivityInstanceQuery().executionId(executionId).list();
    }

}
