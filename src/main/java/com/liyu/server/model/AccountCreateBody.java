package com.liyu.server.model;

import com.liyu.server.tables.pojos.Account;

import java.util.List;

public class AccountCreateBody {
    private Account account;
    private List<String> organizationIds;
    private List<String> roleIds;

    public void setAccount(Account account) {
        this.account = account;
    }

    public Account getAccount() {
        return account;
    }

    public List<String> getOrganizationIds() {
        return organizationIds;
    }

    public void setOrganizationIds(List<String> organizationIds) {
        this.organizationIds = organizationIds;
    }

    public List<String> getRoleIds() {
        return roleIds;
    }
}
