package com.example.zahidhasan.travelfast.model;

/**
 * Created by DarkzGothic on 7/16/2017.
 */

public class PassengerInfo {

    private String id;
    private String phn;
    private String giftCode;
    private String getGiftCodeStatus;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPhn() {
        return phn;
    }

    public void setPhn(String phn) {
        this.phn = phn;
    }

    public String getGiftCode() {
        return giftCode;
    }

    public void setGiftCode(String giftCode) {
        this.giftCode = giftCode;
    }

    public String getGetGiftCodeStatus() {
        return getGiftCodeStatus;
    }

    public void setGetGiftCodeStatus(String getGiftCodeStatus) {
        this.getGiftCodeStatus = getGiftCodeStatus;
    }

    public PassengerInfo(String id, String phn, String giftCode, String getGiftCodeStatus) {

        this.id = id;
        this.phn = phn;
        this.giftCode = giftCode;
        this.getGiftCodeStatus = getGiftCodeStatus;
    }

    public PassengerInfo() {

    }
}
