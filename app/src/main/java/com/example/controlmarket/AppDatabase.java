package com.example.controlmarket;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Purchase.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase INSTANCE;

    public abstract PurchaseDao purchaseDao();

    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "control_market_db")
                    .allowMainThreadQueries() // solo para desarrollo
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return INSTANCE;
    }
}