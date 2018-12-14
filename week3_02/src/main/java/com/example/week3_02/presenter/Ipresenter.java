package com.example.week3_02.presenter;

import java.util.Map;

public interface Ipresenter {
    void startRequest(String url, Map<String, String> params, Class clazz);
}
