package cn.iocoder.yudao.module.hospital.service.aptitude;

import cn.iocoder.yudao.framework.common.enums.CommonStatusEnum;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;

import java.util.*;
import cn.iocoder.yudao.module.hospital.controller.admin.aptitude.vo.*;
import cn.iocoder.yudao.module.hospital.dal.dataobject.aptitude.AptitudeDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;

import cn.iocoder.yudao.module.hospital.convert.aptitude.AptitudeConvert;
import cn.iocoder.yudao.module.hospital.dal.mysql.aptitude.AptitudeMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.hospital.enums.ErrorCodeConstants.*;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;

/**
 * 资质信息 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class AptitudeServiceImpl extends ServiceImpl<AptitudeMapper, AptitudeDO> implements AptitudeService {

    @Resource
    private AptitudeMapper aptitudeMapper;

    @Override
    public Long createAptitude(AptitudeCreateReqVO createReqVO) {
        // 插入
        AptitudeDO aptitude = AptitudeConvert.INSTANCE.convert(createReqVO);
        aptitudeMapper.insert(aptitude);
        // 返回
        return aptitude.getId();
    }

    @Override
    public void updateAptitude(AptitudeUpdateReqVO updateReqVO) {
        // 校验存在
        validateAptitudeExists(updateReqVO.getId());
        // 更新
        AptitudeDO updateObj = AptitudeConvert.INSTANCE.convert(updateReqVO);
        aptitudeMapper.updateById(updateObj);
    }

    @Override
    public void deleteAptitude(Long id) {
        // 校验存在
        validateAptitudeExists(id);
        // 删除
        aptitudeMapper.deleteById(id);
    }

    private void validateAptitudeExists(Long id) {
        if (aptitudeMapper.selectById(id) == null) {
            throw exception(APTITUDE_NOT_EXISTS);
        }
    }

    @Override
    public AptitudeDO getAptitude(Long id) {
        return aptitudeMapper.selectById(id);
    }

    @Override
    public List<AptitudeDO> getAptitudeList() {

        return aptitudeMapper.selectList(AptitudeDO::getStatus, CommonStatusEnum.ENABLE.getStatus());
    }

    @Override
    public PageResult<AptitudeDO> getAptitudePage(AptitudePageReqVO pageReqVO) {
        return aptitudeMapper.selectPage(pageReqVO);
    }

    @Override
    public List<AptitudeDO> getAptitudeList(AptitudeExportReqVO exportReqVO) {
        return aptitudeMapper.selectList(exportReqVO);
    }

    @Override
    public List<AptitudeDO> listAllEnable() {
        return aptitudeMapper.selectList(AptitudeDO::getStatus, CommonStatusEnum.ENABLE.getStatus());
    }

}
