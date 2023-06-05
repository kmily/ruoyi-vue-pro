package com.cw.module.trade.service.followrecord;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;

import java.util.*;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.cw.module.trade.controller.admin.followrecord.vo.*;
import com.cw.module.trade.dal.dataobject.followrecord.FollowRecordDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;

import com.cw.module.trade.convert.followrecord.FollowRecordConvert;
import com.cw.module.trade.dal.mysql.followrecord.FollowRecordMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.cw.module.trade.enums.ErrorCodeConstants.*;

/**
 * 账号跟随记录 Service 实现类
 *
 * @author chengjiale
 */
@Service
@Validated
public class FollowRecordServiceImpl implements FollowRecordService {

    @Resource
    private FollowRecordMapper followRecordMapper;

    @Override
    public Long createFollowRecord(FollowRecordCreateReqVO createReqVO) {
        // 插入
        FollowRecordDO followRecord = FollowRecordConvert.INSTANCE.convert(createReqVO);
        followRecordMapper.insert(followRecord);
        // 返回
        return followRecord.getId();
    }

    @Override
    public void updateFollowRecord(FollowRecordUpdateReqVO updateReqVO) {
        // 校验存在
        validateFollowRecordExists(updateReqVO.getId());
        // 更新
        FollowRecordDO updateObj = FollowRecordConvert.INSTANCE.convert(updateReqVO);
        followRecordMapper.updateById(updateObj);
    }

    @Override
    public void deleteFollowRecord(Long id) {
        // 校验存在
        validateFollowRecordExists(id);
        // 删除
        followRecordMapper.deleteById(id);
    }

    private void validateFollowRecordExists(Long id) {
        if (followRecordMapper.selectById(id) == null) {
//            throw exception(FOLLOW_RECORD_NOT_EXISTS);
        }
    }

    @Override
    public FollowRecordDO getFollowRecord(Long id) {
        return followRecordMapper.selectById(id);
    }

    @Override
    public List<FollowRecordDO> getFollowRecordList(Collection<Long> ids) {
        return followRecordMapper.selectBatchIds(ids);
    }

    @Override
    public PageResult<FollowRecordDO> getFollowRecordPage(FollowRecordPageReqVO pageReqVO) {
        return followRecordMapper.selectPage(pageReqVO);
    }

    @Override
    public List<FollowRecordDO> getFollowRecordList(FollowRecordExportReqVO exportReqVO) {
        return followRecordMapper.selectList(exportReqVO);
    }

    @Override
    public List<FollowRecordDO> listFollowRecord(Long thirdOrderId) {
        return followRecordMapper.selectList(Wrappers.lambdaQuery(
                new FollowRecordDO().setFollowThridOrderId(thirdOrderId)));
    }
}
