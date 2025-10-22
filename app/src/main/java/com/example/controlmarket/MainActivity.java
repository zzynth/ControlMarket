package com.example.controlmarket;

import android.content.Intent;
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
    private TextView tvTotalGastado;
    private Button btnAgregarCompra;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = AppDatabase.getInstance(this);

        rvMovimientos = findViewById(R.id.rvMovimientos);
        tvTotalGastado = findViewById(R.id.tvTotalGastado);
        btnAgregarCompra = findViewById(R.id.btnAgregarCompra);

        rvMovimientos.setLayoutManager(new LinearLayoutManager(this));
        List<Purchase> compras = db.purchaseDao().obtenerTodas();
        adapter = new PurchaseAdapter(compras, compra -> {
            Intent intent = new Intent(MainActivity.this, DetailPurchaseActivity.class);
            intent.putExtra("compra_id", compra.id);
            startActivity(intent);
        });
        rvMovimientos.setAdapter(adapter);

        actualizarTotalGastado();

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
        actualizarTotalGastado();
    }

    private void actualizarTotalGastado() {
        Double total = db.purchaseDao().obtenerTotalGastado();
        if (total == null) total = 0.0;
        tvTotalGastado.setText(String.format("Total gastado: $%.0f", total));
    }
}