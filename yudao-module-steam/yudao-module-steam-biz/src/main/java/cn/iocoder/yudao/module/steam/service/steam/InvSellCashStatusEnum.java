package cn.iocoder.yudao.module.steam.service.steam;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Optional;

@Getter
@AllArgsConstructor
public enum InvSellCashStatusEnum {

    INIT(0, "未收款"),
    CASHED(1, "已收款"),
    DAMAGES(2, "已退款并收取违约金");
    /**
     * 状态值
     */
    private final Integer status;
    /**
     * 状态名
     */
    private final String name;


    public static InvSellCashStatusEnum findByStatus(Integer status){
        Optional<InvSellCashStatusEnum> first = Arrays.stream(values()).filter(item -> item.getStatus().equals(status)).findFirst();
        if(first.isPresent()){
            return first.get();
        }
        return null;
    }
}
