package com.liyu.server.model;

import com.liyu.server.enums.AccountStatusEnum;

public class AccountChangeStatusBody {
    private AccountStatusEnum status;

    public AccountStatusEnum getStatus() {
        return status;
    }

    public void setStatus(AccountStatusEnum status) {
        this.status = status;
    }
}
