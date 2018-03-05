package com.example.zahidhasan.travelfast.model;

/**
 * Created by DarkzGothic on 5/16/2017.
 */

public class Passenger {

    private String destinationAdd;
    private String destinationLng;
    private String destinationLat;
    private String sourceAdd;
    private String sourceLat;
    private String sourceLng;
    private String phn;
    private String status;

    public Passenger() {
    }

    public String getDestinationAdd() {
        return destinationAdd;

    }

    public void setDestinationAdd(String destinationAdd) {
        this.destinationAdd = destinationAdd;
    }

    public String getDestinationLng() {
        return destinationLng;
    }

    public void setDestinationLng(String destinationLng) {
        this.destinationLng = destinationLng;
    }

    public String getDestinationLat() {
        return destinationLat;
    }

    public void setDestinationLat(String destinationLat) {
        this.destinationLat = destinationLat;
    }

    public String getSourceAdd() {
        return sourceAdd;
    }

    public void setSourceAdd(String sourceAdd) {
        this.sourceAdd = sourceAdd;
    }

    public String getSourceLat() {
        return sourceLat;
    }

    public void setSourceLat(String sourceLat) {
        this.sourceLat = sourceLat;
    }

    public String getSourceLng() {
        return sourceLng;
    }

    public void setSourceLng(String sourceLng) {
        this.sourceLng = sourceLng;
    }

    public String getPhn() {
        return phn;
    }

    public void setPhn(String phn) {
        this.phn = phn;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Passenger(String destinationAdd, String destinationLng, String destinationLat, String sourceAdd, String sourceLat, String sourceLng, String phn, String status) {

        this.destinationAdd = destinationAdd;
        this.destinationLng = destinationLng;
        this.destinationLat = destinationLat;
        this.sourceAdd = sourceAdd;
        this.sourceLat = sourceLat;
        this.sourceLng = sourceLng;
        this.phn = phn;
        this.status = status;
    }
}

