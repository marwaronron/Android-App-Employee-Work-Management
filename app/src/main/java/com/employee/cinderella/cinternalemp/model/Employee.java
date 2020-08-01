package com.employee.cinderella.cinternalemp.model;
public class Employee {

    public static Employee session;

    int id ;
    String full_name;
    String position;
    String workphone;
    String email;
    String address;
    String status;
    String pwd;
    String photo;
    String department_id;


    public Employee() {
    }
    public Employee(String full_name) {
        this.full_name = full_name;
    }
    public Employee(int id, String full_name, String position, String workphone, String email, String address, String status, String pwd, String photo, String department_id) {
        this.id = id;
        this.full_name = full_name;
        this.position = position;
        this.workphone = workphone;
        this.email = email;
        this.address = address;
        this.status = status;
        this.pwd = pwd;
        this.photo = photo;
        this.department_id = department_id;

    }
    public Employee(int id, String full_name, String position,  String department_id) {
        this.id = id;
        this.full_name = full_name;
        this.position = position;
        this.department_id = department_id;

    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getWorkphone() {
        return workphone;
    }

    public void setWorkphone(String workphone) {
        this.workphone = workphone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getDepartment_id() {
        return department_id;
    }

    public void setDepartment_id(String department_id) {
        this.department_id = department_id;
    }


}
