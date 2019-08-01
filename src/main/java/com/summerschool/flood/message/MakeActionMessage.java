package com.summerschool.flood.message;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MakeActionMessage extends Message {

   private Map<String, Object> action = new HashMap<>();

}
