package com.liyu.server.model;

import java.util.List;

public class RoleBindMembersBody {
    private List<String> contactIds;

    public List<String> getContactIds() {
        return contactIds;
    }

    public void setContactIds(List<String> contactIds) {
        this.contactIds = contactIds;
    }
}
