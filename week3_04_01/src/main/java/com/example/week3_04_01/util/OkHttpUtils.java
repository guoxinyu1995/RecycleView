package com.example.week3_04_01.util;

import android.os.Handler;
import android.os.Looper;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

public class OkHttpUtils {
    private static OkHttpUtils instance;
    private final OkHttpClient httpClient;
    private Handler handler = new Handler(Looper.myLooper());
    public OkHttpUtils() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        httpClient = new OkHttpClient.Builder()
                .connectTimeout(10,TimeUnit.SECONDS)
                .readTimeout(10,TimeUnit.SECONDS)
                .writeTimeout(10,TimeUnit.SECONDS)
                .addInterceptor(interceptor)
                .build();
    }

    public static OkHttpUtils getInstance() {
        if(instance == null){
            synchronized (OkHttpUtils.class){
                instance = new OkHttpUtils();
            }
        }
        return instance;
    }
    /**
     * 异步get
     * */
    public void doGet(String url, String parame, final Class clazz, final ICallBack iCallBack){
        Request request = new Request.Builder()
                .get()
                .url(url)
                .build();
        Call call = httpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        iCallBack.faniled(e);
                    }
                });

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    String result = response.body().string();
                    Gson gson = new Gson();
                    final Object o = gson.fromJson(result, clazz);
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            iCallBack.success(o);
                        }
                    });
                }catch (Exception e){
                        iCallBack.faniled(e);
                }
            }
        });
    }
    /**
     * 异步post
     * */
    public void doPost(String url, Map<String,String> parame, final Class clazz, final ICallBack iCallBack){
        FormBody.Builder builder = new FormBody.Builder();
        for(Map.Entry<String,String> entry:parame.entrySet()){
            builder.add(entry.getKey(),entry.getValue());
        }
        RequestBody body = builder.build();
        Request request = new Request.Builder()
                .post(body)
                .url(url)
                .build();
        Call call = httpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        iCallBack.faniled(e);
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    String result = response.body().string();
                    Gson gson = new Gson();
                    final Object o = gson.fromJson(result, clazz);
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            iCallBack.success(o);
                        }
                    });
                }catch (Exception e){
                    iCallBack.faniled(e);
                }
            }
        });
    }
}
