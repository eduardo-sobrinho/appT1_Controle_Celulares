package com.example.trab1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private Button btnCadastrar;
    private Button btnCadModelo;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnCadastrar = findViewById(R.id.btnCad);
        btnCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it;
                it = new Intent(MainActivity.this, CadastroMarca.class);
                startActivity(it);
            }
        });

        btnCadModelo = findViewById(R.id.btnCadModelo);
        btnCadModelo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it;
                it = new Intent(MainActivity.this, CadastroModelo.class);
                startActivity(it);
            }
        });
    }
}