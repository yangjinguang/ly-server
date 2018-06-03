package com.liyu.server.service.impl;

import com.liyu.server.enums.StudentProfilePropertyTypeEnum;
import com.liyu.server.model.StudentProfile;
import com.liyu.server.model.StudentProfileProperty;
import com.liyu.server.service.StudentService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

import java.util.ArrayList;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class StudentServiceImplTest {
    @Resource
    private StudentService studentService;

    @Test
    public void createProfile() {
        ArrayList<Integer> position = new ArrayList<>();
        position.add(0);
        position.add(0);
        ArrayList<Integer> size = new ArrayList<>();
        size.add(1);
        size.add(1);
        StudentProfileProperty propertyName = new StudentProfileProperty("姓名", StudentProfilePropertyTypeEnum.TEXT, position, size, null, null);
        ArrayList<StudentProfileProperty> studentProfileProperties = new ArrayList<>();
        studentProfileProperties.add(propertyName);
        StudentProfile newProfile = new StudentProfile();
        newProfile.setName("模板");
        newProfile.setEnabled(true);
        newProfile.setTemplate(true);
        newProfile.setProperties(studentProfileProperties);
        newProfile.setTenantId("b2fe232eda174ce0813c97e20b7d53b9");
        studentService.createProfileTemplate(newProfile);
    }

    @Test
    public void updateProfileTemplate() {
        StudentProfile studentProfile = studentService.getProfileTemplate("b2fe232eda174ce0813c97e20b7d53b9");
        studentProfile.setName("模板-1");
        studentService.updateProfileTemplate(studentProfile.getId(), studentProfile);
    }
}