package com.project.android.hobbyist.Model;

/**
 * Created by hassa on 7/12/2018.
 */

public class Posts {
    private String UID;
    private String city;

    private String country;
    private String hobby;

    private double lat;
    private double lng;

    private int personNum;

    public Posts(String UID, String city, String country, String hobby, double lat, double lng, int personNum) {
        this.UID = UID;
        this.city = city;
        this.country = country;
        this.hobby = hobby;
        this.lat = lat;
        this.lng = lng;
        this.personNum = personNum;
    }

    public Posts() {
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getHobby() {
        return hobby;
    }

    public void setHobby(String hobby) {
        this.hobby = hobby;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public int getPersonNum() {
        return personNum;
    }

    public void setPersonNum(int personNum) {
        this.personNum = personNum;
    }
}
