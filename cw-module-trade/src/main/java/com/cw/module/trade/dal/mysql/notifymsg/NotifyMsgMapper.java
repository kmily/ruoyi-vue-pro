package com.cw.module.trade.dal.mysql.notifymsg;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import com.cw.module.trade.dal.dataobject.notifymsg.NotifyMsgDO;
import org.apache.ibatis.annotations.Mapper;
import com.cw.module.trade.controller.admin.notifymsg.vo.*;

/**
 * 账号通知记录 Mapper
 *
 * @author chengjiale
 */
@Mapper
public interface NotifyMsgMapper extends BaseMapperX<NotifyMsgDO> {

    default PageResult<NotifyMsgDO> selectPage(NotifyMsgPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<NotifyMsgDO>()
                .betweenIfPresent(NotifyMsgDO::getCreateTime, reqVO.getCreateTime())
                .betweenIfPresent(NotifyMsgDO::getAcceptTime, reqVO.getAcceptTime())
                .eqIfPresent(NotifyMsgDO::getAcceptInfo, reqVO.getAcceptInfo())
                .orderByDesc(NotifyMsgDO::getId));
    }

    default List<NotifyMsgDO> selectList(NotifyMsgExportReqVO reqVO) {
        return selectList(new LambdaQueryWrapperX<NotifyMsgDO>()
                .betweenIfPresent(NotifyMsgDO::getCreateTime, reqVO.getCreateTime())
                .betweenIfPresent(NotifyMsgDO::getAcceptTime, reqVO.getAcceptTime())
                .eqIfPresent(NotifyMsgDO::getAcceptInfo, reqVO.getAcceptInfo())
                .orderByDesc(NotifyMsgDO::getId));
    }

}
