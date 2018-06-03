package com.liyu.server.model;

public class StudentProfilePropertyOption {
    private String value;
    private String viewValue;

    public StudentProfilePropertyOption() {
    }

    public StudentProfilePropertyOption(String value, String viewValue) {
        this.value = value;
        this.viewValue = viewValue;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getViewValue() {
        return viewValue;
    }

    public void setViewValue(String viewValue) {
        this.viewValue = viewValue;
    }
}
