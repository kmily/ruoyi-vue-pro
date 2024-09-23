package cn.iocoder.yudao.module.promotion.service.point;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.promotion.controller.admin.point.vo.activity.PointActivityPageReqVO;
import cn.iocoder.yudao.module.promotion.controller.admin.point.vo.activity.PointActivitySaveReqVO;
import cn.iocoder.yudao.module.promotion.dal.dataobject.point.PointActivityDO;
import cn.iocoder.yudao.module.promotion.dal.dataobject.point.PointProductDO;
import jakarta.validation.Valid;

import java.util.Collection;
import java.util.List;

/**
 * 积分商城活动 Service 接口
 *
 * @author HUIHUI
 */
public interface PointActivityService {

    /**
     * 创建积分商城活动
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createPointActivity(@Valid PointActivitySaveReqVO createReqVO);

    /**
     * 更新积分商城活动
     *
     * @param updateReqVO 更新信息
     */
    void updatePointActivity(@Valid PointActivitySaveReqVO updateReqVO);

    /**
     * 关闭积分商城活动
     *
     * @param id 编号
     */
    void closePointActivity(Long id);

    /**
     * 删除积分商城活动
     *
     * @param id 编号
     */
    void deletePointActivity(Long id);

    /**
     * 获得积分商城活动
     *
     * @param id 编号
     * @return 积分商城活动
     */
    PointActivityDO getPointActivity(Long id);

    /**
     * 获得积分商城活动分页
     *
     * @param pageReqVO 分页查询
     * @return 积分商城活动分页
     */
    PageResult<PointActivityDO> getPointActivityPage(PointActivityPageReqVO pageReqVO);

    /**
     * 获得活动商品
     *
     * @param activityIds 活动编号
     * @return 获得活动商品
     */
    List<PointProductDO> getPointProductListByActivityIds(Collection<Long> activityIds);

}