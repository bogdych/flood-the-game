package com.summerschool.flood.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Message implements Serializable {

    private MessageType type;
    private Map<String,Object> payload;

}


