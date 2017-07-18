package com.ayush.shivman.ourmessaging;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by SHIVM@n on 7/11/2017.
 */

public class ChatMessageModel {
    String msg;
    boolean isMine;
    Date date;
    String date_str;


    public String getDate() {
        return date_str;
    }

    public void setDate(Date date) {
        this.date = date;
        SimpleDateFormat formatter = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
        this.date_str=formatter.format(this.date);
    }

    public boolean getIsMine() {
        return isMine;
    }

    public void setIsMine(boolean isMine) {
        this.isMine = isMine;
    }


    public String getmsg() {
        return msg;
    }

    public void setText(String msg) {
        this.msg = msg;
    }
}
