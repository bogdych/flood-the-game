package com.summerschool.flood.game;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlayerInfo {

    @JsonProperty("nickname")
    String nickname;

    private Map<String, Object> params;

}
