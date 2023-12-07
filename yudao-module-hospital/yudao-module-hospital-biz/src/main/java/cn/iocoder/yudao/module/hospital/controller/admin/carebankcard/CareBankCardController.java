package cn.iocoder.yudao.module.hospital.controller.admin.carebankcard;

import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Operation;

import javax.validation.constraints.*;
import javax.validation.*;
import javax.servlet.http.*;
import java.util.*;
import java.io.IOException;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

import cn.iocoder.yudao.framework.excel.core.util.ExcelUtils;

import cn.iocoder.yudao.framework.operatelog.core.annotations.OperateLog;
import static cn.iocoder.yudao.framework.operatelog.core.enums.OperateTypeEnum.*;

import cn.iocoder.yudao.module.hospital.controller.admin.carebankcard.vo.*;
import cn.iocoder.yudao.module.hospital.dal.dataobject.carebankcard.CareBankCardDO;
import cn.iocoder.yudao.module.hospital.convert.carebankcard.CareBankCardConvert;
import cn.iocoder.yudao.module.hospital.service.carebankcard.CareBankCardService;

@Tag(name = "管理后台 - 提现银行卡")
@RestController
@RequestMapping("/hospital/care-bank-card")
@Validated
public class CareBankCardController {

    @Resource
    private CareBankCardService careBankCardService;

    @PostMapping("/create")
    @Operation(summary = "创建提现银行卡")
    @PreAuthorize("@ss.hasPermission('hospital:care-bank-card:create')")
    public CommonResult<Long> createCareBankCard(@Valid @RequestBody CareBankCardCreateReqVO createReqVO) {
        return success(careBankCardService.createCareBankCard(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新提现银行卡")
    @PreAuthorize("@ss.hasPermission('hospital:care-bank-card:update')")
    public CommonResult<Boolean> updateCareBankCard(@Valid @RequestBody CareBankCardUpdateReqVO updateReqVO) {
        careBankCardService.updateCareBankCard(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除提现银行卡")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('hospital:care-bank-card:delete')")
    public CommonResult<Boolean> deleteCareBankCard(@RequestParam("id") Long id) {
        careBankCardService.deleteCareBankCard(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得提现银行卡")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('hospital:care-bank-card:query')")
    public CommonResult<CareBankCardRespVO> getCareBankCard(@RequestParam("id") Long id) {
        CareBankCardDO careBankCard = careBankCardService.getCareBankCard(id);
        return success(CareBankCardConvert.INSTANCE.convert(careBankCard));
    }

    @GetMapping("/list")
    @Operation(summary = "获得提现银行卡列表")
    @Parameter(name = "ids", description = "编号列表", required = true, example = "1024,2048")
    @PreAuthorize("@ss.hasPermission('hospital:care-bank-card:query')")
    public CommonResult<List<CareBankCardRespVO>> getCareBankCardList(@RequestParam("ids") Collection<Long> ids) {
        List<CareBankCardDO> list = careBankCardService.getCareBankCardList(ids);
        return success(CareBankCardConvert.INSTANCE.convertList(list));
    }

    @GetMapping("/page")
    @Operation(summary = "获得提现银行卡分页")
    @PreAuthorize("@ss.hasPermission('hospital:care-bank-card:query')")
    public CommonResult<PageResult<CareBankCardRespVO>> getCareBankCardPage(@Valid CareBankCardPageReqVO pageVO) {
        PageResult<CareBankCardDO> pageResult = careBankCardService.getCareBankCardPage(pageVO);
        return success(CareBankCardConvert.INSTANCE.convertPage(pageResult));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出提现银行卡 Excel")
    @PreAuthorize("@ss.hasPermission('hospital:care-bank-card:export')")
    @OperateLog(type = EXPORT)
    public void exportCareBankCardExcel(@Valid CareBankCardExportReqVO exportReqVO,
              HttpServletResponse response) throws IOException {
        List<CareBankCardDO> list = careBankCardService.getCareBankCardList(exportReqVO);
        // 导出 Excel
        List<CareBankCardExcelVO> datas = CareBankCardConvert.INSTANCE.convertList02(list);
        ExcelUtils.write(response, "提现银行卡.xls", "数据", CareBankCardExcelVO.class, datas);
    }

    /**
     * 根据用户编号查询该用户下的银行卡信息
     */
    @GetMapping("/queryByCareId/list")
    @Operation(summary = "获得用户银行卡列表")
    @Parameter(name = "careId", description = "用户编号", required = true)
    @PreAuthorize("@ss.hasPermission('hospital:care-bank-card:query')")
    public CommonResult<List<CareBankCardRespVO>> getCareBankCardByCareId(@RequestParam("careId") Long careId) {
        List<CareBankCardDO> list = careBankCardService.getCareBankCardByCareId(careId);
        return success(CareBankCardConvert.INSTANCE.convertList(list));
    }

}
