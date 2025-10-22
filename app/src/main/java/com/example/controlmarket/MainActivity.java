package com.example.controlmarket;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView rvMovimientos;
    private PurchaseAdapter adapter;
    private AppDatabase db;
    private TextView tvSaldo;
    private Button btnAgregarCompra;
    private SharedPreferences prefs;
    private static final String PREFS_NAME = "ControlMarketPrefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = AppDatabase.getInstance(this);
        prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        rvMovimientos = findViewById(R.id.rvMovimientos);
        tvSaldo = findViewById(R.id.tvSaldo);
        btnAgregarCompra = findViewById(R.id.btnAgregarCompra);

        rvMovimientos.setLayoutManager(new LinearLayoutManager(this));
        List<Purchase> compras = db.purchaseDao().obtenerTodas();
        adapter = new PurchaseAdapter(compras, compra -> {
            Intent intent = new Intent(MainActivity.this, DetailPurchaseActivity.class);
            intent.putExtra("compra_id", compra.id);
            startActivity(intent);
        });
        rvMovimientos.setAdapter(adapter);

        actualizarSaldo();

        btnAgregarCompra.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, PurchaseActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        List<Purchase> comprasActualizadas = db.purchaseDao().obtenerTodas();
        adapter.setData(comprasActualizadas);
        actualizarSaldo();
    }

    private void actualizarSaldo() {
        long saldoBits = prefs.getLong("saldo", Double.doubleToLongBits(100000));
        double saldo = Double.longBitsToDouble(saldoBits);
        tvSaldo.setText(String.format("Saldo: $%.0f", saldo));
    }
}