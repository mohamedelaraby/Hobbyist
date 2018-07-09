package com.project.android.hobbyist.Model;

/**
 * Created by hassa on 7/6/2018.
 */

public class User {
    private String fullName;
    private String email;
    private String phoneNumber;
    private String password;

    private String dayOfDateBirth;
    private String monthOfDateBirth;
    private String yearOfDateBirth;

    private String gender;

    private String country;

    private String hobby;

    private double lng;
    private double lat;

    private String userId;


    public User(String fullName, String email, String phoneNumber, String password,
                String dayOfDateBirth, String monthOfDateBirth, String yearOfDateBirth,
                String gender, String country, String hobby, double lat, double lng) {
        this.fullName = fullName;
        this.email = email;
        this.lat = lat;
        this.lng = lng;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.dayOfDateBirth = dayOfDateBirth;
        this.monthOfDateBirth = monthOfDateBirth;
        this.yearOfDateBirth = yearOfDateBirth;
        this.gender = gender;
        this.country = country;
        this.hobby = hobby;
    }

    public User() {
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDayOfDateBirth() {
        return dayOfDateBirth;
    }

    public void setDayOfDateBirth(String dayOfDateBirth) {
        this.dayOfDateBirth = dayOfDateBirth;
    }

    public String getMonthOfDateBirth() {
        return monthOfDateBirth;
    }

    public void setMonthOfDateBirth(String monthOfDateBirth) {
        this.monthOfDateBirth = monthOfDateBirth;
    }

    public String getYearOfDateBirth() {
        return yearOfDateBirth;
    }

    public void setYearOfDateBirth(String yearOfDateBirth) {
        this.yearOfDateBirth = yearOfDateBirth;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
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

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }
}
