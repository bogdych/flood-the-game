package com.summerschool.flood.message;

import lombok.Data;

@Data
public class ErrorMessage {

    private final MessageType type = MessageType.ERROR;
    private final String text;

    public ErrorMessage(String errorText) {
        this.text = errorText;
    }

}
