package com.employee.cinderella.cinternalemp.model;

public class ExperienceModel {
    String skill;
    String nbTasksDone;

    public ExperienceModel() {
    }

    public ExperienceModel(String skill, String nbTasksDone) {
        this.skill = skill;
        this.nbTasksDone = nbTasksDone;
    }

    public String getSkill() {
        return skill;
    }

    public void setSkill(String skill) {
        this.skill = skill;
    }

    public String getNbTasksDone() {
        return nbTasksDone;
    }

    public void setNbTasksDone(String nbTasksDone) {
        this.nbTasksDone = nbTasksDone;
    }
}
