package com.example.controlmarket;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


public class PurchaseActivity extends AppCompatActivity {

    private EditText etNombre, etCategoria, etPrecio, etCantidad, etDescuento, etFecha;
    private Button btnGuardar;
    private MapView mapView;
    private GoogleMap mapa;
    private double latitud = -33.0;
    private double longitud = -70.0;

    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase);

        etNombre = findViewById(R.id.etNombreProducto);
        etCategoria = findViewById(R.id.etCategoria);
        etPrecio = findViewById(R.id.etPrecio);
        etCantidad = findViewById(R.id.etCantidad);
        etDescuento = findViewById(R.id.etDescuento);
        etFecha = findViewById(R.id.etFecha);
        btnGuardar = findViewById(R.id.btnGuardar);
        mapView = findViewById(R.id.mapView);

        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mapa = googleMap;
                LatLng ubicacion = new LatLng(latitud, longitud);
                mapa.addMarker(new MarkerOptions().position(ubicacion).title("Ubicación de compra"));
                mapa.moveCamera(CameraUpdateFactory.newLatLngZoom(ubicacion, 15));
            }
        });

        db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "control_market_db").allowMainThreadQueries().build();

        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Purchase compra = new Purchase();
                compra.nombre = etNombre.getText().toString();
                compra.categoria = etCategoria.getText().toString();
                compra.precio = Double.parseDouble(etPrecio.getText().toString());
                compra.cantidad = Integer.parseInt(etCantidad.getText().toString());
                compra.descuento = Double.parseDouble(etDescuento.getText().toString());
                compra.fecha = etFecha.getText().toString();
                compra.latitud = latitud;
                compra.longitud = longitud;

                db.purchaseDao().insertar(compra);
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }
}
