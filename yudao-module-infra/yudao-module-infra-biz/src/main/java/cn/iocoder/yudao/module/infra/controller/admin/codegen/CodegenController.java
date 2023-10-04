package cn.iocoder.yudao.module.infra.controller.admin.codegen;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.ZipUtil;
import cn.hutool.extra.spring.SpringUtil;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.servlet.ServletUtils;
import cn.iocoder.yudao.module.infra.controller.admin.codegen.vo.CodegenCreateListReqVO;
import cn.iocoder.yudao.module.infra.controller.admin.codegen.vo.CodegenDetailRespVO;
import cn.iocoder.yudao.module.infra.controller.admin.codegen.vo.CodegenPreviewRespVO;
import cn.iocoder.yudao.module.infra.controller.admin.codegen.vo.CodegenUpdateReqVO;
import cn.iocoder.yudao.module.infra.controller.admin.codegen.vo.table.CodegenTablePageReqVO;
import cn.iocoder.yudao.module.infra.controller.admin.codegen.vo.table.CodegenTableRespVO;
import cn.iocoder.yudao.module.infra.controller.admin.codegen.vo.table.DatabaseTableRespVO;
import cn.iocoder.yudao.module.infra.convert.codegen.CodegenConvert;
import cn.iocoder.yudao.module.infra.dal.dataobject.codegen.CodegenColumnDO;
import cn.iocoder.yudao.module.infra.dal.dataobject.codegen.CodegenTableDO;
import cn.iocoder.yudao.module.infra.service.codegen.CodegenService;
import com.alibaba.druid.util.JdbcUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import org.apache.ibatis.jdbc.ScriptRunner;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.util.InMemoryResource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import javax.validation.Valid;
import java.io.*;
import java.sql.SQLException;
import java.util.*;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;
import static cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils.getLoginUserId;

@RestController
@RequestMapping("/infra/codegen")
@Validated
public class CodegenController {

    @Resource
    private CodegenService codegenService;

    @Value("${yudao.codegen.local.path:D:\\d\\git\\ruoyi-vue-pro\\yudao-module-yjgl\\}")
    private String path;

