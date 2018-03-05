package com.example.zahidhasan.travelfast.model;

/**
 * Created by DarkzGothic on 6/11/2017.
 */

public class Drivers {

    private String id;
    private String name;
    private String email;
    private String phn;
    private String bikeRegNo;
    private String status;
    private String address;
    private String balance;
    private String completedRide;
    private String gender;
    private String lat;
    private String lng;
    private String nid;
    private String repPoint;
    private String verified;
    private String proImg;
    private String bikeRegImg;
    private String nidImg;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhn() {
        return phn;
    }

    public void setPhn(String phn) {
        this.phn = phn;
    }

    public String getBikeRegNo() {
        return bikeRegNo;
    }

    public void setBikeRegNo(String bikeRegNo) {
        this.bikeRegNo = bikeRegNo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getCompletedRide() {
        return completedRide;
    }

    public void setCompletedRide(String completedRide) {
        this.completedRide = completedRide;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getNid() {
        return nid;
    }

    public void setNid(String nid) {
        this.nid = nid;
    }

    public String getRepPoint() {
        return repPoint;
    }

    public void setRepPoint(String repPoint) {
        this.repPoint = repPoint;
    }

    public String getVerified() {
        return verified;
    }

    public void setVerified(String verified) {
        this.verified = verified;
    }

    public String getProImg() {
        return proImg;
    }

    public void setProImg(String proImg) {
        this.proImg = proImg;
    }

    public String getBikeRegImg() {
        return bikeRegImg;
    }

    public void setBikeRegImg(String bikeRegImg) {
        this.bikeRegImg = bikeRegImg;
    }

    public String getNidImg() {
        return nidImg;
    }

    public void setNidImg(String nidImg) {
        this.nidImg = nidImg;
    }

    public Drivers(String id, String name, String email, String phn, String bikeRegNo, String status, String address, String balance, String completedRide, String gender, String lat, String lng, String nid, String repPoint, String verified, String proImg, String bikeRegImg, String nidImg) {

        this.id = id;
        this.name = name;
        this.email = email;
        this.phn = phn;
        this.bikeRegNo = bikeRegNo;
        this.status = status;
        this.address = address;
        this.balance = balance;
        this.completedRide = completedRide;
        this.gender = gender;
        this.lat = lat;
        this.lng = lng;
        this.nid = nid;
        this.repPoint = repPoint;
        this.verified = verified;
        this.proImg = proImg;
        this.bikeRegImg = bikeRegImg;
        this.nidImg = nidImg;
    }

    public Drivers() {

    }
}
