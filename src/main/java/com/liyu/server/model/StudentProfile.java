package com.liyu.server.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.util.List;

public class StudentProfile {
    @Id
    private String id;
    private String studentId;
    private String name;
    private String tenantId;
    private Boolean enabled;
    private Boolean template;
    @DBRef
    private List<StudentProfileProperty> properties;

    public StudentProfile(String id, String studentId, String name, String tenantId, Boolean enabled, Boolean template, List<StudentProfileProperty> property) {
        this.id = id;
        this.studentId = studentId;
        this.name = name;
        this.tenantId = tenantId;
        this.enabled = enabled;
        this.template = template;
        this.properties = property;
    }

    public StudentProfile() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Boolean getTemplate() {
        return template;
    }

    public void setTemplate(Boolean template) {
        this.template = template;
    }

    public List<StudentProfileProperty> getProperties() {
        return properties;
    }

    public void setProperties(List<StudentProfileProperty> properties) {
        this.properties = properties;
    }
}
