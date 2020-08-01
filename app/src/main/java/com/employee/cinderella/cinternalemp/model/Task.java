package com.employee.cinderella.cinternalemp.model;

public class Task {
    int id ;
    String title;
    String start_date;
    String deadline_date;
    String finish_date;

    String status;
    String project_id;
    String employee_id;
    String skill;



    public Task() {
    }
    public Task(String title) {
        this.title = title;
    }
    public Task(int id, String title, String start_date, String deadline_date, String finish_date,  String status, String project_id, String employee_id) {
        this.id = id;
        this.title = title;
        this.start_date = start_date;
        this.deadline_date = deadline_date;
        this.finish_date = finish_date;

        this.status = status;
        this.project_id = project_id;
        this.employee_id = employee_id;


    }
    public Task(int id, String title, String start_date, String deadline_date, String finish_date,  String status, String project_id, String employee_id, String skill) {
        this.id = id;
        this.title = title;
        this.start_date = start_date;
        this.deadline_date = deadline_date;
        this.finish_date = finish_date;

        this.status = status;
        this.project_id = project_id;
        this.employee_id = employee_id;
        this.skill = skill;


    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getDeadline_date() {
        return deadline_date;
    }

    public void setdeaDline_date(String deadline_date) {
        this.deadline_date = deadline_date;
    }

    public String getFinish_date() {
        return finish_date;
    }

    public void setFinish_date(String finish_date) {
        this.finish_date = finish_date;
    }



    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getProject_id() {
        return project_id;
    }

    public void setProject_id(String project_id) {
        this.project_id = project_id;
    }

    public String getEmployee_id() {
        return employee_id;
    }

    public void setEmployee_id(String employee_id) {
        this.employee_id = employee_id;
    }

    public String getSkill() {
        return skill;
    }

    public void setSkill(String skill) {
        this.skill = skill;
    }
}
