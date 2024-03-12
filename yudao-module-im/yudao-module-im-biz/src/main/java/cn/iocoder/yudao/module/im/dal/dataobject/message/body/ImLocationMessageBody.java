package cn.iocoder.yudao.module.im.dal.dataobject.message.body;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 地址位置消息的 {@link ImMessageBody}
 *
 * @author 芋道源码
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ImLocationMessageBody implements ImMessageBody {

    /**
     * 地理位置
     *
     * 例如说：中国 浙江省 杭州市 网商路 599号
     */
    private String address;
    /**
     * 经度
     */
    private Double longitude;
    /**
     * 纬度
     */
    private Double latitude;

}