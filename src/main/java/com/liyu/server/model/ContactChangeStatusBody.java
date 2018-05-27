package com.liyu.server.model;

import com.liyu.server.enums.ContactStatusEnum;

public class ContactChangeStatusBody {
    private ContactStatusEnum status;

    public ContactStatusEnum getStatus() {
        return status;
    }

    public void setStatus(ContactStatusEnum status) {
        this.status = status;
    }
}
