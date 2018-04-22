package com.liyu.server.utils;

import com.alibaba.fastjson.JSON;
import com.liyu.server.enums.APIResponseCodeEnum;
import com.liyu.server.enums.APIResponseStatusEnum;

public class APIResponse {
    private Integer code;
    private APIResponseStatusEnum status;
    private Object data;
    private String message;

    private APIResponse(APIResponseStatusEnum status, Object data) {
        this.code = APIResponseCodeEnum.SUCCESS.ordinal();
        this.status = status;
        this.data = data;
    }

    public APIResponse(APIResponseCodeEnum code, APIResponseStatusEnum status, Object data, String message) {
        this.code = code.ordinal();
        this.status = status;
        this.data = data;
        this.message = message;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public Integer getCode() {
        return code;
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

    public static APIResponse failed(APIResponseCodeEnum code, String message) {
        return new APIResponse(code, APIResponseStatusEnum.FAILED, null, message);
    }

    public String toJSON() {
        return JSON.toJSONString(this);
    }
}
