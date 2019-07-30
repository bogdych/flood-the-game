package com.summerschool.flood.message;

import com.summerschool.flood.game.GameName;
import com.summerschool.flood.game.GameType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Map;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FindGameMessage extends Message {

    private GameName name;
    private GameType gameType;
    private Map<String, String> gameParams;
}
