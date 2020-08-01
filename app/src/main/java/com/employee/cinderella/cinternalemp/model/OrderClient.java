package com.employee.cinderella.cinternalemp.model;

public class OrderClient {
    int id;
    String order_code;
    String prod_code;
    String acceptance;
    String 	internal_logs;
    String 	client_name;
    String country;
    String email;
    String phone;
    String 	ip_adress;
    String 	extra_details;
    String drag;
    String date;
    String employee;
    String drag_date;
    String drag_time;
    String close_date;
    String 	close_time;
    String note;
    String departmen;

    public OrderClient(){

    }

    public OrderClient(int id, String order_code, String prod_code, String acceptance, String internal_logs, String client_name, String country, String email, String phone, String ip_adress, String extra_details, String drag, String date, String employee, String drag_date, String drag_time, String close_date, String close_time, String note, String departmen) {
        this.id = id;
        this.order_code = order_code;
        this.prod_code = prod_code;
        this.acceptance = acceptance;
        this.internal_logs = internal_logs;
        this.client_name = client_name;
        this.country = country;
        this.email = email;
        this.phone = phone;
        this.ip_adress = ip_adress;
        this.extra_details = extra_details;
        this.drag = drag;
        this.date = date;
        this.employee = employee;
        this.drag_date = drag_date;
        this.drag_time = drag_time;
        this.close_date = close_date;
        this.close_time = close_time;
        this.note = note;
        this.departmen = departmen;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOrder_code() {
        return order_code;
    }

    public void setOrder_code(String order_code) {
        this.order_code = order_code;
    }

    public String getProd_code() {
        return prod_code;
    }

    public void setProd_code(String prod_code) {
        this.prod_code = prod_code;
    }

    public String getAcceptance() {
        return acceptance;
    }

    public void setAcceptance(String acceptance) {
        this.acceptance = acceptance;
    }

    public String getInternal_logs() {
        return internal_logs;
    }

    public void setInternal_logs(String internal_logs) {
        this.internal_logs = internal_logs;
    }

    public String getClient_name() {
        return client_name;
    }

    public void setClient_name(String client_name) {
        this.client_name = client_name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getIp_adress() {
        return ip_adress;
    }

    public void setIp_adress(String ip_adress) {
        this.ip_adress = ip_adress;
    }

    public String getExtra_details() {
        return extra_details;
    }

    public void setExtra_details(String extra_details) {
        this.extra_details = extra_details;
    }

    public String getDrag() {
        return drag;
    }

    public void setDrag(String drag) {
        this.drag = drag;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getEmployee() {
        return employee;
    }

    public void setEmployee(String employee) {
        this.employee = employee;
    }

    public String getDrag_date() {
        return drag_date;
    }

    public void setDrag_date(String drag_date) {
        this.drag_date = drag_date;
    }

    public String getDrag_time() {
        return drag_time;
    }

    public void setDrag_time(String drag_time) {
        this.drag_time = drag_time;
    }

    public String getClose_time() {
        return close_time;
    }

    public void setClose_time(String close_time) {
        this.close_time = close_time;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getDepartmen() {
        return departmen;
    }

    public void setDepartmen(String departmen) {
        this.departmen = departmen;
    }

    public String getClose_date() {
        return close_date;
    }

    public void setClose_date(String close_date) {
        this.close_date = close_date;
    }
}
