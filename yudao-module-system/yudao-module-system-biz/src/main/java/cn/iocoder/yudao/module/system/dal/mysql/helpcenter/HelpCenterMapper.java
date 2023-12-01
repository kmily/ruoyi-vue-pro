package cn.iocoder.yudao.module.system.dal.mysql.helpcenter;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.system.controller.admin.helpcenter.vo.HelpCenterExportReqVO;
import cn.iocoder.yudao.module.system.controller.admin.helpcenter.vo.HelpCenterPageReqVO;
import cn.iocoder.yudao.module.system.dal.dataobject.helpcenter.HelpCenterDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 常见问题 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface HelpCenterMapper extends BaseMapperX<HelpCenterDO>,BaseMapper<HelpCenterDO> {

    default PageResult<HelpCenterDO> selectPage(HelpCenterPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<HelpCenterDO>()
                .eqIfPresent(HelpCenterDO::getType, reqVO.getType())
                .eqIfPresent(HelpCenterDO::getTitle, reqVO.getTitle())
                .eqIfPresent(HelpCenterDO::getDescription, reqVO.getDescription())
                .eqIfPresent(HelpCenterDO::getStatus, reqVO.getStatus())
                .betweenIfPresent(HelpCenterDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(HelpCenterDO::getId));
    }

    default List<HelpCenterDO> selectList(HelpCenterExportReqVO reqVO) {
        return selectList(new LambdaQueryWrapperX<HelpCenterDO>()
                .eqIfPresent(HelpCenterDO::getType, reqVO.getType())
                .eqIfPresent(HelpCenterDO::getTitle, reqVO.getTitle())
                .eqIfPresent(HelpCenterDO::getDescription, reqVO.getDescription())
                .eqIfPresent(HelpCenterDO::getStatus, reqVO.getStatus())
                .betweenIfPresent(HelpCenterDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(HelpCenterDO::getId));
    }

}
