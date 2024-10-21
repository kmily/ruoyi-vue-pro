package cn.iocoder.yudao.module.system.dal.mysql.user;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.system.dal.dataobject.user.LogicDeletedCleanJobDO;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface LogicDeletedCleanJobMapper extends BaseMapperX<LogicDeletedCleanJobDO> {

    //TODO @Yu 这里需要换成更为优雅的实现,当前只是先实现功能.
    @Select("SELECT TABLE_NAME,COLUMN_DEFAULT FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = #{tableSchema} and COLUMN_NAME= #{deletedField}")
    List<LogicDeletedCleanJobDO> selectDeletedList(String tableSchema, String deletedField);

    @Delete("<script>DELETE FROM ${tableName} WHERE ${deletedField} = #{deletedValue}</script>")
    void deleteByTableNameAndDeletedValue(@Param("tableName") String tableName,
                                          @Param("deletedField") String deletedField,
                                          @Param("deletedValue") Integer deletedValue);

}
