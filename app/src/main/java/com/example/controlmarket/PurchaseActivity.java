package com.example.controlmarket;

import android.Manifest;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.room.Room;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class PurchaseActivity extends AppCompatActivity {

    private EditText etNombre, etPrecio, etCantidad, etDescuento;
    private Button btnGuardar;
    private TextView tvDireccion;
    private MapView mapView;
    private GoogleMap mapa;

    private double latitud = -33.0;
    private double longitud = -70.0;

    private AppDatabase db;
    private FusedLocationProviderClient fusedLocationClient;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase);

        etNombre = findViewById(R.id.etNombreProducto);
        etPrecio = findViewById(R.id.etPrecio);
        etCantidad = findViewById(R.id.etCantidad);
        etDescuento = findViewById(R.id.etDescuento);
        btnGuardar = findViewById(R.id.btnGuardarCompra);
        tvDireccion = findViewById(R.id.tvDireccion);
        mapView = findViewById(R.id.mapView);

        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mapa = googleMap;
                mostrarUbicacionEnMapa();

                mapa.setOnMapClickListener(latLng -> {
                    latitud = latLng.latitude;
                    longitud = latLng.longitude;
                    mapa.clear();
                    mapa.addMarker(new MarkerOptions().position(latLng).title("Ubicación seleccionada"));
                    mapa.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
                    obtenerDireccion(latitud, longitud);
                });
            }
        });

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != android.content.pm.PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            obtenerUbicacionActual();
        }

        db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "control_market_db").allowMainThreadQueries().build();

        btnGuardar.setOnClickListener(view -> {
            if (etNombre.getText().toString().isEmpty() ||
                    etPrecio.getText().toString().isEmpty() ||
                    etCantidad.getText().toString().isEmpty() ||
                    etDescuento.getText().toString().isEmpty()) {

                etNombre.setError("Completa todos los campos");
                return;
            }

            if (latitud == -33.0 && longitud == -70.0) {
                Toast.makeText(PurchaseActivity.this, "Selecciona una ubicación en el mapa", Toast.LENGTH_SHORT).show();
                return;
            }

            double precio = Double.parseDouble(etPrecio.getText().toString());
            int cantidad = Integer.parseInt(etCantidad.getText().toString());
            double descuento = Double.parseDouble(etDescuento.getText().toString());
            double total = (precio * cantidad) - descuento;

            Purchase compra = new Purchase();
            compra.nombre = etNombre.getText().toString();
            compra.precio = precio;
            compra.cantidad = cantidad;
            compra.descuento = descuento;
            compra.latitud = latitud;
            compra.longitud = longitud;

            db.purchaseDao().insertar(compra);

            SharedPreferences prefs = getSharedPreferences("ControlMarketPrefs", MODE_PRIVATE);
            double saldo = Double.longBitsToDouble(
                    prefs.getLong("saldo", Double.doubleToLongBits(100000))
            );
            saldo -= total;
            prefs.edit().putLong("saldo", Double.doubleToLongBits(saldo)).apply();

            finish();
        });
    }

    private void obtenerUbicacionActual() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != android.content.pm.PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != android.content.pm.PackageManager.PERMISSION_GRANTED) {
            return;
        }

        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, location -> {
                    if (location != null) {
                        latitud = location.getLatitude();
                        longitud = location.getLongitude();
                        mostrarUbicacionEnMapa();
                        obtenerDireccion(latitud, longitud);
                    }
                });
    }

    private void mostrarUbicacionEnMapa() {
        if (mapa != null) {
            LatLng ubicacion = new LatLng(latitud, longitud);
            mapa.clear();
            mapa.addMarker(new MarkerOptions().position(ubicacion).title("Ubicación actual"));
            mapa.moveCamera(CameraUpdateFactory.newLatLngZoom(ubicacion, 15));
        }
    }

    private void obtenerDireccion(double lat, double lon) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> direcciones = geocoder.getFromLocation(lat, lon, 1);
            if (direcciones != null && !direcciones.isEmpty()) {
                Address direccion = direcciones.get(0);
                String texto = direccion.getAddressLine(0);
                tvDireccion.setText("Dirección: " + texto);
            } else {
                tvDireccion.setText("Dirección: No disponible");
            }
        } catch (IOException e) {
            tvDireccion.setText("Error al obtener dirección");
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == android.content.pm.PackageManager.PERMISSION_GRANTED) {
                obtenerUbicacionActual();
            }
        }
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

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
}