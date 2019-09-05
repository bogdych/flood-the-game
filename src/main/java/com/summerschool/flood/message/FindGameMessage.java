package com.summerschool.flood.message;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.summerschool.flood.game.GameName;
import com.summerschool.flood.game.GameType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FindGameMessage extends Message {

    private GameName name;
    private GameType gameType;
    private Map<String, String> gameParams = new HashMap<>();

    @JsonAnySetter
    public void set(String a, String b) {
        gameParams.put(a, b);
    }
}
