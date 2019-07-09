package com.summerschool.flood.misc;

import java.io.IOException;

public interface JsonSerializable {

    /**
     * Serialize this message class content into json file
     * (supposed to be used with jackson)
     * @return String json representation
     */
    String serialize() throws IOException;

}
