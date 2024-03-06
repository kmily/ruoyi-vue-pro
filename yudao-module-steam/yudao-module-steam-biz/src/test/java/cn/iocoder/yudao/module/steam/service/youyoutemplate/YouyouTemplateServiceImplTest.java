package cn.iocoder.yudao.module.steam.service.youyoutemplate;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import javax.annotation.Resource;

import cn.iocoder.yudao.framework.test.core.ut.BaseDbUnitTest;

import cn.iocoder.yudao.module.steam.controller.admin.youyoutemplate.vo.*;
import cn.iocoder.yudao.module.steam.dal.dataobject.youyoutemplate.YouyouTemplateDO;
import cn.iocoder.yudao.module.steam.dal.mysql.youyoutemplate.UUTemplateMapper;
import cn.iocoder.yudao.framework.common.pojo.PageResult;

import org.springframework.context.annotation.Import;

import static cn.iocoder.yudao.module.steam.enums.ErrorCodeConstants.*;
import static cn.iocoder.yudao.framework.test.core.util.AssertUtils.*;
import static cn.iocoder.yudao.framework.test.core.util.RandomUtils.*;
import static cn.iocoder.yudao.framework.common.util.date.LocalDateTimeUtils.*;
import static cn.iocoder.yudao.framework.common.util.object.ObjectUtils.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * {@link YouyouTemplateServiceImpl} 的单元测试类
 *
 * @author 管理员
 */
@Import(YouyouTemplateServiceImpl.class)
public class YouyouTemplateServiceImplTest extends BaseDbUnitTest {

    @Resource
    private YouyouTemplateServiceImpl youyouTemplateService;

    @Resource
    private UUTemplateMapper UUTemplateMapper;

    @Test
    public void testCreateYouyouTemplate_success() {
        // 准备参数
        UUTemplateSaveReqVO createReqVO = randomPojo(UUTemplateSaveReqVO.class).setId(null);

        // 调用
        Integer youyouTemplateId = youyouTemplateService.createYouyouTemplate(createReqVO);
        // 断言
        assertNotNull(youyouTemplateId);
        // 校验记录的属性是否正确
        YouyouTemplateDO youyouTemplate = UUTemplateMapper.selectById(youyouTemplateId);
        assertPojoEquals(createReqVO, youyouTemplate, "id");
    }

    @Test
    public void testUpdateYouyouTemplate_success() {
        // mock 数据
        YouyouTemplateDO dbYouyouTemplate = randomPojo(YouyouTemplateDO.class);
        UUTemplateMapper.insert(dbYouyouTemplate);// @Sql: 先插入出一条存在的数据
        // 准备参数
        UUTemplateSaveReqVO updateReqVO = randomPojo(UUTemplateSaveReqVO.class, o -> {
            o.setId(dbYouyouTemplate.getId()); // 设置更新的 ID
        });

        // 调用
        youyouTemplateService.updateYouyouTemplate(updateReqVO);
        // 校验是否更新正确
        YouyouTemplateDO youyouTemplate = UUTemplateMapper.selectById(updateReqVO.getId()); // 获取最新的
        assertPojoEquals(updateReqVO, youyouTemplate);
    }

    @Test
    public void testUpdateYouyouTemplate_notExists() {
        // 准备参数
        UUTemplateSaveReqVO updateReqVO = randomPojo(UUTemplateSaveReqVO.class);

        // 调用, 并断言异常
        assertServiceException(() -> youyouTemplateService.updateYouyouTemplate(updateReqVO), YOUYOU_TEMPLATE_NOT_EXISTS);
    }

    @Test
    public void testDeleteYouyouTemplate_success() {
        // mock 数据
        YouyouTemplateDO dbYouyouTemplate = randomPojo(YouyouTemplateDO.class);
        UUTemplateMapper.insert(dbYouyouTemplate);// @Sql: 先插入出一条存在的数据
        // 准备参数
        Integer id = dbYouyouTemplate.getId();

        // 调用
        youyouTemplateService.deleteYouyouTemplate(id);
       // 校验数据不存在了
       assertNull(UUTemplateMapper.selectById(id));
    }

    @Test
    public void testDeleteYouyouTemplate_notExists() {
        // 准备参数
        Integer id = 1;

        // 调用, 并断言异常
        assertServiceException(() -> youyouTemplateService.deleteYouyouTemplate(id), YOUYOU_TEMPLATE_NOT_EXISTS);
    }

    @Test
    @Disabled  // TODO 请修改 null 为需要的值，然后删除 @Disabled 注解
    public void testGetYouyouTemplatePage() {
       // mock 数据
       YouyouTemplateDO dbYouyouTemplate = randomPojo(YouyouTemplateDO.class, o -> { // 等会查询到
           o.setName(null);
           o.setHashName(null);
           o.setTypeId(null);
           o.setTypeName(null);
           o.setTypeHashName(null);
           o.setWeaponId(null);
           o.setWeaponName(null);
           o.setWeaponHashName(null);
           o.setCreateTime(null);
           o.setTemplateId(null);
       });
       UUTemplateMapper.insert(dbYouyouTemplate);
       // 测试 name 不匹配
       UUTemplateMapper.insert(cloneIgnoreId(dbYouyouTemplate, o -> o.setName(null)));
       // 测试 hashName 不匹配
       UUTemplateMapper.insert(cloneIgnoreId(dbYouyouTemplate, o -> o.setHashName(null)));
       // 测试 typeId 不匹配
       UUTemplateMapper.insert(cloneIgnoreId(dbYouyouTemplate, o -> o.setTypeId(null)));
       // 测试 typeName 不匹配
       UUTemplateMapper.insert(cloneIgnoreId(dbYouyouTemplate, o -> o.setTypeName(null)));
       // 测试 typeHashName 不匹配
       UUTemplateMapper.insert(cloneIgnoreId(dbYouyouTemplate, o -> o.setTypeHashName(null)));
       // 测试 weaponId 不匹配
       UUTemplateMapper.insert(cloneIgnoreId(dbYouyouTemplate, o -> o.setWeaponId(null)));
       // 测试 weaponName 不匹配
       UUTemplateMapper.insert(cloneIgnoreId(dbYouyouTemplate, o -> o.setWeaponName(null)));
       // 测试 weaponHashName 不匹配
       UUTemplateMapper.insert(cloneIgnoreId(dbYouyouTemplate, o -> o.setWeaponHashName(null)));
       // 测试 createTime 不匹配
       UUTemplateMapper.insert(cloneIgnoreId(dbYouyouTemplate, o -> o.setCreateTime(null)));
       // 测试 templateId 不匹配
       UUTemplateMapper.insert(cloneIgnoreId(dbYouyouTemplate, o -> o.setTemplateId(null)));
       // 准备参数
       YouyouTemplatePageReqVO reqVO = new YouyouTemplatePageReqVO();
       reqVO.setName(null);
       reqVO.setHashName(null);
       reqVO.setTypeId(null);
       reqVO.setTypeName(null);
       reqVO.setTypeHashName(null);
       reqVO.setWeaponId(null);
       reqVO.setWeaponName(null);
       reqVO.setWeaponHashName(null);
       reqVO.setCreateTime(buildBetweenTime(2023, 2, 1, 2023, 2, 28));
       reqVO.setTemplateId(null);

       // 调用
       PageResult<YouyouTemplateDO> pageResult = youyouTemplateService.getYouyouTemplatePage(reqVO);
       // 断言
       assertEquals(1, pageResult.getTotal());
       assertEquals(1, pageResult.getList().size());
       assertPojoEquals(dbYouyouTemplate, pageResult.getList().get(0));
    }

}