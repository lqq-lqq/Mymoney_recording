package com.example.money_recording.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.money_recording.bean.Money;
import com.example.money_recording.dao.MoneyDao;

@Database(entities = { Money.class }, version = 1,exportSchema = false)
public abstract class MoneyDatabase extends RoomDatabase {
    private static final String DB_NAME = "MoneyDatabase.db";
    private static volatile MoneyDatabase instance;
    static synchronized MoneyDatabase getInstance(Context context) {
        if (instance == null) {
            instance = create(context);
        }
        return instance;
    }
    private static MoneyDatabase create(final Context context) {
        return Room.databaseBuilder(
                context, MoneyDatabase.class, DB_NAME).build();
    }
    public abstract MoneyDao getMoneyDao();
}
