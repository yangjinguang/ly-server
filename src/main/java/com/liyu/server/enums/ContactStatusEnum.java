package com.liyu.server.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum ContactStatusEnum {
    NORMAL("正常"),
    BLOCKED("已停用"),
    DELETED("已删除");

    final String desc;

    ContactStatusEnum(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }

    @JsonValue
    public int toValue() {
        return ordinal();
    }
}
