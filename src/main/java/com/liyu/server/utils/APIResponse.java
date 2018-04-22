package com.liyu.server.utils;

import com.alibaba.fastjson.JSON;
import com.liyu.server.enums.APIResponseStatusEnum;

public class APIResponse {
    private APIResponseStatusEnum status;
    private Object data;
    private String message;

    private APIResponse(APIResponseStatusEnum status, Object data) {
        this.status = status;
        this.data = data;
    }

    public APIResponse(APIResponseStatusEnum status, Object data, String message) {
        this.status = status;
        this.data = data;
        this.message = message;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setStatus(APIResponseStatusEnum status) {
        this.status = status;
    }

    public Object getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }

    public APIResponseStatusEnum getStatus() {
        return status;
    }

    public static APIResponse success(Object data) {
        return new APIResponse(APIResponseStatusEnum.SUCCESS, data);
    }

    public static APIResponse failed(Object data) {
        return new APIResponse(APIResponseStatusEnum.FAILED, data);
    }

    public String toJSON() {
        return JSON.toJSONString(this);
    }
}
