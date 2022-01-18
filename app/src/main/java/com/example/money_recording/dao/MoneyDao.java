package com.example.money_recording.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.money_recording.bean.Money;

import java.util.List;

@Dao
public interface MoneyDao {
    @Query("SELECT * FROM money")
    List<Money> getAllMoney();
    @Query("SELECT * FROM money WHERE day =:day")
    List<Money> getAllMoneyOneDay(int day);
    @Query("SELECT * FROM money WHERE month =:month")
    List<Money> getAllMoneyOneMonth(int month);
    @Query("SELECT * FROM money WHERE year =:year")
    List<Money> getAllMoneyOneYear(int year);
    @Query("select * from money order by id desc LIMIT 5")
    List<Money> getRecentFiveMoney();
    @Query("select * from money WHERE year=:year and month=:month and day=:day and purpose=:purpose")
    List<Money> select(int year,int month,int day,String purpose);
    @Insert
    void insert(Money money);
    @Update
    void update(Money money);
    @Delete
    void delete(Money money);

}
