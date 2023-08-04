package cn.iocoder.yudao.module.member.dal.mysql.family;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.member.dal.dataobject.family.FamilyDO;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.member.controller.app.family.vo.*;

/**
 * 用户家庭 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface FamilyMapper extends BaseMapperX<FamilyDO> {

    default PageResult<FamilyDO> selectPage(FamilyPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<FamilyDO>()
                .eqIfPresent(FamilyDO::getUserId, reqVO.getUserId())
                .likeIfPresent(FamilyDO::getName, reqVO.getName())
                .betweenIfPresent(FamilyDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(FamilyDO::getId));
    }

    default List<FamilyDO> selectList(FamilyExportReqVO reqVO) {
        return selectList(new LambdaQueryWrapperX<FamilyDO>()
                .eqIfPresent(FamilyDO::getUserId, reqVO.getUserId())
                .likeIfPresent(FamilyDO::getName, reqVO.getName())
                .betweenIfPresent(FamilyDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(FamilyDO::getId));
    }

}
