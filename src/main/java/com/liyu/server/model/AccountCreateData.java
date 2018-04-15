package com.liyu.server.model;

public class AccountCreateData {
    private String username;
    private String password;
    private String phone;
    private String email;
    private Long tenantId;
    private Long organizationId;

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public Long getOrganizationId() {
        return organizationId;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }
}
