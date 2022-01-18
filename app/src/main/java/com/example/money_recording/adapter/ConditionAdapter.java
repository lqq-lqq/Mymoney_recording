package com.example.money_recording.adapter;

import android.graphics.Color;
import android.hardware.lights.LightState;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.money_recording.R;

import java.util.ArrayList;
import java.util.List;

public class ConditionAdapter extends RecyclerView.Adapter<ConditionAdapter.ViewHolder>{
    private List<String> conditionList;
    private String chooseCondition;     //记录选择中了什么条件用作筛选用
    private TextView lastConditionText;   //用于记录上一个选中的筛选条件，便于下次选择时，将选中标记删除
    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView conditionText;
        View conditionView;
        public ViewHolder(View view){
            super(view);
            conditionView=view;
            conditionText=view.findViewById(R.id.condition_item);
        }
    }
    public ConditionAdapter(List<String> cl){
        conditionList=cl;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.condition_item,parent,false);
        ViewHolder holder=new ViewHolder(view);
        holder.conditionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position=holder.getAdapterPosition();
                String thiscondition=conditionList.get(position);
                chooseCondition=thiscondition;
                System.out.println(lastConditionText);
                System.out.println(holder.conditionText);
                System.out.println();
                if(lastConditionText!=null){
                    lastConditionText.setTextColor(Color.parseColor("#8A000000"));
                }
                holder.conditionText.setTextColor(Color.parseColor("#E63946"));
                lastConditionText=holder.conditionText;
            }
        });
        return holder;
    }
    //返回正在选择的筛选条件
    public String getChooseCondition(){
        return chooseCondition;
    }
    @Override
    public void onBindViewHolder(ViewHolder holder, int position){
        String condition=conditionList.get(position);
        holder.conditionText.setText(condition);
    }
    @Override
    public int getItemCount(){
        return conditionList.size();
    }
}
