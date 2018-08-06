package com.programmerbaper.focus.entities;

public class Clock {

    public static final String BASE_PATH = "https://fokus.gurisa.com/api/v0/";
    public static final String GET_TRAYEK = "routes";
    public static final String POST_REGISTER = "auth/register";
    public static final String POST_AUTH = "auth/login" ;
    public static final String POST_TRANSACTION = "transactions" ;
    public static final String GET_USER = "users/";
    public static final String GET_TODO = "/tasks";


    private int hours;
    private int minutes;
    private int seconds;

    public Clock(int jam, int menit, int detik) {
        this.hours = jam;
        this.minutes = menit;
        this.seconds = detik;
    }

    public int getHours() {
        return hours;
    }

    public int getMinutes() {
        return minutes;
    }

    public int getSeconds() {
        return seconds;
    }
}