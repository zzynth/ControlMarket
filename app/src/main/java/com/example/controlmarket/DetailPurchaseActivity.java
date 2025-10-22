package com.example.controlmarket;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class DetailPurchaseActivity extends AppCompatActivity {

    private EditText etNombre, etPrecio, etCantidad, etDescuento, etDireccion;
    private Button btnGuardar, btnEliminar, btnVerMapa;
    private AppDatabase db;
    private Purchase compra;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_purchase);

        etNombre = findViewById(R.id.etNombre);
        etPrecio = findViewById(R.id.etPrecio);
        etCantidad = findViewById(R.id.etCantidad);
        etDescuento = findViewById(R.id.etDescuento);
        etDireccion = findViewById(R.id.etDireccion);
        btnGuardar = findViewById(R.id.btnGuardar);
        btnEliminar = findViewById(R.id.btnEliminar);
        btnVerMapa = findViewById(R.id.btnVerMapa);

        db = AppDatabase.getInstance(this);

        int compraId = getIntent().getIntExtra("compra_id", -1);
        if (compraId == -1) {
            Toast.makeText(this, "ID inválido", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        compra = db.purchaseDao().buscarPorId(compraId);
        if (compra == null) {
            Toast.makeText(this, "Compra no encontrada", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        etNombre.setText(compra.nombre);
        etPrecio.setText(String.valueOf(compra.precio));
        etCantidad.setText(String.valueOf(compra.cantidad));
        etDescuento.setText(String.valueOf(compra.descuento));
        etDireccion.setText(compra.direccion);

        btnGuardar.setOnClickListener(v -> {
            try {
                compra.nombre = etNombre.getText().toString();
                compra.precio = Double.parseDouble(etPrecio.getText().toString());
                compra.cantidad = Integer.parseInt(etCantidad.getText().toString());
                compra.descuento = Double.parseDouble(etDescuento.getText().toString());
                compra.direccion = etDireccion.getText().toString();

                db.purchaseDao().actualizar(compra);
                Toast.makeText(this, "Compra actualizada", Toast.LENGTH_SHORT).show();
                finish();
            } catch (Exception e) {
                Toast.makeText(this, "Error al guardar", Toast.LENGTH_SHORT).show();
            }
        });

        btnEliminar.setOnClickListener(v -> {
            db.purchaseDao().eliminar(compra);
            Toast.makeText(this, "Compra eliminada", Toast.LENGTH_SHORT).show();
            finish();
        });

        btnVerMapa.setOnClickListener(v -> {
            Intent intent = new Intent(DetailPurchaseActivity.this, MapActivity.class);
            intent.putExtra("latitud", compra.latitud);
            intent.putExtra("longitud", compra.longitud);
            startActivity(intent);
        });
    }
}
