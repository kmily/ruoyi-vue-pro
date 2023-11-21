package cn.iocoder.yudao.module.hospital.dal.mysql.medicalcare;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.hospital.dal.dataobject.medicalcare.MedicalCareDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.hospital.controller.admin.medicalcare.vo.*;

/**
 * 医护管理 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface MedicalCareMapper extends BaseMapperX<MedicalCareDO>,BaseMapper<MedicalCareDO> {

    default PageResult<MedicalCareDO> selectPage(MedicalCarePageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<MedicalCareDO>()
                .eqIfPresent(MedicalCareDO::getOrgId, reqVO.getOrgId())
                .likeIfPresent(MedicalCareDO::getName, reqVO.getName())
                .eqIfPresent(MedicalCareDO::getMobile, reqVO.getMobile())
                .eqIfPresent(MedicalCareDO::getSex, reqVO.getSex())
                .likeIfPresent(MedicalCareDO::getRealname, reqVO.getRealname())
                .eqIfPresent(MedicalCareDO::getTitle, reqVO.getTitle())
                .eqIfPresent(MedicalCareDO::getOrganization, reqVO.getOrganization())
                .eqIfPresent(MedicalCareDO::getStatus, reqVO.getStatus())
                .inIfPresent(MedicalCareDO::getStatus, reqVO.status())
                .betweenIfPresent(MedicalCareDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(MedicalCareDO::getId));
    }

    default List<MedicalCareDO> selectList(MedicalCareExportReqVO reqVO) {
        return selectList(new LambdaQueryWrapperX<MedicalCareDO>()
                .eqIfPresent(MedicalCareDO::getOrgId, reqVO.getOrgId())
                .likeIfPresent(MedicalCareDO::getName, reqVO.getName())
                .eqIfPresent(MedicalCareDO::getMobile, reqVO.getMobile())
                .likeIfPresent(MedicalCareDO::getRealname, reqVO.getRealname())
                .eqIfPresent(MedicalCareDO::getTitle, reqVO.getTitle())
                .eqIfPresent(MedicalCareDO::getStatus, reqVO.getStatus())
                .betweenIfPresent(MedicalCareDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(MedicalCareDO::getId));
    }

}
