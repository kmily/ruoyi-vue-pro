package cn.iocoder.yudao.module.member.dal.mysql.serverperson;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.member.dal.dataobject.serverperson.ServerPersonDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.member.controller.app.serverperson.vo.*;

/**
 * 被服务人 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface ServerPersonMapper extends BaseMapperX<ServerPersonDO>,BaseMapper<ServerPersonDO> {

    default PageResult<ServerPersonDO> selectPage(AppServerPersonPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<ServerPersonDO>()
                .likeIfPresent(ServerPersonDO::getName, reqVO.getName())
                .eqIfPresent(ServerPersonDO::getMobile, reqVO.getMobile())
                .likeIfPresent(ServerPersonDO::getRealname, reqVO.getRealname())
                .eqIfPresent(ServerPersonDO::getStatus, reqVO.getStatus())
                .betweenIfPresent(ServerPersonDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(ServerPersonDO::getId));
    }

}
