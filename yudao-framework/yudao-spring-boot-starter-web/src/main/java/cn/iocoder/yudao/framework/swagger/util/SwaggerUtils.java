package cn.iocoder.yudao.framework.swagger.util;

import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.security.SecurityScheme;
import lombok.experimental.UtilityClass;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.http.HttpHeaders;

import static cn.iocoder.yudao.framework.web.core.util.WebFrameworkUtils.HEADER_TENANT_ID;

/**
 * Swagger 工具类
 *
 * @author 山野羡民（1032694760@qq.com）
 * @since 2024/11/29
 */
@UtilityClass
public class SwaggerUtils {

    /**
     * 按指定路径规则匹配进行分组
     *
     * @param group        分组名称，如：member
     * @param pathsToMatch 路径规则，如：/admin-api/member/**
     */
    public static GroupedOpenApi buildGroupedOpenApi(String group, String... pathsToMatch) {
        return GroupedOpenApi.builder()
                .group(group)
                .pathsToMatch(pathsToMatch)
                .addOperationCustomizer((operation, handlerMethod) -> operation
                        .addParametersItem(buildTenantHeaderParameter())
                        .addParametersItem(buildSecurityHeaderParameter()))
                .build();
    }

    /**
     * 按“{group}/**”规则匹配进行分组，包含 admin-api 和 app-api
     *
     * @param group 分组名称，如：member
     */
    public static GroupedOpenApi buildGroupedOpenApi(String group) {
        return buildGroupedOpenApi(group, group + "/**");
    }

    /**
     * 按“{group}/**”规则匹配进行分组，包含 admin-api 和 app-api
     *
     * @param group 分组名称，如：member
     */
    public static GroupedOpenApi buildGroupedAdminAndAppApi(String group) {
        return buildGroupedAdminAndAppApi(group, group + "/**");
    }

    /**
     * 按“{pathPattern}”规则匹配进行分组，包含 admin-api 和 app-api
     *
     * @param group 分组名称，如：member
     * @param pathPattern 路径，如：member/**
     */
    public static GroupedOpenApi buildGroupedAdminAndAppApi(String group, String pathPattern) {
        return buildGroupedOpenApi(group, "/admin-api/" + pathPattern, "/app-api/" + pathPattern);
    }

    /**
     * 按“{group}/**”规则匹配进行分组，只包含 app-api
     *
     * @param group 分组名称，如：member
     */
    public static GroupedOpenApi buildGroupedAppApi(String group) {
        return buildGroupedAppApi(group, group + "/**");
    }

    /**
     * 按“{pathPattern}”规则匹配进行分组，只包含 app-api
     *
     * @param group 分组名称，如：member
     * @param pathPattern 路径规则，如：member/**
     */
    public static GroupedOpenApi buildGroupedAppApi(String group, String pathPattern) {
        return buildGroupedOpenApi(group, "/app-api/" + pathPattern);
    }

    /**
     * 按“{group}/**”规则匹配进行分组，只包含 admin-api
     *
     * @param group 分组名称，如：member
     */
    public static GroupedOpenApi buildGroupedAdminApi(String group) {
        return buildGroupedAdminApi(group, group + "/**");
    }

    /**
     * 按“{pathPattern}”规则匹配进行分组，只包含 admin-api
     *
     * @param group 分组名称，如：member
     * @param pathPattern 路径规则，如：member/**
     */
    public static GroupedOpenApi buildGroupedAdminApi(String group, String pathPattern) {
        return buildGroupedOpenApi(group, "/admin-api/" + pathPattern);
    }

    /**
     * 构建 Tenant 租户编号请求头参数
     *
     * @return 多租户参数
     */
    private static Parameter buildTenantHeaderParameter() {
        return new Parameter()
                .name(HEADER_TENANT_ID)
                .description("租户编号")
                .in(String.valueOf(SecurityScheme.In.HEADER))
                .example("1");
    }

    /**
     * 构建 Authorization 认证请求头参数
     * <p>
     * 解决 Knife4j <a href="https://gitee.com/xiaoym/knife4j/issues/I69QBU">Authorize 未生效，请求header里未包含参数</a>
     *
     * @return 认证参数
     */
    private static Parameter buildSecurityHeaderParameter() {
        return new Parameter()
                .name(HttpHeaders.AUTHORIZATION)
                .description("认证令牌，格式为“Bearer {token}”，token 从登录接口获取")
                .in(String.valueOf(SecurityScheme.In.HEADER))
                .example("Bearer test1");
    }

}
