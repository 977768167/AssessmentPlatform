package com.example.assessmentalatform.bean;

public class ProblemDifficulty {

    private String subject;
    private Integer simple;
    private Integer medium;
    private Integer difficulty;

    public Integer getSimple() {
        return simple;
    }

    public void setSimple(Integer simple) {
        this.simple = simple;
    }

    public Integer getMedium() {
        return medium;
    }

    public void setMedium(Integer medium) {
        this.medium = medium;
    }

    public Integer getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(Integer difficulty) {
        this.difficulty = difficulty;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }
}
