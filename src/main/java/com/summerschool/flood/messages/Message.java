package com.summerschool.flood.messages;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Message implements Serializable {

    private Action action;
    private Object payload;

    public enum Action {
        @JsonProperty("Error")
        ERROR
    }
}


