package com.liyu.server.model;

import com.liyu.server.tables.Role;
import com.liyu.server.tables.pojos.Account;
import com.liyu.server.tables.pojos.Organization;

import java.util.List;

public class AccountDetail extends Account {
    private List<Organization> organizations;
    private List<Role> roles;

    public AccountDetail(Account value) {
        super(value);
    }

    public List<Organization> getOrganizations() {
        return organizations;
    }

    public void setOrganizations(List<Organization> organizations) {
        this.organizations = organizations;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }
}
