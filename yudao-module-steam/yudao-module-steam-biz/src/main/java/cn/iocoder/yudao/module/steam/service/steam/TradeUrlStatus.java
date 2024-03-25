package cn.iocoder.yudao.module.steam.service.steam;

public enum TradeUrlStatus {
    OK(1,"正常"),ERRORURL(1,"交易链接格式错误"),BUSY(3,"请稍后重试"),
    DISALLOW(4,"账号交易权限被封禁，无法交易"),NotAvailable(5,"该交易链接已不再可用"),
    PRIVATE(6,"该账户库存私密无法交易"),PRIVATE2(7,"该账户库存私密无法交易"),LIMIT(8,"此用户帐户功能受限");
    private Integer status;
    private String message;


    TradeUrlStatus(Integer status, String message) {
        this.status = status;
        this.message = message;
    }

    public Integer getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}
