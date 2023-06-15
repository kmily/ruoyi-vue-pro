package cn.iocoder.yudao.module.oa.controller.app.borrow;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.security.core.annotations.PreAuthenticated;
import cn.iocoder.yudao.module.oa.controller.admin.borrow.vo.BorrowCreateReqVO;
import cn.iocoder.yudao.module.oa.controller.admin.borrow.vo.BorrowPageReqVO;
import cn.iocoder.yudao.module.oa.controller.admin.borrow.vo.BorrowRespVO;
import cn.iocoder.yudao.module.oa.controller.admin.borrow.vo.BorrowUpdateReqVO;
import cn.iocoder.yudao.module.oa.convert.borrow.BorrowConvert;
import cn.iocoder.yudao.module.oa.dal.dataobject.borrow.BorrowDO;
import cn.iocoder.yudao.module.oa.service.borrow.BorrowService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.Collection;
import java.util.List;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "用户App - 借支申请")
@RestController
@RequestMapping("/oa/borrow")
@Validated
public class AppBorrowController {

    @Resource
    private BorrowService borrowService;

    @PostMapping("/create")
    @Operation(summary = "创建借支申请")
    @PreAuthenticated
    public CommonResult<Long> createBorrow(@Valid @RequestBody BorrowCreateReqVO createReqVO) {
        return success(borrowService.createBorrow(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新借支申请")
    @PreAuthenticated
    public CommonResult<Boolean> updateBorrow(@Valid @RequestBody BorrowUpdateReqVO updateReqVO) {
        borrowService.updateBorrow(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除借支申请")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthenticated
    public CommonResult<Boolean> deleteBorrow(@RequestParam("id") Long id) {
        borrowService.deleteBorrow(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得借支申请")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthenticated
    public CommonResult<BorrowRespVO> getBorrow(@RequestParam("id") Long id) {
        BorrowDO borrow = borrowService.getBorrow(id);
        return success(BorrowConvert.INSTANCE.convert(borrow));
    }

    @GetMapping("/list")
    @Operation(summary = "获得借支申请列表")
    @Parameter(name = "ids", description = "编号列表", required = true, example = "1024,2048")
    @PreAuthenticated
    public CommonResult<List<BorrowRespVO>> getBorrowList(@RequestParam("ids") Collection<Long> ids) {
        List<BorrowDO> list = borrowService.getBorrowList(ids);
        return success(BorrowConvert.INSTANCE.convertList(list));
    }

    @GetMapping("/page")
    @Operation(summary = "获得借支申请分页")
    @PreAuthenticated
    public CommonResult<PageResult<BorrowRespVO>> getBorrowPage(@Valid BorrowPageReqVO pageVO) {
        PageResult<BorrowDO> pageResult = borrowService.getBorrowPage(pageVO);
        return success(BorrowConvert.INSTANCE.convertPage(pageResult));
    }

}
