package com.summerschool.flood.messages;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Message, sent from UI client to server. Must provide
 * message type (essential value) and any params map.
 */
public class ClientActionMessage {

    private String messageType;
    private Map<String,String> properties = new HashMap<>();

    public String getMessageType() {
        return messageType;
    }

    public Map<String, String> getProperties() {
        return properties;
    }

    @JsonSetter
    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

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
