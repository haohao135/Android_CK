package com.example.myapplication.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Theater implements Serializable {
    private String id;
    private String name;
    private String address;
    List<Showtime> showtimeList;
    List<Seat> seatList;

    public Theater() {
    }

    public Theater(String id, String name, String address, List<Showtime> showtimeList, List<Seat> seatList) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.showtimeList = showtimeList;
        this.seatList = seatList;
    }

    public Theater(String id, String name, String address, List<Seat> seatList) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.seatList = seatList;
        this.showtimeList  = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<Showtime> getShowtimeList() {
        return showtimeList;
    }

    public void setShowtimeList(List<Showtime> showtimeList) {
        this.showtimeList = showtimeList;
    }

    public List<Seat> getSeatList() {
        return seatList;
    }

    public void setSeatList(List<Seat> seatList) {
        this.seatList = seatList;
    }
}
