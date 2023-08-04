package cn.iocoder.yudao.module.member.service.room;

import cn.iocoder.yudao.module.member.dal.dataobject.family.FamilyDO;
import cn.iocoder.yudao.module.member.enums.ErrorCodeConstants;
import cn.iocoder.yudao.module.member.service.family.FamilyService;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;

import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;

import java.util.*;
import cn.iocoder.yudao.module.member.controller.app.room.vo.*;
import cn.iocoder.yudao.module.member.dal.dataobject.room.RoomDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;

import cn.iocoder.yudao.module.member.convert.room.RoomConvert;
import cn.iocoder.yudao.module.member.dal.mysql.room.RoomMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.member.enums.ErrorCodeConstants.*;

/**
 * 用户房间 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class RoomServiceImpl implements RoomService {

    @Resource
    private RoomMapper roomMapper;
    @Resource
    private FamilyService familyService;

    @Override
    public Long createRoom(RoomCreateReqVO createReqVO) {
        FamilyDO family = familyService.getFamily(createReqVO.getFamilyId());
        if(family == null){
            throw exception(FAMILY_NOT_EXISTS);
        }
        // 插入
        RoomDO room = RoomConvert.INSTANCE.convert(createReqVO);
        roomMapper.insert(room);
        // 返回
        return room.getId();
    }

    @Override
    public void updateRoom(RoomUpdateReqVO updateReqVO) {
        // 校验存在
        validateRoomExists(updateReqVO.getId());
        // 更新
        RoomDO updateObj = RoomConvert.INSTANCE.convert(updateReqVO);
        roomMapper.updateById(updateObj);
    }

    @Override
    public void deleteRoom(Long id) {
        // 校验存在
        validateRoomExists(id);
        // 删除
        roomMapper.deleteById(id);
    }

    private void validateRoomExists(Long id) {
        if (roomMapper.selectById(id) == null) {
            throw exception(ROOM_NOT_EXISTS);
        }
    }

    @Override
    public RoomDO getRoom(Long id) {
        return roomMapper.selectById(id);
    }

    @Override
    public List<RoomDO> getRoomList(Collection<Long> ids) {
        return roomMapper.selectBatchIds(ids);
    }

    @Override
    public PageResult<RoomDO> getRoomPage(RoomPageReqVO pageReqVO) {
        return roomMapper.selectPage(pageReqVO);
    }

    @Override
    public List<RoomDO> getRoomList(RoomExportReqVO exportReqVO) {
        return roomMapper.selectList(exportReqVO);
    }

}
