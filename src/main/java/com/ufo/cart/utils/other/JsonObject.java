package com.ufo.cart.utils.other;

import java.util.*;

public class JsonObject {
    private Map<String, Object> jsonObject;

    public JsonObject() {
        this.jsonObject = new HashMap<>();
    }

    public void add(String key, Object value) {
        jsonObject.put(key, value);
    }

    public Object get(String key) {
        return jsonObject.get(key);
    }

    public Map<String, Object> getJsonObject() {
        return jsonObject;
    }

    @Override
    public String toString() {
        return jsonObject.toString();
    }
}
