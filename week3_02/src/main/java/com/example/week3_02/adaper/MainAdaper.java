package com.example.week3_02.adaper;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.week3_02.R;
import com.example.week3_02.bean.PhonoBean;

import java.util.ArrayList;
import java.util.List;

public class MainAdaper extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Boolean b;
    private Context mContext;
    private List<PhonoBean.DataBean> mData;
    private List<String> image= new ArrayList<>();
    private View view;

    public MainAdaper(Boolean b, Context mContext) {
        this.b = b;
        this.mContext = mContext;
        mData = new ArrayList<>();
    }
    public void setmData(List<PhonoBean.DataBean> datas){
        mData.clear();
        if(datas!=null){
            mData.addAll(datas);
        }
        notifyDataSetChanged();
    }
    public void addmData(List<PhonoBean.DataBean> datas){
        if(datas!=null){
            mData.addAll(datas);
        }
        notifyDataSetChanged();
    }

    public void setB(boolean b){
        this.b = b;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if(b){
            view = LayoutInflater.from(mContext).inflate(R.layout.grid_item,viewGroup,false);
        }else{
            view = LayoutInflater.from(mContext).inflate(R.layout.linear_item,viewGroup,false);
        }
        return new ViewHolder1(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int i) {
        PhonoBean.DataBean dataBean = mData.get(i);
        ViewHolder1 holder = (ViewHolder1) viewHolder;
        holder.title.setText(dataBean.getTitle());
        holder.price.setText(dataBean.getPrice()+"");
        sub(dataBean.getImages());
        Glide.with(mContext).load(image.get(i)).into(holder.image);
        //点击
        holder.constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(clickCallBack!=null){
                    clickCallBack.setOnClick(i);
                }
            }
        });
    }
    /**截取字符串是方法*/
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
    @Override
    public int getItemCount() {
        return mData.size();
    }
    public static class ViewHolder1 extends RecyclerView.ViewHolder{
        public final ImageView image;
        public final TextView title;
        public final TextView price;
        public final ConstraintLayout constraintLayout;
        public ViewHolder1(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            title = itemView.findViewById(R.id.title);
            price = itemView.findViewById(R.id.price);
            constraintLayout = itemView.findViewById(R.id.con);
        }
    }
    //点击
    private ClickCallBack clickCallBack;
    //定义接口
    public interface ClickCallBack{
        void setOnClick(int i);
    }
    public void setClickCallBack(ClickCallBack clickCallBack){
        this.clickCallBack = clickCallBack;
    }

}
