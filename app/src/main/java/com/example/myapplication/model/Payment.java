package com.example.myapplication.model;

import java.io.Serializable;
import java.util.Date;

public class Payment implements Serializable {
    private String id;
    private double price_total;
    private String payment_method;
    private Date payment_date;

    public Payment() {
    }

    public Payment(String id, double price_total, String payment_method, Date payment_date) {
        this.id = id;
        this.price_total = price_total;
        this.payment_method = payment_method;
        this.payment_date = payment_date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getPrice_total() {
        return price_total;
    }

    public void setPrice_total(double price_total) {
        this.price_total = price_total;
    }

    public String getPayment_method() {
        return payment_method;
    }

    public void setPayment_method(String payment_method) {
        this.payment_method = payment_method;
    }

    public Date getPayment_date() {
        return payment_date;
    }

    public void setPayment_date(Date payment_date) {
        this.payment_date = payment_date;
    }
}
