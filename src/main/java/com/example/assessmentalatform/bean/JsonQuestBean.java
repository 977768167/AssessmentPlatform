package com.example.assessmentalatform.bean;

import java.util.List;

public class JsonQuestBean {

    private String status;
    private String code;
    private List<QuestBean> messages;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<QuestBean> getMessages() {
        return messages;
    }

    public void setMessages(List<QuestBean> messages) {
        this.messages = messages;
    }
}
