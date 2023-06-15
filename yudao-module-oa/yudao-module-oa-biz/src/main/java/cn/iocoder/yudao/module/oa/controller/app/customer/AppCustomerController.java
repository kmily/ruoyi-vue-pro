package cn.iocoder.yudao.module.oa.controller.app.customer;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.excel.core.util.ExcelUtils;
import cn.iocoder.yudao.framework.operatelog.core.annotations.OperateLog;
import cn.iocoder.yudao.framework.security.core.annotations.PreAuthenticated;
import cn.iocoder.yudao.module.oa.controller.admin.customer.vo.*;
import cn.iocoder.yudao.module.oa.convert.customer.CustomerConvert;
import cn.iocoder.yudao.module.oa.dal.dataobject.customer.CustomerDO;
import cn.iocoder.yudao.module.oa.service.customer.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
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

@Tag(name = "用户App - 客户管理")
@RestController
@RequestMapping("/oa/customer")
@Validated
public class AppCustomerController {

    @Resource
    private CustomerService customerService;

    @PostMapping("/create")
    @Operation(summary = "创建客户管理")
    @PreAuthenticated
    public CommonResult<Long> createCustomer(@Valid @RequestBody CustomerCreateReqVO createReqVO) {
        return success(customerService.createCustomer(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新客户管理")
    @PreAuthenticated
    public CommonResult<Boolean> updateCustomer(@Valid @RequestBody CustomerUpdateReqVO updateReqVO) {
        customerService.updateCustomer(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除客户管理")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthenticated
    public CommonResult<Boolean> deleteCustomer(@RequestParam("id") Long id) {
        customerService.deleteCustomer(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得客户管理")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthenticated
    public CommonResult<CustomerRespVO> getCustomer(@RequestParam("id") Long id) {
        CustomerDO customer = customerService.getCustomer(id);
        return success(CustomerConvert.INSTANCE.convert(customer));
    }

    @GetMapping("/list")
    @Operation(summary = "获得客户管理列表")
    @Parameter(name = "ids", description = "编号列表", required = true, example = "1024,2048")
    @PreAuthenticated
    public CommonResult<List<CustomerRespVO>> getCustomerList(@RequestParam("ids") Collection<Long> ids) {
        List<CustomerDO> list = customerService.getCustomerList(ids);
        return success(CustomerConvert.INSTANCE.convertList(list));
    }

    @GetMapping("/getByName")
    @Operation(summary = "通过名字获得客户列表")
    @PreAuthenticated
    public CommonResult<List<CustomerRespVO>> getCustomerByName(@RequestParam("name")String name){
        List<CustomerDO> list = customerService.getCustomerByName(name);
        return success(CustomerConvert.INSTANCE.convertList(list));
    }

    @GetMapping("/page")
    @Operation(summary = "获得客户管理分页")
    @PreAuthenticated
    public CommonResult<PageResult<CustomerRespVO>> getCustomerPage(@Valid CustomerPageReqVO pageVO) {
        PageResult<CustomerDO> pageResult = customerService.getCustomerPage(pageVO);
        return success(CustomerConvert.INSTANCE.convertPage(pageResult));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出客户管理 Excel")
    @OperateLog(type = EXPORT)
    @PreAuthenticated
    public void exportCustomerExcel(@Valid CustomerExportReqVO exportReqVO,
              HttpServletResponse response) throws IOException {
        List<CustomerDO> list = customerService.getCustomerList(exportReqVO);
        // 导出 Excel
        List<CustomerExcelVO> datas = CustomerConvert.INSTANCE.convertList02(list);
        ExcelUtils.write(response, "客户管理.xls", "数据", CustomerExcelVO.class, datas);
    }

}
