package cn.iocoder.yudao.module.yr.controller.admin.sys.dict;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.excel.core.util.ExcelUtils;
import cn.iocoder.yudao.framework.operatelog.core.annotations.OperateLog;
import cn.iocoder.yudao.module.yr.common.vo.UpdateStatusVO;
import cn.iocoder.yudao.module.yr.controller.admin.sys.dict.vo.data.*;
import cn.iocoder.yudao.module.yr.convert.sys.dict.QgDictDataConvert;
import cn.iocoder.yudao.module.yr.dal.dataobject.sys.dict.QgDictDataDO;
import cn.iocoder.yudao.module.yr.service.sys.dict.QgDictDataService;

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
import java.util.List;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;
import static cn.iocoder.yudao.framework.operatelog.core.enums.OperateTypeEnum.EXPORT;

@Api(tags = "管理后台 - 字典数据")
@RestController
@RequestMapping("/qg/sys/dict-data")
@Validated
public class QgDictDataController {

    @Resource
    private QgDictDataService dictDataService;

    @PostMapping("/create")
    @ApiOperation("新增字典数据")
    @PreAuthorize("@ss.hasPermission('qg:dict:create')")
    public CommonResult<Long> createDictData(@Valid @RequestBody QgDictDataCreateReqVO reqVO) {
        Long dictDataId = dictDataService.createDictData(reqVO);
        return success(dictDataId);
    }

    @PutMapping("update")
    @ApiOperation("修改字典数据")
    @PreAuthorize("@ss.hasPermission('qg:dict:update')")
    public CommonResult<Boolean> updateDictData(@Valid @RequestBody QgDictDataUpdateReqVO reqVO) {
        dictDataService.updateDictData(reqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @ApiOperation("删除字典数据")
    @ApiImplicitParam(name = "id", value = "编号", required = true, example = "1024", dataTypeClass = Long.class)
    @PreAuthorize("@ss.hasPermission('qg:dict:delete')")
    public CommonResult<Boolean> deleteDictData(Long id) {
        dictDataService.deleteDictData(id);
        return success(true);
    }

    @GetMapping("/list-all-simple")
    @ApiOperation(value = "获得全部字典数据列表", notes = "一般用于管理后台缓存字典数据在本地")
    // 无需添加权限认证，因为前端全局都需要
    public CommonResult<List<QgDictDataRespVO>> getSimpleDictDatas() {
        List<QgDictDataDO> list = dictDataService.getDictDatas();
        return success(QgDictDataConvert.INSTANCE.convertListAll(list));
    }

    @GetMapping("/page")
    @ApiOperation("/获得字典类型的分页列表")
    @PreAuthorize("@ss.hasPermission('qg:dict:query')")
    public CommonResult<PageResult<QgDictDataRespVO>> getDictTypePage(@Valid QgDictDataPageReqVO reqVO) {
        return success(QgDictDataConvert.INSTANCE.convertPage(dictDataService.getDictDataPage(reqVO)));
    }

    @GetMapping(value = "/get")
    @ApiOperation("/查询字典数据详细")
    @ApiImplicitParam(name = "id", value = "编号", required = true, example = "1024", dataTypeClass = Long.class)
    @PreAuthorize("@ss.hasPermission('qg:dict:query')")
    public CommonResult<QgDictDataRespVO> getDictData(@RequestParam("id") Long id) {
        return success(QgDictDataConvert.INSTANCE.convert(dictDataService.getDictData(id)));
    }

    @GetMapping("/export")
    @ApiOperation("导出字典数据")
    @PreAuthorize("@ss.hasPermission('qg:dict:export')")
    @OperateLog(type = EXPORT)
    public void export(HttpServletResponse response, @Valid QgDictDataExportReqVO reqVO) throws IOException {
        List<QgDictDataDO> list = dictDataService.getDictDatas(reqVO);
        List<QgDictDataExcelVO> data = QgDictDataConvert.INSTANCE.convertList02(list);
        // 输出
        ExcelUtils.write(response, "字典数据.xls", "数据列表", QgDictDataExcelVO.class, data);
    }

    @PutMapping("/updateForStatus")
    @ApiOperation("更新状态")
    @PreAuthorize("@ss.hasPermission('qg:dict:update')")
    public CommonResult<Boolean> updateForStatus(@Valid @RequestBody UpdateStatusVO updateStatusVO) {
        dictDataService.updateStatus(updateStatusVO);
        return success(true);
    }
}
