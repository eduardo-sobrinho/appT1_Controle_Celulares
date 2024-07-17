package com.example.trab1;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AtualizaMarca extends AppCompatActivity {
    String marcaStr;
    EditText marca;
    Button btnVoltar;
    Button btnAtualizar;
    private SQLiteDatabase database;
    BaseDAO helper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.atualiza_marca);

        Intent itRecebedora = getIntent();
        Bundle param = itRecebedora.getExtras();
        if (param != null) {
            marcaStr = param.getString("marca");
            marca = findViewById(R.id.txtMarca);
            marca.setText(marcaStr);
        }

        helper = new BaseDAO(AtualizaMarca.this);
        database = helper.getWritableDatabase();

        btnVoltar = findViewById(R.id.btnVoltar);
        btnVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnAtualizar = findViewById(R.id.btnAtualizar);
        btnAtualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                marca = findViewById(R.id.txtMarca);
                String marcaNovo = marca.getText().toString().trim();
                if (!marcaNovo.isEmpty()) {
                    ContentValues args = new ContentValues();
                    args.put("marca", marcaNovo);
                    database.update("Marca", args, "marca=?", new String[]{marcaStr});

                    finish();
                } else {
                    Toast.makeText(AtualizaMarca.this, "Campo não pode ser vazio!", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}