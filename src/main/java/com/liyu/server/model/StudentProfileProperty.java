package com.liyu.server.model;

import com.liyu.server.enums.StudentProfilePropertyTypeEnum;
import org.springframework.data.annotation.Id;

import java.util.List;

public class StudentProfileProperty {
    @Id
    private String id;
    private String name;
    private StudentProfilePropertyTypeEnum type;
    private List<Integer> position;
    private List<Integer> size;
    private Object defaultValue;
    private List<StudentProfilePropertyOption> options;

    public StudentProfileProperty() {
    }

    public StudentProfileProperty(String name, StudentProfilePropertyTypeEnum type, List<Integer> position, List<Integer> size, Object defaultValue, List<StudentProfilePropertyOption> options) {
        this.name = name;
        this.type = type;
        this.position = position;
        this.size = size;
        this.defaultValue = defaultValue;
        this.options = options;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public StudentProfilePropertyTypeEnum getType() {
        return type;
    }

    public void setType(StudentProfilePropertyTypeEnum type) {
        this.type = type;
    }

    public List<Integer> getPosition() {
        return position;
    }

    public void setPosition(List<Integer> position) {
        this.position = position;
    }

    public List<Integer> getSize() {
        return size;
    }

    public void setSize(List<Integer> size) {
        this.size = size;
    }

    public Object getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(Object defaultValue) {
        this.defaultValue = defaultValue;
    }

    public List<StudentProfilePropertyOption> getOptions() {
        return options;
    }

    public void setOptions(List<StudentProfilePropertyOption> options) {
        this.options = options;
    }

    @Override
    public String toString() {
        return "StudentProfileProperty{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", type=" + type +
                ", position=" + position +
                ", size=" + size +
                ", defaultValue=" + defaultValue +
                ", options=" + options +
                '}';
    }
}
