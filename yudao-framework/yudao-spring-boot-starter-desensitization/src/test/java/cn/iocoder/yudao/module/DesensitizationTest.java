package cn.iocoder.yudao.module;

import cn.iocoder.yudao.framework.desensitization.config.YudaoDesensitizationAutoConfiguration;
import cn.iocoder.yudao.framework.test.core.ut.BaseDbUnitTest;
import cn.iocoder.yudao.module.pojo.po.UserInfo;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;

/**
 * @author VampireAchao
 * @since 2022/10/6 15:11
 */
@Import(YudaoDesensitizationAutoConfiguration.class)
@Sql(scripts = "/sql/insert_data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD) // 每个单元测试结束前，新增 DB
class DesensitizationTest extends BaseDbUnitTest {

    @Test
    void testQuery() {
        UserInfo userInfo = SqlHelper.execute(UserInfo.class, m -> m.selectById(1L));
        Assertions.assertEquals("123****8910", userInfo.getMobile());
        Assertions.assertEquals("a**************@gmail.com", userInfo.getEmail());
    }

}
