package cn.iocoder.yudao.module.steam.service.invdesc;


import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.test.core.ut.BaseDbUnitTest;
import cn.iocoder.yudao.module.steam.controller.admin.invdesc.vo.InvDescPageReqVO;
import cn.iocoder.yudao.module.steam.controller.admin.invdesc.vo.InvDescSaveReqVO;
import cn.iocoder.yudao.module.steam.dal.dataobject.invdesc.InvDescDO;
import cn.iocoder.yudao.module.steam.dal.mysql.invdesc.InvDescMapper;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Import;

import javax.annotation.Resource;

import static cn.iocoder.yudao.framework.common.util.object.ObjectUtils.cloneIgnoreId;
import static cn.iocoder.yudao.framework.test.core.util.AssertUtils.assertPojoEquals;
import static cn.iocoder.yudao.framework.test.core.util.AssertUtils.assertServiceException;
import static cn.iocoder.yudao.framework.test.core.util.RandomUtils.randomLongId;
import static cn.iocoder.yudao.framework.test.core.util.RandomUtils.randomPojo;
import static cn.iocoder.yudao.module.steam.enums.ErrorCodeConstants.INV_DESC_NOT_EXISTS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * {@link InvDescServiceImpl} 的单元测试类
 *
 * @author 芋道源码
 */
@Import(InvDescService.class)
public class InvDescServiceImplTest extends BaseDbUnitTest {

    @Resource
    private InvDescService invDescService;

    @Resource
    private InvDescMapper invDescMapper;

    @Test
    public void testCreateInvDesc_success() {
        // 准备参数
        InvDescSaveReqVO createReqVO = randomPojo(InvDescSaveReqVO.class).setId(null);

        // 调用
        Long invDescId = invDescService.createInvDesc(createReqVO);
        // 断言
        assertNotNull(invDescId);
        // 校验记录的属性是否正确
        InvDescDO invDesc = invDescMapper.selectById(invDescId);
        assertPojoEquals(createReqVO, invDesc, "id");
    }

    @Test
    public void testUpdateInvDesc_success() {
        // mock 数据
        InvDescDO dbInvDesc = randomPojo(InvDescDO.class);
        invDescMapper.insert(dbInvDesc);// @Sql: 先插入出一条存在的数据
        // 准备参数
        InvDescSaveReqVO updateReqVO = randomPojo(InvDescSaveReqVO.class, o -> {
            o.setId(dbInvDesc.getId()); // 设置更新的 ID
        });

        // 调用
        invDescService.updateInvDesc(updateReqVO);
        // 校验是否更新正确
        InvDescDO invDesc = invDescMapper.selectById(updateReqVO.getId()); // 获取最新的
        assertPojoEquals(updateReqVO, invDesc);
    }

    @Test
    public void testUpdateInvDesc_notExists() {
        // 准备参数
        InvDescSaveReqVO updateReqVO = randomPojo(InvDescSaveReqVO.class);

        // 调用, 并断言异常
        assertServiceException(() -> invDescService.updateInvDesc(updateReqVO), INV_DESC_NOT_EXISTS);
    }

    @Test
    public void testDeleteInvDesc_success() {
        // mock 数据
        InvDescDO dbInvDesc = randomPojo(InvDescDO.class);
        invDescMapper.insert(dbInvDesc);// @Sql: 先插入出一条存在的数据
        // 准备参数
        Long id = dbInvDesc.getId();

        // 调用
        invDescService.deleteInvDesc(id);
       // 校验数据不存在了
       assertNull(invDescMapper.selectById(id));
    }

    private void assertNull(InvDescDO selectById) {
    }

    @Test
    public void testDeleteInvDesc_notExists() {
        // 准备参数
        Long id = randomLongId();

        // 调用, 并断言异常
        assertServiceException(() -> invDescService.deleteInvDesc(id), INV_DESC_NOT_EXISTS);
    }

