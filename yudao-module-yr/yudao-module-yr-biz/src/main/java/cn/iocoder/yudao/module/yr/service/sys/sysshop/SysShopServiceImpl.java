package cn.iocoder.yudao.module.yr.service.sys.sysshop;

import cn.iocoder.yudao.module.yr.controller.admin.sys.sysshop.vo.SysShopCreateReqVO;
import cn.iocoder.yudao.module.yr.controller.admin.sys.sysshop.vo.SysShopExportReqVO;
import cn.iocoder.yudao.module.yr.controller.admin.sys.sysshop.vo.SysShopPageReqVO;
import cn.iocoder.yudao.module.yr.controller.admin.sys.sysshop.vo.SysShopUpdateReqVO;
import cn.iocoder.yudao.module.yr.convert.sys.sysshop.SysShopConvert;
import cn.iocoder.yudao.module.yr.dal.dataobject.sys.sysshop.SysShopDO;
import cn.iocoder.yudao.module.yr.dal.mysql.sys.sysshop.SysShopMapper;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;



import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.yr.enums.ErrorCodeConstants.*;

/**
 * 店面 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class SysShopServiceImpl implements SysShopService {

    @Resource
    private SysShopMapper sysShopMapper;

    @Override
    public Long createSysShop(SysShopCreateReqVO createReqVO) {
        // 插入
        SysShopDO sysShop = SysShopConvert.INSTANCE.convert(createReqVO);
        sysShopMapper.insert(sysShop);
        // 返回
        return sysShop.getId();
    }

    @Override
    public void updateSysShop(SysShopUpdateReqVO updateReqVO) {
        // 校验存在
        this.validateSysShopExists(updateReqVO.getId());
        // 更新
        SysShopDO updateObj = SysShopConvert.INSTANCE.convert(updateReqVO);
        sysShopMapper.updateById(updateObj);
    }

    @Override
    public void deleteSysShop(Long id) {
        // 校验存在
        this.validateSysShopExists(id);
        // 删除
        sysShopMapper.deleteById(id);
    }

    private void validateSysShopExists(Long id) {
        if (sysShopMapper.selectById(id) == null) {
            throw exception(SYS_SHOP_NOT_EXISTS);
        }
    }

    @Override
    public SysShopDO getSysShop(Long id) {
        return sysShopMapper.selectById(id);
    }

    @Override
    public List<SysShopDO> getSysShopList(Collection<Long> ids) {
        return sysShopMapper.selectBatchIds(ids);
    }

    @Override
    public PageResult<SysShopDO> getSysShopPage(SysShopPageReqVO pageReqVO) {
        return sysShopMapper.selectPage(pageReqVO);
    }

    @Override
    public List<SysShopDO> getSysShopList(SysShopExportReqVO exportReqVO) {
        return sysShopMapper.selectList(exportReqVO);
    }

}
