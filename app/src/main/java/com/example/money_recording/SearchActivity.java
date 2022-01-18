package com.example.money_recording;


import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Toast;

import com.example.money_recording.adapter.ConditionAdapter;
import com.example.money_recording.adapter.MoneyAdapter;
import com.example.money_recording.bean.Money;
import com.example.money_recording.database.MoneyDatabase;
import com.example.money_recording.fragment.ShowMoneyFragment;
import com.example.money_recording.interface1.OnItemClickListener;


import java.util.ArrayList;
import java.util.List;

//public class SearchActivity extends AppCompatActivity{
public class SearchActivity extends FragmentActivity{
    private List<String> year;
    private List<String> month;
    private List<String> day;
    private List<String> category;
    private List<Money> moneySearched;
    private MoneyDatabase moneyDB;
    private String dataBaseName = "MoneyDatabase";
    private String[] chosen;  //选中的筛选条件
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        chosen=new String[4];
        initDatabase();
        //添加选择年份的recycleView
        initYears();
        RecyclerView yearRecycleView=findViewById(R.id.yearRecyclerView);
        LinearLayoutManager yearLayoutManager=new LinearLayoutManager(this);
        yearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        yearRecycleView.setLayoutManager(yearLayoutManager);
        ConditionAdapter yearConditionAdapter=new ConditionAdapter(year);
        yearRecycleView.setAdapter(yearConditionAdapter);

        //添加选择月份的recycleView
        initMonths();
        RecyclerView monthRecycleView=findViewById(R.id.monthRecyclerView);
        LinearLayoutManager monthLayoutManager=new LinearLayoutManager(this);
        monthLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        monthRecycleView.setLayoutManager(monthLayoutManager);
        ConditionAdapter monthConditionAdapter=new ConditionAdapter(month);
        monthRecycleView.setAdapter(monthConditionAdapter);

        //添加选择日的recycleView
        initdays();
        RecyclerView dayRecycleView=findViewById(R.id.dayRecyclerView);
        LinearLayoutManager dayLayoutManager=new LinearLayoutManager(this);
        dayLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        dayRecycleView.setLayoutManager(dayLayoutManager);
        ConditionAdapter dayConditionAdapter=new ConditionAdapter(day);
        dayRecycleView.setAdapter(dayConditionAdapter);

        //添加选择种类的recycleView
        initCategory();
        RecyclerView classRecycleView=findViewById(R.id.classRecyclerView);
        LinearLayoutManager classLayoutManager=new LinearLayoutManager(this);
        classLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        classRecycleView.setLayoutManager(classLayoutManager);
        ConditionAdapter classConditionAdapter=new ConditionAdapter(category);
        classRecycleView.setAdapter(classConditionAdapter);
        chosen[3]=classConditionAdapter.getChooseCondition();

        //fragment
        ShowMoneyFragment showMoneyFragment=new ShowMoneyFragment();
        //getFragmentManager().beginTransaction().show(mAFragment);


        //展示搜索结果的recycleView
        RecyclerView searchResultRecycleView=findViewById(R.id.searchResultRecycleView);
        LinearLayoutManager resultLayoutManager=new LinearLayoutManager(this);
        searchResultRecycleView.setLayoutManager(resultLayoutManager);
        moneySearched=new ArrayList<>();

        //开始筛选的按钮及响应事件
        Button decide_search=findViewById(R.id.decide_search);
        decide_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //得到当前选中的四个条件
                chosen[0]=yearConditionAdapter.getChooseCondition();
                chosen[1]=monthConditionAdapter.getChooseCondition();
                chosen[2]=dayConditionAdapter.getChooseCondition();
                chosen[3]=classConditionAdapter.getChooseCondition();
                if(chosen[0]==null || chosen[1]==null || chosen[2]==null || chosen[3]==null){
                    Toast.makeText(SearchActivity.this, "请完整地选择筛选条件", Toast.LENGTH_SHORT).show();

                }
                else{
                    List<Money> searchResult=moneyDB.getMoneyDao().select(Integer.parseInt(chosen[0]),
                            Integer.parseInt(chosen[1]),
                            Integer.parseInt(chosen[2]),
                            chosen[3]);
                    moneySearched.clear();
                    for(int i=0;i<searchResult.size();i++){

                        moneySearched.add(searchResult.get(i));
                    }
                    MoneyAdapter moneyAdapter=new MoneyAdapter(moneySearched);
                    searchResultRecycleView.setAdapter(moneyAdapter);
                    moneyAdapter.setOnItemClickListener(new OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {
                            Money clickMoney=moneySearched.get(position);
                            final AlertDialog.Builder normalDialog =
                                    new AlertDialog.Builder(SearchActivity.this);
                            //normalDialog.setIcon(R.drawable.icon_dialog);
                            //normalDialog.setTitle("确认删除？");
                            normalDialog.setMessage("确认删除？");
                            normalDialog.setPositiveButton("确定",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            moneyDB.getMoneyDao().delete(clickMoney);
                                            Toast.makeText(SearchActivity.this, "已成功删除", Toast.LENGTH_SHORT).show();
                                            //moneyAdapter.notifyDataSetChanged();
                                        }
                                    });
                            normalDialog.setNegativeButton("取消",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            return;
                                        }
                                    });
                            // 显示
                            normalDialog.show();
                        }
                    });
                }

            }
        });

    }
    private void initYears(){
        year=new ArrayList<>();
        year.add("2021");
        year.add("2020");
        year.add("2019");
    }
    private void initMonths(){
        month=new ArrayList<>();
        month.add("1");
        month.add("2");
        month.add("3");
        month.add("4");
        month.add("5");
        month.add("6");
        month.add("7");
        month.add("8");
        month.add("9");
        month.add("10");
        month.add("11");
        month.add("12");
    }
    private void initdays(){
        day=new ArrayList<>();
        day.add("1");
        day.add("2");
        day.add("3");
        day.add("4");
        day.add("5");
        day.add("6");
        day.add("7");
        day.add("8");
        day.add("9");
        day.add("10");
        day.add("11");
        day.add("12");
        day.add("13");
        day.add("14");
        day.add("15");
        day.add("16");
        day.add("17");
        day.add("18");
        day.add("19");
        day.add("20");
        day.add("21");
        day.add("22");
        day.add("23");
        day.add("24");
        day.add("25");
        day.add("26");
        day.add("27");
        day.add("28");
        day.add("29");
        day.add("30");
        day.add("31");
    }
    private void initCategory(){
        category=new ArrayList<>();
        category.add("食物");
        category.add("衣物");
        category.add("生活用品");
        category.add("其他支出");
        category.add("工资");
        category.add("奖金");
    }
    //初始化数据库
    private  void initDatabase(){
        moneyDB = Room.databaseBuilder(this, MoneyDatabase.class, dataBaseName)
                .allowMainThreadQueries()
                .build();
    }
}