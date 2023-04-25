package cn.iocoder.yudao.module.system.dal.mysql.signreq;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.system.dal.dataobject.signreq.SignReqDO;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.system.controller.admin.signreq.vo.*;

/**
 * 注册申请 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface SignReqMapper extends BaseMapperX<SignReqDO> {

    default PageResult<SignReqDO> selectPage(SignReqPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<SignReqDO>()
                .eqIfPresent(SignReqDO::getOpenId, reqVO.getOpenId())
                .eqIfPresent(SignReqDO::getEmail, reqVO.getEmail())
                .eqIfPresent(SignReqDO::getPhonenumber, reqVO.getPhonenumber())
                .likeIfPresent(SignReqDO::getUserName, reqVO.getUserName())
                .orderByDesc(SignReqDO::getId));
    }

    default List<SignReqDO> selectList(SignReqExportReqVO reqVO) {
        return selectList(new LambdaQueryWrapperX<SignReqDO>()
                .eqIfPresent(SignReqDO::getOpenId, reqVO.getOpenId())
                .eqIfPresent(SignReqDO::getEmail, reqVO.getEmail())
                .eqIfPresent(SignReqDO::getPhonenumber, reqVO.getPhonenumber())
                .likeIfPresent(SignReqDO::getUserName, reqVO.getUserName())
                .orderByDesc(SignReqDO::getId));
    }

}
