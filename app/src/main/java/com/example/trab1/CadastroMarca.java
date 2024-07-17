package com.example.trab1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class CadastroMarca extends AppCompatActivity {
    private SQLiteDatabase database;
    private EditText marca;
    private Button btnVoltar;
    ListView listView;
    BaseDAO helper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cadastro_marcas);

        btnVoltar = findViewById(R.id.btnVoltar);
        btnVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        listView = (ListView) findViewById(R.id.listView1);
        helper = new BaseDAO(this);
        database = helper.getWritableDatabase();
        listar();
    }

    public void btnInserir_Click(View v){
        marca = findViewById(R.id.txtMarca);
        String txtMarca = marca.getText().toString().trim();
        marca.setText("");

        if (txtMarca.length() > 0) {
            database.execSQL("INSERT INTO Marca (marca) VALUES " + "('" + txtMarca + "')");

            Toast.makeText(this, "Registro inserido com sucesso!", Toast.LENGTH_SHORT).show();
            listar();
        } else {
            Toast.makeText(this, "Campo não pode ser vazio!", Toast.LENGTH_SHORT).show();

            listar();
        }
    }

    public void listar() {
        List<String> ls = helper.getAllPosts();
        ArrayAdapter<String> marcas = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, ls);
        listView.setAdapter(marcas);

        if (ls.size() != 0) {
            registerForContextMenu(listView);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        listar();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        String chave[] = { ((TextView) info.targetView).getText().toString() };

        if (item.getItemId() == R.id.update) {
            String usersSelectQuery = String.format("SELECT marcaId FROM Marca WHERE marca = ?");
            Cursor cursor = database.rawQuery(usersSelectQuery, new String[]{chave[0]});

            Intent it;
            it = new Intent(CadastroMarca.this, AtualizaMarca.class);
            Bundle param = new Bundle();
            param.putString("marca", chave[0]);
            it.putExtras(param);
            startActivityForResult(it, 1);

            return true;
        } else if (item.getItemId() == R.id.delete) {
            String usersSelectQuery = String.format("SELECT marcaId FROM Marca WHERE marca = ?");
            Cursor cursor = database.rawQuery(usersSelectQuery, new String[]{chave[0]});

            if (cursor.moveToFirst()) {
                String marcaId = cursor.getString(0);
                usersSelectQuery = String.format("SELECT marcaId FROM Celular WHERE marcaId = ?");
                Cursor cs = database.rawQuery(usersSelectQuery, new String[]{marcaId});

                if (cs.moveToFirst()) {
                    Toast.makeText(this, "Primeiro delete os modelos dependentes desta marca!", Toast.LENGTH_LONG).show();
                } else {
                    database.delete("Marca" ,"marca=?", chave);
                }
            }
            listar();
            return true;
        } else {
            return false;
        }
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        //database.execSQL("DELETE FROM Marca");
        database.close();
    }
}