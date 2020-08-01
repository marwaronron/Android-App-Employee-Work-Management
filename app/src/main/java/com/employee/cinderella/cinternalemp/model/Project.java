package com.employee.cinderella.cinternalemp.model;

public class Project {

 
        int id;
        String Name;
        String Description;
        String Start_date;
        String Deadline_date;
        String Finish_date;
        String Status;
        String Department_id;

        public Project() {
        }

    public Project(int id,String name, String description,  String start_date , String deadline_date, String finish_date, String status, String department_id) {
        this.id = id;
        this.Name = name;
        this.Description = description;
        this.Start_date = start_date;
        this.Deadline_date = deadline_date;
        this.Finish_date = finish_date;
        this.Status = status;
        this.Department_id = department_id;
    }
        public Project(String name, String description,  String start_date , String deadline_date, String finish_date, String status, String department_id) {
            Name = name;
            Description = description;
            Start_date = start_date;
            Deadline_date = deadline_date;
            Finish_date = finish_date;
            Status = status;
            Department_id = department_id;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return Name;
        }

        public void setName(String name) {
            Name = name;
        }



        public String getDescription() {
            return Description;
        }

        public void setDescription(String description) {
            Description = description;
        }

        public String getStart_date() {
            return Start_date;
        }

        public void setStart_date(String start_date) {
            Start_date = start_date;
        }

        public String getDeadline_date() {
            return Deadline_date;
        }

        public void setDeadline_date(String deadline_date) {
            Deadline_date = deadline_date;
        }

        public String getFinish_date() {
            return Finish_date;
        }

        public void setFinish_date(String finish_date) {
            Finish_date = finish_date;
        }

        public String getStatus() {
            return Status;
        }

        public void setStatus(String status) {
            Status = status;
        }

        public String getDepartment_id() {
            return Department_id;
        }

        public void setDepartment_id(String department_id) {
            this.Department_id = department_id;
        }


}
