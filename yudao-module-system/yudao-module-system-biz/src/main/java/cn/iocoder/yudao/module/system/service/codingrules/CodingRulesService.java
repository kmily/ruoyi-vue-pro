package cn.iocoder.yudao.module.system.service.codingrules;

import java.util.*;

import cn.iocoder.yudao.module.system.dal.dataobject.codingrulesdetails.CodingRulesDetailsDO;
import jakarta.validation.*;
import cn.iocoder.yudao.module.system.controller.admin.codingrules.vo.*;
import cn.iocoder.yudao.module.system.dal.dataobject.codingrules.CodingRulesDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;

/**
 * 编号规则 Service 接口
 *
 * @author panjiabao
 */
public interface CodingRulesService {

    /**
     * 创建编号规则表头
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    String createCodingRules(@Valid CodingRulesSaveReqVO createReqVO);

    /**
     * 更新编号规则表头
     *
     * @param updateReqVO 更新信息
     */
    void updateCodingRules(@Valid CodingRulesSaveReqVO updateReqVO);

    /**
     * 删除编号规则表头
     *
     * @param id id
     */
    void deleteCodingRules(String id);

    /**
     * 获得编号规则表头
     *
     * @param id 编号
     * @return 编号规则表头
     */
    CodingRulesDO getCodingRules(String id);

    /**
     * 获得编号规则表头分页
     *
     * @param pageReqVO 分页查询
     * @return 编号规则表头分页
     */
    PageResult<CodingRulesDO> getCodingRulesPage(CodingRulesPageReqVO pageReqVO);

    /**
     * 获得编码规则明细
     *
     * @param id 编码规则头ID
     * @return 编号规则明细
     */
    List<CodingRulesDetailsDO> getDetails(String id);

    /**
     * 新增编码规则明细
     *
     * @param codingRulesDetailsSaveReqVO 创建信息
     * @return id
     */
    String insertDetails(CodingRulesDetailsSaveReqVO codingRulesDetailsSaveReqVO);

    /**
     * 更新编号规则明细
     *
     * @param updateReqVO 更新信息
     */
    void updateDetails(CodingRulesDetailsSaveReqVO updateReqVO);

    /**
     * 删除编号规则明细
     *
     * @param id id
     */
    void deleteDetails(String id);
}
