package com.example.demo.common.gencode;

import com.google.gson.Gson;

import java.io.Reader;

public class FunctionCommon {
    private static final Gson gson = new Gson();

    public static Object convertJsonToObject(Reader reader, Class<?> clazz) {
            return gson.fromJson(reader, clazz);
    }
}
