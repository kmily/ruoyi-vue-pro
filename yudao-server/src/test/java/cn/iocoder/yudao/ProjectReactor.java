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
 * <p>
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
     * 为取出banner-new.txt做准备
     */
    static File bannerFile = new File("");

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
        // 开始时间
        long start = System.currentTimeMillis();

        // ========== 配置，需要你手动修改 ==========
        String groupIdNew = "cn.star.gg";
        String artifactIdNew = "star";
        String packageNameNew = "cn.start.pp";
        String titleNew = "土豆管理系统";

        // 原项目路径
        String projectBaseDir = getProjectBaseDir();
        log.info("[main]原项目路径： {}", projectBaseDir);

        // 一键改名后，“新”项目所在的目录（默认加new方式）
//        String projectBaseDirNew = projectBaseDir + "-new";

        // 一键改名后，“新”项目所在的目录（自定义目录方式）
        String projectBaseDirNew = "E:\\ruoyi-api-server";
        log.info("[main]新项目路径：{}", projectBaseDirNew);

        // 创建web-ui文件夹，对web层进行归类
        String dirPath = projectBaseDirNew + "\\web-ui";
        createDir(dirPath);

        // 创建resource文件夹路径
        String resourcePath = projectBaseDirNew + "\\resource";
        createDir(resourcePath);

        // 获得需要复制的文件
        log.info("[main]开始获得需要重写的文件，预计需要 10-20 秒");
        Collection<File> files = listFiles(projectBaseDir);
        log.info("[main]需要重写的文件数量：{}，预计需要 15-30 秒", files.size());

        // 遍历出banner-new.txt文件
        bannerFile = files.stream().filter(file -> file.getName().equals("banner-new.txt")).findFirst().get();
        log.info("[main]banner-new.txt路径：{}", bannerFile.getPath());

        // 循环处理文件
        files.forEach(file -> {
            // 如果是白名单的文件类型，不进行重写，直接拷贝
            String fileType = FileTypeUtil.getType(file);
            if (WHITE_FILE_TYPES.contains(fileType)) {
                copyFile(file, projectBaseDir, projectBaseDirNew, packageNameNew, artifactIdNew);
                return;
            }
            // 替换文件内容字符，如果非白名单的文件类型，重写内容，在生成文件
            String content = replaceFileContent(file, groupIdNew, artifactIdNew, packageNameNew, titleNew);

            // 写入文件
            writeFile(file, content, projectBaseDir, projectBaseDirNew, packageNameNew, artifactIdNew);
        });
        log.info("[main]重写完成]共耗时：{} 秒", (System.currentTimeMillis() - start) / 1000);
    }

    /**
     * 获得项目基础路径
     *
     * @return String 项目基础路径
     */
    private static String getProjectBaseDir() {
        String baseDir = System.getProperty("user.dir");
        if (StrUtil.isEmpty(baseDir)) {
            throw new NullPointerException("项目基础路径不存在");
        }
        return baseDir;
    }

    /**
     * 通过项项目基础路径获取所有需要操作的文件
     *
     * @param projectBaseDir 项目基础路径
     * @return Collection<File> 返回文件集合
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
     * 文件内容处理
     *
     * @param file
     * @param groupIdNew
     * @param artifactIdNew
     * @param packageNameNew
     * @param titleNew
     * @return
     */
    private static String replaceFileContent(File file, String groupIdNew,
                                             String artifactIdNew, String packageNameNew,
                                             String titleNew) {
        log.info("正在处理文件：{}", file.getName());

        // banner单独处理
        if (file.getName().equals("banner.txt")) {
            return FileUtil.readString(bannerFile.getPath(), StandardCharsets.UTF_8);
        }

        // 读取文件内容
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
                .replaceAll(StrUtil.upperFirst(ARTIFACT_ID), StrUtil.upperFirst(artifactIdNew))
                .replaceAll(TITLE, titleNew)

                // 端口替换
                .replaceAll("48080", "8080")
                .replaceAll("28080", "8080")
                .replaceAll("6379", "6397")

                // 网址还原
                .replaceAll("star.iocoder.cn", "yudao.iocoder.cn");
    }

    /**
     * 写入文件
     *
     * @param file
     * @param fileContent
     * @param projectBaseDir
     * @param projectBaseDirNew
     * @param packageNameNew
     * @param artifactIdNew
     */
    private static void writeFile(File file, String fileContent, String projectBaseDir,
                                  String projectBaseDirNew, String packageNameNew, String artifactIdNew) {
        // 新目录
        String newPath = buildNewFilePath(file, projectBaseDir, projectBaseDirNew, packageNameNew, artifactIdNew);
        log.info("正在写入的新文件路径：{}", newPath);

        // 写入最终的文件
        FileUtil.writeUtf8String(fileContent, newPath);
    }

    /**
     * 复制文件
     *
     * @param file
     * @param projectBaseDir
     * @param projectBaseDirNew
     * @param packageNameNew
     * @param artifactIdNew
     */
    private static void copyFile(File file, String projectBaseDir,
                                 String projectBaseDirNew, String packageNameNew, String artifactIdNew) {
        String newPath = buildNewFilePath(file, projectBaseDir, projectBaseDirNew, packageNameNew, artifactIdNew);
        log.info("正在【复制】的新文件路径：{}", newPath);
        FileUtil.copyFile(file, new File(newPath));
    }

    /**
     * 构建新路径
     *
     * @param file
     * @param projectBaseDir
     * @param projectBaseDirNew
     * @param packageNameNew
     * @param artifactIdNew
     * @return
     */
    private static String buildNewFilePath(File file, String projectBaseDir,
                                           String projectBaseDirNew, String packageNameNew, String artifactIdNew) {

        // UI文件特殊处理，增加web-ui目录，全部移动到web-ui中
        if (file.getPath().contains(projectBaseDir + "\\" + ARTIFACT_ID + "-ui")) {
            projectBaseDirNew = projectBaseDirNew + "\\web-ui";
            log.info("web-ui的文件目录：{}", projectBaseDirNew);
        }

        // bin和sql文件特殊处理，增加resource目录，全部移动到resource中
        if ((file.getPath().contains(projectBaseDir + "\\" + "bin")) || (file.getPath().contains(projectBaseDir +
                "\\" + "sql"))) {
            projectBaseDirNew = projectBaseDirNew + "\\resource";
            log.info("bin和sql的文件目录：{}", projectBaseDirNew);
        }

        // 新目录
        return file.getPath().replace(projectBaseDir, projectBaseDirNew)
                .replace(PACKAGE_NAME.replaceAll("\\.", Matcher.quoteReplacement(separator)),
                        packageNameNew.replaceAll("\\.", Matcher.quoteReplacement(separator)))
                .replace(ARTIFACT_ID, artifactIdNew)
                // StrUtil.upperFirst(ARTIFACT_ID)是首字母变大写；
                .replaceAll(StrUtil.upperFirst(ARTIFACT_ID), StrUtil.upperFirst(artifactIdNew))
                // 将shansima.sql文件名变成shansima.sql。
                .replace("ruoyi-vue-pro.sql", artifactIdNew + ".sql");
    }

    /**
     * 创建文件夹
     *
     * @param path
     */
    private static void createDir(String path) {
        File directory = new File(path);
        //如果不存在
        if (!directory.exists()) {
            //创建目录
            boolean hasSucceeded = directory.mkdirs();
            log.info("[main]创建文件夹：{}，路径：{}", hasSucceeded, path);
        }
    }
}