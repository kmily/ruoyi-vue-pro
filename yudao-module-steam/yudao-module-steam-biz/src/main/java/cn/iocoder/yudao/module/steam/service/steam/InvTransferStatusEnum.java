package cn.iocoder.yudao.module.steam.service.steam;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum InvTransferStatusEnum {

    INIT(0, "未出售"),
    SELL(1, "出售中"),
    INORDER(2, "下单"),
    TransferFINISH(3, "发货完成"),
    OFFSHELF(4, "已下架"),
    TransferERROR(10,"发货失败"),
    CLOSE(20,"关闭");
    /**
     * 状态值
     */
    private final Integer status;
    /**
     * 状态名
     */
    private final String name;
}
