package cn.iocoder.yudao.module.steam.enums;


import cn.hutool.core.util.ArrayUtil;
import cn.iocoder.yudao.framework.common.core.IntArrayValuable;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * 小状态smallStatus说明
 */
@AllArgsConstructor
@Getter
public enum UUOrderSubStatus implements IntArrayValuable {
    SUB_CODE1101(1101,"待发送报价"),
    SUB_CODE1412(1102,"已取消_拒绝报价"),
    SUB_CODE1103(1103,"待确认报价"),
    SUB_CODE1104(1104,"交易暂挂"),
    SUB_CODE1105(1105,"系统验证中,steam报价状态非(1-11)"),
    SUB_CODE1106(1106,"系统验证中,steam报价状态为8"),
    SUB_CODE1107(1107,"系统验证中,无法通过apiKey核实报价"),
    SUB_CODE1108(1108,"延时转款"),
    SUB_CODE1109(1109,"待steam令牌确认"),
    SUB_CODE1110(1110,"客服核验中"),
    SUB_CODE1201(1201,"系统结算"),
    SUB_CODE1202(1202,"客服结算"),
    SUB_CODE1301(1301,"系统结算"),
    SUB_CODE1302(1302,"客服结算"),
    SUB_CODE1303(1303,"客服结算订单"),
    SUB_CODE1401(1401,"买家主动取消，取消支付"),
    SUB_CODE1402(1402,"支付超时"),
    SUB_CODE1403(1403,"客服取消订单"),
    SUB_CODE1404(1404,"已取消_发送报价超时"),
    SUB_CODE1405(1405,"已取消_报价发送失败，系统取消订单"),
    SUB_CODE1406(1406,"已取消_买家取消订单，卖家未发货"),
    SUB_CODE1407(1407,"已取消_确认报价超时"),
    SUB_CODE1408(1408,"已取消_报价失效"),
    SUB_CODE1409(1409,"已取消_报价被修改"),
    SUB_CODE1410(1410,"已取消_报价在steam超时"),
    SUB_CODE1411(1411,"已取消_报价在Steam令牌取消"),
    SUB_CODE1413(1413,"已取消_取消报价"),
    SUB_CODE1414(1414,"已取消_商品缺失"),
    SUB_CODE1417(1417,"已取消_支付前取消"),
    SUB_CODE1423(1423,"已取消-卖家取消订单");

    public static final int[] ARRAYS = Arrays.stream(values()).mapToInt(UUOrderSubStatus::getCode).toArray();

    private Integer code;
    private String msg;

    public static UUOrderSubStatus valueOf(Integer value) {
        return ArrayUtil.firstMatch(userType -> userType.getCode().equals(value), UUOrderSubStatus.values());
    }


    @Override
    public int[] array() {
        return ARRAYS;
    }
}
