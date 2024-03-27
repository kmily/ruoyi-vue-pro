package cn.iocoder.yudao.module.im.dal.mysql.group;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.im.dal.dataobject.group.GroupDO;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.im.controller.admin.group.vo.*;

/**
 * 群 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface ImGroupMapper extends BaseMapperX<GroupDO> {

    default PageResult<GroupDO> selectPage(ImGroupPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<GroupDO>()
                .likeIfPresent(GroupDO::getGroupName, reqVO.getGroupName())
                .eqIfPresent(GroupDO::getOwnerId, reqVO.getOwnerId())
                .eqIfPresent(GroupDO::getHeadImage, reqVO.getHeadImage())
                .eqIfPresent(GroupDO::getHeadImageThumb, reqVO.getHeadImageThumb())
                .eqIfPresent(GroupDO::getNotice, reqVO.getNotice())
                .eqIfPresent(GroupDO::getRemark, reqVO.getRemark())
                .betweenIfPresent(GroupDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(GroupDO::getId));
    }

}