package com.dom.model;

/**
 * Модель «Сотрудник». В дополнение к базовым полям (id — атрибут тега,
 * name, age, gender, role) добавлены поля salary, email и department.
 */
public class Employee {
    private int id;
    private String name;
    private int age;
    private String gender;
    private String role;
    private double salary;
    private String email;
    private String department;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    @Override
    public String toString() {
        return "Employee[id=" + getId() + ", name=" + getName() + ", gender=" + getGender() + ", age=" +
                getAge() + ", role=" + getRole() + ", salary=" + getSalary() + ", email=" +
                getEmail() + ", department=" + getDepartment() + "]";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
