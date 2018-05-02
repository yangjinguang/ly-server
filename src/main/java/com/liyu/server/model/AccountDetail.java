package com.liyu.server.model;

import com.liyu.server.tables.Role;
import com.liyu.server.tables.pojos.Account;

import java.util.List;

public class AccountDetail extends Account {
    private List<String> organizationIds;
    private List<OrganizationDetail> organizations;
    private List<Role> roles;

    public AccountDetail() {
    }

    public AccountDetail(Account value) {
        super(value);
    }

    public void setOrganizationIds(List<String> organizaitonIds) {
        this.organizationIds = organizaitonIds;
    }

    public List<String> getOrganizationIds() {
        return organizationIds;
    }

    public List<OrganizationDetail> getOrganizations() {
        return organizations;
    }

    public void setOrganizations(List<OrganizationDetail> organizations) {
        this.organizations = organizations;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }
}
