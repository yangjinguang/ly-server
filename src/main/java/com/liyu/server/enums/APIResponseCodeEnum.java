package com.liyu.server.enums;

public enum APIResponseCodeEnum {
    SUCCESS("请求成功"),
    TENANT_ID_CHECK_FAILED("tenant id 检查失败");
    final String desc;

    APIResponseCodeEnum(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }
}
