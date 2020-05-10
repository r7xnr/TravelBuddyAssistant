package com.example.travelbuddyapp.Model;

import com.google.firebase.Timestamp;

public class Pin {


    private String pinID;
    private String name;
    private String description;
    private String location;
    private double latitude;
    private double longitude;
    private String imagePath;
    private Timestamp date;
    private String username;
    private String catData;

    public Pin()
    {

    }

//    public Pin(String pinID, String name, String description, String location, String username, String imagePath, double latitude, double longitude, Timestamp date){
//        this.pinID = pinID;
//        this.name = name;
//        this.description = description;
//        this.location = location;
//        this.username = username;
//        this.imagePath = imagePath;
//        this.latitude = latitude;
//        this.longitude = longitude;
//        this.date = date;
//    }

    public Pin(String pinID, String name, String description, String location, String username, String imagePath, double latitude, double longitude, Timestamp date, String category)
    {
        this.pinID = pinID;
        this.name = name;
        this.description = description;
        this.location = location;
        this.username = username;
        this.latitude = latitude;
        this.longitude = longitude;
        this.imagePath = imagePath;
        this.catData = category;
        this.date = date;
    }

    public String getPinID() {
        return pinID;
    }

    public String getUsername(){
        return username;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getLocation() {
        return location;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getImagePath() {
        return imagePath;
    }

    public Timestamp getDate() {
        return date;
    }

    public String getCatData() {
        return catData;
    }
}