package cn.iocoder.yudao.module.mapper;

import cn.iocoder.yudao.module.pojo.po.UserInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author VampireAchao
 * @since 2022/10/6 15:30
 */
@Mapper
public interface UserMapper extends BaseMapper<UserInfo> {
}
