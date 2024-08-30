package cn.iocoder.yudao.module.promotion.api.reward;

import cn.iocoder.yudao.module.promotion.api.reward.dto.RewardActivityMatchRespDTO;
import cn.iocoder.yudao.module.promotion.convert.reward.RewardActivityConvert;
import cn.iocoder.yudao.module.promotion.dal.dataobject.reward.RewardActivityDO;
import cn.iocoder.yudao.module.promotion.service.reward.RewardActivityService;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

/**
 * 满减送活动 API 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class RewardActivityApiImpl implements RewardActivityApi {

    @Resource
    private RewardActivityService rewardActivityService;

    @Override
    public List<RewardActivityMatchRespDTO> getMatchRewardActivityList(Collection<Long> spuIds) {
        return rewardActivityService.getMatchRewardActivityList(spuIds);
    }

    @Override
    public List<RewardActivityMatchRespDTO> getRewardActivityBySpuIdsAndStatusAndDateTimeLt(Collection<Long> spuIds, Integer status, LocalDateTime dateTime) {
        List<RewardActivityDO> rewardActivityBySpuIdsAndStatusAndDateTimeLt = rewardActivityService.getRewardActivityBySpuIdsAndStatusAndDateTimeLt(spuIds, status, dateTime);
        return RewardActivityConvert.INSTANCE.convertList(rewardActivityBySpuIdsAndStatusAndDateTimeLt);
    }

}
