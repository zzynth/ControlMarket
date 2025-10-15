package com.example.controlmarket;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import androidx.room.Delete;

import java.util.List;

@Dao
public interface PurchaseDao {

    @Insert
    void insertar(Purchase compra);

    @Update
    void actualizar(Purchase compra);

    @Delete
    void eliminar(Purchase compra);

    @Query("SELECT * FROM compras ORDER BY fecha DESC")
    List<Purchase> obtenerTodas();
}
