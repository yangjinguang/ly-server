package com.liyu.server.model;

import com.liyu.server.tables.Role;
import com.liyu.server.tables.pojos.Contact;

import java.util.List;

public class ContactDetail extends Contact {
    private List<String> organizationIds;
    private List<OrganizationDetail> organizations;
    private List<Role> roles;

    public ContactDetail() {
    }

    public ContactDetail(Contact value) {
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
