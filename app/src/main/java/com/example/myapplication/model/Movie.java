package com.example.myapplication.model;

import android.widget.ImageView;

import java.io.Serializable;
import java.util.List;

import kotlin.jvm.internal.PackageReference;

public class Movie implements Serializable {
    private String id;
    private String title;
    private String image;
    private double price;
    private String duration;
    private String director;
    private String genre;
    private String description;
    private String trailer;
    List<Showtime> showtimeList;

    public Movie() {
    }

    public Movie(String id, String title, String image, double price, String duration, String director, String genre, String description, String trailer) {
        this.id = id;
        this.title = title;
        this.image = image;
        this.price = price;
        this.duration = duration;
        this.director = director;
        this.genre = genre;
        this.description = description;
        this.trailer = trailer;
    }

    public String getTrailer() {
        return trailer;
    }

    public List<Showtime> getShowtimeList() {
        return showtimeList;
    }

    public void setShowtimeList(List<Showtime> showtimeList) {
        this.showtimeList = showtimeList;
    }

    public void setTrailer(String trailer) {
        this.trailer = trailer;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", image='" + image + '\'' +
                ", duration='" + duration + '\'' +
                ", director='" + director + '\'' +
                ", genre='" + genre + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
