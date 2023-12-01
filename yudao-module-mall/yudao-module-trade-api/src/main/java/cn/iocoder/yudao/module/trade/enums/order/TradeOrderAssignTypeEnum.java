package cn.iocoder.yudao.module.trade.enums.order;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author whycode
 * @title: TradeOrderAssignTypeEnum
 * @projectName home-care
 * @description: TODO
 * @date 2023/11/2816:36
 */
@AllArgsConstructor
@Getter
public enum TradeOrderAssignTypeEnum {

    USER("用户指定"),
    SYSTEM("系统指定"),
    ;

    private final String message;


    public static boolean isUser(String type){
        return USER.name().equals(type);
    }

    public static boolean isSystem(String type){
        return SYSTEM.name().equals(type);
    }


}
