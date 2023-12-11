package com.example.myapplication.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class Showtime implements Serializable {
    private String id;
    private Date showDate;
    private String starTime;
    private String endTime;
    private String movie_id;
    private String theater_id;
    List<Booking> bookingList;
}
