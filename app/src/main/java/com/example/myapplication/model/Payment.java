package com.example.myapplication.model;

import java.io.Serializable;
import java.util.Date;

public class Payment implements Serializable {
    private String id;
    private double price_total;
    private String payment_method;
    private Date payment_date;
}
