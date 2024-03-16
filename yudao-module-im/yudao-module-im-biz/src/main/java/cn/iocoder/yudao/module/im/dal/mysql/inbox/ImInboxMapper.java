package cn.iocoder.yudao.module.im.dal.mysql.inbox;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.im.controller.admin.inbox.vo.ImInboxPageReqVO;
import cn.iocoder.yudao.module.im.dal.dataobject.inbox.ImInboxDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 收件箱 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface ImInboxMapper extends BaseMapperX<ImInboxDO> {

    default PageResult<ImInboxDO> selectPage(ImInboxPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<ImInboxDO>()
                .eqIfPresent(ImInboxDO::getUserId, reqVO.getUserId())
                .eqIfPresent(ImInboxDO::getMessageId, reqVO.getMessageId())
                .eqIfPresent(ImInboxDO::getSequence, reqVO.getSequence())
                .betweenIfPresent(ImInboxDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(ImInboxDO::getId));
    }

}