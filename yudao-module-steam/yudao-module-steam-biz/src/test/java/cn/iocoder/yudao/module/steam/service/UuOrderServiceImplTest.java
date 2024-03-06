package cn.iocoder.yudao.module.steam.service;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.test.core.ut.BaseDbUnitTest;
import cn.iocoder.yudao.module.steam.controller.admin.youyouorder.vo.YouyouOrderPageReqVO;
import cn.iocoder.yudao.module.steam.controller.admin.youyouorder.vo.YouyouOrderSaveReqVO;
import cn.iocoder.yudao.module.steam.dal.dataobject.youyouorder.YouyouOrderDO;
import cn.iocoder.yudao.module.steam.dal.mysql.youyouorder.YouyouOrderMapper;
import cn.iocoder.yudao.module.steam.service.youyouorder.YouyouOrderServiceImpl;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Import;

import javax.annotation.Resource;

import static cn.iocoder.yudao.framework.common.util.date.LocalDateTimeUtils.buildBetweenTime;
import static cn.iocoder.yudao.framework.common.util.object.ObjectUtils.cloneIgnoreId;
import static cn.iocoder.yudao.framework.test.core.util.AssertUtils.assertPojoEquals;
import static cn.iocoder.yudao.framework.test.core.util.AssertUtils.assertServiceException;
import static cn.iocoder.yudao.framework.test.core.util.RandomUtils.randomLongId;
import static cn.iocoder.yudao.framework.test.core.util.RandomUtils.randomPojo;
import static cn.iocoder.yudao.module.steam.enums.ErrorCodeConstants.YOUYOU_ORDER_NOT_EXISTS;
import static org.junit.jupiter.api.Assertions.*;

/**
 * {@link YouyouOrderServiceImpl} 的单元测试类
 *
 * @author 管理员
 */
@Import(YouyouOrderServiceImpl.class)
public class UuOrderServiceImplTest extends BaseDbUnitTest {


    @Test
    public void testCreateYouyouOrder_success() {
        // 准备参数

    }


}