package com.lbttecnology.persistence;

/**
 * Created by irene on 24/12/2014.
 */
public class Notes {
    private Long id;
    private String title;
    private String description;
    private int concluded;

    public Notes(){}

    public Notes(Long id, String title, String description, int concluded){
        this.id = id;
        this.title = title;
        this.description = description;
        this.concluded = concluded;
    }

    public Notes(String title, String description, int concluded){
        this.title = title;
        this.description = description;
        this.concluded = concluded;
    }

    public Notes(String title, String description){
        this.title = title;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public int isConcluded() {
        return concluded;
    }

    public void setConcluded(int concluded) {
        this.concluded = concluded;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

