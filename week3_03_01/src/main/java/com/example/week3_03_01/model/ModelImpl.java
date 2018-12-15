package com.example.week3_03_01.model;

import com.example.week3_03_01.util.ICallBack;
import com.example.week3_03_01.util.OkHttpUtils;

import java.util.Map;

public class ModelImpl implements Imodel {
    private MyCallBack myCallBack;

    @Override
    public void getRequest(String url, Map<String, String> params, Class clazz, final MyCallBack myCallBack) {
        this.myCallBack = myCallBack;
        OkHttpUtils.getInstance().doGet(url, null, clazz, new ICallBack() {
            @Override
            public void success(Object o) {
                myCallBack.getData(o);
            }

            @Override
            public void faniled(Exception e) {
                myCallBack.getData(e.getMessage());
            }
        });
    }
}
