package cn.iocoder.yudao.module.hospital.dal.mysql.aptitude;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.hospital.dal.dataobject.aptitude.AptitudeDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.hospital.controller.admin.aptitude.vo.*;

/**
 * 资质信息 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface AptitudeMapper extends BaseMapperX<AptitudeDO>,BaseMapper<AptitudeDO> {

    default PageResult<AptitudeDO> selectPage(AptitudePageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<AptitudeDO>()
                .likeIfPresent(AptitudeDO::getName, reqVO.getName())
                .eqIfPresent(AptitudeDO::getPicUrl, reqVO.getPicUrl())
                .eqIfPresent(AptitudeDO::getStatus, reqVO.getStatus())
                .eqIfPresent(AptitudeDO::getRemark, reqVO.getRemark())
                .betweenIfPresent(AptitudeDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(AptitudeDO::getId));
    }

    default List<AptitudeDO> selectList(AptitudeExportReqVO reqVO) {
        return selectList(new LambdaQueryWrapperX<AptitudeDO>()
                .likeIfPresent(AptitudeDO::getName, reqVO.getName())
                .eqIfPresent(AptitudeDO::getPicUrl, reqVO.getPicUrl())
                .eqIfPresent(AptitudeDO::getStatus, reqVO.getStatus())
                .eqIfPresent(AptitudeDO::getRemark, reqVO.getRemark())
                .betweenIfPresent(AptitudeDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(AptitudeDO::getId));
    }

}
