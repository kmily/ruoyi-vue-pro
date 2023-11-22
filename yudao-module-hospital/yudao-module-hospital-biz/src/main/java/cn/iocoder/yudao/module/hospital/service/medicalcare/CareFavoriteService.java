package cn.iocoder.yudao.module.hospital.service.medicalcare;

import java.util.*;
import javax.validation.*;
import cn.iocoder.yudao.module.hospital.controller.app.medicalcare.vo.*;
import cn.iocoder.yudao.module.hospital.dal.dataobject.medicalcare.CareFavoriteDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import com.baomidou.mybatisplus.extension.service.IService;
/**
 * 医护收藏 Service 接口
 *
 * @author 芋道源码
 */
public interface CareFavoriteService extends IService<CareFavoriteDO>{


    /**
     * 获得医护收藏分页
     *
     * @param pageReqVO 分页查询
     * @return 医护收藏分页
     */
    PageResult<CareFavoriteDO> getCareFavoritePage(AppCareFavoritePageReqVO pageReqVO);


    /**
     * 创建收藏
     * @param careId 医护ID
     * @param memberId 会员编号
     * @return
     */
    Long createCareFavorite(Long careId, Long memberId);

    /**
     * 取消收藏
     * @param careId 医护编号
     * @param memberId 会员编号
     */
    void deleteCareFavorite(Long careId, Long memberId);

    /**
     * 判断 会员是否收藏了此医护
     * @param careId 医护编号
     * @param memberId 会员编号
     */
    Long getCareFavorite(Long careId, Long memberId);
}
