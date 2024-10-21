package cn.iocoder.yudao.module.system.dal.dataobject.user;

import lombok.Data;

@Data
public class LogicDeletedCleanJobDO {
    private String tableName;
    private String columnDefault;
}
