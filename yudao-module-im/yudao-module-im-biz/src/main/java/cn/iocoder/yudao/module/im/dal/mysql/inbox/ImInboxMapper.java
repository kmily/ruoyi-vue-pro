package cn.iocoder.yudao.module.im.dal.mysql.inbox;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.im.controller.admin.inbox.vo.ImInboxPageReqVO;
import cn.iocoder.yudao.module.im.dal.dataobject.inbox.InboxDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * IM 收件箱 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface ImInboxMapper extends BaseMapperX<InboxDO> {

    default PageResult<InboxDO> selectPage(ImInboxPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<InboxDO>()
                .eqIfPresent(InboxDO::getUserId, reqVO.getUserId())
                .eqIfPresent(InboxDO::getMessageId, reqVO.getMessageId())
                .eqIfPresent(InboxDO::getSequence, reqVO.getSequence())
                .betweenIfPresent(InboxDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(InboxDO::getId));
    }

}