package com.example.week3_04_01.adaper;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.week3_04_01.R;
import com.example.week3_04_01.bean.PhoneBean;

import java.util.ArrayList;
import java.util.List;

public class MainAdaper extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<PhoneBean.DataBean> mData;
    private Context mContext;
    private Boolean b;

    public MainAdaper(Context mContext, Boolean b) {
        this.mContext = mContext;
        this.b = b;
        mData = new ArrayList<>();
    }
    public void setmData(List<PhoneBean.DataBean> datas){
        mData.clear();
        if(datas!=null){
            mData.addAll(datas);
        }
        notifyDataSetChanged();
    }
    public void addmData(List<PhoneBean.DataBean> datas){
        if(datas!=null){
            mData.addAll(datas);
        }
        notifyDataSetChanged();
    }
    public List<PhoneBean.DataBean> getData(){
        return mData;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        RecyclerView.ViewHolder holder = null;
        if(b){
            View view = LayoutInflater.from(mContext).inflate(R.layout.linear_item,viewGroup,false);
            holder = new ViewHolderLg(view);
        }else{
            View view = LayoutInflater.from(mContext).inflate(R.layout.grid_item,viewGroup,false);
            holder = new ViewHolderLg(view);
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        PhoneBean.DataBean dataBean = mData.get(i);
        String images = dataBean.getImages();
        String[] img = images.split("\\|");
        ViewHolderLg holderLg = (ViewHolderLg) viewHolder;
        holderLg.title.setText(dataBean.getTitle());
        holderLg.price.setText("价格："+dataBean.getPrice());
        holderLg.salenum.setText("销量："+dataBean.getSalenum());
        Glide.with(mContext).load(img[0]).into(holderLg.image);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }
    class ViewHolderLg extends RecyclerView.ViewHolder{
        public final ImageView image;
        public final TextView title;
        public final TextView price;
        public final TextView salenum;
        public ViewHolderLg(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            title = itemView.findViewById(R.id.title);
            price = itemView.findViewById(R.id.price);
            salenum = itemView.findViewById(R.id.salenum);
        }
    }
}
