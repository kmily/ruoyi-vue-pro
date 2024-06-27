package cn.iocoder.yudao.module.member.dal.mysql.user;


import cn.hutool.core.lang.Assert;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.member.dal.dataobject.user.MemberUserDO;
import cn.iocoder.yudao.module.member.dal.dataobject.user.MemberUserExtDO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Author:lidongw_1
 * @Date 2024/5/23
 * @Description: 患者扩展表
 **/
@Mapper
public interface MemberUserExtMapper extends BaseMapperX<MemberUserExtDO> {

    default List<MemberUserExtDO> getList(List<Long> userIds){
        LambdaQueryWrapper<MemberUserExtDO> queryWrapper= Wrappers.lambdaQuery(MemberUserExtDO.class)
                .in(MemberUserExtDO::getUserId,userIds);
        return selectList(queryWrapper);
    }

    default void setTestGroup(Long userId,Integer groupId){
        Assert.isTrue(groupId > 0,"groupId不对");
        Assert.isTrue(userId > 0,"userId不对");
        LambdaUpdateWrapper<MemberUserExtDO> lambdaUpdateWrapper = new LambdaUpdateWrapper<MemberUserExtDO>()
                .setSql(" test_group = "+ groupId)
                .eq(MemberUserExtDO::getUserId, userId);
        update(null, lambdaUpdateWrapper);
    }

}
