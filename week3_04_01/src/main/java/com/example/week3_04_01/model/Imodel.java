package com.example.week3_04_01.model;

import java.util.Map;

public interface Imodel {
    void getRequest(String url, Map<String, String> params, Class clazz, MyCallBack myCallBack);
}
