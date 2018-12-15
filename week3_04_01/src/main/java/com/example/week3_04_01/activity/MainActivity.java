package com.example.week3_04_01.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.week3_04_01.R;
import com.example.week3_04_01.adaper.MainAdaper;
import com.example.week3_04_01.api.Apis;
import com.example.week3_04_01.bean.PhoneBean;
import com.example.week3_04_01.presenter.PresenterImpl;
import com.example.week3_04_01.view.Iview;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements Iview,View.OnClickListener {

    public static boolean isForeground;
    private PresenterImpl presenter;
    private ImageButton button;
    private boolean b = true;
    private MainAdaper adaper;
    private XRecyclerView recyclerView;
    private int mPage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //p层的实例
        initPresenter();
        //初始化view
        initView();
        //初始化Recycleview
        initRecycleView();
    }

    private void initRecycleView() {
        //设置适配器
        recyclerView.setAdapter(adaper);
        //设置支持刷新和加载
        recyclerView.setPullRefreshEnabled(true);
        recyclerView.setLoadingMoreEnabled(true);
        //设置监听
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
        //切换布局
        selectRecycleView();
        //加载数据
        initData();
    }
    //切换布局
    private void selectRecycleView() {
        if(b){
            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(layoutManager);
            layoutManager.setOrientation(OrientationHelper.VERTICAL);
            DividerItemDecoration decoration = new DividerItemDecoration(this,DividerItemDecoration.VERTICAL);
            recyclerView.addItemDecoration(decoration);
        }else{
            GridLayoutManager layoutManager = new GridLayoutManager(this,2);
            recyclerView.setLayoutManager(layoutManager);
            layoutManager.setOrientation(OrientationHelper.VERTICAL);
            DividerItemDecoration decoration = new DividerItemDecoration(this,DividerItemDecoration.VERTICAL);
            recyclerView.addItemDecoration(decoration);
        }
        adaper = new MainAdaper(this,b);
        recyclerView.setAdapter(adaper);
        b = !b;
    }

    //加载数据
    private void initData() {
        Map<String,String> map = new HashMap<>();
        map.put("keywords","手机");
        map.put("page",String.valueOf(mPage));
        map.put("sort","0");
        presenter.startRequest(Apis.URL_PHONE,map,PhoneBean.class);
    }
    //初始化view
    private void initView() {
        mPage = 1;
        //获取资源id
        button = findViewById(R.id.image_btn);
        recyclerView = findViewById(R.id.recycle);
        //创建适配器
        adaper = new MainAdaper(this,b);
        button.setOnClickListener(this);
    }

    private void initPresenter() {
        presenter = new PresenterImpl(this);
    }

    @Override
    public void requestData(Object o) {
        if(o instanceof PhoneBean){
            PhoneBean bean = (PhoneBean) o;
            if(bean == null || !bean.isSuccess()){
                Toast.makeText(MainActivity.this,bean.getMsg(),Toast.LENGTH_SHORT).show();
            }else{
                if(mPage == 1){
                    adaper.setmData(bean.getData());
                }else{
                    adaper.addmData(bean.getData());
                }
                mPage++;
                recyclerView.refreshComplete();
                recyclerView.loadMoreComplete();
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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.image_btn:
                if(b){
                    button.setImageResource(R.drawable.ic_action_name);
                }else{
                    button.setImageResource(R.drawable.ic_grid_name);
                }
                List<PhoneBean.DataBean> data = adaper.getData();
                    selectRecycleView();
                    adaper.setmData(data);
                break;
                default:
                    break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (presenter!=null){
            presenter.onDetach();
        }

    }
}
