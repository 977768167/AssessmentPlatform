package com.example.assessmentalatform.bean;

public class MultipleChoice {

    private Integer questionId;
    private String question;
    private String A;
    private String B;
    private String C;
    private String D;
    private String answer;//正确答案
    private String difficulty;//困难程度
    private String uploadTeacherNumber;//上传教师编号
    private String courseName;//所属课程

    public Integer getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Integer questionId) {
        this.questionId = questionId;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getA() {
        return A;
    }

    public void setA(String a) {
        A = a;
    }

    public String getB() {
        return B;
    }

    public void setB(String b) {
        B = b;
    }

    public String getC() {
        return C;
    }

    public void setC(String c) {
        C = c;
    }

    public String getD() {
        return D;
    }

    public void setD(String d) {
        D = d;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public String getUploadTeacherNumber() {
        return uploadTeacherNumber;
    }

    public void setUploadTeacherNumber(String uploadTeacherNumber) {
        this.uploadTeacherNumber = uploadTeacherNumber;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }
}
