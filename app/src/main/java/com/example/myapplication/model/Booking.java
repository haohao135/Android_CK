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
}