    @GetMapping("/db/table/list")
    @Operation(summary = "获得数据库自带的表定义列表", description = "会过滤掉已经导入 Codegen 的表")
    @Parameters({
            @Parameter(name = "dataSourceConfigId", description = "数据源配置的编号", required = true, example = "1"),
            @Parameter(name = "name", description = "表名，模糊匹配", example = "yudao"),
            @Parameter(name = "comment", description = "描述，模糊匹配", example = "芋道")
    })
    @PreAuthorize("@ss.hasPermission('infra:codegen:query')")
    public CommonResult<List<DatabaseTableRespVO>> getDatabaseTableList(
            @RequestParam(value = "dataSourceConfigId") Long dataSourceConfigId,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "comment", required = false) String comment) {
        return success(codegenService.getDatabaseTableList(dataSourceConfigId, name, comment));
    }

    @GetMapping("/table/page")
    @Operation(summary = "获得表定义分页")
    @PreAuthorize("@ss.hasPermission('infra:codegen:query')")
    public CommonResult<PageResult<CodegenTableRespVO>> getCodeGenTablePage(@Valid CodegenTablePageReqVO pageReqVO) {
        PageResult<CodegenTableDO> pageResult = codegenService.getCodegenTablePage(pageReqVO);
        return success(CodegenConvert.INSTANCE.convertPage(pageResult));
    }

    @GetMapping("/detail")
    @Operation(summary = "获得表和字段的明细")
    @Parameter(name = "tableId", description = "表编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('infra:codegen:query')")
    public CommonResult<CodegenDetailRespVO> getCodegenDetail(@RequestParam("tableId") Long tableId) {
        CodegenTableDO table = codegenService.getCodegenTablePage(tableId);
        List<CodegenColumnDO> columns = codegenService.getCodegenColumnListByTableId(tableId);
        // 拼装返回
        return success(CodegenConvert.INSTANCE.convert(table, columns));
    }

    @Operation(summary = "基于数据库的表结构，创建代码生成器的表和字段定义")
    @PostMapping("/create-list")
    @PreAuthorize("@ss.hasPermission('infra:codegen:create')")
    public CommonResult<List<Long>> createCodegenList(@Valid @RequestBody CodegenCreateListReqVO reqVO) {
        // todo 需要同时能够生成数据字典
        return success(codegenService.createCodegenList(getLoginUserId(), reqVO));
    }

    @Operation(summary = "生成代码到数据库，同时将代码安装到本地项目作为源码")
    @PostMapping("/genAndInstall")
    @PreAuthorize("@ss.hasPermission('infra:codegen:create')")
    public CommonResult<List<Long>> genAndInstall(@Valid @RequestBody CodegenCreateListReqVO reqVO) throws IOException {
        // todo 需要同时能够生成数据字典
        List<Long> codegenList = codegenService.createCodegenList(getLoginUserId(), reqVO);
        String ids = StrUtil.join(",", codegenList);

        // todo 需要考虑 数据库超时问题。 因为 大量的表的同时的操作可能会导致耗时很长。
        install(ids, null);
        return success(codegenList);
    }

    @Operation(summary = "更新数据库的表和字段定义")
    @PutMapping("/update")
    @PreAuthorize("@ss.hasPermission('infra:codegen:update')")
    public CommonResult<Boolean> updateCodegen(@Valid @RequestBody CodegenUpdateReqVO updateReqVO) {
        // todo 需要同时能够 更新 数据字典
        codegenService.updateCodegen(updateReqVO);
        return success(true);
    }

    @Operation(summary = "基于数据库的表结构和字段描述信息，对其中数据字典类型的字段，生成对应数据字典")
    @PutMapping("/gen-dict")
    @Parameter(name = "tableId", description = "表编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('infra:codegen:update')")
    public CommonResult<List<String>> genDict(@RequestParam("tableId") Long tableId) {
        List<String> ret = codegenService.genDict(tableId);
        return success(ret);
    }

    @Operation(summary = "基于数据库的表结构，同步数据库的表和字段定义")
    @PutMapping("/sync-from-db")
    @Parameter(name = "tableId", description = "表编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('infra:codegen:update')")
    public CommonResult<Boolean> syncCodegenFromDB(@RequestParam("tableId") Long tableId) {
        codegenService.syncCodegenFromDB(tableId);
        return success(true);
    }

    @Operation(summary = "删除数据库的表和字段定义")
    @DeleteMapping("/delete")
    @Parameter(name = "tableId", description = "表编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('infra:codegen:delete')")
    public CommonResult<Boolean> deleteCodegen(@RequestParam("tableId") Long tableId) {
        codegenService.deleteCodegen(tableId);
        return success(true);
    }

    @Operation(summary = "预览生成代码")
    @GetMapping("/preview")
    @Parameter(name = "tableId", description = "表编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('infra:codegen:preview')")
    public CommonResult<List<CodegenPreviewRespVO>> previewCodegen(@RequestParam("tableId") Long tableId) {
        Map<String, String> codes = codegenService.generationCodes(tableId);
        return success(CodegenConvert.INSTANCE.convert(codes));
    }

    @Operation(summary = "生成代码到本地项目中")
    @GetMapping("/install")
    @Parameter(name = "tableId", description = "表编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('infra:codegen:download')")
    public void install(@RequestParam("tableId") String tableId,
                                HttpServletResponse response) throws IOException {
        String[] tableIds = tableId.split(",");
        ArrayList<String> pathList = new ArrayList<>();
        ArrayList<ByteArrayInputStream> insList = new ArrayList<>();
        for (String id : tableIds) {
            // 生成代码
//            if (Objects.nonNull(path)) {
//                CodegenEngine.path.set(path);
//            }

            Map<String, String> codes = codegenService.generationCodes(Long.valueOf(id));
            // 如果pathStr有值就自动填充
            if (Objects.nonNull(path)) {
                fillFile(codes);
            }
//            codes.keySet().stream().filter(s -> s.contains("ErrorCodeConstants_手动操作")).collect(Collectors.toList()).forEach(codes::remove);
            String[] paths = codes.keySet().toArray(new String[0]);
            pathList.addAll(Arrays.asList(paths));
            ByteArrayInputStream[] ins = codes.values().stream().map(IoUtil::toUtf8Stream).toArray(ByteArrayInputStream[]::new);
            insList.addAll(Arrays.asList(ins));
        }
        if (response != null) {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ZipUtil.zip(outputStream, pathList.toArray(new String[pathList.size()]), insList.toArray(new ByteArrayInputStream[insList.size()]));
            // 输出
            ServletUtils.writeAttachment(response, "codegen.zip", outputStream.toByteArray());
        }

    }


    @Operation(summary = "下载生成代码")
    @GetMapping("/download")
    @Parameter(name = "tableId", description = "表编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('infra:codegen:download')")
    public void downloadCodegen(@RequestParam("tableId") Long tableId,
                                HttpServletResponse response) throws IOException {
        // 生成代码
        Map<String, String> codes = codegenService.generationCodes(tableId);
        // 构建 zip 包
        String[] paths = codes.keySet().toArray(new String[0]);
        ByteArrayInputStream[] ins = codes.values().stream().map(IoUtil::toUtf8Stream).toArray(ByteArrayInputStream[]::new);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ZipUtil.zip(outputStream, paths, ins);
        // 输出
        ServletUtils.writeAttachment(response, "codegen.zip", outputStream.toByteArray());
    }

    private void fillFile(Map<String, String> codes) {
        List<String> nonPath = Arrays.asList("yudao-ui-admin", "yudao-ui-admin-vue3", "sql");
        // 构建 zip 包
        codes.forEach((path, code) -> {
            String filePath = "" + path;
            File ff = new File(filePath);
            if (ff.exists()) {
                System.out.println("已经存在， 跳过： "+ff);
                return;
            }

            try {
                for (String s : nonPath) {
                    if (path.startsWith(s)) {
                        if (path.startsWith("yudao-ui-admin-vue3")) {
                            filePath = "D:\\d\\git\\sefl\\" + path;
                            File ff2 = new File(filePath);
                            if (ff2.exists()) {
                                System.out.println("已经存在， 跳过： "+ff);
//                                return;
                            }
                            break;
                        } else if (path.endsWith("sql.sql")) {
                            // ${table.parentMenuId} == 2303 数据采集
                            // ${table.parentMenuId} == 2304 数据浏览
                            code = code.replace("${table.parentMenuId}", "2304");

                            DataSource ds = SpringUtil.getBean(DataSource.class);
//                            cn.hutool.core.io.resource.NoResourceException: Default db setting [config/db.setting] or [db.setting] in classpath not found !
//                            DataSource ds = DbUtil.getDs();
//                    SqlConnRunner sqlConnRunner = DbUtil.newSqlConnRunner(ds);
//                    sqlConnRunner.find()
//                            String sql = FileUtil.readString(ff, StandardCharsets.UTF_8);
                            org.springframework.core.io.Resource classPathResource = new InMemoryResource(code);
//                            org.springframework.core.io.Resource classPathResource = new ByteArrayResource(filePath.getBytes());
//                            org.springframework.core.io.Resource classPathResource = new FileSystemResource(filePath);
                            try {
                                int i = code.indexOf("'");
                                String menuName = code.substring(i+1, code.indexOf("'", i+1));
                                String menuNameSql = "select  s.id from system_menu as s where  s.name = '" + menuName + "'";
                                ScriptRunner scriptRunner = new ScriptRunner(ds.getConnection());
//                                ScriptRunner scriptRunner = new ScriptRunner();
                                List<Map<String, Object>> maps = JdbcUtils.executeQuery(ds, menuNameSql);
                                System.out.println("maps = " + maps); // maps = [{id=2546}]
//                                ScriptUtils.
                                if (maps == null || maps.isEmpty()) {
                                    System.out.println("sql.sql : \n " + code);
                                    ScriptUtils.executeSqlScript(ds.getConnection(), classPathResource);
//                                    return;
                                } else {
                                    System.out.println("已经存在菜单， 跳过： " + code);
                                }
                            } catch (SQLException e) {
                                throw new RuntimeException(e);
                            }
                            System.out.println("sql 执行成功， ~~： "+ 000);
                            return;
                        } else {
                            return;
                        }
                    }
                }

                if (path.contains("ErrorCodeConstants")) {
                    String pathUrl = path.split("/")[path.split("/").length - 1];
                    String errorEnumPath = filePath.replace(pathUrl, "ErrorCodeConstants.java");
                    File directory = new File(errorEnumPath);
                    if (!directory.getParentFile().canExecute()) {
                        directory.getParentFile().mkdirs();
                    }
                    String allStr = "";
                    if (directory.exists()) {
                        BufferedReader bufferedReader = new BufferedReader(new FileReader(directory));
                        String st;
                        while ((st = bufferedReader.readLine()) != null) allStr = allStr + st + "\n";
                        bufferedReader.close();

//                        ErrorCode IMPORTANT_HYD_INVESTIGATE_NOT_EXISTS = new ErrorCode
//
                        if(allStr.contains(code.split(",")[1].trim())) {
                            System.out.println("已经存在， 跳过： "+ code);
                            return;
                        }
                    } else {
                        allStr = "package cn.iocoder.yudao.module.infra.enums;\n" +
                                "\n" +
                                "import cn.iocoder.yudao.framework.common.exception.ErrorCode;\n" +
                                "\n" +
                                "/**\n" +
                                " * Infra 错误码枚举类\n" +
                                " *\n" +
                                " * infra 系统，使用 1-001-000-000 段\n" +
                                " */\n" +
                                "public interface ErrorCodeConstants {\n" +
                                "\n" +
                                "}\n";
                    }
                    allStr = allStr.replace("}", code + "\n}");
                    BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(directory));
                    bufferedWriter.write(allStr);
                    bufferedWriter.flush();
                    bufferedWriter.close();
                    return;
                }
                File directory = new File(filePath);
                if (!directory.getParentFile().canExecute()) {
                    directory.getParentFile().mkdirs();
                }
                BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(directory));
                bufferedWriter.write(code);
                bufferedWriter.flush();
                bufferedWriter.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

}
