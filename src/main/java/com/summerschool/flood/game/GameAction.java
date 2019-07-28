package com.summerschool.flood.game;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GameAction {

    private ActionType type;
    private Map<String, String> payload = new HashMap<>();

    @JsonAnySetter
    public void setPayload(String key, String value) {
        payload.put(key, value);
    }

}
