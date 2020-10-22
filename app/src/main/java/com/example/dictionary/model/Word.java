package com.example.dictionary.model;

import java.io.Serializable;

public class Word implements Serializable {
    private int type;
    private int id;
    private String content;
    private String definition;

    public Word(int type, int id, String content, String definition) {
        this.id = id;
        this.content = content;
        this.definition = definition;
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
