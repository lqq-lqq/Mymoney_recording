package com.example.money_recording.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.money_recording.R;
import com.example.money_recording.bean.Money;
import com.example.money_recording.interface1.OnItemClickListener;

import java.util.List;

public class MoneyAdapter extends RecyclerView.Adapter<MoneyAdapter.ViewHolder>{
    private List<Money> moneyList;
    //private AdapterView.OnItemClickListener onItemClickListener;
    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView moneyText;
        View moneyView;
        public ViewHolder(View view){
            super(view);
            moneyView=view;
            moneyText=view.findViewById(R.id.one_money);
        }
    }
    public MoneyAdapter(List<Money> ml){
        moneyList=ml;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.one_money,parent,false);
        ViewHolder holder=new ViewHolder(view);
        return holder;
    }
    @Override
    public void onBindViewHolder(ViewHolder holder, int position){
        Money money=moneyList.get(position);
        holder.moneyText.setText(money.getString());
        if (mOnItemClickListener != null) {
            holder.moneyText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = holder.getLayoutPosition();
                    mOnItemClickListener.onItemClick(holder.itemView, position);
                }
            });
        }
    }
    @Override
    public int getItemCount(){
        return moneyList.size();
    }
    private OnItemClickListener mOnItemClickListener;//声明接口

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

}
