package com.nagarro.miniassignment.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class TestUtils {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static JsonNode convertJsonStringToObject(String jsonString, Class<?> valueType) {
        try {
            return objectMapper.readValue(jsonString, JsonNode.class);
        } catch (Exception e) {
            throw new RuntimeException("Error converting JSON string to object", e);
        }
    }
}
