package cn.iocoder.yudao.module.radar.service.banner;

import java.util.*;
import javax.validation.*;
import cn.iocoder.yudao.module.radar.controller.admin.banner.vo.*;
import cn.iocoder.yudao.module.radar.dal.dataobject.banner.BannerDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;

/**
 * 雷达模块banner图 Service 接口
 *
 * @author 芋道源码
 */
public interface BannerService {

    /**
     * 创建雷达模块banner图
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createBanner(@Valid BannerCreateReqVO createReqVO);

    /**
     * 更新雷达模块banner图
     *
     * @param updateReqVO 更新信息
     */
    void updateBanner(@Valid BannerUpdateReqVO updateReqVO);

    /**
     * 删除雷达模块banner图
     *
     * @param id 编号
     */
    void deleteBanner(Long id);

    /**
     * 获得雷达模块banner图
     *
     * @param id 编号
     * @return 雷达模块banner图
     */
    BannerDO getBanner(Long id);

    /**
     * 获得雷达模块banner图列表
     *
     * @param ids 编号
     * @return 雷达模块banner图列表
     */
    List<BannerDO> getBannerList(Collection<Long> ids);

    /**
     * 获得雷达模块banner图分页
     *
     * @param pageReqVO 分页查询
     * @return 雷达模块banner图分页
     */
    PageResult<BannerDO> getBannerPage(BannerPageReqVO pageReqVO);

    /**
     * 获得雷达模块banner图列表, 用于 Excel 导出
     *
     * @param exportReqVO 查询条件
     * @return 雷达模块banner图列表
     */
    List<BannerDO> getBannerList(BannerExportReqVO exportReqVO);

    /**
     * 查询所有
     * @return
     */
    List<BannerDO> getBannerList();
}
