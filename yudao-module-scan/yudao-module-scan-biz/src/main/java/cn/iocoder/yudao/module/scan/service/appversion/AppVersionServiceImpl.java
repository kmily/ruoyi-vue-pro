package cn.iocoder.yudao.module.scan.service.appversion;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;

import java.util.*;
import cn.iocoder.yudao.module.scan.controller.admin.appversion.vo.*;
import cn.iocoder.yudao.module.scan.dal.dataobject.appversion.AppVersionDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;

import cn.iocoder.yudao.module.scan.convert.appversion.AppVersionConvert;
import cn.iocoder.yudao.module.scan.dal.mysql.appversion.AppVersionMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.scan.enums.ErrorCodeConstants.*;

/**
 * 应用版本记录 Service 实现类
 *
 * @author lyz
 */
@Service
@Validated
public class AppVersionServiceImpl implements AppVersionService {

    @Resource
    private AppVersionMapper appVersionMapper;

    @Override
    public Long createAppVersion(AppVersionCreateReqVO createReqVO) {
        // 插入
        AppVersionDO appVersion = AppVersionConvert.INSTANCE.convert(createReqVO);
        appVersionMapper.insert(appVersion);
        // 返回
        return appVersion.getId();
    }

    @Override
    public void updateAppVersion(AppVersionUpdateReqVO updateReqVO) {
        // 校验存在
        this.validateAppVersionExists(updateReqVO.getId());
        // 更新
        AppVersionDO updateObj = AppVersionConvert.INSTANCE.convert(updateReqVO);
        appVersionMapper.updateById(updateObj);
    }

    @Override
    public void deleteAppVersion(Long id) {
        // 校验存在
        this.validateAppVersionExists(id);
        // 删除
        appVersionMapper.deleteById(id);
    }

    private void validateAppVersionExists(Long id) {
        if (appVersionMapper.selectById(id) == null) {
            throw exception(APP_VERSION_NOT_EXISTS);
        }
    }

    @Override
    public AppVersionDO getAppVersion(Long id) {
        return appVersionMapper.selectById(id);
    }

    @Override
    public List<AppVersionDO> getAppVersionList(Collection<Long> ids) {
        return appVersionMapper.selectBatchIds(ids);
    }

    @Override
    public PageResult<AppVersionDO> getAppVersionPage(AppVersionPageReqVO pageReqVO) {
        return appVersionMapper.selectPage(pageReqVO);
    }

    @Override
    public List<AppVersionDO> getAppVersionList(AppVersionExportReqVO exportReqVO) {
        return appVersionMapper.selectList(exportReqVO);
    }
    @Override
    public AppVersionDO getNewVersion(){
        return appVersionMapper.selectNewVersion();
    }
}
