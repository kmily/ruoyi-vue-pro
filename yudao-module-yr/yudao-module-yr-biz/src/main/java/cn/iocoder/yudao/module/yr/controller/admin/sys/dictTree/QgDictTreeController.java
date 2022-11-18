package cn.iocoder.yudao.module.yr.controller.admin.sys.dictTree;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.excel.core.util.ExcelUtils;
import cn.iocoder.yudao.framework.operatelog.core.annotations.OperateLog;

import cn.iocoder.yudao.module.yr.controller.admin.sys.dictTree.vo.*;
import cn.iocoder.yudao.module.yr.convert.sys.dictTree.QgDictTreeConvert;
import cn.iocoder.yudao.module.yr.dal.dataobject.sys.dictTree.QgDictTreeDO;

import cn.iocoder.yudao.module.yr.service.sys.dictTree.QgDictTreeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
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

@Api(tags = "管理后台 - 业务字典分类")
@RestController
@RequestMapping("/qg/sys/dict-tree")
@Validated
public class QgDictTreeController {

    @Resource
    private QgDictTreeService qgDictTreeService;

    @GetMapping("/list-all")
    @ApiOperation(value = "获取所有分类信息", notes = "主要用于前端的下拉选项")
    public CommonResult<List<QgDictTreeRespVO>> getKindListAll() {
        List<QgDictTreeDO> list = qgDictTreeService.getListTop(new QgDictTreeKindTopVO());
        // 返回给前端
        return success(QgDictTreeConvert.INSTANCE.convertList(list));
    }

    @PostMapping("/create")
    @ApiOperation("创建产品分类")
    @PreAuthorize("@ss.hasPermission('edu:QgDict-tree:create')")
    public CommonResult<Long> createQgDictTree(@Valid @RequestBody QgDictTreeCreateReqVO createReqVO) {
        return success(qgDictTreeService.createQgDictTree(createReqVO));
    }

    @PutMapping("/update")
    @ApiOperation("更新产品分类")
    @PreAuthorize("@ss.hasPermission('edu:QgDict-tree:update')")
    public CommonResult<Boolean> updateQgDictTree(@Valid @RequestBody QgDictTreeUpdateReqVO updateReqVO) {
        qgDictTreeService.updateQgDictTree(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @ApiOperation("删除产品分类")
    @ApiImplicitParam(name = "id", value = "编号", required = true, dataTypeClass = Long.class)
    @PreAuthorize("@ss.hasPermission('edu:QgDict-tree:delete')")
    public CommonResult<Boolean> deleteQgDictTree(@RequestParam("id") Long id) {
        qgDictTreeService.deleteQgDictTree(id);
        return success(true);
    }

    @GetMapping("/get")
    @ApiOperation("获得产品分类")
    @ApiImplicitParam(name = "id", value = "编号", required = true, example = "1024", dataTypeClass = Long.class)
    @PreAuthorize("@ss.hasPermission('edu:QgDict-tree:query')")
    public CommonResult<QgDictTreeRespVO> getQgDictTree(@RequestParam("id") Long id) {
        QgDictTreeDO qgDictTree = qgDictTreeService.getQgDictTree(id);
        return success(QgDictTreeConvert.INSTANCE.convert(qgDictTree));
    }

    @GetMapping("/list")
    @ApiOperation("获得产品分类列表")
    @ApiImplicitParam(name = "ids", value = "编号列表", required = true, example = "1024,2048", dataTypeClass = List.class)
    @PreAuthorize("@ss.hasPermission('edu:QgDict-tree:query')")
    public CommonResult<List<QgDictTreeRespVO>> getQgDictTreeList(@RequestParam("ids") Collection<Long> ids) {
        List<QgDictTreeDO> list = qgDictTreeService.getQgDictTreeList(ids);
        return success(QgDictTreeConvert.INSTANCE.convertList(list));
    }

    @GetMapping("/listByType")
    @ApiOperation("按类型获得产品分类列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "parentId", value = "父节点ID",  example = "2", dataTypeClass = Long.class)
    })
    @PreAuthorize("@ss.hasPermission('edu:QgDict-tree:query')")
    public CommonResult<List<QgDictTreeRespVO>> getQgDictTreeListByType(@RequestParam(value = "parentId")Long parentId) {
        List<QgDictTreeDO> list = qgDictTreeService.getQgDictTreeListByType(parentId);
        return success(QgDictTreeConvert.INSTANCE.convertList(list));
    }

    @GetMapping("/page")
    @ApiOperation("获得产品分类分页")
    @PreAuthorize("@ss.hasPermission('edu:QgDict-tree:query')")
    public CommonResult<PageResult<QgDictTreeRespVO>> getQgDictTreePage(@Valid QgDictTreePageReqVO pageVO) {
        PageResult<QgDictTreeDO> pageResult = qgDictTreeService.getQgDictTreePage(pageVO);
        return success(QgDictTreeConvert.INSTANCE.convertPage(pageResult));
    }

    @GetMapping("/export-excel")
    @ApiOperation("导出产品分类 Excel")
    @PreAuthorize("@ss.hasPermission('edu:QgDict-tree:export')")
    @OperateLog(type = EXPORT)
    public void exportQgDictTreeExcel(@Valid QgDictTreeExportReqVO exportReqVO,
              HttpServletResponse response) throws IOException {
        List<QgDictTreeDO> list = qgDictTreeService.getQgDictTreeList(exportReqVO);
        // 导出 Excel
        List<QgDictTreeExcelVO> datas = QgDictTreeConvert.INSTANCE.convertList02(list);
        ExcelUtils.write(response, "产品分类.xls", "数据", QgDictTreeExcelVO.class, datas);
    }

}
