package com.example.trab1;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class AtualizaModelo extends AppCompatActivity {
    private SQLiteDatabase database;
    private EditText modelo;
    private Button btnVoltar;
    private Button btnAtualizar;
    String modeloStr;
    String marcaIdStr;
    String marcaStr;
    private String itemMarca;
    private String itemFkId;

    BaseDAO helper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.atualiza_modelo);

        Intent itRecebedora = getIntent();
        Bundle param = itRecebedora.getExtras();
        if (param != null) {
            modeloStr = param.getString("modelo");
            marcaIdStr = param.getString("marcaId");
            marcaStr = param.getString("marca");
            modelo = findViewById(R.id.txtModelo);
            modelo.setText(modeloStr);

            ArrayList<String> marca = getIntent().getStringArrayListExtra("marcas");
            final Spinner spinner = findViewById(R.id.spinner1);

            ArrayAdapter<String> adapter1 = new ArrayAdapter<String>
                    (AtualizaModelo.this, android.R.layout.simple_expandable_list_item_1, marca);
            spinner.setAdapter(adapter1);

            adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter1);

            int spinnerPosition = adapter1.getPosition(marcaStr);
            spinner.setSelection(spinnerPosition);

            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    Object objMarca = parent.getItemAtPosition(position);
                    itemMarca = objMarca.toString();
                }
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });
        }

        btnVoltar = findViewById(R.id.btnVoltar);
        btnVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnAtualizar = findViewById(R.id.btnAtModelo);
        btnAtualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modelo = findViewById(R.id.txtModelo);
                String txtModelo = modelo.getText().toString().trim();
                modelo.setText("");

                if (txtModelo.length() > 0) {
                    helper = new BaseDAO(AtualizaModelo.this);
                    database = helper.getWritableDatabase();

                    String usersSelectQuery = String.format("SELECT marcaId FROM Marca WHERE marca = ?");
                    Cursor cursor = database.rawQuery(usersSelectQuery, new String[]{itemMarca});

                    if (cursor.moveToFirst()) {
                        do {
                            itemFkId = cursor.getString(0);
                        } while(cursor.moveToNext());
                    }

                    ContentValues args = new ContentValues();
                    args.put("marcaId", itemFkId);
                    args.put("modelo", txtModelo);
                    database.update("Celular", args, "marcaId=? and modelo=?", new String[]{marcaIdStr, modeloStr});

                    Toast.makeText(AtualizaModelo.this, "Registro atualizado!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        //database.execSQL("DELETE FROM Celular");
        //database.close();
    }
}