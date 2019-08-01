package com.summerschool.flood.message;

import lombok.Data;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        visible = true,
        property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = FindGameMessage.class, name = "findGame"),
        @JsonSubTypes.Type(value = MakeActionMessage.class, name = "makeAction")})
@Data
public abstract class Message {

    MessageType type;
}