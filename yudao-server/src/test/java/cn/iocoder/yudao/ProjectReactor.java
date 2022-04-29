package cn.iocoder.yudao;

import cn.hutool.core.io.FileTypeUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import cn.iocoder.yudao.framework.common.util.collection.SetUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.stream.Collectors;

import static java.io.File.separator;

/**
 * 项目修改器，一键替换 Maven 的 groupId、artifactId，项目的 package 等
 * 通过修改 groupIdNew、artifactIdNew、projectBaseDirNew 三个变量
 *
 * @author 芋道源码
 */
@Slf4j
public class ProjectReactor {

    /**
     * 定义待修改的基础信息
     */
    private static final String GROUP_ID = "cn.iocoder.boot";
    private static final String ARTIFACT_ID = "yudao";
    private static final String PACKAGE_NAME = "cn.iocoder.yudao";
    private static final String TITLE = "芋道管理系统";

    /**
     * 白名单文件，不进行重写，避免出问题
     */
    private static final Set<String> WHITE_FILE_TYPES = SetUtils.asSet("gif", "jpg", "svg", "png", // 图片
            "eot", "woff2", "ttf", "woff"); // 字体

    /**
     * 主方法
     *
     * @param args
     */
    public static void main(String[] args) {
        // 开始执行使劲按
        long start = System.currentTimeMillis();

        // 原项目路径
        String projectBaseDir = getProjectBaseDir();
        log.info("[main][原项目路劲改地址 ({})]", projectBaseDir);

        // ========== 配置，需要你手动修改 ==========
        String groupIdNew = "cn.star.gg";
        String artifactIdNew = "star";
        String packageNameNew = "cn.start.pp";
        String titleNew = "土豆管理系统";

        // 一键改名后，“新”项目所在的目录
        String projectBaseDirNew = projectBaseDir + "-new";
        log.info("[main][新项目路径地址 ({})]", projectBaseDirNew);

        // 获得需要复制的文件
        log.info("[main][开始获得需要重写的文件，预计需要 10-20 秒]");
        Collection<File> files = listFiles(projectBaseDir);
        log.info("[main][需要重写的文件数量：{}，预计需要 15-30 秒]", files.size());

        // 写入文件
        files.forEach(file -> {
            // 如果是白名单的文件类型，不进行重写，直接拷贝
            String fileType = FileTypeUtil.getType(file);
            if (WHITE_FILE_TYPES.contains(fileType)) {
                // 复制文件
                copyFile(file, projectBaseDir, projectBaseDirNew, packageNameNew, artifactIdNew);
                return;
            }
            // 如果非白名单的文件类型，重写内容，在生成文件
            String content = replaceFileContent(file, groupIdNew, artifactIdNew, packageNameNew, titleNew);
            // 写文件
            writeFile(file, content, projectBaseDir, projectBaseDirNew, packageNameNew, artifactIdNew);
        });
        log.info("[main][重写完成]共耗时：{} 秒", (System.currentTimeMillis() - start) / 1000);
    }

    /**
     * 获得项目基础路径
     *
     * @return 项目基础路径
     */
    private static String getProjectBaseDir() {
        String baseDir = System.getProperty("user.dir");
        if (StrUtil.isEmpty(baseDir)) {
            throw new NullPointerException("项目基础路径不存在");
        }
        return baseDir;
    }

    /**
     * 获取有效文件列表
     *
     * @param projectBaseDir 基础路径
     * @return 有效文件列表
     */
    private static Collection<File> listFiles(String projectBaseDir) {
        Collection<File> files = FileUtils.listFiles(new File(projectBaseDir), null, true);
        // 移除 IDEA、Git 自身的文件、Node 编译出来的文件
        files = files.stream()
                .filter(file -> !file.getPath().contains(separator + "target" + separator)
                        && !file.getPath().contains(separator + "node_modules" + separator)
                        && !file.getPath().contains(separator + ".idea" + separator)
                        && !file.getPath().contains(separator + ".git" + separator)
                        && !file.getPath().contains(separator + "dist" + separator)
                        && !file.getPath().contains(".iml")
                        && !file.getPath().contains(".html.gz"))
                .collect(Collectors.toList());
        return files;
    }

