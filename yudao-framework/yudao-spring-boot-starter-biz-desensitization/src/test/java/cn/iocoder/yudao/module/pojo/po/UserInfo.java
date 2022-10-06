package cn.iocoder.yudao.module.pojo.po;

import cn.hutool.core.util.DesensitizedUtil;
import cn.iocoder.yudao.framework.desensitization.core.annotation.Desensitization;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;

/**
 * @author VampireAchao
 * @since 2022/10/6 15:19
 */
@Data
@Builder
public class UserInfo {

    @Tolerate
    public UserInfo() {
        // this is an accessible parameterless constructor.
    }

    private Long id;
    private String name;
    @Desensitization(type = DesensitizedUtil.DesensitizedType.EMAIL)
    private String email;
    @Desensitization(regex = "(?<=\\d{3})\\d(?=\\d{4})")
    private String mobile;

}
