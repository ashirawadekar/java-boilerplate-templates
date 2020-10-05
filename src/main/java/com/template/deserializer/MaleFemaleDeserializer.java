package com.template.deserializer;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

public class MaleFemaleDeserializer extends StdDeserializer<Character> {

    public MaleFemaleDeserializer() {
        this(null);
    }

    protected MaleFemaleDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public Character deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
         String content = p.getText();
         if(content.equals("MALE")) {
             return 'M';
         } else if(content.equals("FEMALE")) {
             return 'F';
         } else {
             return '-';
         }
    }
}
