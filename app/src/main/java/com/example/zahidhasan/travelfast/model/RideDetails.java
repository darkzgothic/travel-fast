package com.example.zahidhasan.travelfast.model;

/**
 * Created by DarkzGothic on 6/14/2017.
 */

public class RideDetails {

    private String id;
    private String driverName;
    private String driverEmail;
    private String driverLat;
    private String driverLng;
    private String passengerDesAdd;
    private String passengerDesLat;
    private String passengerDesLng;
    private String passengerSourceAdd;
    private String passengerSourceLat;
    private String passengerSourceLng;
    private String passengerPhn;
    private String cost;
    private String time;
    private String distance;
    private String rideStatus;

    public String getId() { return id; }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getDriverEmail() {
        return driverEmail;
    }

    public void setDriverEmail(String driverEmail) {
        this.driverEmail = driverEmail;
    }

    public String getDriverLat() {
        return driverLat;
    }

    public void setDriverLat(String driverLat) {
        this.driverLat = driverLat;
    }

    public String getDriverLng() {
        return driverLng;
    }

    public void setDriverLng(String driverLng) {
        this.driverLng = driverLng;
    }

    public String getPassengerDesAdd() {
        return passengerDesAdd;
    }

    public void setPassengerDesAdd(String passengerDesAdd) {
        this.passengerDesAdd = passengerDesAdd;
    }

    public String getPassengerDesLat() {
        return passengerDesLat;
    }

    public void setPassengerDesLat(String passengerDesLat) {
        this.passengerDesLat = passengerDesLat;
    }

    public String getPassengerDesLng() {
        return passengerDesLng;
    }

    public void setPassengerDesLng(String passengerDesLng) {
        this.passengerDesLng = passengerDesLng;
    }

    public String getPassengerSourceAdd() {
        return passengerSourceAdd;
    }

    public void setPassengerSourceAdd(String passengerSourceAdd) {
        this.passengerSourceAdd = passengerSourceAdd;
    }
    public void setId(String id){
        this.id = id;
    }

    public String getPassengerSourceLat() {
        return passengerSourceLat;
    }

    public void setPassengerSourceLat(String passengerSourceLat) {
        this.passengerSourceLat = passengerSourceLat;
    }

    public String getPassengerSourceLng() {
        return passengerSourceLng;
    }

    public void setPassengerSourceLng(String passengerSourceLng) {
        this.passengerSourceLng = passengerSourceLng;
    }

    public String getPassengerPhn() {
        return passengerPhn;
    }

    public void setPassengerPhn(String passengerPhn) {
        this.passengerPhn = passengerPhn;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getRideStatus() {
        return rideStatus;
    }

    public void setRideStatus(String rideStatus) {
        this.rideStatus = rideStatus;
    }

    public RideDetails() {

    }

    public RideDetails(String driverName, String driverEmail, String driverLat, String driverLng, String passengerDesAdd, String passengerDesLat, String passengerDesLng, String passengerSourceAdd, String passengerSourceLat, String passengerSourceLng, String passengerPhn, String cost, String time, String distance, String rideStatus) {

        this.driverName = driverName;
        this.driverEmail = driverEmail;
        this.driverLat = driverLat;
        this.driverLng = driverLng;
        this.passengerDesAdd = passengerDesAdd;
        this.passengerDesLat = passengerDesLat;
        this.passengerDesLng = passengerDesLng;
        this.passengerSourceAdd = passengerSourceAdd;
        this.passengerSourceLat = passengerSourceLat;
        this.passengerSourceLng = passengerSourceLng;
        this.passengerPhn = passengerPhn;
        this.cost = cost;
        this.time = time;
        this.distance = distance;
        this.rideStatus = rideStatus;
    }
}

