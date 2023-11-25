package cn.iocoder.yudao.module.wms.service.ro;

import cn.iocoder.yudao.module.wms.dal.dataobject.ro.RoLpnDO;
import cn.iocoder.yudao.module.wms.dal.dataobject.ro.RoMtrlDO;
import cn.iocoder.yudao.module.wms.dal.mysql.ro.RoLpnMapper;
import cn.iocoder.yudao.module.wms.dal.mysql.ro.RoMtrlMapper;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import cn.iocoder.yudao.module.wms.controller.admin.ro.vo.*;
import cn.iocoder.yudao.module.wms.dal.dataobject.ro.RoDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import cn.iocoder.yudao.module.wms.dal.mysql.ro.RoMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.wms.enums.ErrorCodeConstants.*;

/**
 * 收料单 Service 实现类
 *
 * @author Arlen
 */
@Service
@Validated
public class RoServiceImpl implements RoService {

    @Resource
    private RoMapper roMapper;
    @Resource
    private RoLpnMapper roLpnMapper;
    @Resource
    private RoMtrlMapper roMtrlMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String createRo(RoSaveReqVO createReqVO) {
        // 插入
        RoDO ro = BeanUtils.toBean(createReqVO, RoDO.class);
        roMapper.insert(ro);

        // 插入子表
        createRoLpnList(ro.getRoId(), createReqVO.getRoLpns());
        createRoMtrlList(ro.getRoId(), createReqVO.getRoMtrls());
        // 返回
        return ro.getRoId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateRo(RoSaveReqVO updateReqVO) {
        // 校验存在
        validateRoExists(updateReqVO.getRoId());
        // 更新
        RoDO updateObj = BeanUtils.toBean(updateReqVO, RoDO.class);
        roMapper.updateById(updateObj);

        // 更新子表
        updateRoLpnList(updateReqVO.getRoId(), updateReqVO.getRoLpns());
        updateRoMtrlList(updateReqVO.getRoId(), updateReqVO.getRoMtrls());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteRo(String id) {
        // 校验存在
        validateRoExists(id);
        // 删除
        roMapper.deleteById(id);

        // 删除子表
        deleteRoLpnByRoId(id);
        deleteRoMtrlByRoId(id);
    }

    private void validateRoExists(String id) {
        if (roMapper.selectById(id) == null) {
            throw exception(RO_NOT_EXISTS);
        }
    }

    @Override
    public RoDO getRo(String id) {
        return roMapper.selectById(id);
    }

    @Override
    public PageResult<RoDO> getRoPage(RoPageReqVO pageReqVO) {
        return roMapper.selectPage(pageReqVO);
    }

    // ==================== 子表（收料单LPN明细） ====================

    @Override
    public List<RoLpnDO> getRoLpnListByRoId(String roId) {
        return roLpnMapper.selectListByRoId(roId);
    }

    private void createRoLpnList(String roId, List<RoLpnDO> list) {
        list.forEach(o -> o.setRoId(roId));
        roLpnMapper.insertBatch(list);
    }

    private void updateRoLpnList(String roId, List<RoLpnDO> list) {
        deleteRoLpnByRoId(roId);
        list.forEach(o -> o.setRoId(null).setUpdater(null).setUpdateTime(null)); // 解决更新情况下：1）id 冲突；2）updateTime 不更新
        createRoLpnList(roId, list);
    }

    private void deleteRoLpnByRoId(String roId) {
        roLpnMapper.deleteByRoId(roId);
    }

    // ==================== 子表（收料单物料明细） ====================

    @Override
    public List<RoMtrlDO> getRoMtrlListByRoId(String roId) {
        return roMtrlMapper.selectListByRoId(roId);
    }

    private void createRoMtrlList(String roId, List<RoMtrlDO> list) {
        list.forEach(o -> o.setRoId(roId));
        roMtrlMapper.insertBatch(list);
    }

    private void updateRoMtrlList(String roId, List<RoMtrlDO> list) {
        deleteRoMtrlByRoId(roId);
        list.forEach(o -> o.setRoId(null).setUpdater(null).setUpdateTime(null)); // 解决更新情况下：1）id 冲突；2）updateTime 不更新
        createRoMtrlList(roId, list);
    }

    private void deleteRoMtrlByRoId(String roId) {
        roMtrlMapper.deleteByRoId(roId);
    }

}