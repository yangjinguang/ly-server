package com.liyu.server.service.impl;

import com.liyu.server.model.StudentProfile;
import com.liyu.server.model.StudentProfileProperty;
import com.liyu.server.mongo.StudentProfilePropertyRepository;
import com.liyu.server.mongo.StudentProfileRepository;
import com.liyu.server.service.StudentService;
import com.liyu.server.tables.pojos.Student;
import com.liyu.server.tables.records.StudentRecord;
import com.liyu.server.utils.CommonUtils;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.exception.NoDataFoundException;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.List;

import static com.liyu.server.tables.Student.STUDENT;

@Service
public class StudentServiceImpl implements StudentService {
    @Resource
    private DSLContext context;
    @Resource
    private MongoTemplate mongoTemplate;
    @Resource
    private StudentProfileRepository studentProfileRepository;
    @Resource
    private StudentProfilePropertyRepository studentProfilePropertyRepository;

    @Override
    public Integer count(String tenantId, String organizationId) {
        if (tenantId == null || tenantId.isEmpty()) {
            throw new IllegalArgumentException("缺少tenantId");
        }
        HashSet<Condition> conditions = new HashSet<>();
        conditions.add(STUDENT.TENANT_ID.eq(tenantId));
        if (organizationId != null && !organizationId.isEmpty()) {
            conditions.add(STUDENT.ORGANIZATION_ID.eq(organizationId));
        }
        return context.selectCount().from(STUDENT).where(conditions).fetchOne().into(int.class);
    }

    @Override
    public List<Student> query(String tenantId, String organizationId, Integer offset, Integer size) {
        if (tenantId == null || tenantId.isEmpty()) {
            throw new IllegalArgumentException("缺少tenantId");
        }
        HashSet<Condition> conditions = new HashSet<>();
        conditions.add(STUDENT.TENANT_ID.eq(tenantId));
        if (organizationId != null && !organizationId.isEmpty()) {
            conditions.add(STUDENT.ORGANIZATION_ID.eq(organizationId));
        }
        return context.selectFrom(STUDENT)
                .where(conditions)
                .offset(offset)
                .limit(size)
                .fetch()
                .into(Student.class);
    }

    @Override
    public Student create(Student newStudent) {
        return context.insertInto(STUDENT)
                .columns(
                        STUDENT.STUDENT_ID,
                        STUDENT.ACCOUNT_ID,
                        STUDENT.AVATAR,
                        STUDENT.ORGANIZATION_ID,
                        STUDENT.PROFILE_ID,
                        STUDENT.TENANT_ID,
                        STUDENT.NAME,
                        STUDENT.ENABLED
                )
                .values(
                        CommonUtils.UUIDGenerator(),
                        newStudent.getAccountId(),
                        newStudent.getAvatar(),
                        newStudent.getOrganizationId(),
                        newStudent.getProfileId(),
                        newStudent.getTenantId(),
                        newStudent.getName(),
                        true
                ).returning()
                .fetchOne()
                .into(Student.class);
    }

    @Override
    public Student update(String studentId, Student newStudent) {
        StudentRecord studentRecord = context.selectFrom(STUDENT)
                .where(STUDENT.STUDENT_ID.eq(studentId))
                .fetchOptional()
                .orElseThrow(() -> new NoDataFoundException("未找到此学生"));
        String name = newStudent.getName();
        if (name != null && !name.isEmpty()) {
            studentRecord.setName(name);
        }
        String accountId = newStudent.getAccountId();
        if (accountId != null && !accountId.isEmpty()) {
            studentRecord.setAccountId(accountId);
        }
        String avatar = newStudent.getAvatar();
        if (avatar != null && !avatar.isEmpty()) {
            studentRecord.setAvatar(avatar);
        }
        String profileId = newStudent.getProfileId();
        if (profileId != null && !profileId.isEmpty()) {
            studentRecord.setProfileId(profileId);
        }
        String organizationId = newStudent.getOrganizationId();
        if (organizationId != null && !organizationId.isEmpty()) {
            studentRecord.setOrganizationId(organizationId);
        }
        studentRecord.update();
        return studentRecord.into(Student.class);
    }

    @Override
    public void delete(String studentId) {
        context.deleteFrom(STUDENT).where(STUDENT.STUDENT_ID.eq(studentId)).execute();
    }

    @Override
    public StudentProfile getProfileTemplate(String tenantId) {
        return studentProfileRepository.findFirstByTenantIdAndTemplateIsTrueAndEnabledIsTrue(tenantId);
    }

    @Override
    public StudentProfile createProfileTemplate(StudentProfile newProfile) {
        StudentProfile profile = studentProfileRepository.findFirstByTenantIdAndTemplateIsTrueAndEnabledIsTrue(newProfile.getTenantId());
        if (profile != null) {
            throw new IllegalArgumentException("不能重复创建");
        }
        studentProfilePropertyRepository.save(newProfile.getProperties());
        return studentProfileRepository.save(newProfile);
    }

    @Override
    public StudentProfile updateProfileTemplate(String id, StudentProfile newProfile) {
        StudentProfile profile = studentProfileRepository.findOne(id);
        if (profile == null) {
            throw new IllegalArgumentException("模板不存在");
        }
        studentProfilePropertyRepository.save(newProfile.getProperties());
        return studentProfileRepository.save(newProfile);
    }

}
