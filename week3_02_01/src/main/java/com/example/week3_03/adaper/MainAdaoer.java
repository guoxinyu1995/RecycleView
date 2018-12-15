package com.example.week3_03.adaper;

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
import com.example.week3_03.R;
import com.example.week3_03.bean.PhoneBean;

import java.util.ArrayList;
import java.util.List;

public class MainAdaoer extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Boolean b;
    private Context mContext;
    private List<PhoneBean.DataBean> mData;

    public MainAdaoer(Boolean b, Context mContext) {
        this.b = b;
        this.mContext = mContext;
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
            holder = new ViewHolderLinear(view);
        }else{
            View view = LayoutInflater.from(mContext).inflate(R.layout.grid_item,viewGroup,false);
            holder = new ViewHolderGrid(view);
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int i) {
        PhoneBean.DataBean dataBean = mData.get(i);
        String images = dataBean.getImages();
        String[] img = images.split("\\|");
        if(b){
            ViewHolderLinear holderLinear = (ViewHolderLinear) viewHolder;
            holderLinear.title.setText(dataBean.getTitle());
            holderLinear.price.setText("价格："+dataBean.getPrice());
            holderLinear.salenum.setText("销量："+dataBean.getSalenum());
            Glide.with(mContext).load(img[0]).into(holderLinear.image);
            //点击
            holderLinear.layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(clickCallBaack!=null){
                        clickCallBaack.setOnclick(i);
                    }
                }
            });
        }else{
            ViewHolderGrid holderGrid = (ViewHolderGrid) viewHolder;
            holderGrid.title.setText(dataBean.getTitle());
            holderGrid.price.setText("价格："+dataBean.getPrice());
            holderGrid.salenum.setText("销量："+dataBean.getSalenum());
            Glide.with(mContext).load(img[0]).into(holderGrid.image);
            //点击
            holderGrid.layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(clickCallBaack!=null){
                        clickCallBaack.setOnclick(i);
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }
    //线性
    class ViewHolderLinear extends RecyclerView.ViewHolder{
        public final ImageView image;
        public final TextView title;
        public final TextView price;
        public final TextView salenum;
        public final ConstraintLayout layout;
        public ViewHolderLinear(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            title = itemView.findViewById(R.id.title);
            price = itemView.findViewById(R.id.price);
            salenum = itemView.findViewById(R.id.salenum);
            layout = itemView.findViewById(R.id.con);
        }
    }
    //网格
    class ViewHolderGrid extends RecyclerView.ViewHolder{
        public final ImageView image;
        public final TextView title;
        public final TextView price;
        public final TextView salenum;
        public final ConstraintLayout layout;
        public ViewHolderGrid(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            title = itemView.findViewById(R.id.title);
            price = itemView.findViewById(R.id.price);
            salenum = itemView.findViewById(R.id.salenum);
            layout = itemView.findViewById(R.id.con);
        }
    }
    //定义接口
    private ClickCallBaack clickCallBaack;
    public interface ClickCallBaack{
        void setOnclick(int i);
    }
    public void setClickCallBaack(ClickCallBaack clickCallBaack){
        this.clickCallBaack = clickCallBaack;
    }
}
