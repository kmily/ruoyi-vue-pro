package cn.iocoder.yudao.module.system.job;

import cn.iocoder.yudao.framework.quartz.core.handler.JobHandler;
import cn.iocoder.yudao.framework.tenant.core.aop.TenantIgnore;
import cn.iocoder.yudao.module.system.service.user.LogicDeletedCleanJobService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

/**
 * 清理逻辑删除数据 Job
 * 高风险Job,建议手动调用
 *
 * @author QusayRok
 */

@Component
public class LogicDeletedCleanJob implements JobHandler {

    @Resource
    private LogicDeletedCleanJobService deletedCleanJobService;

    /**
     * 执行清理逻辑删除的数据
     * 处理器名称: logicDeletedCleanJob
     * 它接受一个参数param，该参数用于指定清理操作的特定条件或配置
     * 参数示例: {"tableSchema":"ruoyi-vue-pro", "deletedField":"deleted", "deletedValue":1}
     * tableSchema:表示清理名为ruoyi-vue-pro的数据库中的逻辑删除数据
     * deletedField:表示逻辑删除字段的名称，默认为deleted
     * deletedValue:表示逻辑删除字段的值，默认为1
     * 由于后两个参数带来的风险不可控,目前只需传递 tableSchema参数即可
     *
     * @param param 清理操作的参数，用于指定清理的条件或配置
     * @return 返回清理操作的结果信息，格式化为字符串
     */

    @Override
    @TenantIgnore
    public String execute(String param) {
        String message = deletedCleanJobService.cleanLogicDeletedDatas(param);
        return String.format(message);
    }

}
