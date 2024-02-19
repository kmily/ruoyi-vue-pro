package cn.iocoder.yudao.module.steam.service.steam;

public enum ConfirmAction {
    /**
     * 页面传入参数为allow和reject
     */
//    ALLOW("确认","allow"),ACCEPT("确认","accept"),REJECT("拒绝","reject"),CANCEL("拒绝","cancel");
    ALLOW("确认","allow"),CANCEL("拒绝","cancel");
    private String name;
    private String code;

    ConfirmAction(String name, String code) {
        this.name = name;
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }
}
