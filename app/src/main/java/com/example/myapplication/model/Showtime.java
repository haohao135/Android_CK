package com.example.myapplication.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Showtime implements Serializable {
    private String id;
    private String showDate;
    private String starTime;
    private String endTime;
    private String movie_id;
    private String theater_id;
    List<Booking> bookingList;

    public Showtime() {
    }

    public Showtime(String id, String showDate, String starTime, String endTime, String movie_id, String theater_id) {
        this.id = id;
        this.showDate = showDate;
        this.starTime = starTime;
        this.endTime = endTime;
        this.movie_id = movie_id;
        this.theater_id = theater_id;
        this.bookingList = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getShowDate() {
        return showDate;
    }

    public void setShowDate(String showDate) {
        this.showDate = showDate;
    }

    public String getStarTime() {
        return starTime;
    }

    public void setStarTime(String starTime) {
        this.starTime = starTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getMovie_id() {
        return movie_id;
    }

    public void setMovie_id(String movie_id) {
        this.movie_id = movie_id;
    }

    public String getTheater_id() {
        return theater_id;
    }

    public void setTheater_id(String theater_id) {
        this.theater_id = theater_id;
    }

    public List<Booking> getBookingList() {
        return bookingList;
    }

    public void setBookingList(List<Booking> bookingList) {
        this.bookingList = bookingList;
    }

    @Override
    public String toString() {
        return "Showtime{" +
                "id='" + id + '\'' +
                ", showDate='" + showDate + '\'' +
                ", starTime='" + starTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", movie_id='" + movie_id + '\'' +
                ", theater_id='" + theater_id + '\'' +
                ", bookingList=" + bookingList +
                '}';
    }
}
