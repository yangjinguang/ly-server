package com.liyu.server.model;

import com.liyu.server.tables.pojos.Contact;

import java.util.List;

public class ContactCreateBody extends Contact {
    private String username;
    private List<String> organizationIds;
    private List<String> roleIds;

    public ContactCreateBody() {
    }

    public ContactCreateBody(Contact value) {
        super(value);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public void setRoleIds(List<String> roleIds) {
        this.roleIds = roleIds;
    }
}
