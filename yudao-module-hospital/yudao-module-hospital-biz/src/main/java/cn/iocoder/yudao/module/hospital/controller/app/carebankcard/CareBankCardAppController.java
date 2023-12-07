package cn.iocoder.yudao.module.hospital.controller.app.carebankcard;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.excel.core.util.ExcelUtils;
import cn.iocoder.yudao.framework.operatelog.core.annotations.OperateLog;
import cn.iocoder.yudao.framework.security.core.annotations.PreAuthenticated;
import cn.iocoder.yudao.module.hospital.controller.admin.carebankcard.vo.*;
import cn.iocoder.yudao.module.hospital.controller.app.carebankcard.vo.CareBankCardAppCreateReqVO;
import cn.iocoder.yudao.module.hospital.convert.carebankcard.CareBankCardConvert;
import cn.iocoder.yudao.module.hospital.dal.dataobject.carebankcard.CareBankCardDO;
import cn.iocoder.yudao.module.hospital.service.carebankcard.CareBankCardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
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

@Tag(name = "用户APP - 提现银行卡")
@RestController
@RequestMapping("/hospital/care-bank-card")
@Validated
public class CareBankCardAppController {

    @Resource
    private CareBankCardService careBankCardService;

    /**
     * 2023-12-06 根据app端修改的创建方法
     */
    @PostMapping("/create")
    @Operation(summary = "创建提现银行卡")
    @PreAuthenticated
    public CommonResult<Long> createCareBankCard(@Valid @RequestBody CareBankCardAppCreateReqVO createAppReqVO) {
        return success(careBankCardService.createCareBankCardByApp(createAppReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新提现银行卡")
    @PreAuthenticated
    public CommonResult<Boolean> updateCareBankCard(@Valid @RequestBody CareBankCardUpdateReqVO updateReqVO) {
        careBankCardService.updateCareBankCard(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除提现银行卡")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthenticated
    public CommonResult<Boolean> deleteCareBankCard(@RequestParam("id") Long id) {
        careBankCardService.deleteCareBankCard(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得提现银行卡")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthenticated
    public CommonResult<CareBankCardRespVO> getCareBankCard(@RequestParam("id") Long id) {
        CareBankCardDO careBankCard = careBankCardService.getCareBankCard(id);
        return success(CareBankCardConvert.INSTANCE.convert(careBankCard));
    }

    /**
     * 修改为根据careId获取对应的银行卡列表
     * @param careId 用户编号
     * @return
     */
    @GetMapping("/list")
    @Operation(summary = "获得提现银行卡列表")
    @Parameter(name = "careId", description = "用户编号", required = true)
    @PreAuthenticated
    public CommonResult<List<CareBankCardRespVO>> getCareBankCardByCareId(@RequestParam("careId") Long careId) {
        List<CareBankCardDO> list = careBankCardService.getCareBankCardByCareId(careId);
        return success(CareBankCardConvert.INSTANCE.convertList(list));
    }

    @GetMapping("/page")
    @Operation(summary = "获得提现银行卡分页")
    @PreAuthenticated
    public CommonResult<PageResult<CareBankCardRespVO>> getCareBankCardPage(@Valid CareBankCardPageReqVO pageVO) {
        PageResult<CareBankCardDO> pageResult = careBankCardService.getCareBankCardPage(pageVO);
        return success(CareBankCardConvert.INSTANCE.convertPage(pageResult));
    }


}
