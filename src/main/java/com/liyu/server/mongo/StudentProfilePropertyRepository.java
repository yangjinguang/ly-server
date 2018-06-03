package com.liyu.server.mongo;

import com.liyu.server.model.StudentProfileProperty;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface StudentProfilePropertyRepository extends MongoRepository<StudentProfileProperty, String> {
}
