package com.example.week3_03_01.activity;

import android.animation.ObjectAnimator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.week3_03_01.R;
import com.example.week3_03_01.adaper.NewsAdaper;
import com.example.week3_03_01.api.Apis;
import com.example.week3_03_01.bean.NewBean;
import com.example.week3_03_01.presenter.PresenterImpl;
import com.example.week3_03_01.view.Iview;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

public class MainActivity extends AppCompatActivity implements Iview {

    private PresenterImpl presenter;
    private ImageView image_xin;
    private XRecyclerView recyclerView;
    private int mPage;
    private NewsAdaper adaper;
    boolean isChanged = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        presenter = new PresenterImpl(this);
        initView();
    }

    private void initView() {
        mPage = 1;
        //获取资源id
        image_xin = findViewById(R.id.image_xin);
        recyclerView = findViewById(R.id.x_recycle);
        adaper = new NewsAdaper(this);
        recyclerView.setAdapter(adaper);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(OrientationHelper.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration decoration = new DividerItemDecoration(this,DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(decoration);
        recyclerView.setLoadingMoreEnabled(true);
        recyclerView.setPullRefreshEnabled(true);
        recyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                mPage = 1;
                initData();
            }

            @Override
            public void onLoadMore() {
                initData();
            }
        });
        initData();
        adaper.setCilckCallBack(new NewsAdaper.CilckCallBack() {
            @Override
            public void setClick(int i) {
                adaper.delData(i);
            }
        });
        //点击心
        image_xin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //图片平移出去还能弹回来
                //如果没有mimg.getTranslationX()则图片不会弹回来
                image_xin.setImageResource(R.drawable.xin_w);
                ObjectAnimator translationX = ObjectAnimator.ofFloat(image_xin, "translationX",image_xin.getTranslationX(), -600f,image_xin.getTranslationX());
                ObjectAnimator translationY = ObjectAnimator.ofFloat(image_xin, "translationY",image_xin.getTranslationY(), 1100f,image_xin.getTranslationY());
                //动画时间
                translationX.setDuration(3000);//时间
                translationY.setDuration(3000);//时间
                //执行动画
                //渐变透明
                ObjectAnimator alpha = ObjectAnimator.ofFloat(image_xin, "alpha", 0.0f, 1.0f);
                alpha.setDuration(3000);//时间


                if(v == image_xin)
                {

                    if(isChanged){
                        Toast.makeText(MainActivity.this, "取消选中", Toast.LENGTH_LONG).show();
                    }else {
                        translationY.start();//开始执行
                        translationX.start();//开始执行
                        alpha.start();//开始执行
                        image_xin.setImageResource(R.drawable.xin_x);

                        Toast.makeText(MainActivity.this, "选中状态", Toast.LENGTH_LONG).show();

                    }
                    isChanged = !isChanged;

                }

            }
        });
    }

    private void initData() {
        presenter.startRequest(String.format(Apis.URL_NEWS,mPage),null,NewBean.class);
    }

    @Override
    public void requestData(Object o) {
        if(o instanceof NewBean){
            NewBean bean = (NewBean) o;
            if(bean==null || !bean.isSuccess()){
                Toast.makeText(MainActivity.this,bean.getMsg(),Toast.LENGTH_SHORT).show();
            }else{
                if(mPage == 1){
                    adaper.setmData(bean.getData());
                }else{
                    adaper.addmData(bean.getData());
                }
                mPage++;
                recyclerView.loadMoreComplete();
                recyclerView.refreshComplete();
            }
        }
    }

    @Override
    public void requestFail(Object o) {
        if(o instanceof Exception){
            Exception e = (Exception) o;
            e.printStackTrace();
        }
        Toast.makeText(MainActivity.this,"网络请求错误",Toast.LENGTH_SHORT).show();

    }
}
