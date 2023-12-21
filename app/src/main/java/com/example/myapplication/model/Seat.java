package com.example.myapplication.model;

import java.io.Serializable;

public class Seat implements Serializable {
    private String id;
    private int seat_number;
    private int status;
    private String theater_id;

    public Seat() {
    }

    public Seat(String id, int seat_number, int status, String theater_id) {
        this.id = id;
        this.seat_number = seat_number;
        this.status = status;
        this.theater_id = theater_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getSeat_number() {
        return seat_number;
    }

    public void setSeat_number(int seat_number) {
        this.seat_number = seat_number;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getTheater_id() {
        return theater_id;
    }

    public void setTheater_id(String theater_id) {
        this.theater_id = theater_id;
    }

    public int getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return "Seat{" +
                "id='" + id + '\'' +
                ", seat_number=" + seat_number +
                ", status=" + status +
                ", theater_id='" + theater_id + '\'' +
                '}';
    }
}
