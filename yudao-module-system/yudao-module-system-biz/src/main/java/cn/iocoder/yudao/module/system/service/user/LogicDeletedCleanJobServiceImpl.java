package cn.iocoder.yudao.module.system.service.user;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.iocoder.yudao.module.system.dal.dataobject.user.LogicDeletedCleanJobDO;
import cn.iocoder.yudao.module.system.dal.mysql.user.LogicDeletedCleanJobMapper;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Job任务 清除逻辑删除数据 实现类
 *
 * @author QusayRok
 *
 */

@Service("logicDeletedCleanJobService")
@Slf4j
public class LogicDeletedCleanJobServiceImpl implements LogicDeletedCleanJobService {

    private final static String LOGIC_DELETE_FIELD = "deleted";

    @Value("${mybatis-plus.global-config.db-config.logic-delete-value}")
    private Integer LOGIC_DELETE_VALUE;

    @Resource
    private LogicDeletedCleanJobMapper clearDeletedMapper;

    @Override
    public String cleanLogicDeletedDatas(String param) {
        JSONObject paramMap = JSONUtil.parseObj(param);
        //参数校验
        String tableSchema = ObjectUtil.isNotEmpty(paramMap.get("tableSchema")) ? paramMap.getStr("tableSchema") : "";
        //传入该字段风险过高, 暂时先写死
//        String deletedField = ObjectUtil.isNotEmpty(paramMap.get("deletedField")) ? paramMap.getStr("deletedField") : "";
        String deletedField = LOGIC_DELETE_FIELD;
        //自定义删除值(如果需要删除遗留数据,请解开该行注释)
//        Integer deletedValue = ObjectUtil.isNotEmpty(paramMap.get("deletedValue")) ? paramMap.getInt("deletedValue") : null;
        Integer deletedValue = LOGIC_DELETE_VALUE; //如果需要删除遗留数据,请注释该行

        String message = "";

        if ("".equals(tableSchema) || "".equals(deletedField)) {
            message = "Job中未配置[tableSchema]参数";
            log.info(message);
            return message;
        }

        if (LOGIC_DELETE_VALUE == null) {
            message = "逻辑删除值未配置，请先配置[mybatis-plus.global-config.db-config.logic-delete-value]属性.";
            log.warn(message);
            return message;

//           如果需要删除遗留数据,请在Job参数中配置[deletedValue]的值,并解除相关注释.
//            if (deletedValue == null) {
//                message = "Job参数中未配置[deletedValue]的逻辑删除的值";
//                return message;
//            }
        } else {
            //查询带有逻辑删除字段[deletedField]的表
            List<LogicDeletedCleanJobDO> deletedList = clearDeletedMapper.selectDeletedList(tableSchema, deletedField);
            if (CollUtil.isEmpty(deletedList)) {
                message = "未查询到逻辑删除字段[deletedField]的表";
                log.info(message);
                return message;
            }
            /*
             * 如果需要删除遗留数据,请解开以下注释
            //获取当前表中逻辑删除字段的默认值,并去重
            List<String> deletedValueList = deletedList.stream().map(ClearDeletedDatasDO::getColumnDefault).toList();
            deletedValueList = deletedValueList.stream().distinct().toList();
            assert deletedValue != null;
            if (ListUtil.indexOfAll(deletedValueList, deletedValue::equals).length > 0) {
                message = "您所传入的[deletedValue]逻辑删除字段的值,属于当前数据库中逻辑删除字段[deletedField]的默认值,不能执行删除,请检查";
                return message;
            }
            */

            //删除逻辑删除的数据
            deletedList.forEach(table -> cleanByTableName(table.getTableName(), deletedField, deletedValue));
        }
        return null;
    }

    @Async
    public void cleanByTableName (String tableName, String deletedField, Integer deletedValue) {
        log.info("开始删除表[%s]的逻辑删除数据".formatted(tableName));
        clearDeletedMapper.deleteByTableNameAndDeletedValue(tableName, deletedField, deletedValue);
        log.info("已完成删除表[%s]的逻辑删除数据".formatted(tableName));
    }
}
