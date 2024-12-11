package cn.iocoder.yudao.module.system.api.codingrules;

/**
 * 编码规则 API 接口
 *
 * @author panjiabao
 */
public interface CodingRulesApi {
    /**
     * 根据规则编码生成编码规则
     *
     * @param code 编码
     * @param preview 是否是预览，true代表是预览模式，不消耗流水号
     * @return 生成的编码
     */
    String genCodingRules(String code,boolean preview);
}
