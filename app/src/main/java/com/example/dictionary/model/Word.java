package com.example.dictionary.model;

import java.io.Serializable;

public class Word implements Serializable {
    private String content;
    private String definition;

    public Word(String content, String definition) {
        this.content = content;
        this.definition = definition;
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
