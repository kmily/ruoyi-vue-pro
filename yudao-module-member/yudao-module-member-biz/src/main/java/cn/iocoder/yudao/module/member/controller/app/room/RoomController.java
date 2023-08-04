package cn.iocoder.yudao.module.member.controller.app.room;

import cn.iocoder.yudao.framework.security.core.annotations.PreAuthenticated;
import static cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils.*;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Operation;

import javax.validation.*;
import javax.servlet.http.*;
import java.util.*;
import java.io.IOException;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;


import cn.iocoder.yudao.framework.operatelog.core.annotations.OperateLog;
import static cn.iocoder.yudao.framework.operatelog.core.enums.OperateTypeEnum.*;

import cn.iocoder.yudao.module.member.controller.app.room.vo.*;
import cn.iocoder.yudao.module.member.dal.dataobject.room.RoomDO;
import cn.iocoder.yudao.module.member.convert.room.RoomConvert;
import cn.iocoder.yudao.module.member.service.room.RoomService;

@Tag(name = "用户 APP - 用户房间")
@RestController
@RequestMapping("/member/room")
@Validated
public class RoomController {

    @Resource
    private RoomService roomService;

    @PostMapping("/create")
    @Operation(summary = "创建用户房间")
    @PreAuthenticated
    public CommonResult<Long> createRoom(@Valid @RequestBody RoomCreateReqVO createReqVO) {
        createReqVO.setUserId(getLoginUserId());
        return success(roomService.createRoom(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新用户房间")
    @PreAuthenticated
    public CommonResult<Boolean> updateRoom(@Valid @RequestBody RoomUpdateReqVO updateReqVO) {
        roomService.updateRoom(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除用户房间")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthenticated
    public CommonResult<Boolean> deleteRoom(@RequestParam("id") Long id) {
        roomService.deleteRoom(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得用户房间")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthenticated
    public CommonResult<RoomRespVO> getRoom(@RequestParam("id") Long id) {
        RoomDO room = roomService.getRoom(id);
        return success(RoomConvert.INSTANCE.convert(room));
    }

    @GetMapping("/list")
    @Operation(summary = "获得用户房间列表")
    @Parameter(name = "familyId", description = "家庭ID", required = true, example = "123")
    @PreAuthenticated
    public CommonResult<List<RoomRespVO>> getRoomList(@RequestParam(value = "familyId") Long familyId) {
        List<RoomDO> list = roomService.getRoomList(new RoomExportReqVO().setUserId(getLoginUserId()).setFamilyId(familyId));
        return success(RoomConvert.INSTANCE.convertList(list));
    }

    @GetMapping("/page")
    @Operation(summary = "获得用户房间分页")
    @PreAuthenticated
    public CommonResult<PageResult<RoomRespVO>> getRoomPage(@Valid RoomPageReqVO pageVO) {
        PageResult<RoomDO> pageResult = roomService.getRoomPage(pageVO);
        return success(RoomConvert.INSTANCE.convertPage(pageResult));
    }


}
