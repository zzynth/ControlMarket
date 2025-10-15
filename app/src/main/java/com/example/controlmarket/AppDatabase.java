package com.example.controlmarket;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Purchase.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract PurchaseDao purchaseDao();
}