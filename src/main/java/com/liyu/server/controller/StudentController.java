package com.liyu.server.controller;

import com.liyu.server.model.StudentProfile;
import com.liyu.server.service.StudentService;
import com.liyu.server.tables.pojos.Student;
import com.liyu.server.utils.APIResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@Api(value = "学生", description = "学生操作", tags = {"学生接口"})
@RestController
@RequestMapping(value = "/api/students")
public class StudentController {
    @Resource
    private StudentService studentService;

    @ApiOperation(value = "学生综合查询")
    @RequestMapping(value = "", method = RequestMethod.GET)
    public APIResponse query(@RequestHeader(value = "X-TENANT-ID") String tenantId,
                             @RequestParam(value = "organizationId", required = false) String organizationId,
                             @RequestParam(value = "page", defaultValue = "1") Integer page,
                             @RequestParam(value = "size", defaultValue = "20") Integer size) {
        Integer count = studentService.count(tenantId, organizationId);
        List<Student> students = studentService.query(tenantId, organizationId, (page - 1) * size, size);
        return APIResponse.withPagination(students, count, page, size);
    }

    @ApiOperation(value = "创建学生")
    @RequestMapping(value = "", method = RequestMethod.POST)
    public APIResponse create(@RequestBody Student newStudent) {
        Student student = studentService.create(newStudent);
        return APIResponse.success(student);
    }

    @ApiOperation(value = "更新学生信息")
    @RequestMapping(value = "/{studentId}", method = RequestMethod.PUT)
    public APIResponse create(@PathVariable(value = "studentId") String studentId,
                              @RequestBody Student newStudent) {
        Student student = studentService.update(studentId, newStudent);
        return APIResponse.success(student);
    }

    @ApiOperation(value = "删除学生")
    @RequestMapping(value = "/{studentId}", method = RequestMethod.DELETE)
    public APIResponse delete(@PathVariable(value = "studentId") String studentId) {
        studentService.delete(studentId);
        return APIResponse.success("success");
    }

    @ApiOperation(value = "获取学生信息模板")
    @RequestMapping(value = "/profile/template", method = RequestMethod.GET)
    public APIResponse getProfileTemplate(@RequestHeader(value = "X-TENANT-ID") String tenantId) {
        StudentProfile profileTemplate = studentService.getProfileTemplate(tenantId);
        return APIResponse.success(profileTemplate);
    }

    @ApiOperation(value = "更新学生信息模板")
    @RequestMapping(value = "/profile/template/{id}", method = RequestMethod.PUT)
    public APIResponse updateProfileTemplate(@PathVariable(value = "id") String id,
                                             @RequestBody StudentProfile newProfile) {
        StudentProfile studentProfile = studentService.updateProfileTemplate(id, newProfile);
        return APIResponse.success(studentProfile);
    }
}
