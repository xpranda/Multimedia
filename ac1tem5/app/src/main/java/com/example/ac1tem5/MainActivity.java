package com.example.ac1tem5;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private EditText tCorreo;
    private EditText tContraseña;
    private Button bContinuar;
    private Switch sRecordar;
    private TextView tvMensaje;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        tCorreo = findViewById(R.id.etCorreo);
        tContraseña = findViewById(R.id.etContraseña);
        bContinuar = findViewById(R.id.bContinuar);
        sRecordar = findViewById(R.id.sRecordar);
        tvMensaje = findViewById(R.id.tvMensaje);

        bContinuar.setOnClickListener(v -> {
            String correo = tCorreo.getText().toString();
            String contraseña = tContraseña.getText().toString();

            Boolean recordar = sRecordar.isChecked();


            if(correo.equals("ines@correo.com") && contraseña.equals("limon")){
                tvMensaje.setText("usuario y contraseña correctos \n Almacenados para siguientes accesos");
                tvMensaje.setTextColor(ContextCompat.getColor(this, R.color.green));
            }
            else{
                tvMensaje.setText("usuario y/o contraseña incorrectos");
                tvMensaje.setTextColor(ContextCompat.getColor(this, R.color.red));

            }


        });
    }
}