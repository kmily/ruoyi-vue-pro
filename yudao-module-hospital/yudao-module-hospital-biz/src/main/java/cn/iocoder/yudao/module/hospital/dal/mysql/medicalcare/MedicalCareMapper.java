package cn.iocoder.yudao.module.hospital.dal.mysql.medicalcare;

import java.util.*;

import cn.hutool.core.util.StrUtil;
import cn.iocoder.yudao.framework.common.enums.CommonStatusEnum;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.hospital.controller.app.medicalcare.vo.AppMedicalCarePageReqVO;
import cn.iocoder.yudao.module.hospital.dal.dataobject.careaptitude.CareAptitudeDO;
import cn.iocoder.yudao.module.hospital.dal.dataobject.medicalcare.MedicalCareDO;
import cn.iocoder.yudao.module.hospital.enums.medicalcare.MedicalCareStatusEnum;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.yulichang.base.MPJBaseMapper;
import com.github.yulichang.interfaces.MPJBaseJoin;
import com.github.yulichang.query.MPJLambdaQueryWrapper;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.hospital.controller.admin.medicalcare.vo.*;

/**
 * 医护管理 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface MedicalCareMapper extends BaseMapperX<MedicalCareDO>,BaseMapper<MedicalCareDO>, MPJBaseMapper<MedicalCareDO> {

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

    default PageResult<MedicalCareDO> selectPage(AppMedicalCarePageReqVO pageVO){
        return selectPage(pageVO, new LambdaQueryWrapperX<MedicalCareDO>()
                .eq(MedicalCareDO::getStatus, MedicalCareStatusEnum.OPEN.value())
                .eq(MedicalCareDO::getRealname, CommonStatusEnum.YES.name())
                .eq(MedicalCareDO::getAptitude, CommonStatusEnum.YES.name())
                .likeIfPresent(MedicalCareDO::getName, pageVO.getName()));
    }

    default PageResult<MedicalCareDO> selectPageByAptitude(AppMedicalCarePageReqVO pageVO){

        IPage<MedicalCareDO> page = new Page<>(pageVO.getPageNo(), pageVO.getPageSize());
        IPage<MedicalCareDO> careDOIPage = selectJoinPage(page, MedicalCareDO.class, new MPJLambdaWrapper<MedicalCareDO>()
                .selectAll(MedicalCareDO.class)
                .leftJoin(CareAptitudeDO.class, CareAptitudeDO::getCareId, MedicalCareDO::getId)
                .eq(MedicalCareDO::getStatus, MedicalCareStatusEnum.OPEN.value())
                .eq(MedicalCareDO::getRealname, CommonStatusEnum.YES.name())
                .eq(MedicalCareDO::getAptitude, CommonStatusEnum.YES.name())
                .in(CareAptitudeDO::getAptitudeId, pageVO.getAptitudes())
                .like(StrUtil.isNotBlank(pageVO.getName()), MedicalCareDO::getName, pageVO.getName())
                .distinct());
        return new PageResult<MedicalCareDO>()
                .setList(careDOIPage.getRecords())
                .setTotal(careDOIPage.getTotal());


    }
}
