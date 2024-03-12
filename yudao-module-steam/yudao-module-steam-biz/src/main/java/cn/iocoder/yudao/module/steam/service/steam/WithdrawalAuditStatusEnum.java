package cn.iocoder.yudao.module.steam.service.steam;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum WithdrawalAuditStatusEnum {

    NOAUDIT(0, "不需要审核"),
    AUDIT(1, "审核中"),
    AUDIT_SUCCESS(2, "审核成功"),
    AUDIT_FAIL(3, "审核失败");
    /**
     * 状态值
     */
    private final Integer status;
    /**
     * 状态名
     */
    private final String name;
}
