package com.example.controlmarket;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "compras")
public class Purchase {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public String nombre;
    public String categoria;
    public double precio;
    public int cantidad;
    public double descuento;
    public String fecha;
    public double latitud;
    public double longitud;

    public Purchase() {}

    public double calcularTotal() {
        double subtotal = precio * cantidad;
        return subtotal - (subtotal * descuento / 100);
    }
}