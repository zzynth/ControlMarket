package com.example.controlmarket;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private AppDatabase db;
    private RecyclerView rvMovimientos;
    private PurchaseAdapter adapter;
    private Button btnAgregarCompra;
    private Button btnResetDB;
    private TextView tvSaldo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rvMovimientos = findViewById(R.id.rvMovimientos);
        btnAgregarCompra = findViewById(R.id.btnAgregarCompra);
        btnResetDB = findViewById(R.id.btnResetDB);
        tvSaldo = findViewById(R.id.tvSaldo);

        db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "control_market_db").allowMainThreadQueries().build();

        // Lista inicial
        List<Purchase> compras = db.purchaseDao().obtenerTodas();
        adapter = new PurchaseAdapter(compras);
        rvMovimientos.setLayoutManager(new LinearLayoutManager(this));
        rvMovimientos.setAdapter(adapter);

        // Mostrar saldo inicial
        actualizarSaldo();

        btnAgregarCompra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, PurchaseActivity.class);
                startActivity(intent);
            }
        });

        // Resetear base de datos (vaciar todas las tablas) y reiniciar saldo
        btnResetDB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.clearAllTables();
                adapter.setData(new ArrayList<>());

                SharedPreferences prefs = getSharedPreferences("ControlMarketPrefs", MODE_PRIVATE);
                prefs.edit().putLong("saldo", Double.doubleToLongBits(100000)).apply();
                actualizarSaldo();

                Toast.makeText(MainActivity.this, "Base de datos reseteada", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Refrescar saldo y lista al volver
        actualizarSaldo();
        List<Purchase> comprasActualizadas = db.purchaseDao().obtenerTodas();
        adapter.setData(comprasActualizadas);
    }

    private void actualizarSaldo() {
        SharedPreferences prefs = getSharedPreferences("ControlMarketPrefs", MODE_PRIVATE);
        double saldo = Double.longBitsToDouble(
                prefs.getLong("saldo", Double.doubleToLongBits(100000)) // saldo inicial 100000
        );
        tvSaldo.setText("Saldo: $" + saldo);
    }
}