package cn.iocoder.yudao.module.hospital.service.careaptitude;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;

import java.util.*;
import cn.iocoder.yudao.module.hospital.controller.admin.careaptitude.vo.*;
import cn.iocoder.yudao.module.hospital.dal.dataobject.careaptitude.CareAptitudeCheckLogDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;

import cn.iocoder.yudao.module.hospital.convert.careaptitude.CareAptitudeCheckLogConvert;
import cn.iocoder.yudao.module.hospital.dal.mysql.careaptitude.CareAptitudeCheckLogMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.hospital.enums.ErrorCodeConstants.*;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;

/**
 * 医护资质审核记录 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class CareAptitudeCheckLogServiceImpl extends ServiceImpl<CareAptitudeCheckLogMapper, CareAptitudeCheckLogDO> implements CareAptitudeCheckLogService {

    @Resource
    private CareAptitudeCheckLogMapper careAptitudeCheckLogMapper;

    @Override
    public CareAptitudeCheckLogDO getCareAptitudeCheckLog(Long id) {
        return careAptitudeCheckLogMapper.selectById(id);
    }

    @Override
    public List<CareAptitudeCheckLogDO> getCareAptitudeCheckLogList(Collection<Long> ids) {
        if (CollUtil.isEmpty(ids)) {
            return ListUtil.empty();
        }
        return careAptitudeCheckLogMapper.selectBatchIds(ids);
    }

    @Override
    public PageResult<CareAptitudeCheckLogDO> getCareAptitudeCheckLogPage(CareAptitudeCheckLogPageReqVO pageReqVO) {
        return careAptitudeCheckLogMapper.selectPage(pageReqVO);
    }

    @Override
    public List<CareAptitudeCheckLogDO> getCareAptitudeCheckLogList(CareAptitudeCheckLogExportReqVO exportReqVO) {
        return careAptitudeCheckLogMapper.selectList(exportReqVO);
    }

}
