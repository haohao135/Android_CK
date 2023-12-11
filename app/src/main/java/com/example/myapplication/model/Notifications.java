package com.example.myapplication.model;

import java.io.Serializable;
import java.util.Date;

public class Notifications implements Serializable {
    private String id;
    private String message;
    private Date dateNotify;
    private Date timeNotify;
    private String user_id;
}
