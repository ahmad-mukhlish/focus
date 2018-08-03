package com.programmerbaper.focus.entities;

public class Jam {

    public static final String BASE_PATH = "https://fokus.gurisa.com/api/v0/";
    public static final String GET_TRAYEK = "routes";
    public static final String POST_REGISTER = "auth/register";
    public static final String POST_AUTH = "auth/login" ;
    public static final String POST_TRANSACTION = "transactions" ;
    public static final String GET_TIKET = "users/";
    public static final String GET_TIKET_2 = "/details";


    private int jam ;
    private int menit ;
    private int detik ;

    public Jam(int jam, int menit, int detik) {
        this.jam = jam;
        this.menit = menit;
        this.detik = detik;
    }

    public int getJam() {
        return jam;
    }

    public int getMenit() {
        return menit;
    }

    public int getDetik() {
        return detik;
    }
}