package com.liyu.server.model;

import com.liyu.server.tables.pojos.Organization;

import java.util.List;

public class OrganizationTree extends Organization {
    private List<OrganizationTree> children;

    public OrganizationTree(Organization organization) {
        this.setId(organization.getId());
        this.setOrganizationId(organization.getOrganizationId());
        this.setParentId(organization.getParentId());
        this.setName(organization.getName());
        this.setDescription(organization.getDescription());
        this.setIsClass(organization.getIsClass());
        this.setIsRoot(organization.getIsRoot());
        this.setAvatar(organization.getAvatar());
        this.setTenantId(organization.getTenantId());
        this.setEnabled(organization.getEnabled());
        this.setCreatedAt(organization.getCreatedAt());
        this.setUpdatedAt(organization.getUpdatedAt());
    }

    public void setChildren(List<OrganizationTree> children) {
        this.children = children;
    }

    public List<OrganizationTree> getChildren() {
        return children;
    }
}
