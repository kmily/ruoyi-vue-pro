package cn.iocoder.yudao.module.promotion.service.reward;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.number.MoneyUtils;
import cn.iocoder.yudao.module.promotion.api.reward.dto.RewardActivityMatchRespDTO;
import cn.iocoder.yudao.module.promotion.controller.admin.reward.vo.RewardActivityCreateReqVO;
import cn.iocoder.yudao.module.promotion.controller.admin.reward.vo.RewardActivityPageReqVO;
import cn.iocoder.yudao.module.promotion.controller.admin.reward.vo.RewardActivityUpdateReqVO;
import cn.iocoder.yudao.module.promotion.dal.dataobject.reward.RewardActivityDO;
import cn.iocoder.yudao.module.promotion.enums.common.PromotionConditionTypeEnum;
import cn.iocoder.yudao.module.promotion.service.coupon.CouponService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.getSumValue;

/**
 * 满减送活动 Service 接口
 *
 * @author 芋道源码
 */
public interface RewardActivityService {

    /**
     * 创建满减送活动
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createRewardActivity(@Valid RewardActivityCreateReqVO createReqVO);

    /**
     * 更新满减送活动
     *
     * @param updateReqVO 更新信息
     */
    void updateRewardActivity(@Valid RewardActivityUpdateReqVO updateReqVO);

    /**
     * 关闭满减送活动
     *
     * @param id 活动编号
     */
    void closeRewardActivity(Long id);

    /**
     * 删除满减送活动
     *
     * @param id 编号
     */
    void deleteRewardActivity(Long id);

    /**
     * 获得满减送活动
     *
     * @param id 编号
     * @return 满减送活动
     */
    RewardActivityDO getRewardActivity(Long id);

    /**
     * 获得满减送活动分页
     *
     * @param pageReqVO 分页查询
     * @return 满减送活动分页
     */
    PageResult<RewardActivityDO> getRewardActivityPage(RewardActivityPageReqVO pageReqVO);

    /**
     * 获得 spuId 商品匹配的的满减送活动列表
     *
     * @param spuIds   SPU 编号数组
     * @return 满减送活动列表
     */
    List<RewardActivityMatchRespDTO> getMatchRewardActivityListBySpuIds(Collection<Long> spuIds);

    /**
     * 获得 spuId 商品匹配的的满减送活动列表
     *
     * @param conditionType   条件类型
     * @param rule   优惠规则
     * @return key 类型 RewardTypeEnum
     *       value 优惠描述
     */
    Map<Integer, String> getRewardActivityRuleDescription(Integer conditionType, RewardActivityDO.Rule rule);

}
