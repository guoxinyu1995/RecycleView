package com.example.week3_03.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.week3_03.R;
import com.example.week3_03.adaper.MainAdaoer;
import com.example.week3_03.api.Apis;
import com.example.week3_03.api.Constants;
import com.example.week3_03.bean.PhoneBean;
import com.example.week3_03.presenter.PresenterImpl;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,Iview {

    private PresenterImpl presenter;
    private ImageView image_search;
    private ImageView image_switch;
    private TextView synthesize;
    private TextView sales;
    private TextView price;
    private TextView screen;
    private XRecyclerView recyclerView;
    private EditText editText;
    private int mPage;
    private int mSort = 0;
    private boolean b = true;
    private MainAdaoer adaoer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //实现p层
        initPresenter();
        //初始化view
        initView();
        //初始化Recycleview
        initRecycleview();

    }
    //初始化Recycleview
    private void initRecycleview() {
        recyclerView.setPullRefreshEnabled(true);
        recyclerView.setLoadingMoreEnabled(true);
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
        changeRecycleView();
        initData();
    }
    /**
     * 切换管理器
     * */
    private void changeRecycleView() {
        //根据状态判断展示线性还是网格
        if(b){
            //创建布局管理器
            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            //设置方向
            layoutManager.setOrientation(OrientationHelper.VERTICAL);
            //设置布局管理器
            recyclerView.setLayoutManager(layoutManager);
            //设置分割线
            DividerItemDecoration decoration = new DividerItemDecoration(this,DividerItemDecoration.VERTICAL);
            recyclerView.addItemDecoration(decoration);
        }else{
            //创建布局管理器
            GridLayoutManager layoutManager = new GridLayoutManager(this,2);
            //设置方向
            layoutManager.setOrientation(OrientationHelper.VERTICAL);
            //设置布局管理器
            recyclerView.setLayoutManager(layoutManager);
            //设置分割线
            DividerItemDecoration decoration = new DividerItemDecoration(this,DividerItemDecoration.VERTICAL);
            recyclerView.addItemDecoration(decoration);
        }
        //重新创建adapter，因为new 之后数据和点击事件都没有了，所以需要重新添加点击事件
        adaoer = new MainAdaoer(b,this);
        adaoer.setClickCallBaack(new MainAdaoer.ClickCallBaack() {
            @Override
            public void setOnclick(int i) {
                List<PhoneBean.DataBean> data = adaoer.getData();
                Intent intent = new Intent(MainActivity.this,ShowActivity.class);
                intent.putExtra(Constants.INTENT_KEY_PRODUCES_PID,data.get(i).getPid());
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(adaoer);
        //将状态反选
        b = !b;
    }

    //初始化view
    private void initView() {
        mPage = 1;
        //获取资源id
        //输入框
        editText = findViewById(R.id.ed);
        //放大镜
        image_search = findViewById(R.id.image_search);
        //切换
        image_switch = findViewById(R.id.image_switch);
        //综合
        synthesize = findViewById(R.id.synthesize);
        synthesize.setSelected(true);
        //销量
        sales = findViewById(R.id.sales);
        //价格
        price = findViewById(R.id.price);
        //筛选
        screen = findViewById(R.id.screen);
        //recycleview
        recyclerView = findViewById(R.id.x_recycle);
        //点击
        image_search.setOnClickListener(this);
        image_switch.setOnClickListener(this);
        synthesize.setOnClickListener(this);
        sales.setOnClickListener(this);
        price.setOnClickListener(this);
        adaoer = new MainAdaoer(b,this);
    }
    //实现p层
    private void initPresenter() {
        presenter = new PresenterImpl(this);
    }
    //加载数据
    private void initData() {
        Map<String,String> map = new HashMap<>();
        map.put(Constants.MAP_KEY_SEARCH_PRODUCTS_KEYWORDS,"手机");
        //map.put(Constants.MAP_KEY_SEARCH_PRODUCTS_KEYWORDS,editText.getText().toString());
        map.put(Constants.MAP_KEY_SEARCH_PRODUCES_PAGE,String.valueOf(mPage));
        map.put(Constants.MAP_KEY_SEARCH_PRODUCES_SORT,String.valueOf(mSort));
        presenter.startRequest(Apis.URL_SEARCH_PRODUCTS,map,PhoneBean.class);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            //搜索
            case R.id.image_search:
                //开始从网路获取
                //initData();
                break;
                //切换
            case R.id.image_switch:
                //切换按钮
                if(b){
                    image_switch.setImageResource(R.drawable.ic_action_name);
                }else{
                    image_switch.setImageResource(R.drawable.ic_grid_name);
                }
                //切换布局
                //因为切换布局时需要new 一个adapter，为了保证数据的一致性，所以先取出老adapter中的老数据
                List<PhoneBean.DataBean> data = adaoer.getData();
                changeRecycleView();
                //将老数据赋值给新的adapter
                adaoer.setmData(data);
                break;
            //综合
            case R.id.synthesize:
                //如果当前栏目已经被选中，则再次点击无效，不然就会再次请求
                if(!synthesize.isSelected()){
                    //切换时一定要从第一页获取数据，不然会造成新老数据在一起，导致排序出现问题
                    mPage = 1;
                    mSort = 0;
                    initData();
                    synthesize.setSelected(true);
                    sales.setSelected(false);
                    price.setSelected(false);
                }
                break;
            //销量
            case R.id.sales:
                if(!sales.isSelected()){
                    mPage = 1;
                    mSort = 1;
                    initData();
                    sales.setSelected(true);
                    synthesize.setSelected(false);
                    price.setSelected(false);
                }
                break;
            //价格
            case R.id.price:
                if(!price.isSelected()){
                    mPage = 1;
                    mSort = 2;
                    initData();
                    price.setSelected(true);
                    sales.setSelected(false);
                    synthesize.setSelected(false);
                }
                break;
                default:
                    break;
        }
    }

    @Override
    public void requestData(Object o) {
        if(o instanceof PhoneBean){
            PhoneBean bean = (PhoneBean) o;
            if(bean==null || !bean.isSuccess()){
                Toast.makeText(MainActivity.this,bean.getMsg(),Toast.LENGTH_SHORT).show();
            }else{
                if(mPage == 1){
                    adaoer.setmData(bean.getData());
                }else{
                    adaoer.addmData(bean.getData());
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
}
