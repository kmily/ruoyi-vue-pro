package cn.iocoder.yudao.module.member.controller.admin.serverperson;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.IdcardUtil;
import cn.hutool.core.util.StrUtil;
import cn.iocoder.yudao.framework.common.enums.CommonStatusEnum;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.member.controller.admin.serverperson.vo.ServerPersonAuditVO;
import cn.iocoder.yudao.module.member.controller.admin.serverperson.vo.ServerPersonPageReqVO;
import cn.iocoder.yudao.module.member.controller.admin.serverperson.vo.ServerPersonRespVO;
import cn.iocoder.yudao.module.member.convert.serverperson.ServerPersonConvert;
import cn.iocoder.yudao.module.member.dal.dataobject.serverperson.ServerPersonDO;
import cn.iocoder.yudao.module.member.dal.dataobject.user.MemberUserDO;
import cn.iocoder.yudao.module.member.service.serverperson.ServerPersonService;
import cn.iocoder.yudao.module.member.service.user.MemberUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "用户 APP - 被服务人")
@RestController
@RequestMapping("/member/server-person")
@Validated
public class ServerPersonController {

    @Resource
    private ServerPersonService serverPersonService;

    @Resource
    private MemberUserService memberUserService;


    @GetMapping("/get")
    @Operation(summary = "获得被服务人")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('member:server-person:query')")
    public CommonResult<ServerPersonRespVO> getServerPerson(@RequestParam("id") Long id) {
        ServerPersonDO serverPerson = serverPersonService.getServerPerson(id);
        ServerPersonRespVO personRespVO = ServerPersonConvert.INSTANCE.convert01(serverPerson);
        if(StrUtil.isNotBlank(personRespVO.getIdCard())){
            personRespVO.setAge(IdcardUtil.getAgeByIdCard(personRespVO.getIdCard()));
        }
        return success(personRespVO);
    }



    @GetMapping("/page")
    @Operation(summary = "获得被服务人分页")
    @PreAuthorize("@ss.hasPermission('member:server-person:query')")
    public CommonResult<PageResult<ServerPersonRespVO>> getServerPersonPage(@Valid ServerPersonPageReqVO pageVO) {
        PageResult<ServerPersonDO> pageResult = serverPersonService.getServerPersonPage(ServerPersonConvert.INSTANCE.convert(pageVO));
        PageResult<ServerPersonRespVO> result = ServerPersonConvert.INSTANCE.convertPage01(pageResult);
        List<ServerPersonRespVO> list = result.getList();
        if(CollUtil.isNotEmpty(list)){
            Set<Long> memberIds = list.stream().map(ServerPersonRespVO::getMemberId).collect(Collectors.toSet());
            List<MemberUserDO> userList = memberUserService.getUserList(memberIds);
            Map<Long, String> memberMap = userList.stream().collect(Collectors.toMap(MemberUserDO::getId, MemberUserDO::getNickname));

            list.forEach(item -> {
                item.setMemberName(memberMap.get(item.getMemberId()));
                if(Objects.equals(CommonStatusEnum.APPLYING.name(), item.getStatus())){
                    item.setCheckName("").setCheckTime(null);
                }
            });
        }
        return success(result);
    }

    @PutMapping("/audit")
    @Operation(summary = "被户人审核")
    @PreAuthorize("@ss.hasPermission('member:server-person:audit')")
    public CommonResult<Boolean> auditServerPerson(@RequestBody ServerPersonAuditVO auditVO){
        serverPersonService.auditServerPerson(auditVO);
        return success(true);
    }

}
