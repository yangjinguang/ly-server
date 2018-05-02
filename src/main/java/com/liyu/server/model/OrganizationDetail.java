package com.liyu.server.model;

import com.liyu.server.tables.pojos.Organization;

import java.util.List;

public class OrganizationDetail extends Organization {
    private List<String> route;

    public OrganizationDetail(Organization organization) {
        super(organization);
    }

    public void setRoute(List<String> route) {
        this.route = route;
    }

    public List<String> getRoute() {
        return route;
    }
}
