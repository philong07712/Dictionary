package com.example.dictionary.model;

import java.io.Serializable;

public class Word implements Serializable {
    private int id;
    private String content;
    private String definition;

    public Word(int id, String content, String definition) {
        this.id = id;
        this.content = content;
        this.definition = definition;
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
}
