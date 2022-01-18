package com.example.money_recording.bean;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "money")
public class Money {  //金额，用途，日期，分类
    @PrimaryKey(autoGenerate = true) //主键是否自动增长，默认为false
    private int id;
    private int year;
    private int month;
    private int day;
    private String purpose;
    private float amount;
    private boolean isincome;  //收入还是支出

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }
    public float getAmount() {
        return amount;
    }

    public void setYear(int year) {
        this.year = year;
    }
    public int getYear() {
        return year;
    }

    public void setMonth(int month) {
        this.month = month;
    }
    public int getMonth() {
        return month;
    }

    public void setDay(int day) {
        this.day = day;
    }
    public int getDay() {
        return day;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }
    public String getPurpose() {
        return purpose;
    }

    public void setIsincome(boolean isincome) {
        this.isincome = isincome;
    }
    public boolean getIsincome() {
        return isincome;
    }
    public String getString(){
        String result="";
        if(isincome){
            result+="收入 ";
        }
        else {
            result+="支出 ";
        }
        result+=year;
        result+="年";
        result+=month;
        result+="月";
        result+=day;
        result+="日 ";
        result+="用途为：";
        result+=purpose;
        result+="  金额：";
        if(isincome){
            result+="+";
        }
        else {
            result+="-";
        }
        result+=amount;
        return result;
    }
}
