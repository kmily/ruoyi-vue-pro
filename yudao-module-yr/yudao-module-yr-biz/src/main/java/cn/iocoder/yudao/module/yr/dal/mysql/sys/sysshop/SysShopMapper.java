package cn.iocoder.yudao.module.yr.dal.mysql.sys.sysshop;

import java.util.*;


import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.yr.controller.admin.sys.sysshop.vo.SysShopExportReqVO;
import cn.iocoder.yudao.module.yr.controller.admin.sys.sysshop.vo.SysShopPageReqVO;
import cn.iocoder.yudao.module.yr.dal.dataobject.sys.sysshop.SysShopDO;
import org.apache.ibatis.annotations.Mapper;


/**
 * 店面 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface SysShopMapper extends BaseMapperX<SysShopDO> {

    default PageResult<SysShopDO> selectPage(SysShopPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<SysShopDO>()
                .likeIfPresent(SysShopDO::getShopName, reqVO.getShopName())
                .eqIfPresent(SysShopDO::getShopCity, reqVO.getShopCity())
                .eqIfPresent(SysShopDO::getShopAddress, reqVO.getShopAddress())
                .eqIfPresent(SysShopDO::getShopAddressNum, reqVO.getShopAddressNum())
                .eqIfPresent(SysShopDO::getShopGroup, reqVO.getShopGroup())
                .eqIfPresent(SysShopDO::getStatus, reqVO.getStatus())
                .betweenIfPresent(SysShopDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(SysShopDO::getId));
    }

    default List<SysShopDO> selectList(SysShopExportReqVO reqVO) {
        return selectList(new LambdaQueryWrapperX<SysShopDO>()
                .likeIfPresent(SysShopDO::getShopName, reqVO.getShopName())
                .eqIfPresent(SysShopDO::getShopCity, reqVO.getShopCity())
                .eqIfPresent(SysShopDO::getShopAddress, reqVO.getShopAddress())
                .eqIfPresent(SysShopDO::getShopAddressNum, reqVO.getShopAddressNum())
                .eqIfPresent(SysShopDO::getShopGroup, reqVO.getShopGroup())
                .eqIfPresent(SysShopDO::getStatus, reqVO.getStatus())
                .betweenIfPresent(SysShopDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(SysShopDO::getId));
    }

}
