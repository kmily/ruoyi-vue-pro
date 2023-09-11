package cn.iocoder.yudao.module.member.service.visitpage;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;

import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.validation.annotation.Validated;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;

import cn.iocoder.yudao.module.member.controller.admin.visitpage.vo.*;
import cn.iocoder.yudao.module.member.dal.dataobject.visitpage.VisitPageDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;

import cn.iocoder.yudao.module.member.convert.visitpage.VisitPageConvert;
import cn.iocoder.yudao.module.member.dal.mysql.visitpage.VisitPageMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.member.enums.ErrorCodeConstants.*;

/**
 * 页面访问数据 Service 实现类
 *
 * @author 和尘同光
 */
@Service
@Validated
public class VisitPageServiceImpl implements VisitPageService {

    @Resource
    private VisitPageMapper visitPageMapper;

    @Override
    public Long createVisitPage(VisitPageCreateReqVO createReqVO) {
        // 插入
        VisitPageDO visitPage = VisitPageConvert.INSTANCE.convert(createReqVO);
        visitPage.setStartTime(LocalDateTime.now());

        visitPageMapper.insert(visitPage);

        // 返回
        return visitPage.getId();
    }


    @Override
    public void exit(Long id) {
        VisitPageDO visitPageDO = visitPageMapper.selectById(id);
        if(visitPageDO != null){
            LocalDateTime startTime = visitPageDO.getStartTime();
            LocalDateTime now = LocalDateTime.now();
            visitPageMapper.updateById(new VisitPageDO().setId(id).setEndTime(LocalDateTime.now())
                    .setUseTime(Duration.between(startTime, now).toMillis()));
        }
    }


    @Override
    public void updateVisitPage(VisitPageUpdateReqVO updateReqVO) {
        // 校验存在
        // 更新
        VisitPageDO updateObj = VisitPageConvert.INSTANCE.convert(updateReqVO);
        visitPageMapper.updateById(updateObj);
    }

    @Override
    public void deleteVisitPage(Long id) {
        // 校验存在
        // 删除
        visitPageMapper.deleteById(id);
    }



    @Override
    public VisitPageDO getVisitPage(Long id) {
        return visitPageMapper.selectById(id);
    }

    @Override
    public List<VisitPageDO> getVisitPageList(Collection<Long> ids) {
        return visitPageMapper.selectBatchIds(ids);
    }

    @Override
    public PageResult<VisitPageDO> getVisitPagePage(VisitPagePageReqVO pageReqVO) {
        return null;
    }

    @Override
    public List<VisitPageDO> getVisitPageList(VisitPageExportReqVO exportReqVO) {
        return visitPageMapper.selectList(exportReqVO);
    }



}
