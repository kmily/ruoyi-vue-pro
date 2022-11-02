package cn.iocoder.yudao.module.promotion.service.coupon;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.promotion.controller.admin.coupon.vo.CouponTemplateCreateReqVO;
import cn.iocoder.yudao.module.promotion.controller.admin.coupon.vo.CouponTemplatePageReqVO;
import cn.iocoder.yudao.module.promotion.controller.admin.coupon.vo.CouponTemplateUpdateReqVO;
import cn.iocoder.yudao.module.promotion.dal.dataobject.coupon.CouponTemplateDO;

import javax.validation.Valid;

/**
 * 优惠劵模板 Service 接口
 *
 * @author 芋道源码
 */
public interface CouponTemplateService {

    /**
     * 创建优惠劵模板
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createCouponTemplate(@Valid CouponTemplateCreateReqVO createReqVO);

    /**
     * 更新优惠劵模板
     *
     * @param updateReqVO 更新信息
     */
    void updateCouponTemplate(@Valid CouponTemplateUpdateReqVO updateReqVO);

    /**
     * 删除优惠劵模板
     *
     * @param id 编号
     */
    void deleteCouponTemplate(Long id);

    /**
     * 获得优惠劵模板
     *
     * @param id 编号
     * @return 优惠劵模板
     */
    CouponTemplateDO getCouponTemplate(Long id);

    /**
     * 获得优惠劵模板分页
     *
     * @param pageReqVO 分页查询
     * @return 优惠劵模板分页
     */
    PageResult<CouponTemplateDO> getCouponTemplatePage(CouponTemplatePageReqVO pageReqVO);

}
