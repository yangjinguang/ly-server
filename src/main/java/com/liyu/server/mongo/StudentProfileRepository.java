package com.liyu.server.mongo;

import com.liyu.server.model.StudentProfile;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface StudentProfileRepository extends MongoRepository<StudentProfile, String> {
    StudentProfile findFirstByTenantIdAndTemplateIsTrueAndEnabledIsTrue(String tenantId);
}
