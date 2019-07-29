package com.summerschool.flood.message;

import lombok.Data;

import java.text.SimpleDateFormat;
import java.util.Date;

@Data
public class ErrorMessage extends Message {

    private String time;
    private String message;

    public ErrorMessage(String message) {
        this.type = MessageType.ERROR;
        this.time = getCurrentTimeStamp();
        this.message = message;
    }

    private String getCurrentTimeStamp() {
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//dd/MM/yyyy
        Date now = new Date();
        return sdfDate.format(now);
    }
}
