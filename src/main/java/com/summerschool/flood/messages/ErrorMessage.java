package com.summerschool.flood.messages;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.summerschool.flood.misc.JsonSerializable;

import java.io.IOException;

public class ErrorMessage implements JsonSerializable {

    public final String messageType;
    public final String errorText;

    public ErrorMessage(String messageType, String errorText) {
        this.messageType = messageType;
        this.errorText = errorText;
    }

    @Override
    public String serialize() throws IOException {
        return new ObjectMapper()
                .writerFor(ErrorMessage.class)
                .writeValueAsString(this);
    }

}
