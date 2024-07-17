package com.example.trab1;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class CadastroModelo extends AppCompatActivity {
    private SQLiteDatabase database;
    private EditText modelo_;
    private Button btnVoltar;
    private String itemFkId;
    private String itemMarca;
    ArrayList<Celular> celularList;
    Celular celular;
    static Celular itemSelecionado;

    ListView listView;
    BaseDAO helper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cadastro_modelos);

        btnVoltar = findViewById(R.id.btnVoltar);
        btnVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        listView = (ListView) findViewById(R.id.listView2);
        helper = new BaseDAO(this);

        database = helper.getWritableDatabase();
        final Spinner spinner = findViewById(R.id.spinner1);

        List<String> ls = helper.getAllPosts();
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>
                (CadastroModelo.this, android.R.layout.simple_expandable_list_item_1, ls);
        spinner.setAdapter(adapter1);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Object obj = parent.getItemIdAtPosition(position);
                itemFkId = obj.toString();

                Object objMarca = parent.getItemAtPosition(position);
                itemMarca = objMarca.toString();
            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        listarMarcaModelo();
    }

    public void btnCadastrarModelo(View v){
        modelo_ = findViewById(R.id.txtModelo);
        String txtModelo = modelo_.getText().toString().trim();
        modelo_.setText("");

        if (txtModelo.length() > 0) {

            String usersSelectQuery = String.format("SELECT marcaId FROM Marca WHERE marca = ?");
            Cursor cursor = database.rawQuery(usersSelectQuery, new String[]{itemMarca});

            if (cursor.moveToFirst()) {
                do {
                    itemFkId = cursor.getString(0);
                } while(cursor.moveToNext());
            }

            database.execSQL("INSERT INTO Celular (marcaId , modelo) VALUES " + "(" + itemFkId + ", '" + txtModelo + "')");

            listarMarcaModelo();
        } else {
            Toast.makeText(this, "Campo não pode ser vazio!", Toast.LENGTH_SHORT).show();

            listarMarcaModelo();
        }
    }

    private void listarMarcaModelo() {
        celularList = new ArrayList<>();
        Cursor data = helper.getListContents();
        int numRows = data.getCount();
        if (numRows != 0) {
            while (data.moveToNext()) {
                celular = new Celular(data.getString(0), data.getString(1), String.valueOf(data.getInt(2)));
                celularList.add(celular);
            }

            listView.setOnItemLongClickListener(listClick);
            registerForContextMenu(listView);
        }
        ThreeColumn_ListAdapter adapter = new ThreeColumn_ListAdapter(CadastroModelo.this, R.layout.list_adapter_view, celularList);
        listView = (ListView) findViewById(R.id.listView2);
        listView.setAdapter(adapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        listarMarcaModelo();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.update) {
            final Spinner spinner = findViewById(R.id.spinner1);

            Intent it;
            it = new Intent(CadastroModelo.this, AtualizaModelo.class);
            Bundle param = new Bundle();
            param.putString("modelo", itemSelecionado.getModelo_());
            param.putString("marcaId", itemSelecionado.getMarcaId_());
            param.putString("marca", itemSelecionado.getMarca_());

            Adapter adapter = spinner.getAdapter();
            int n = adapter.getCount();
            List<String> marcasList = new ArrayList<>(n);
            for (int i = 0; i < n; i++) {
                String marcaStr = (String) adapter.getItem(i);
                marcasList.add(marcaStr);
            }

            it.putExtras(param);
            it.putStringArrayListExtra("marcas", (ArrayList<String>) marcasList);

            startActivityForResult(it, 1);

            return true;
        } else if (item.getItemId() == R.id.delete) {
            database.delete("Celular" ,"(marcaId=? and modelo=?)", new String[]{itemSelecionado.getMarcaId_(), itemSelecionado.getModelo_()});
            listarMarcaModelo();
            return true;
        } else {
            return false;
        }
    }

    public AdapterView.OnItemLongClickListener listClick = new AdapterView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            itemSelecionado = (Celular) listView.getItemAtPosition(position);
            return false;
        }
    };

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        //database.execSQL("DELETE FROM Celular");
        database.close();
    }
}