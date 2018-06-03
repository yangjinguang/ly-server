package com.liyu.server.service;

import com.liyu.server.model.StudentProfile;
import com.liyu.server.tables.pojos.Student;

import java.util.List;

public interface StudentService {

    Integer count(String tenantId, String organizationId);

    List<Student> query(String tenantId, String organizationId, Integer offset, Integer size);

    Student create(Student newStudent);

    Student update(String studentId, Student newStudent);

    void delete(String studentId);

    StudentProfile getProfileTemplate(String tenantId);

    StudentProfile createProfileTemplate(StudentProfile newProfile);

    StudentProfile updateProfileTemplate(String id, StudentProfile newProfile);
}
