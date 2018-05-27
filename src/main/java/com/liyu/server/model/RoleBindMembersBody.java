package com.liyu.server.model;

import java.util.List;

public class RoleBindMembersBody {
    private List<String> accountIds;

    public List<String> getAccountIds() {
        return accountIds;
    }

    public void setAccountIds(List<String> accountIds) {
        this.accountIds = accountIds;
    }
}
