/**
 * <pre>
 * -.-----......../-.-..----.-...-/--------...--.-./--..---.-..---./---..---...-..-/--.--...-.-----
 * 使用方法介绍，以商品的店铺权限为例：
 *
 * 一、登录后将有店铺编号存到用户上下文
 *  private Map<String, String> buildUserInfo(Long userId, Integer userType) {
 *      // ！！！重要！！！存储用户所属角色及店铺到登录上下文，用于辅助实现店铺数据权限
 *      // 需要重写 OAuth2TokenServiceImpl，建议 OAuth2TokenService 里将 buildUserInfo 暴露出来
 *      Set<String> roleCodes = adminUserApiV2.getRoleCodes(userId);
 *      Set<Long> shopIds = shopAdminApi.getShopIdsByUserId(userId, userType);
 *      return MapUtil.builder(LOGIN_USER_CONTEXT_KEY_ROLE_CODES, JsonUtils.toJsonString(roleCodes))
 *               .put(LOGIN_USER_CONTEXT_KEY_SHOP_IDS, JsonUtils.toJsonString(shopIds))
 *               .build();
 * }
 *
 * 二、数据权限范围的丹普编号从哪里得来
 *  &#064;Configuration(proxyBeanMethods = false)
 * public class ShopDataPermissionConfiguration {
 *
 *     &#064;Bean
 *     public UniDataPermissionScope shopDataPermissionScope() {
 *         return new UniDataPermissionScope() {
 *             &#064;Nonnull
 *             &#064;Override
 *             public String getScopeKey() {
 *                 return "shop_id";
 *             }
 *
 *             &#064;Override
 *             public Set<Long> getScopeValues() {
 *                 // 坑：这里只能通过登录上下文 LoginUser 获取，不能通过直接从数据库获取，
 *                 // 否则会造成递归调用的死循环，触发异常：java.lang.StackOverflowError
 *                 LoginUser loginUser = SecurityFrameworkUtils.getLoginUser();
 *                 if (loginUser == null) {
 *                     return new HashSet<>();
 *                 }
 *                 String shopIdsStr = MapUtil.get(loginUser.getInfo(), LOGIN_USER_CONTEXT_KEY_SHOP_IDS, String.class);
 *                 StringHashSet shopIds = JsonUtils.parseObject(shopIdsStr, StringHashSet.class);
 *                 if (CollUtil.isEmpty(shopIds)) {
 *                     return new HashSet<>();
 *                 }
 *                 return shopIds.stream().map(NumberUtil::parseLong).collect(Collectors.toSet());
 *             }
 *         };
 *     }
 *
 * }
 *
 * 三、需要自动根据店铺编号进行数据过滤的表
 *  &#064;Configuration(proxyBeanMethods = false)
 * public class ProductDataPermissionConfiguration {
 *
 *     &#064;Bean
 *     public UniDataPermissionRuleCustomizer productDataPermissionRuleCustomizer() {
 *         return rule -> {
 *             rule.addDataColumn(ProductCategoryDO.class, "shop_id");
 *             rule.addDataColumn(ProductSpuDO.class, "shop_id");
 *             rule.addDataColumn(ProductSkuDO.class, "shop_id");
 *             rule.addDataColumn(ProductPropertyDO.class, "shop_id");
 *             rule.addDataColumn(ProductPropertyValueDO.class, "shop_id");
 *             rule.addDataColumn(ProductCommentDO.class, "shop_id");
 *         };
 *     }
 *
 * }
 * --.-/-.-..------.---/--------...--.-./.----/-----/...--/..---/-..../----./....-/--.../-..../-----
 * </pre>
 *
 * @author 山野羡民（1032694760@qq.com）
 * @since 2024/12/14
 */
package cn.iocoder.yudao.framework.datapermission.core.rule.uni;