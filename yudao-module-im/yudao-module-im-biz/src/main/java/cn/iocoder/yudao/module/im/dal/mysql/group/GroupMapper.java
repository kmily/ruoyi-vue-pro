package cn.iocoder.yudao.module.im.dal.mysql.group;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.im.dal.dataobject.group.ImGroupDO;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.im.controller.admin.group.vo.*;

// TODO @hao：这个也要有 Im 前缀
/**
 * 群 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface GroupMapper extends BaseMapperX<ImGroupDO> {

    default PageResult<ImGroupDO> selectPage(ImGroupPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<ImGroupDO>()
                .likeIfPresent(ImGroupDO::getGroupName, reqVO.getGroupName())
                .eqIfPresent(ImGroupDO::getOwnerId, reqVO.getOwnerId())
                .eqIfPresent(ImGroupDO::getHeadImage, reqVO.getHeadImage())
                .eqIfPresent(ImGroupDO::getHeadImageThumb, reqVO.getHeadImageThumb())
                .eqIfPresent(ImGroupDO::getNotice, reqVO.getNotice())
                .eqIfPresent(ImGroupDO::getRemark, reqVO.getRemark())
                .betweenIfPresent(ImGroupDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(ImGroupDO::getId));
    }

}