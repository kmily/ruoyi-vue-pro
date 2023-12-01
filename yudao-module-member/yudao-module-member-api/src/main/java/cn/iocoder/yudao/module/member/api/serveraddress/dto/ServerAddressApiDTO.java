package cn.iocoder.yudao.module.member.api.serveraddress.dto;

import lombok.Data;

@Data
public class ServerAddressApiDTO {

    /**
     * 主键
     */

    private Long id;

    /**
     * 用户编号
     */
    private Long userId;

    /**
     * 地区编码
     */
    private Long areaId;

    /**
     * 省市县/区
     */
    private String address;

    /**
     * 收件详细地址
     */
    private String detailAddress;

    /**
     * 经纬度
     */
    private String coordinate;


}
