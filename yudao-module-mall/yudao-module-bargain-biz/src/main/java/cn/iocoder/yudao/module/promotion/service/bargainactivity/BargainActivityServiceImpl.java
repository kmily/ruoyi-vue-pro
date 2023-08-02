package cn.iocoder.yudao.module.promotion.service.bargainactivity;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.promotion.controller.admin.bargainactivity.vo.BargainActivityCreateReqVO;
import cn.iocoder.yudao.module.promotion.controller.admin.bargainactivity.vo.BargainActivityExportReqVO;
import cn.iocoder.yudao.module.promotion.controller.admin.bargainactivity.vo.BargainActivityPageReqVO;
import cn.iocoder.yudao.module.promotion.controller.admin.bargainactivity.vo.BargainActivityUpdateReqVO;
import cn.iocoder.yudao.module.promotion.convert.bargainactivity.BargainActivityConvert;
import cn.iocoder.yudao.module.promotion.dal.dataobject.bargainactivity.BargainActivityDO;
import cn.iocoder.yudao.module.promotion.dal.mysql.bargainactivity.BargainActivityMapper;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.promotion.enums.ErrorCodeConstants.*;

/**
 * 砍价 Service 实现类
 *
 * @author WangBosheng
 */
@Service
@Validated
public class BargainActivityServiceImpl implements BargainActivityService {

    @Resource
    private BargainActivityMapper bargainActivityMapper;

    @Override
    public Long createBargainActivity(BargainActivityCreateReqVO createReqVO) {
        // 插入
        BargainActivityDO bargainActivity = BargainActivityConvert.INSTANCE.convert(createReqVO);
        bargainActivityMapper.insert(bargainActivity);
        // 返回
        return bargainActivity.getId();
    }

    @Override
    public void updateBargainActivity(BargainActivityUpdateReqVO updateReqVO) {
        // 校验存在
        validateBargainActivityExists(updateReqVO.getId());
        // 更新
        BargainActivityDO updateObj = BargainActivityConvert.INSTANCE.convert(updateReqVO);
        bargainActivityMapper.updateById(updateObj);
    }

    @Override
    public void deleteBargainActivity(Long id) {
        // 校验存在
        validateBargainActivityExists(id);
        // 删除
        bargainActivityMapper.deleteById(id);
    }

    private void validateBargainActivityExists(Long id) {
        if (bargainActivityMapper.selectById(id) == null) {
            throw exception(BARGAIN_ACTIVITY_NOT_EXISTS);
        }
    }

    @Override
    public BargainActivityDO getBargainActivity(Long id) {
        return bargainActivityMapper.selectById(id);
    }

    @Override
    public List<BargainActivityDO> getBargainActivityList(Collection<Long> ids) {
        return bargainActivityMapper.selectBatchIds(ids);
    }

    @Override
    public PageResult<BargainActivityDO> getBargainActivityPage(BargainActivityPageReqVO pageReqVO) {
        return bargainActivityMapper.selectPage(pageReqVO);
    }

    @Override
    public List<BargainActivityDO> getBargainActivityList(BargainActivityExportReqVO exportReqVO) {
        return bargainActivityMapper.selectList(exportReqVO);
    }

}
