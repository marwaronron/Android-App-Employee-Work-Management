package com.employee.cinderella.cinternalemp.model;

public class Meeting {
    int id;
    String day;
    String start_hour;
    String 	end_hour;
    String topic;
    String agenda;
    String decision;
    String 	location;
    String is_present;

    public Meeting(){

    }
    public Meeting(int id,   String day,String start_hour,String end_hour,String topic, String agenda,String decision,String location){
        this.id = id;
        this.day = day;
        this.start_hour = start_hour;
        this.end_hour = end_hour;
        this.topic = topic;
        this.agenda = agenda;
        this.decision = decision;
        this.location = location;

    }

    public Meeting(int id, String day, String start_hour, String end_hour, String topic, String agenda, String decision, String location, String is_present) {
        this.id = id;
        this.day = day;
        this.start_hour = start_hour;
        this.end_hour = end_hour;
        this.topic = topic;
        this.agenda = agenda;
        this.decision = decision;
        this.location = location;
        this.is_present = is_present;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getStart_hour() {
        return start_hour;
    }

    public void setStart_hour(String start_hour) {
        this.start_hour = start_hour;
    }

    public String getEnd_hour() {
        return end_hour;
    }

    public void setEnd_hour(String end_hour) {
        this.end_hour = end_hour;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getAgenda() {
        return agenda;
    }

    public void setAgenda(String agenda) {
        this.agenda = agenda;
    }

    public String getDecision() {
        return decision;
    }

    public void setDecision(String decision) {
        this.decision = decision;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getIs_present() {
        return is_present;
    }

    public void setIs_present(String is_present) {
        this.is_present = is_present;
    }
}
