package cn.iocoder.yudao.module.member.dal.mysql.bootup;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.member.dal.dataobject.bootup.BootUpDO;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.member.controller.admin.bootup.vo.*;

/**
 * 用户启动数据 Mapper
 *
 * @author 和尘同光
 */
@Mapper
public interface BootUpMapper extends BaseMapperX<BootUpDO> {

    default PageResult<BootUpDO> selectPage(BootUpPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<BootUpDO>()
                .eqIfPresent(BootUpDO::getUserId, reqVO.getUserId())
                .betweenIfPresent(BootUpDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(BootUpDO::getId));
    }

    default List<BootUpDO> selectList(BootUpExportReqVO reqVO) {
        return selectList(new LambdaQueryWrapperX<BootUpDO>()
                .eqIfPresent(BootUpDO::getUserId, reqVO.getUserId())
                .betweenIfPresent(BootUpDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(BootUpDO::getId));
    }

}
