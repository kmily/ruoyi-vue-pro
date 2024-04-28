package cn.iocoder.yudao.module.im.dal.mysql.inbox;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.im.dal.dataobject.inbox.ImInboxDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

// TODO @hao：IM 前缀
/**
 * IM 收件箱 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface InboxMapper extends BaseMapperX<ImInboxDO> {

    default List<ImInboxDO> selectListByUserIdAndSequence(Long userId, Long sequence, Integer size) {
        return selectList(new LambdaQueryWrapperX<ImInboxDO>()
                .gt(ImInboxDO::getUserId, userId)
                .gt(ImInboxDO::getSequence, sequence)
                .orderByAsc(ImInboxDO::getSequence)
                .last("LIMIT " + size));
    }

}