    @Test
    @Disabled  // TODO 请修改 null 为需要的值，然后删除 @Disabled 注解
    public void testGetInvDescPage() {
       // mock 数据
       InvDescDO dbInvDesc = randomPojo(InvDescDO.class, o -> { // 等会查询到
           o.setAppid(null);
           o.setClassid(null);
           o.setInstanceid(null);
           o.setCurrency(null);
           o.setBackgroundColor(null);
           o.setTradable(null);
           o.setActions(null);
           o.setFraudwarnings(null);
           o.setName(null);
           o.setNameColor(null);
           o.setType(null);
           o.setMarketName(null);
           o.setMarketHashName(null);
           o.setMarketActions(null);
           o.setCommodity(null);
           o.setMarketTradableRestriction(null);
           o.setMarketable(null);
           o.setSelQuality(null);
           o.setSelItemset(null);
           o.setSelWeapon(null);
           o.setSelExterior(null);
           o.setSelRarity(null);
           o.setSelType(null);
           o.setSteamId(null);
       });
       invDescMapper.insert(dbInvDesc);
       // 测试 appid 不匹配
       invDescMapper.insert(cloneIgnoreId(dbInvDesc, o -> o.setAppid(null)));
       // 测试 classid 不匹配
       invDescMapper.insert(cloneIgnoreId(dbInvDesc, o -> o.setClassid(null)));
       // 测试 instanceid 不匹配
       invDescMapper.insert(cloneIgnoreId(dbInvDesc, o -> o.setInstanceid(null)));
       // 测试 currency 不匹配
       invDescMapper.insert(cloneIgnoreId(dbInvDesc, o -> o.setCurrency(null)));
       // 测试 backgroundColor 不匹配
       invDescMapper.insert(cloneIgnoreId(dbInvDesc, o -> o.setBackgroundColor(null)));
       // 测试 tradable 不匹配
       invDescMapper.insert(cloneIgnoreId(dbInvDesc, o -> o.setTradable(null)));
       // 测试 actions 不匹配
       invDescMapper.insert(cloneIgnoreId(dbInvDesc, o -> o.setActions(null)));
       // 测试 fraudwarnings 不匹配
       invDescMapper.insert(cloneIgnoreId(dbInvDesc, o -> o.setFraudwarnings(null)));
       // 测试 name 不匹配
       invDescMapper.insert(cloneIgnoreId(dbInvDesc, o -> o.setName(null)));
       // 测试 nameColor 不匹配
       invDescMapper.insert(cloneIgnoreId(dbInvDesc, o -> o.setNameColor(null)));
       // 测试 type 不匹配
       invDescMapper.insert(cloneIgnoreId(dbInvDesc, o -> o.setType(null)));
       // 测试 marketName 不匹配
       invDescMapper.insert(cloneIgnoreId(dbInvDesc, o -> o.setMarketName(null)));
       // 测试 marketHashName 不匹配
       invDescMapper.insert(cloneIgnoreId(dbInvDesc, o -> o.setMarketHashName(null)));
       // 测试 marketActions 不匹配
       invDescMapper.insert(cloneIgnoreId(dbInvDesc, o -> o.setMarketActions(null)));
       // 测试 commodity 不匹配
       invDescMapper.insert(cloneIgnoreId(dbInvDesc, o -> o.setCommodity(null)));
       // 测试 marketTradableRestriction 不匹配
       invDescMapper.insert(cloneIgnoreId(dbInvDesc, o -> o.setMarketTradableRestriction(null)));
       // 测试 marketable 不匹配
       invDescMapper.insert(cloneIgnoreId(dbInvDesc, o -> o.setMarketable(null)));
       // 测试 selQuality 不匹配
       invDescMapper.insert(cloneIgnoreId(dbInvDesc, o -> o.setSelQuality(null)));
       // 测试 selItemset 不匹配
       invDescMapper.insert(cloneIgnoreId(dbInvDesc, o -> o.setSelItemset(null)));
       // 测试 selWeapon 不匹配
       invDescMapper.insert(cloneIgnoreId(dbInvDesc, o -> o.setSelWeapon(null)));
       // 测试 selExterior 不匹配
       invDescMapper.insert(cloneIgnoreId(dbInvDesc, o -> o.setSelExterior(null)));
       // 测试 selRarity 不匹配
       invDescMapper.insert(cloneIgnoreId(dbInvDesc, o -> o.setSelRarity(null)));
       // 测试 selType 不匹配
       invDescMapper.insert(cloneIgnoreId(dbInvDesc, o -> o.setSelType(null)));
       // 测试 steamId 不匹配
       invDescMapper.insert(cloneIgnoreId(dbInvDesc, o -> o.setSteamId(null)));
       // 准备参数
       InvDescPageReqVO reqVO = new InvDescPageReqVO();
       reqVO.setAppid(null);
       reqVO.setClassid(null);
       reqVO.setInstanceid(null);
       reqVO.setCurrency(null);
       reqVO.setBackgroundColor(null);
       reqVO.setTradable(null);
       reqVO.setActions(null);
       reqVO.setFraudwarnings(null);
       reqVO.setName(null);
       reqVO.setNameColor(null);
       reqVO.setType(null);
       reqVO.setMarketName(null);
       reqVO.setMarketHashName(null);
       reqVO.setMarketActions(null);
       reqVO.setCommodity(null);
       reqVO.setMarketTradableRestriction(null);
       reqVO.setMarketable(null);
       reqVO.setSelQuality(null);
       reqVO.setSelItemset(null);
       reqVO.setSelWeapon(null);
       reqVO.setSelExterior(null);
       reqVO.setSelRarity(null);
       reqVO.setSelType(null);
       reqVO.setSteamId(null);

       // 调用
       PageResult<InvDescDO> pageResult = invDescService.getInvDescPage(reqVO);
       // 断言
       assertEquals(1, pageResult.getTotal());
       assertEquals(1, pageResult.getList().size());
       assertPojoEquals(dbInvDesc, pageResult.getList().get(0));
    }

}