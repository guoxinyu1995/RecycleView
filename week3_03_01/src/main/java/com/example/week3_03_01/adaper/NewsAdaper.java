package com.example.week3_03_01.adaper;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.week3_03_01.R;
import com.example.week3_03_01.bean.NewBean;

import java.util.ArrayList;
import java.util.List;

public class NewsAdaper extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<NewBean.DataBean> mData;
    private Context mContext;
    private final int IMAGE_ONE = 0;
    private final int IMAGE_TWO = 1;

    public NewsAdaper(Context mContext) {
        this.mContext = mContext;
        mData = new ArrayList<>();
    }

    public void setmData(List<NewBean.DataBean> datas) {
        mData.clear();
        if (datas != null) {
            mData.addAll(datas);
        }
        notifyDataSetChanged();
    }

    public void addmData(List<NewBean.DataBean> datas) {
        if (datas != null) {
            mData.addAll(datas);
        }
        notifyDataSetChanged();
    }
    public void delData(int i){
        mData.remove(i);
       notifyDataSetChanged();
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        RecyclerView.ViewHolder holder = null;
        if (i == IMAGE_ONE) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.imageone_item, viewGroup, false);
            holder = new ViewHolderOne(view);
        } else if (i == IMAGE_TWO) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.imagetwo_item, viewGroup, false);
            holder = new ViewHoldrTwo(view);
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int i) {
        int type = getItemViewType(i);
        switch (type) {
            case IMAGE_ONE:
                ViewHolderOne holderOne = (ViewHolderOne) viewHolder;
                holderOne.title.setText(mData.get(i).getTitle());
                holderOne.date.setText(mData.get(i).getDate());
                Glide.with(mContext).load(mData.get(i).getThumbnail_pic_s()).into(holderOne.image_one);
                holderOne.image_clear.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                            if(cilckCallBack!=null){
                                cilckCallBack.setClick(i);
                            }
                    }
                });
                break;
            case IMAGE_TWO:
                ViewHoldrTwo holdrTwo = (ViewHoldrTwo) viewHolder;
                holdrTwo.title.setText(mData.get(i).getTitle());
                Glide.with(mContext).load(mData.get(i).getThumbnail_pic_s()).into(holdrTwo.image_one);
                Glide.with(mContext).load(mData.get(i).getThumbnail_pic_s02()).into(holdrTwo.image_two);
                Glide.with(mContext).load(mData.get(i).getThumbnail_pic_s03()).into(holdrTwo.image_three);
                holdrTwo.image_clear.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(cilckCallBack!=null){
                            cilckCallBack.setClick(i);
                        }
                    }
                });
                break;
            default:
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        String thumbnail_pic_s = mData.get(position).getThumbnail_pic_s();
        String thumbnail_pic_s02 = mData.get(position).getThumbnail_pic_s02();
        String thumbnail_pic_s03 = mData.get(position).getThumbnail_pic_s03();
        if (thumbnail_pic_s != null && thumbnail_pic_s02 == null && thumbnail_pic_s03 == null) {
            return IMAGE_ONE;
        } else if (thumbnail_pic_s != null && thumbnail_pic_s02 != null && thumbnail_pic_s03 != null) {
            return IMAGE_TWO;
        }
        return 0;
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    class ViewHolderOne extends RecyclerView.ViewHolder {
        public final ImageView image_one;
        public final TextView title;
        public final TextView date;
        public final ImageButton image_clear;

        public ViewHolderOne(@NonNull View itemView) {
            super(itemView);
            image_one = itemView.findViewById(R.id.image_one);
            title = itemView.findViewById(R.id.title);
            date = itemView.findViewById(R.id.date);
            image_clear = itemView.findViewById(R.id.image_clear);
        }
    }

    class ViewHoldrTwo extends RecyclerView.ViewHolder {
        public final TextView title;
        public final ImageView image_one;
        public final ImageView image_two;
        public final ImageView image_three;
        public final ImageButton image_clear;

        public ViewHoldrTwo(@NonNull View itemView) {
            super(itemView);
            image_one = itemView.findViewById(R.id.image_one);
            image_two = itemView.findViewById(R.id.image_two);
            image_three = itemView.findViewById(R.id.image_three);
            title = itemView.findViewById(R.id.title);
            image_clear = itemView.findViewById(R.id.image_clear);
        }

    }
    private CilckCallBack cilckCallBack;
    public interface CilckCallBack{
        void setClick(int i);
    }
    public void setCilckCallBack(CilckCallBack cilckCallBack){
        this.cilckCallBack = cilckCallBack;
    }
}
