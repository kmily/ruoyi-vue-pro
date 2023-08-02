package cn.iocoder.yudao.module.promotion.service.bargainactivity;

import java.util.*;
import javax.validation.*;
import cn.iocoder.yudao.module.promotion.controller.admin.bargainactivity.vo.*;
import cn.iocoder.yudao.module.promotion.dal.dataobject.bargainactivity.BargainActivityDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;

/**
 * 砍价 Service 接口
 *
 * @author WangBosheng
 */
public interface BargainActivityService {

    /**
     * 创建砍价
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createBargainActivity(@Valid BargainActivityCreateReqVO createReqVO);

    /**
     * 更新砍价
     *
     * @param updateReqVO 更新信息
     */
    void updateBargainActivity(@Valid BargainActivityUpdateReqVO updateReqVO);

    /**
     * 删除砍价
     *
     * @param id 编号
     */
    void deleteBargainActivity(Long id);

    /**
     * 获得砍价
     *
     * @param id 编号
     * @return 砍价
     */
    BargainActivityDO getBargainActivity(Long id);

    /**
     * 获得砍价列表
     *
     * @param ids 编号
     * @return 砍价列表
     */
    List<BargainActivityDO> getBargainActivityList(Collection<Long> ids);

    /**
     * 获得砍价分页
     *
     * @param pageReqVO 分页查询
     * @return 砍价分页
     */
    PageResult<BargainActivityDO> getBargainActivityPage(BargainActivityPageReqVO pageReqVO);

    /**
     * 获得砍价列表, 用于 Excel 导出
     *
     * @param exportReqVO 查询条件
     * @return 砍价列表
     */
    List<BargainActivityDO> getBargainActivityList(BargainActivityExportReqVO exportReqVO);

}
