package com.example.controlmarket;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private AppDatabase db;
    private RecyclerView rvMovimientos;
    private PurchaseAdapter adapter;
    private Button btnAgregarCompra;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rvMovimientos = findViewById(R.id.rvMovimientos);
        btnAgregarCompra = findViewById(R.id.btnAgregarCompra);

        db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "control_market_db").allowMainThreadQueries().build();

        List<Purchase> compras = db.purchaseDao().obtenerTodas();
        adapter = new PurchaseAdapter(compras);
        rvMovimientos.setLayoutManager(new LinearLayoutManager(this));
        rvMovimientos.setAdapter(adapter);

        btnAgregarCompra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, PurchaseActivity.class);
                startActivity(intent);
            }
        });
    }
}