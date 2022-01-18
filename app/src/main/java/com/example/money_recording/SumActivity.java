package com.example.money_recording;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.TestLooperManager;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.money_recording.bean.Money;
import com.example.money_recording.database.MoneyDatabase;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class SumActivity extends AppCompatActivity {
    private Boolean isIncome =true;   //选中的是否是收入
    private MoneyDatabase moneyDB;
    private String dataBaseName = "MoneyDatabase";
    private TextView recentFive;
    private PieChart pieChart;
    private TextView income;
    private TextView expense;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sum);

        Button oneYear=findViewById(R.id.oneYear);
        Button oneMonth=findViewById(R.id.oneMonth);
        Button oneDay=findViewById(R.id.oneDay);
        income=findViewById(R.id.income);   //记录选区的范围内总的收入
        expense=findViewById(R.id.expense);   //记录选区的范围内总的支出
        recentFive=findViewById(R.id.recentFive);
        initDatabase();
        showRecentFive();
        //点击按钮后按钮变色，待补充
        oneYear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                oneYear.setBackgroundColor(Color.parseColor("#597f95"));
                oneMonth.setBackgroundColor(Color.parseColor("#597C65"));
                oneDay.setBackgroundColor(Color.parseColor("#597C65"));
                displayPieChart("year");
                System.out.println("year");
            }
        });
        oneMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                oneMonth.setBackgroundColor(Color.parseColor("#597f95"));
                oneYear.setBackgroundColor(Color.parseColor("#597C65"));
                oneDay.setBackgroundColor(Color.parseColor("#597C65"));
                displayPieChart("month");
                System.out.println("month");
            }
        });
        oneDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                oneDay.setBackgroundColor(Color.parseColor("#597f95"));
                oneYear.setBackgroundColor(Color.parseColor("#597C65"));
                oneMonth.setBackgroundColor(Color.parseColor("#597C65"));
                displayPieChart("day");
                System.out.println("day");
            }
        });
        //对于可视化年月日收支的饼图
        pieChart= findViewById(R.id.pie_chart);
        pieChart.setNoDataText("请点击上方的年/月/日收支以查看");



    }
    //初始化数据库
    private void initDatabase(){
        moneyDB = Room.databaseBuilder(this, MoneyDatabase.class, dataBaseName)
                .allowMainThreadQueries()
                .build();
    }
    //将最近的五条账目显示在主页
    private void showRecentFive(){
        List<Money> fiveMoney=moneyDB.getMoneyDao().getRecentFiveMoney();
        recentFive.setText("");
        for(int i=0;i<fiveMoney.size();i++){
            recentFive.append(fiveMoney.get(i).getString()+'\n');
        }

    }
    //显示添加账目的对话框
    private void showMyDialog(){
        View view = LayoutInflater.from(this).inflate(R.layout.my_dialog,null,false);
        final AlertDialog my_dialog= new AlertDialog.Builder(this).setView(view).create();
        //选中收入或支出
        RadioButton radioIncome=view.findViewById(R.id.income);
        RadioButton radioExpense=view.findViewById(R.id.expense);
        //显示收入或支出种类
        Spinner choice=view.findViewById(R.id.choice);
        Context context=this;
        Button decide_add=view.findViewById(R.id.decide_add);
        Spinner spin_year=view.findViewById(R.id.year);
        Spinner spin_month=view.findViewById(R.id.month);
        Spinner spin_day=view.findViewById(R.id.day);
        EditText edit_amount=view.findViewById(R.id.amount);
        //给年月日的spinner初始化可选项
        ArrayAdapter<CharSequence> year_adapter = ArrayAdapter.createFromResource(context,
                R.array.year, android.R.layout.simple_spinner_item);
        year_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin_year.setAdapter(year_adapter);
        ArrayAdapter<CharSequence> month_adapter = ArrayAdapter.createFromResource(context,
                R.array.month, android.R.layout.simple_spinner_item);
        month_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin_month.setAdapter(month_adapter);
        ArrayAdapter<CharSequence> day_adapter = ArrayAdapter.createFromResource(context,
                R.array.day, android.R.layout.simple_spinner_item);
        day_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin_day.setAdapter(day_adapter);
        //给年月日的spinner设置默认显示今天：
        Calendar calendar=Calendar.getInstance();
        spin_year.setSelection(0);
        spin_month.setSelection(calendar.get(Calendar.MONTH));
        spin_day.setSelection(calendar.get(Calendar.DATE)-1);
        //可选的种类 随选中收入还是支出决定
        radioIncome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(radioIncome.isChecked()){
                    ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context, R.array.income, android.R.layout.simple_spinner_item);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    choice.setAdapter(adapter);
                    isIncome =true;
                }
            }
        });
        radioExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (radioExpense.isChecked()){
                    ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context, R.array.expense, android.R.layout.simple_spinner_item);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    choice.setAdapter(adapter);
                    isIncome =false;
                }
            }
        });
        //确认添加账目
        decide_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //缺少年月日的合法性，待补充
                int year=Integer.parseInt(spin_year.getSelectedItem().toString());
                int month=Integer.parseInt(spin_month.getSelectedItem().toString());
                int day=Integer.parseInt(spin_day.getSelectedItem().toString());
                String purpose=choice.getSelectedItem().toString();
                float amount=Float.parseFloat(edit_amount.getText().toString());
                Money money=new Money();
                money.setYear(year);
                money.setDay(day);
                money.setMonth(month);
                money.setIsincome(isIncome);
                money.setPurpose(purpose);
                money.setAmount(amount);
                moneyDB.getMoneyDao().insert(money);
                showRecentFive();
                Toast t = Toast.makeText(context,"成功添加一笔账目", Toast.LENGTH_SHORT);
                t.show();
                my_dialog.dismiss();

            }
        });
        my_dialog.show();
    }
    //显示年/月/日的可视化饼图，category用于分辨显示哪种
    private void displayPieChart(String category){
        ArrayList<String> name = new ArrayList<>(Arrays.asList("食物", "衣物", "生活用品","其他支出", "工资","奖金"));
        List<PieEntry> pieEntries = new ArrayList<>();
        ArrayList<String> rate = new ArrayList<>();
        //计算百分比
        float[] data=new float[6];
        float inMoney=0;
        float outMoney=0;
        Calendar calendar=Calendar.getInstance();
        //分别是年月日的收支统计情况
        if(category=="year"){
            int year=calendar.get(Calendar.YEAR);
            List<Money> yearMoney=moneyDB.getMoneyDao().getAllMoneyOneYear(year);
            System.out.print("yearMoney    "+yearMoney.size()+'\n');
            for(int i=0;i<yearMoney.size();i++){
                System.out.print("yearMoney "+i+" "+yearMoney.get(i).getPurpose()+"   "+yearMoney.get(i).getAmount()+"\n");
                System.out.println(yearMoney.get(i).getPurpose()=="食物");
                if(yearMoney.get(i).getPurpose().equals("食物")){
                    data[0]+=yearMoney.get(i).getAmount();
                }
                else if (yearMoney.get(i).getPurpose().equals("衣物")){
                    data[1]+=yearMoney.get(i).getAmount();
                }
                else if (yearMoney.get(i).getPurpose().equals("生活用品")){
                    data[2]+=yearMoney.get(i).getAmount();
                }
                else if (yearMoney.get(i).getPurpose().equals("其他支出")){
                    data[3]+=yearMoney.get(i).getAmount();
                }
                else if (yearMoney.get(i).getPurpose().equals("工资")){
                    data[4]+=yearMoney.get(i).getAmount();
                }
                else if (yearMoney.get(i).getPurpose().equals("奖金")){
                    data[5]+=yearMoney.get(i).getAmount();
                }
            }


        }
        else if (category=="month"){
            int month=calendar.get(Calendar.MONTH)+1;
            List<Money> monthMoney=moneyDB.getMoneyDao().getAllMoneyOneMonth(month);
            for(int i=0;i<monthMoney.size();i++){
                if(monthMoney.get(i).getPurpose().equals("食物")){
                    data[0]+=monthMoney.get(i).getAmount();
                }
                else if (monthMoney.get(i).getPurpose().equals("衣物")){
                    data[1]+=monthMoney.get(i).getAmount();
                }
                else if (monthMoney.get(i).getPurpose().equals("生活用品")){
                    data[2]+=monthMoney.get(i).getAmount();
                }
                else if (monthMoney.get(i).getPurpose().equals("其他支出")){
                    data[3]+=monthMoney.get(i).getAmount();
                }
                else if (monthMoney.get(i).getPurpose().equals("工资")){
                    data[4]+=monthMoney.get(i).getAmount();
                }
                else if (monthMoney.get(i).getPurpose().equals("奖金")){
                    data[5]+=monthMoney.get(i).getAmount();
                }
            }

        }
        else if(category=="day"){
            int day=calendar.get(Calendar.DATE);
            List<Money> dayMoney=moneyDB.getMoneyDao().getAllMoneyOneDay(day);
            for(int i=0;i<dayMoney.size();i++){
                if(dayMoney.get(i).getPurpose().equals("食物")){
                    data[0]+=dayMoney.get(i).getAmount();
                }
                else if (dayMoney.get(i).getPurpose().equals("衣物")){
                    data[1]+=dayMoney.get(i).getAmount();
                }
                else if (dayMoney.get(i).getPurpose().equals("生活用品")){
                    data[2]+=dayMoney.get(i).getAmount();
                }
                else if (dayMoney.get(i).getPurpose().equals("其他支出")){
                    data[3]+=dayMoney.get(i).getAmount();
                }
                else if (dayMoney.get(i).getPurpose().equals("工资")){
                    data[4]+=dayMoney.get(i).getAmount();
                }
                else if (dayMoney.get(i).getPurpose().equals("奖金")){
                    data[5]+=dayMoney.get(i).getAmount();
                }
            }

        }
        float sum=data[0] + data[1] + data[2] + data[3] + data[4] + data[5];
        inMoney=data[0] + data[1] + data[2] + data[3];
        outMoney=data[4] + data[5];
        income.setText(String.valueOf(inMoney));
        expense.setText(String.valueOf(outMoney));
        //计算占比
        for(int i = 0; i < name.size(); i++) {
            DecimalFormat df = new DecimalFormat("0.00%");
            String decimal = df.format(data[i] / sum);
            PieEntry pieEntry = new PieEntry(data[i] / sum, name.get(i));
            pieEntries.add(pieEntry);
            rate.add(decimal);
        }
        PieDataSet pieDataSet = new PieDataSet(pieEntries,"");
        // 设置颜色list
        ArrayList<Integer> colors = new ArrayList<Integer>();
        colors.add(Color.parseColor("#FFFF66"));
        colors.add(Color.parseColor("#99CCCC"));
        colors.add(Color.parseColor("#CCCC99"));
        colors.add(Color.parseColor("#0099CC"));
        colors.add(Color.parseColor("#FFBB86FC"));
        colors.add(Color.parseColor("#CCCCFF"));
        pieDataSet.setColors(colors);
        pieDataSet.setSliceSpace(1f); //设置个饼状图之间的距离
        pieDataSet.setValueTextSize(0);
        //PieData
        PieData pieData = new PieData(pieDataSet);
        pieChart.setData(pieData);
        pieChart.setEntryLabelTextSize(0);//标签不显示
        Legend legend = pieChart.getLegend();
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        legend.setTextSize(12);
        //设置点击事件，点击扇形图的一部分可以显示占比
        pieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                if(e == null) {
                    return;
                }
                if(pieEntries.get(0).getValue() == e.getY()) {
                    Toast.makeText(SumActivity.this, name.get(0) + "占比" + rate.get(0), Toast.LENGTH_SHORT).show();
                }else if(pieEntries.get(1).getValue() == e.getY()) {
                    Toast.makeText(SumActivity.this, name.get(1) + "占比" + rate.get(1), Toast.LENGTH_SHORT).show();
                }else if(pieEntries.get(2).getValue() == e.getY()) {
                    Toast.makeText(SumActivity.this, name.get(2) + "占比" + rate.get(2), Toast.LENGTH_SHORT).show();
                }else if(pieEntries.get(3).getValue() == e.getY()) {
                    Toast.makeText(SumActivity.this, name.get(3) + "占比" + rate.get(3), Toast.LENGTH_SHORT).show();
                }else if(pieEntries.get(4).getValue() == e.getY()) {
                    Toast.makeText(SumActivity.this, name.get(4) + "占比" + rate.get(4), Toast.LENGTH_SHORT).show();
                }else if(pieEntries.get(5).getValue() == e.getY()) {
                    Toast.makeText(SumActivity.this, name.get(5) + "占比" + rate.get(5), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onNothingSelected() {

            }
        });
        pieChart.invalidate();  //显示扇形图


    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu){
        return super.onPrepareOptionsMenu(menu);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //创建Menu菜单
        getMenuInflater().inflate(R.menu.my_menu,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //对菜单项点击内容进行设置
        int id = item.getItemId();
        if (id == R.id.addMoney) {
            showMyDialog();
        }
        else if(id == R.id.searchMoney){
            Intent intent=new Intent(SumActivity.this, SearchActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}
