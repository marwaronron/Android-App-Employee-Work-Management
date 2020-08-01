package com.employee.cinderella.cinternalemp.model;

public class RequestModel {

    int id;
    String type;
    String title;
    String desc;
    String employee_id;
    String status;
    String date;
    String privacy;

    public RequestModel(){

    }
    public RequestModel(int id,String type,String title,String desc,String employee_id,String status,String date, String privacy){
        this.id = id;
        this.type = type;
        this.title = title;
        this.desc = desc;
        this.employee_id = employee_id;
        this.status = status;
        this.date = date;
        this.privacy = privacy;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getEmployee_id() {
        return employee_id;
    }

    public void setEmployee_id(String employee_id) {
        this.employee_id = employee_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPrivacy() {
        return privacy;
    }

    public void setPrivacy(String privacy) {
        this.privacy = privacy;
    }
}
