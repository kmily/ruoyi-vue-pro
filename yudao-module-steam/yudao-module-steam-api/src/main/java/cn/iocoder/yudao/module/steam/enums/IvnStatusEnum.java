package cn.iocoder.yudao.module.steam.enums;

import java.util.Arrays;
import java.util.Optional;

/**
 * 订单子状态
 */
public enum IvnStatusEnum {
    ACTIONING(1,"进行中"),
    DONE(2,"完成"),
    CANCEL(3,"作废");
    IvnStatusEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }
    private Integer code;
    private String name;

    public Integer getCode() {
        return code;
    }


    public String getName() {
        return name;
    }
    public static IvnStatusEnum findByCode(Integer code){
        Optional<IvnStatusEnum> first = Arrays.stream(values()).filter(item -> item.getCode().equals(code)).findFirst();
        if(first.isPresent()){
            return first.get();
        }
        return null;
    }
}
