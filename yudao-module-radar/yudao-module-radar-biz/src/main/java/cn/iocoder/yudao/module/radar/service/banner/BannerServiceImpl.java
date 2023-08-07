package cn.iocoder.yudao.module.radar.service.banner;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;

import java.util.*;
import cn.iocoder.yudao.module.radar.controller.admin.banner.vo.*;
import cn.iocoder.yudao.module.radar.dal.dataobject.banner.BannerDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;

import cn.iocoder.yudao.module.radar.convert.banner.BannerConvert;
import cn.iocoder.yudao.module.radar.dal.mysql.banner.BannerMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.radar.enums.ErrorCodeConstants.*;

/**
 * 雷达模块banner图 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class BannerServiceImpl implements BannerService {

    @Resource
    private BannerMapper bannerMapper;

    @Override
    public Long createBanner(BannerCreateReqVO createReqVO) {
        // 插入
        BannerDO banner = BannerConvert.INSTANCE.convert(createReqVO);
        bannerMapper.insert(banner);
        // 返回
        return banner.getId();
    }

    @Override
    public void updateBanner(BannerUpdateReqVO updateReqVO) {
        // 校验存在
        validateBannerExists(updateReqVO.getId());
        // 更新
        BannerDO updateObj = BannerConvert.INSTANCE.convert(updateReqVO);
        bannerMapper.updateById(updateObj);
    }

    @Override
    public void deleteBanner(Long id) {
        // 校验存在
        validateBannerExists(id);
        // 删除
        bannerMapper.deleteById(id);
    }

    private void validateBannerExists(Long id) {
        if (bannerMapper.selectById(id) == null) {
            throw exception(BANNER_NOT_EXISTS);
        }
    }

    @Override
    public BannerDO getBanner(Long id) {
        return bannerMapper.selectById(id);
    }

    @Override
    public List<BannerDO> getBannerList(Collection<Long> ids) {
        return bannerMapper.selectBatchIds(ids);
    }

    @Override
    public PageResult<BannerDO> getBannerPage(BannerPageReqVO pageReqVO) {
        return bannerMapper.selectPage(pageReqVO);
    }

    @Override
    public List<BannerDO> getBannerList(BannerExportReqVO exportReqVO) {
        return bannerMapper.selectList(exportReqVO);
    }

    @Override
    public List<BannerDO> getBannerList() {
        return bannerMapper.selectList();
    }

}
