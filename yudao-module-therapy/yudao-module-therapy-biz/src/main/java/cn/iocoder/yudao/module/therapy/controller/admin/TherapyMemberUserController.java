package cn.iocoder.yudao.module.therapy.controller.admin;

import cn.hutool.core.collection.CollUtil;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.collection.CollectionUtils;
import cn.iocoder.yudao.module.member.api.user.MemberUserApi;
import cn.iocoder.yudao.module.member.api.user.dto.MemberUserExtDTO;
import cn.iocoder.yudao.module.member.api.user.dto.MemberUserPageReqDTO;
import cn.iocoder.yudao.module.member.api.user.dto.MemberUserRespDTO;
import cn.iocoder.yudao.module.therapy.controller.admin.vo.AdminSetAppointmentTimeVO;
import cn.iocoder.yudao.module.therapy.controller.admin.vo.UserRespVO;
import cn.iocoder.yudao.module.therapy.convert.SurveyConvert;
import cn.iocoder.yudao.module.therapy.dal.dataobject.definition.TreatmentInstanceDO;
import cn.iocoder.yudao.module.therapy.service.TreatmentService;
import cn.iocoder.yudao.module.therapy.service.TreatmentStatisticsDataService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

/**
 * @Author:lidongw_1
 * @Date 2024/5/23
 * @Description: 管理后台 - 患者管理
 **/

@Tag(name = "管理后台 - 患者管理")
@RestController
@RequestMapping("/therapy/member/user")
@Validated
@Slf4j
public class TherapyMemberUserController {
    @Resource
    private TreatmentStatisticsDataService treatmentStatisticsDataService;
    @Resource
    private MemberUserApi memberUserApi;
    @Resource
    private TreatmentService treatmentService;

    @PostMapping("/set-appointment-time")
    @Operation(summary = "设置预约时间")
    public CommonResult<Boolean> setAppointmentTime(@RequestBody @Valid AdminSetAppointmentTimeVO reqVO) {
        treatmentService.setAppointmentTime(reqVO.getUserId(),reqVO);
        return success(true);
    }

    @GetMapping("/page")
    @Operation(summary = "获得会员用户分页")
//    @PreAuthorize("@ss.hasPermission('member:user:query')")
    public CommonResult<PageResult<UserRespVO>> getUserPage(@Valid MemberUserPageReqDTO pageVO) {
        PageResult<MemberUserRespDTO> pageResult = memberUserApi.getUserPage(pageVO);
        if (CollUtil.isEmpty(pageResult.getList())) {
            return success(new PageResult(0L));
        }

        //处理患者最新的治疗流程返显
        List<Long> userIds = pageResult.getList().stream().map(p -> p.getId()).collect(Collectors.toList());
        Map<Long, TreatmentInstanceDO> map = treatmentStatisticsDataService.queryLatestTreatmentInstanceId(userIds);
        Map<Long, List<String>> map1 = treatmentStatisticsDataService.queryPsycoTroubleCategory(userIds);
        //处理扩展信息
        List<MemberUserExtDTO> extDOS=memberUserApi.getUserExtInfoList(userIds);
        Map<Long,MemberUserExtDTO> map2= CollectionUtils.convertMap(extDOS,MemberUserExtDTO::getUserId);

        PageResult<UserRespVO> res = SurveyConvert.INSTANCE.convertUserDTOPage(pageResult);
        for (UserRespVO item : res.getList()) {
            if (map.containsKey(item.getId())) {
                TreatmentInstanceDO instanceDO = map.get(item.getId());
                item.setInstanceState(instanceDO.getStatus());
                item.setTreatmentInstanceId(instanceDO.getId());
            }
            if (map1 != null && map1.containsKey(item.getId())) {
                item.setLlm(map1.get(item.getId()));
            }
            if (map2 != null && map2.containsKey(item.getId())) {
                item.setAppointmentDate(map2.get(item.getId()).getAppointmentDate());
                item.setAppointmentTimeRange(map2.get(item.getId()).getAppointmentTimeRange());
                item.setTestGroup(map2.get(item.getId()).getTestGroup());
            }
        }

        return success(res);

    }
}
