package com.example.myapplication.model;

import java.io.Serializable;
import java.util.Date;

public class Booking implements Serializable {
    private String id;
    private Date date_booking;
    private int seat_number;
    private String user_id;
    private String payment_id;
    private String showtime_id;

    public Booking() {
    }

    public Booking(String id, Date date_booking, int seat_number, String user_id, String payment_id, String showtime_id) {
        this.id = id;
        this.date_booking = date_booking;
        this.seat_number = seat_number;
        this.user_id = user_id;
        this.payment_id = payment_id;
        this.showtime_id = showtime_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getDate_booking() {
        return date_booking;
    }

    public void setDate_booking(Date date_booking) {
        this.date_booking = date_booking;
    }

    public int getSeat_number() {
        return seat_number;
    }

    public void setSeat_number(int seat_number) {
        this.seat_number = seat_number;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getPayment_id() {
        return payment_id;
    }

    public void setPayment_id(String payment_id) {
        this.payment_id = payment_id;
    }

    public String getShowtime_id() {
        return showtime_id;
    }

    public void setShowtime_id(String showtime_id) {
        this.showtime_id = showtime_id;
    }
}
