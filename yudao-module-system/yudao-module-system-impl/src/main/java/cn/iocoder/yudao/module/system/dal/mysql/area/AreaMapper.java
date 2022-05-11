package cn.iocoder.yudao.module.system.dal.mysql.area;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.system.dal.dataobject.area.AreaDO;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.system.controller.admin.area.vo.*;

/**
 * 行政区域 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface AreaMapper extends BaseMapperX<AreaDO> {

    default PageResult<AreaDO> selectPage(AreaPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<AreaDO>()
                .eqIfPresent(AreaDO::getDataVersion, reqVO.getDataVersion())
                .eqIfPresent(AreaDO::getProvinceCode, reqVO.getProvinceCode())
                .eqIfPresent(AreaDO::getProvince, reqVO.getProvince())
                .eqIfPresent(AreaDO::getCityCode, reqVO.getCityCode())
                .eqIfPresent(AreaDO::getCity, reqVO.getCity())
                .eqIfPresent(AreaDO::getCountyCode, reqVO.getCountyCode())
                .eqIfPresent(AreaDO::getCounty, reqVO.getCounty())
                .eqIfPresent(AreaDO::getTownCode, reqVO.getTownCode())
                .eqIfPresent(AreaDO::getTown, reqVO.getTown())
                .eqIfPresent(AreaDO::getVillageCode, reqVO.getVillageCode())
                .eqIfPresent(AreaDO::getVillage, reqVO.getVillage())
                .eqIfPresent(AreaDO::getLongitude, reqVO.getLongitude())
                .eqIfPresent(AreaDO::getLatitude, reqVO.getLatitude())
                .betweenIfPresent(AreaDO::getCreateTime, reqVO.getBeginCreateTime(), reqVO.getEndCreateTime())
                .eqIfPresent(AreaDO::getUseFlag, reqVO.getUseFlag())
                .orderByAsc(AreaDO::getId));
    }

    default List<AreaDO> selectList(AreaExportReqVO reqVO) {
        return selectList(new LambdaQueryWrapperX<AreaDO>()
                .eqIfPresent(AreaDO::getDataVersion, reqVO.getDataVersion())
                .eqIfPresent(AreaDO::getProvinceCode, reqVO.getProvinceCode())
                .eqIfPresent(AreaDO::getProvince, reqVO.getProvince())
                .eqIfPresent(AreaDO::getCityCode, reqVO.getCityCode())
                .eqIfPresent(AreaDO::getCity, reqVO.getCity())
                .eqIfPresent(AreaDO::getCountyCode, reqVO.getCountyCode())
                .eqIfPresent(AreaDO::getCounty, reqVO.getCounty())
                .eqIfPresent(AreaDO::getTownCode, reqVO.getTownCode())
                .eqIfPresent(AreaDO::getTown, reqVO.getTown())
                .eqIfPresent(AreaDO::getVillageCode, reqVO.getVillageCode())
                .eqIfPresent(AreaDO::getVillage, reqVO.getVillage())
                .eqIfPresent(AreaDO::getLongitude, reqVO.getLongitude())
                .eqIfPresent(AreaDO::getLatitude, reqVO.getLatitude())
                .betweenIfPresent(AreaDO::getCreateTime, reqVO.getBeginCreateTime(), reqVO.getEndCreateTime())
                .eqIfPresent(AreaDO::getUseFlag, reqVO.getUseFlag())
                .orderByDesc(AreaDO::getId));
    }

}
