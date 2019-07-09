package com.summerschool.flood.messages;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ClientActionMessage {

    public String messageType;
    private Map<String,String> properties = new HashMap<>();

    @JsonAnySetter
    public void addProperty(String key, String value) {
        properties.put(key, value);
    }

    @Override
    public String toString() {
        return messageType + " " + properties.toString();
    }

    public static ClientActionMessage deserialize(String input) throws IOException {
        return new ObjectMapper()
                .readerFor(ClientActionMessage.class)
                .readValue(input);
    }

}
