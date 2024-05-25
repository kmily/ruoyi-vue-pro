package cn.iocoder.yudao.module.therapy.dal.mysql.definition;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.therapy.dal.dataobject.user.MemberUserExtDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Author:lidongw_1
 * @Date 2024/5/23
 * @Description: 患者扩展表
 **/
@Mapper
public interface MemberUserExtMapper extends BaseMapperX<MemberUserExtDO> {

}