    /**
     * 文件内容替换
     *
     * @param file           文件
     * @param groupIdNew     需要替换的groupId
     * @param artifactIdNew  需要替换的artifactId
     * @param packageNameNew 需要替换的包名
     * @param titleNew       需要替换的title
     * @return 替换后的内容
     */
    private static String replaceFileContent(File file, String groupIdNew,
                                             String artifactIdNew, String packageNameNew,
                                             String titleNew) {
        // 读取文件，设置文件编码
        String content = FileUtil.readString(file, StandardCharsets.UTF_8);

        // 如果是白名单的文件类型，不进行重写
        String fileType = FileTypeUtil.getType(file);
        if (WHITE_FILE_TYPES.contains(fileType)) {
            return content;
        }
        // 执行文件内容都重写
        return content.replaceAll(GROUP_ID, groupIdNew)
                // 基础替换
                .replaceAll(PACKAGE_NAME, packageNameNew)
                // 必须放在最后替换，因为 ARTIFACT_ID 太短！
                .replaceAll(ARTIFACT_ID, artifactIdNew)
                // StrUtil.upperFirst(ARTIFACT_ID)是首字母变大写；
                .replaceAll(StrUtil.upperFirst(ARTIFACT_ID), StrUtil.upperFirst(artifactIdNew))
                .replaceAll(TITLE, titleNew)
                // 网址还原,否则一键改包后，有些网址打不开了
                .replaceAll(artifactIdNew + ".iocoder.cn", "yudao.iocoder.cn");
    }

    /**
     * 写文件
     *
     * @param file              文件
     * @param fileContent       文件内容
     * @param projectBaseDir    原基础目录
     * @param projectBaseDirNew 新projectBaseDir
     * @param packageNameNew    新packageName
     * @param artifactIdNew     新artifactId
     */
    private static void writeFile(File file, String fileContent, String projectBaseDir,
                                  String projectBaseDirNew, String packageNameNew, String artifactIdNew) {
        // 得到新的构建文件路径
        String newPath = buildNewFilePath(file, projectBaseDir, projectBaseDirNew, packageNameNew, artifactIdNew);
        // 写入最终的文件
        FileUtil.writeUtf8String(fileContent, newPath);
    }

    /**
     * 复制文件
     *
     * @param file              文件
     * @param projectBaseDir    原项目基础目录
     * @param projectBaseDirNew 新项目目录
     * @param packageNameNew    新packageName
     * @param artifactIdNew     新artifactId
     */
    private static void copyFile(File file, String projectBaseDir,
                                 String projectBaseDirNew, String packageNameNew, String artifactIdNew) {
        // 构建新的文件路径
        String newPath = buildNewFilePath(file, projectBaseDir, projectBaseDirNew, packageNameNew, artifactIdNew);
        // 复制文件
        FileUtil.copyFile(file, new File(newPath));
    }

    /**
     * 构建新的文件路径
     *
     * @param file              文件
     * @param projectBaseDir    原项目基础目录
     * @param projectBaseDirNew 新项目目录
     * @param packageNameNew    新packageName
     * @param artifactIdNew     新artifactId
     * @return 新的文件路径
     */
    private static String buildNewFilePath(File file, String projectBaseDir,
                                           String projectBaseDirNew, String packageNameNew, String artifactIdNew) {
        return file.getPath().replace(projectBaseDir, projectBaseDirNew) // 新目录
                .replace(PACKAGE_NAME.replaceAll("\\.", Matcher.quoteReplacement(separator)),
                        packageNameNew.replaceAll("\\.", Matcher.quoteReplacement(separator)))
                .replace(ARTIFACT_ID, artifactIdNew)
                // StrUtil.upperFirst(ARTIFACT_ID)是首字母变大写；
                .replaceAll(StrUtil.upperFirst(ARTIFACT_ID), StrUtil.upperFirst(artifactIdNew))
                // 修改ruoyi-vue-pro.sql为你需要替换的文件名
                .replace("ruoyi-vue-pro.sql", artifactIdNew + ".sql");
    }

}
