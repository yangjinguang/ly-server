package com.liyu.server.enums;

public enum ContactTypeEnum {
    STAFF("员工"),
    CUSTOMER("客户");

    final String desc;

    ContactTypeEnum(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }
}
