package cn.iocoder.yudao.module.system.controller.admin.mqtttopic;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.excel.core.util.ExcelUtils;
import cn.iocoder.yudao.framework.operatelog.core.annotations.OperateLog;
import cn.iocoder.yudao.module.system.controller.admin.mqtttopic.vo.*;
import cn.iocoder.yudao.module.system.convert.mqtttopic.MqttTopicConvert;
import cn.iocoder.yudao.module.system.dal.dataobject.mqtttopic.MqttTopicDO;
import cn.iocoder.yudao.module.system.service.mqtttopic.MqttTopicService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;
import static cn.iocoder.yudao.framework.operatelog.core.enums.OperateTypeEnum.EXPORT;

/**
 * @author ZanGe
 */
@Api(tags = "管理后台 - mqtt主题")
@RestController
@RequestMapping("/system/mqtt-topic")
@Validated
public class MqttTopicController {

    @Resource
    private MqttTopicService mqttTopicService;

    @PostMapping("/create")
    @ApiOperation("创建mqtt主题")
    @PreAuthorize("@ss.hasPermission('system:mqtt-topic:create')")
    public CommonResult<Long> createMqttTopic(@Valid @RequestBody MqttTopicCreateReqVO createReqVO) {
        return success(mqttTopicService.createMqttTopic(createReqVO));
    }

    @PutMapping("/update")
    @ApiOperation("更新mqtt主题")
    @PreAuthorize("@ss.hasPermission('system:mqtt-topic:update')")
    public CommonResult<Boolean> updateMqttTopic(@Valid @RequestBody MqttTopicUpdateReqVO updateReqVO) {
        mqttTopicService.updateMqttTopic(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @ApiOperation("删除mqtt主题")
    @ApiImplicitParam(name = "id", value = "编号", required = true, dataTypeClass = Long.class)
    @PreAuthorize("@ss.hasPermission('system:mqtt-topic:delete')")
    public CommonResult<Boolean> deleteMqttTopic(@RequestParam("id") Long id) {
        mqttTopicService.deleteMqttTopic(id);
        return success(true);
    }

    @GetMapping("/get")
    @ApiOperation("获得mqtt主题")
    @ApiImplicitParam(name = "id", value = "编号", required = true, example = "1024", dataTypeClass = Long.class)
    @PreAuthorize("@ss.hasPermission('system:mqtt-topic:query')")
    public CommonResult<MqttTopicRespVO> getMqttTopic(@RequestParam("id") Long id) {
        MqttTopicDO mqttTopic = mqttTopicService.getMqttTopic(id);
        return success(MqttTopicConvert.INSTANCE.convert(mqttTopic));
    }

    @GetMapping("/list")
    @ApiOperation("获得mqtt主题列表")
    @ApiImplicitParam(name = "ids", value = "编号列表", required = true, example = "1024,2048", dataTypeClass = List.class)
    @PreAuthorize("@ss.hasPermission('system:mqtt-topic:query')")
    public CommonResult<List<MqttTopicRespVO>> getMqttTopicList(@RequestParam("ids") Collection<Long> ids) {
        List<MqttTopicDO> list = mqttTopicService.getMqttTopicList(ids);
        return success(MqttTopicConvert.INSTANCE.convertList(list));
    }

    @GetMapping("/page")
    @ApiOperation("获得mqtt主题分页")
    @PreAuthorize("@ss.hasPermission('system:mqtt-topic:query')")
    public CommonResult<PageResult<MqttTopicRespVO>> getMqttTopicPage(@Valid MqttTopicPageReqVO pageVO) {
        PageResult<MqttTopicDO> pageResult = mqttTopicService.getMqttTopicPage(pageVO);
        return success(MqttTopicConvert.INSTANCE.convertPage(pageResult));
    }

    @GetMapping("/export-excel")
    @ApiOperation("导出mqtt主题 Excel")
    @PreAuthorize("@ss.hasPermission('system:mqtt-topic:export')")
    @OperateLog(type = EXPORT)
    public void exportMqttTopicExcel(@Valid MqttTopicExportReqVO exportReqVO,
              HttpServletResponse response) throws IOException {
        List<MqttTopicDO> list = mqttTopicService.getMqttTopicList(exportReqVO);
        // 导出 Excel
        List<MqttTopicExcelVO> datas = MqttTopicConvert.INSTANCE.convertList02(list);
        ExcelUtils.write(response, "mqtt主题.xls", "数据", MqttTopicExcelVO.class, datas);
    }

}
