package com.jaxb.model;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.List;

/**
 * Отдел организации (JAXB). Коллекция сотрудников оборачивается тегом <employees>.
 */
@XmlRootElement(name = "department")
@XmlAccessorType(XmlAccessType.FIELD)
public class Department {
    private String departmentNumber;
    private String departmentName;
    private String location;

    @XmlElementWrapper(name = "employees")
    @XmlElement(name = "employee")
    private List<Employee> employees;

    public Department() {
    }

    public Department(String departmentNumber, String departmentName, String location) {
        this.departmentNumber = departmentNumber;
        this.departmentName = departmentName;
        this.location = location;
    }

    public String getDepartmentNumber() {
        return departmentNumber;
    }

    public void setDepartmentNumber(String departmentNumber) {
        this.departmentNumber = departmentNumber;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public List<Employee> getEmployees() {
        return employees;
    }

    public void setEmployees(List<Employee> employees) {
        this.employees = employees;
    }

    @Override
    public String toString() {
        return "Department{number=" + departmentNumber + ", name=" + departmentName +
                ", location=" + location + ", employees=" + employees + "}";
    }
}
