package com.example.controlmarket;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {

    private EditText etUsuario, etClave;
    private Button btnRegistrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etUsuario = findViewById(R.id.etUsuarioRegistro);
        etClave = findViewById(R.id.etClaveRegistro);
        btnRegistrar = findViewById(R.id.btnRegistrar);

        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String usuario = etUsuario.getText().toString();
                String clave = etClave.getText().toString();

                if (!usuario.isEmpty() && !clave.isEmpty()) {
                    SharedPreferences prefs = getSharedPreferences("usuarios", MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString(usuario, clave);
                    editor.apply();

                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    if (usuario.isEmpty()) {
                        etUsuario.setError(getString(R.string.msg_register_error));
                    }
                    if (clave.isEmpty()) {
                        etClave.setError(getString(R.string.msg_register_error));
                    }
                }
            }
        });
    }
}
