package com.example.week3_02.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.week3_02.R;
import com.example.week3_02.api.Apis;
import com.example.week3_02.bean.ShowBean;
import com.example.week3_02.presenter.PresenterImpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShowActivity extends AppCompatActivity implements Iview{

    private PresenterImpl presenter;
    private ImageView imageView;
    private TextView title;
    private TextView price;
    private List<String> image= new ArrayList<>();
    private int pid;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);
        presenter = new PresenterImpl(this);
        initView();
    }

    private void initView() {
        //获取资源id
        imageView = findViewById(R.id.show_image);
        title = findViewById(R.id.show_titlle);
        price = findViewById(R.id.show_price);
        Intent intent = getIntent();
        int i = intent.getIntExtra("i", 0);
        pid = i;
        Map<String,String> map = new HashMap<>();
        map.put("pid",String.valueOf(pid));
        presenter.startRequest(Apis.URL_SHOP,map,ShowBean.class);
    }

    @Override
    public void requestData(Object o) {
        if(o instanceof ShowBean){
            ShowBean bean = (ShowBean) o;
            if(bean==null || !bean.isSuccess()){
                Toast.makeText(ShowActivity.this,bean.getMsg(),Toast.LENGTH_SHORT).show();
            }else{
                sub(bean.getData().getImages());
                Glide.with(ShowActivity.this).load(image.get(0)).into(imageView);
                title.setText(bean.getData().getTitle());
                price.setText(bean.getData().getPrice()+"");
            }
        }
    }
    public void sub(String url){
        //获取以“|”为截取的下标位置
        int i = url.indexOf("|");
        if (i>=0){
            String substring = url.substring(0, i);
            image.add(substring);
            sub(url.substring(i+1,url.length()));
        }else{
            image.add(url);
        }
    }
}
