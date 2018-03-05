package com.example.zahidhasan.travelfast.model;

/**
 * Created by DarkzGothic on 6/15/2017.
 */

public class Messages {

    private String id;
    private String msg;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Messages(String id, String msg) {

        this.id = id;
        this.msg = msg;
    }

    public Messages() {

    }
}

