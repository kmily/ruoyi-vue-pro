package cn.iocoder.yudao.module.promotion.service.banner;

import java.util.*;
import javax.validation.*;
import cn.iocoder.yudao.module.promotion.controller.admin.banner.vo.*;
import cn.iocoder.yudao.module.promotion.dal.dataobject.banner.BannerDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import com.baomidou.mybatisplus.extension.service.IService;
/**
 * Banner Service 接口
 *
 * @author 芋道源码
 */
public interface BannerService extends IService<BannerDO>{

    /**
     * 创建Banner
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createBanner(@Valid BannerCreateReqVO createReqVO);

    /**
     * 更新Banner
     *
     * @param updateReqVO 更新信息
     */
    void updateBanner(@Valid BannerUpdateReqVO updateReqVO);

    /**
     * 删除Banner
     *
     * @param id 编号
     */
    void deleteBanner(Long id);

    /**
     * 获得Banner
     *
     * @param id 编号
     * @return Banner
     */
    BannerDO getBanner(Long id);

    /**
     * 获得Banner列表
     *
     * @param ids 编号
     * @return Banner列表
     */
    List<BannerDO> getBannerList(Collection<Long> ids);

    /**
     * 获得Banner分页
     *
     * @param pageReqVO 分页查询
     * @return Banner分页
     */
    PageResult<BannerDO> getBannerPage(BannerPageReqVO pageReqVO);

    /**
     * 获得Banner列表, 用于 Excel 导出
     *
     * @param exportReqVO 查询条件
     * @return Banner列表
     */
    List<BannerDO> getBannerList(BannerExportReqVO exportReqVO);

}
