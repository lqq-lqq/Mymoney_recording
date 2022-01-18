package com.example.money_recording;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.money_recording.adapter.StartPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ViewPager startViewPager;
    private ArrayList<View> viewList;
    private StartPagerAdapter sAdapter;
    private List<ImageView> dots;    //关于下面显示的小圆点
    private LinearLayout mLinearLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startViewPager = (ViewPager) findViewById(R.id.startViewPager);
        viewList = new ArrayList<View>();
        LayoutInflater li = getLayoutInflater();
        //以下将三个layout加入到viewPager中
        viewList.add(li.inflate(R.layout.view_one,null,false));
        viewList.add(li.inflate(R.layout.view_two,null,false));
        viewList.add(li.inflate(R.layout.view_three,null,false));
        sAdapter = new StartPagerAdapter(viewList);
        startViewPager.setAdapter(sAdapter);
        //跳转到主页面的“进入”按钮
        Button start=viewList.get(2).findViewById(R.id.start);
        System.out.println(start);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this, SumActivity.class);
                startActivity(intent);
            }
        });
    }
}