package com.liyu.server.enums;

public enum APIResponseStatusEnum {
    SUCCESS("请求成功"),
    FAILED("请求失败");
    final String desc;

    APIResponseStatusEnum(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }
}
