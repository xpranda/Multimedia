package com.example.ac1tem5;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MenuPrincipal extends MainActivity {

    private TextView textView7;
    private Button button5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_principal);


        textView7 = findViewById(R.id.textView7);
        button5 = findViewById(R.id.button5);


        String correo = getIntent().getStringExtra("correo");


        if (correo != null) {
            textView7.setText(correo);
        }


        button5.setOnClickListener(v -> {
            finish();
        });
    }
}