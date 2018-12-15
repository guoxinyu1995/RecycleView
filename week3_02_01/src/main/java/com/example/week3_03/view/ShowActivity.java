package com.example.week3_03.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.week3_03.R;
import com.example.week3_03.api.Apis;
import com.example.week3_03.api.Constants;
import com.example.week3_03.bean.ContentBean;
import com.example.week3_03.presenter.PresenterImpl;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.loader.ImageLoaderInterface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ShowActivity extends AppCompatActivity implements Iview{

    private PresenterImpl presenter;

    private TextView title;
    private TextView price;
    private List<String> image= new ArrayList<>();
    private Banner banner;
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
        banner = findViewById(R.id.show_image);
        title = findViewById(R.id.show_titlle);
        price = findViewById(R.id.show_price);
        Intent intent = getIntent();
        pid = intent.getIntExtra(Constants.MAP_KEY_PRODUCTS_DETAIL_PID, 0);
        Map<String,String> map = new HashMap<>();
        map.put(Constants.MAP_KEY_PRODUCTS_DETAIL_PID,String.valueOf(pid));
        presenter.startRequest(Apis.URL_PRODUCTS_DETAIL,map,ContentBean.class);
    }

    @Override
    public void requestData(Object o) {
        if(o instanceof ContentBean){
            ContentBean bean = (ContentBean) o;
            if(bean==null || !bean.isSuccess()){
                Toast.makeText(ShowActivity.this,bean.getMsg(),Toast.LENGTH_SHORT).show();
            }else{
                title.setText(bean.getData().getTitle());
                price.setText("￥"+bean.getData().getPrice());

                banner.setBannerStyle(BannerConfig.NUM_INDICATOR);

                banner.setImageLoader(new ImageLoaderInterface<ImageView>() {

                    @Override
                    public void displayImage(Context context, Object path, ImageView imageView) {
                        Glide.with(ShowActivity.this).load(path).into(imageView);
                    }
                    @Override
                    public ImageView createImageView(Context context) {
                        ImageView imageView = new ImageView(context);
                        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                        return imageView;
                    }
                });
                sub(bean.getData().getImages());
                banner.setImages(image);
                banner.start();
            }

        }
    }

    @Override
    public void requestFail(Object o) {
        if(o instanceof Exception){
            Exception e = (Exception) o;
            e.printStackTrace();
        }
        Toast.makeText(ShowActivity.this,"网络请求错误",Toast.LENGTH_SHORT).show();
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
