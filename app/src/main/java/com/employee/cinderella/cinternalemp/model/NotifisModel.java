package com.employee.cinderella.cinternalemp.model;

public class NotifisModel {
    int id;
    String title;
    String topic;
    String date;
    String time;
    String seen;
    String type;
    String employee;

    public NotifisModel() {
    }

    public NotifisModel(int id, String title, String topic, String date, String time, String seen, String type, String employee) {
        this.id = id;
        this.title = title;
        this.topic = topic;
        this.date = date;
        this.time = time;
        this.seen = seen;
        this.type = type;
        this.employee = employee;
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

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getSeen() {
        return seen;
    }

    public void setSeen(String seen) {
        this.seen = seen;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getEmployee() {
        return employee;
    }

    public void setEmployee(String employee) {
        this.employee = employee;
    }
}
