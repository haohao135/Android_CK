package com.example.myapplication.model;

import java.io.Serializable;

public class Actor implements Serializable {
    private String id;
    private String actorName;
    private String movie_id;

    public Actor() {
    }

    public Actor(String id, String actorName, String movie_id) {
        this.id = id;
        this.actorName = actorName;
        this.movie_id = movie_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getActorName() {
        return actorName;
    }

    public void setActorName(String actorName) {
        this.actorName = actorName;
    }

    public String getMovie_id() {
        return movie_id;
    }

    public void setMovie_id(String movie_id) {
        this.movie_id = movie_id;
    }

    @Override
    public String toString() {
        return "Actor{" +
                "id='" + id + '\'' +
                ", actorName='" + actorName + '\'' +
                ", movie_id='" + movie_id + '\'' +
                '}';
    }
}
