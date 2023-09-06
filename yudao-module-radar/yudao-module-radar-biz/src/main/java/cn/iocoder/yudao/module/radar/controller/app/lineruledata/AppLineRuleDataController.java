package cn.iocoder.yudao.module.radar.controller.app.lineruledata;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import cn.iocoder.yudao.framework.security.core.annotations.PreAuthenticated;
import cn.iocoder.yudao.module.radar.controller.admin.lineruledata.vo.LineRuleDataPageReqVO;
import cn.iocoder.yudao.module.radar.controller.admin.lineruledata.vo.LineRuleDataRespVO;
import cn.iocoder.yudao.module.radar.controller.app.lineruledata.vo.AppLineRuleDataPageReqVO;
import cn.iocoder.yudao.module.radar.controller.app.lineruledata.vo.AppLineRuleDataReqVO;
import cn.iocoder.yudao.module.radar.controller.app.lineruledata.vo.AppLineRuleDataResVO;
import cn.iocoder.yudao.module.radar.convert.lineruledata.LineRuleDataConvert;
import cn.iocoder.yudao.module.radar.dal.dataobject.lineruledata.LineRuleDataDO;
import cn.iocoder.yudao.module.radar.service.lineruledata.LineRuleDataService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

/**
 * @author whycode
 * @title: AppLineRuleDataController
 * @projectName ruoyi-vue-pro
 * @description: TODO
 * @date 2023/9/69:26
 */

@Tag(name = "用户APP - 离回家数据")
@RestController
@RequestMapping("/radar/line-rule-data")
@Validated
public class AppLineRuleDataController {

    @Resource
    private LineRuleDataService lineRuleDataService;

    @GetMapping("/enter-leave")
    @Operation(summary = "查询离回家数据")
    @Parameter(name = "deviceId", description = "设备编号", required = true, example = "1024")
    @Parameter(name = "startDate", description = "开始日期", required = true, example = "2023-08-07")
    @Parameter(name = "endDate", description = "结束日期", required = true, example = "2023-08-11")
    @PreAuthenticated
    public CommonResult<List<AppLineRuleDataResVO>> queryEnterAndLeave(@RequestParam("deviceId") Long deviceId,
                                                                       @RequestParam("startDate") String startDate,
                                                                       @RequestParam("endDate") String endDate){

        List<AppLineRuleDataResVO> lineRuleDataResVOS = lineRuleDataService.getLineRuleDataList(deviceId, startDate, endDate);

        return CommonResult.success(lineRuleDataResVOS);

    }


    @GetMapping("/page")
    @Operation(summary = "获得绊线数据分页")
    @PreAuthenticated
    public CommonResult<PageResult<LineRuleDataRespVO>> getLineRuleDataPage(@Valid AppLineRuleDataPageReqVO appPageVO) {

        LineRuleDataPageReqVO pageVO = new LineRuleDataPageReqVO();
        pageVO.setDeviceId(appPageVO.getDeviceId());
        pageVO.setPageNo(appPageVO.getPageNo())
                .setPageSize(appPageVO.getPageSize());

        LocalDate createDate = appPageVO.getCreateDate();
        if(createDate == null){
            createDate = LocalDate.now();
            LocalDateTime start = createDate.plusDays(-7).atTime(0, 0, 0);
            LocalDateTime end = createDate.atTime(23, 59,59);
            pageVO.setCreateTime(new LocalDateTime[]{start, end});
        }else{
            LocalDateTime start = createDate.atTime(0, 0, 0);
            LocalDateTime end = createDate.atTime(23, 59,59);
            pageVO.setCreateTime(new LocalDateTime[]{start, end});
        }
        PageResult<LineRuleDataDO> pageResult = lineRuleDataService.getLineRuleDataPage(pageVO);

        List<LineRuleDataDO> doList = pageResult.getList().stream().map(item -> {
            LineRuleDataDO ruleDataDO = new LineRuleDataDO()
                    .setDeviceId(item.getDeviceId())
                    .setEnter(item.getEnter())
                    .setGoOut(item.getGoOut());
            ruleDataDO.setCreateTime(item.getCreateTime());
            return ruleDataDO;
        }).collect(Collectors.toList());

        pageResult.setList(doList);

        return success(LineRuleDataConvert.INSTANCE.convertPage(pageResult));
    }

}
