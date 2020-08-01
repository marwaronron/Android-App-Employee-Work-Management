package com.employee.cinderella.cinternalemp.model;

public class ReportOrder {
    int id;
    String employee_name;
    String report_txt;
    String date;
    String emp_id;
    String order_db_id;

    public ReportOrder(int id, String employee_name, String report_txt, String date) {
        this.id = id;
        this.employee_name = employee_name;
        this.report_txt = report_txt;
        this.date = date;
    }

    public ReportOrder(int id, String employee_name, String report_txt, String date, String emp_id) {
        this.id = id;
        this.employee_name = employee_name;
        this.report_txt = report_txt;
        this.date = date;
        this.emp_id = emp_id;
    }


    public ReportOrder(int id, String employee_name, String report_txt, String date, String emp_id, String order_db_id) {
        this.id = id;
        this.employee_name = employee_name;
        this.report_txt = report_txt;
        this.date = date;
        this.emp_id = emp_id;
        this.order_db_id = order_db_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmployee_name() {
        return employee_name;
    }

    public void setEmployee_name(String employee_name) {
        this.employee_name = employee_name;
    }

    public String getReport_txt() {
        return report_txt;
    }

    public void setReport_txt(String report_txt) {
        this.report_txt = report_txt;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }


    public String getEmp_id() {
        return emp_id;
    }

    public void setEmp_id(String emp_id) {
        this.emp_id = emp_id;
    }

    public String getOrder_db_id() {
        return order_db_id;
    }

    public void setOrder_db_id(String order_db_id) {
        this.order_db_id = order_db_id;
    }
}
