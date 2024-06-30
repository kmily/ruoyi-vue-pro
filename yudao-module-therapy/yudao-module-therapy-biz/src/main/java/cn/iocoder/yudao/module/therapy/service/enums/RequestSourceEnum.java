package cn.iocoder.yudao.module.therapy.service.enums;

public enum RequestSourceEnum {

    MAIN_PROCESS(1, "主流程"),
    TOOLBOX(2, "工具箱");

    private final Integer value;
    private final String name;

    RequestSourceEnum(Integer value, String name) {
        this.value = value;
        this.name = name;
    }

    public Integer getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

    public static RequestSourceEnum fromValue(Integer value) {
        for (RequestSourceEnum source : RequestSourceEnum.values()) {
            if (source.getValue().equals(value)) {
                return source;
            }
        }
        return null;
    }

    public static RequestSourceEnum fromName(String name) {
        for (RequestSourceEnum source : RequestSourceEnum.values()) {
            if (source.getName().equals(name)) {
                return source;
            }
        }
        return null;
    }
}
