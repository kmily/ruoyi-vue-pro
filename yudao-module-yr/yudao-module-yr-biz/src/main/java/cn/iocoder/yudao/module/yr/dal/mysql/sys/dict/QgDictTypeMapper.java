package cn.iocoder.yudao.module.yr.dal.mysql.sys.dict;


import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.yr.controller.admin.sys.dict.vo.type.QgDictTypeExportReqVO;
import cn.iocoder.yudao.module.yr.controller.admin.sys.dict.vo.type.QgDictTypePageReqVO;
import cn.iocoder.yudao.module.yr.dal.dataobject.sys.dict.QgDictTypeDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface QgDictTypeMapper extends BaseMapperX<QgDictTypeDO> {

    default PageResult<QgDictTypeDO> selectPage(QgDictTypePageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<QgDictTypeDO>()
                .likeIfPresent(QgDictTypeDO::getName, reqVO.getName())
                .likeIfPresent(QgDictTypeDO::getType, reqVO.getType())
                .eqIfPresent(QgDictTypeDO::getStatus, reqVO.getStatus())
                .betweenIfPresent(QgDictTypeDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(QgDictTypeDO::getId));
    }

    default List<QgDictTypeDO> selectList(QgDictTypeExportReqVO reqVO) {
        return selectList(new LambdaQueryWrapperX<QgDictTypeDO>()
                .likeIfPresent(QgDictTypeDO::getName, reqVO.getName())
                .likeIfPresent(QgDictTypeDO::getType, reqVO.getType())
                .eqIfPresent(QgDictTypeDO::getStatus, reqVO.getStatus())
                .betweenIfPresent(QgDictTypeDO::getCreateTime, reqVO.getCreateTime()));
    }

    default QgDictTypeDO selectByType(String type) {
        return selectOne(QgDictTypeDO::getType, type);
    }

    default QgDictTypeDO selectByName(String name) {
        return selectOne(QgDictTypeDO::getName, name);
    }

}
