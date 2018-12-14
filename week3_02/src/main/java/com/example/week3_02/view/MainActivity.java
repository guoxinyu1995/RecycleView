package com.example.week3_02.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.week3_02.R;
import com.example.week3_02.adaper.MainAdaper;
import com.example.week3_02.api.Apis;
import com.example.week3_02.bean.PhonoBean;
import com.example.week3_02.presenter.PresenterImpl;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, Iview {

    private ImageView image_return;
    private ImageView image_switch;
    private boolean b = false;
    private TextView synthesize;
    private TextView screen;
    private TextView sales;
    private PresenterImpl presenter;
    private XRecyclerView recyclerView;
    private int mPage;
    private final int SORT_ZERO = 0;
    private final int SORT_ONE = 1;
    private final int SORT_TWO = 2;
    private MainAdaper adaper;
    private TextView price;

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
        image_return = findViewById(R.id.image_return);
        image_switch = findViewById(R.id.image_switch);
        synthesize = findViewById(R.id.synthesize);
        screen = findViewById(R.id.screen);
        sales = findViewById(R.id.sales);
        price = findViewById(R.id.price);
        recyclerView = findViewById(R.id.x_recycle);
        //切换按钮
        image_switch.setOnClickListener(this);
        sales.setOnClickListener(this);
        price.setOnClickListener(this);
        synthesize.setOnClickListener(this);
        adaper = new MainAdaper(b, this);
        recyclerView.setAdapter(adaper);
        //设置支持刷新和加载
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
        if (b) {
            getGrid();
        } else {
            getLinear();
        }
        //点击跳转
      adaper.setClickCallBack(new MainAdaper.ClickCallBack() {
          @Override
          public void setOnClick(int i) {
              Intent intent = new Intent(MainActivity.this,ShowActivity.class);
              intent.putExtra("i",i+1);
              startActivity(intent);
          }
      });
    }

    private void initData() {
        Map<String, String> params = new HashMap<>();
        params.put("keywords", "手机");
        params.put("page", mPage + "");
        params.put("sort", SORT_ZERO + "");
        presenter.startRequest(Apis.URL_CODE, params, PhonoBean.class);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.image_switch:
                mPage = 1;
                if (b == false) {
                    b = true;
                    getGrid();
                    image_switch.setImageResource(R.drawable.ic_action_name);
                } else if (b == true) {
                    b = false;
                    getLinear();
                    image_switch.setImageResource(R.drawable.ic_grid_name);
                }
                adaper = new MainAdaper(b, this);
                adaper.addmData(mList);
                recyclerView.setAdapter(adaper);
                break;
            case R.id.sales:
                Toast.makeText(MainActivity.this, "销量", Toast.LENGTH_SHORT).show();
                mPage = 1;
                Map<String, String> params = new HashMap<>();
                params.put("keywords", "手机");
                params.put("page", mPage + "");
                params.put("sort", SORT_ONE + "");
                presenter.startRequest(Apis.URL_CODE, params, PhonoBean.class);
                break;
            case R.id.price:
                mPage = 1;
                Toast.makeText(MainActivity.this, "价格", Toast.LENGTH_SHORT).show();
                Map<String, String> params1 = new HashMap<>();
                params1.put("keywords", "手机");
                params1.put("page", mPage + "");
                params1.put("sort", SORT_TWO + "");
                presenter.startRequest(Apis.URL_CODE, params1, PhonoBean.class);
                break;
            case R.id.synthesize:
                Toast.makeText(MainActivity.this, "综合", Toast.LENGTH_SHORT).show();
                mPage = 1;
                Map<String, String> params2 = new HashMap<>();
                params2.put("keywords", "手机");
                params2.put("page", mPage + "");
                params2.put("sort", SORT_ZERO + "");
                presenter.startRequest(Apis.URL_CODE, params2, PhonoBean.class);
                break;
            default:
                break;
        }
    }

    //网格
    private void getGrid() {
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);
        layoutManager.setOrientation(OrientationHelper.VERTICAL);
        DividerItemDecoration decoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(decoration);
    }

    //线性
    private void getLinear() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        layoutManager.setOrientation(OrientationHelper.VERTICAL);
        //设置分割线
        DividerItemDecoration decoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(decoration);
    }

    List<PhonoBean.DataBean> mList = new ArrayList<>();

    @Override
    public void requestData(Object o) {
        if (o instanceof PhonoBean) {
            PhonoBean bean = (PhonoBean) o;
            if (bean == null || !bean.isSuccess()) {
                Toast.makeText(MainActivity.this, bean.getMsg(), Toast.LENGTH_SHORT).show();
            } else {
                if (mPage == 1) {
                    mList.clear();
                    adaper.setmData(bean.getData());
                } else {
                    adaper.addmData(bean.getData());
                }
                mList.addAll(bean.getData());
                mPage++;
                recyclerView.loadMoreComplete();
                recyclerView.refreshComplete();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onDetach();
    }


}
