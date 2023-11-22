package cn.iocoder.yudao.module.hospital.service.medicalcare;

import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;

import java.util.*;
import cn.iocoder.yudao.module.hospital.controller.app.medicalcare.vo.*;
import cn.iocoder.yudao.module.hospital.dal.dataobject.medicalcare.CareFavoriteDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;

import cn.iocoder.yudao.module.hospital.convert.medicalcare.CareFavoriteConvert;
import cn.iocoder.yudao.module.hospital.dal.mysql.medicalcare.CareFavoriteMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.hospital.enums.ErrorCodeConstants.*;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;

/**
 * 医护收藏 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class CareFavoriteServiceImpl extends ServiceImpl<CareFavoriteMapper, CareFavoriteDO> implements CareFavoriteService {

    @Resource
    private CareFavoriteMapper careFavoriteMapper;


    @Override
    public PageResult<CareFavoriteDO> getCareFavoritePage(AppCareFavoritePageReqVO pageReqVO) {
        return careFavoriteMapper.selectPage(pageReqVO);
    }

    @Override
    public Long createCareFavorite(Long careId, Long memberId) {
        CareFavoriteDO careFavoriteDO = new CareFavoriteDO().setCareId(careId).setMemberId(memberId);
        careFavoriteMapper.insert(careFavoriteDO);
        return careFavoriteDO.getId();
    }

    @Override
    public void deleteCareFavorite(Long careId, Long memberId) {
        careFavoriteMapper.delete(new LambdaQueryWrapper<CareFavoriteDO>().eq(CareFavoriteDO::getCareId,careId)
                .eq(CareFavoriteDO::getMemberId, memberId));
    }

    @Override
    public Long getCareFavorite(Long careId, Long memberId) {
       return careFavoriteMapper.selectCount(new LambdaQueryWrapperX<CareFavoriteDO>()
               .eq(CareFavoriteDO::getCareId,careId)
               .eqIfPresent(CareFavoriteDO::getMemberId, memberId));
    }

}
