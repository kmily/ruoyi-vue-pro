package cn.iocoder.yudao.module.member.dal.mysql.serverperson;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.member.dal.dataobject.serverperson.ServerPersonCheckLogDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.member.controller.admin.serverperson.vo.*;

/**
 * 被户人审核记录 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface ServerPersonCheckLogMapper extends BaseMapperX<ServerPersonCheckLogDO>,BaseMapper<ServerPersonCheckLogDO> {

    default PageResult<ServerPersonCheckLogDO> selectPage(ServerPersonCheckLogPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<ServerPersonCheckLogDO>()
                .eqIfPresent(ServerPersonCheckLogDO::getPersonId, reqVO.getPersonId())
                .betweenIfPresent(ServerPersonCheckLogDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(ServerPersonCheckLogDO::getId));
    }

}
