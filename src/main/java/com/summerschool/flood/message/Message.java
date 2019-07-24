package com.summerschool.flood.message;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Message implements Serializable {

    private MessageType type;
    private Map<String,String> payload = new HashMap<>();

    @JsonAnySetter
    void setPayload(String key, String value) {
        payload.put(key, value);
    }

}

/*

{
    "type":"findGame",
    "gameParams":{
        "name":"flood",
        "type":"standard"
    }
}

*/