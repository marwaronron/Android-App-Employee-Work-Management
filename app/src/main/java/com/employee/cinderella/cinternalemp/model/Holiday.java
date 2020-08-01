package com.employee.cinderella.cinternalemp.model;

public class Holiday {
    int id;
    String type;
    String start_date;
    String end_date;
    String status;
    String reason;

    String employee_id;

    public Holiday(){}
    public Holiday(int id,String type,String start_date,String end_date,String status,String reason,String employee_id){
        this.id = id;
        this.type = type;
        this.start_date = start_date;
        this.end_date = end_date;
        this.status = status;
        this.reason = reason;
        this.employee_id = employee_id;
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

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getEmployee_id() {
        return employee_id;
    }

    public void setEmployee_id(String employee_id) {
        this.employee_id = employee_id;
    }
}
