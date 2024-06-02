package cn.iocoder.yudao.module.im.dal.mysql.inbox;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.im.dal.dataobject.inbox.ImInboxDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * IM 收件箱 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface ImInboxMapper extends BaseMapperX<ImInboxDO> {

    default List<ImInboxDO> selectListByUserIdAndSequence(Long userId, Long sequence, Integer size) {
        return selectList(new LambdaQueryWrapperX<ImInboxDO>()
                .eq(ImInboxDO::getUserId, userId)
                .gt(sequence != null, ImInboxDO::getSequence, sequence)
                .orderByDesc(ImInboxDO::getSequence)
                .last("LIMIT " + size));
    }

}