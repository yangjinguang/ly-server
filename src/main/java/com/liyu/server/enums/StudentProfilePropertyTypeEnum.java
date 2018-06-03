package com.liyu.server.enums;

public enum StudentProfilePropertyTypeEnum {
    TEXT("字符串"),
    NUMBER("数字"),
    SELECT("下拉选择"),
    RADIO("单选"),
    CHECKBOX("复选");

    final String desc;

    StudentProfilePropertyTypeEnum(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }
}
