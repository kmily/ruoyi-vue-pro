package cn.iocoder.yudao.module.promotion.service.seckilltime;

import cn.iocoder.yudao.module.promotion.controller.admin.seckilltime.vo.SeckillTimeCreateReqVO;
import cn.iocoder.yudao.module.promotion.controller.admin.seckilltime.vo.SeckillTimeUpdateReqVO;
import cn.iocoder.yudao.module.promotion.dal.dataobject.seckilltime.SeckillTimeDO;

import javax.validation.Valid;
import java.util.List;

/**
 * 秒杀时段 Service 接口
 *
 * @author 芋道源码
 */
public interface SeckillTimeService {

    /**
     * 创建秒杀时段
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createSeckillTime(@Valid SeckillTimeCreateReqVO createReqVO);

    /**
     * 更新秒杀时段
     *
     * @param updateReqVO 更新信息
     */
    void updateSeckillTime(@Valid SeckillTimeUpdateReqVO updateReqVO);

    /**
     * 删除秒杀时段
     *
     * @param id 编号
     */
    void deleteSeckillTime(Long id);

    /**
     * 获得秒杀时段
     *
     * @param id 编号
     * @return 秒杀时段
     */
    SeckillTimeDO getSeckillTime(Long id);

    /**
     * 获得所有秒杀时段列表
     *
     * @return 所有秒杀时段列表
     */
    List<SeckillTimeDO> getSeckillTimeList();

    /**
     * 秒杀时段列表的秒杀活动数量加 1
     *
     * @param ids 秒杀时段id列表
     */
    void sekillActivityCountAdd(List<Long> ids);


    /**
     * 秒杀时段列表的秒杀活动数量减 1
     *
     * @param ids 秒杀时段id列表
     */
    void sekillActivityCountReduce(List<Long> ids);
}
