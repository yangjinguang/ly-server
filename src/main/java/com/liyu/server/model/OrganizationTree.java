package com.liyu.server.model;

import com.liyu.server.tables.pojos.Organization;

import java.util.List;

public class OrganizationTree extends Organization {
    private Integer numberOfChildren;
    private List<OrganizationTree> children;

    public void setNumberOfChildren(Integer numberOfChildren) {
        this.numberOfChildren = numberOfChildren;
    }

    public Integer getNumberOfChildren() {
        return numberOfChildren;
    }

    public OrganizationTree(Organization organization) {
        super(organization);
    }

    public void setChildren(List<OrganizationTree> children) {
        this.children = children;
    }

    public List<OrganizationTree> getChildren() {
        return children;
    }
}
