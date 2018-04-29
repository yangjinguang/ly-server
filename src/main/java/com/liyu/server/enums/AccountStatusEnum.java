package com.liyu.server.enums;

public enum AccountStatusEnum {
    NORMAL("正常"),
    BLOCKED("已停用"),
    DELETED("已删除");

    final String desc;

    AccountStatusEnum(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }
}
