package com.employee.cinderella.cinternalemp.model;

public class Poll {
    int id;
    String employee;
    String day;
    String question;
    String include;
    String optiona;

    String optionb;

    String optionc;

    String optiond;

    String answer ;
    String votesa;
    String votesb;
    String votesc;
    String votesd;



    public Poll(){

    }

    public Poll(int id,String employee,String day,String question,String include,String optiona,String optionb,String optionc,String optiond,String answer){
        this.id = id;
        this.employee = employee;
        this.day = day;
        this.question = question;
        this.include = include;

        this.optiona = optiona;
        this.optionb = optionb;
        this.optionc = optionc;
        this.optiond = optiond;



        this.answer = answer;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmployee() {
        return employee;
    }

    public void setEmployee(String employee) {
        this.employee = employee;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getOptiona() {
        return optiona;
    }

    public void setOptiona(String optiona) {
        this.optiona = optiona;
    }

    public String getOptionb() {
        return optionb;
    }

    public void setOptionb(String optionb) {
        this.optionb = optionb;
    }

    public String getOptionc() {
        return optionc;
    }

    public void setOptionc(String optionc) {
        this.optionc = optionc;
    }

    public String getOptiond() {
        return optiond;
    }

    public void setOptiond(String optiond) {
        this.optiond = optiond;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getVotesa() {
        return votesa;
    }

    public void setVotesa(String votesa) {
        this.votesa = votesa;
    }

    public String getVotesb() {
        return votesb;
    }

    public void setVotesb(String votesb) {
        this.votesb = votesb;
    }

    public String getVotesc() {
        return votesc;
    }

    public void setVotesc(String votesc) {
        this.votesc = votesc;
    }

    public String getVotesd() {
        return votesd;
    }

    public void setVotesd(String votesd) {
        this.votesd = votesd;
    }

    public String getInclude() {
        return include;
    }

    public void setInclude(String include) {
        this.include = include;
    }
}
