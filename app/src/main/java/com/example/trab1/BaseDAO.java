package com.example.trab1;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class BaseDAO extends SQLiteOpenHelper {

    public static final String TBL_MARCA = "Marca";
    public static final String MARCA_ID = "marcaId";
    public static final String MARCA = "marca";
    public static final String TBL_CELULAR = "Celular";
    public static final String IDCELULAR = "idCelular";
    public static final String MODELO = "modelo";

    private static final String DATABASE_NAME = "Empresa";
    private static final int DATABASE_VERSION = 1;

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    public BaseDAO(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        String CREATE_TBL_MARCA = "create table " +
                TBL_MARCA + "( " + MARCA_ID       + " integer primary key autoincrement, " +
                MARCA     + " text not null);";

        String CREATE_TBL_CELULAR = "create table " +
                TBL_CELULAR + "(" + IDCELULAR       + " integer primary key autoincrement, " +
                MARCA_ID     + " integer references " + TBL_MARCA + ", " + MODELO + " text not null);";

        database.execSQL(CREATE_TBL_MARCA);
        database.execSQL(CREATE_TBL_CELULAR);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TBL_MARCA);
        onCreate(db);

        db.execSQL("DROP TABLE IF EXISTS " + TBL_CELULAR);
        onCreate(db);
    }

    @SuppressLint("LongLogTag")
    public List<String> getAllPosts() {
        List<String> posts = new ArrayList<>();
        String POSTS_SELECT_QUERY = String.format("SELECT %s FROM %s", MARCA, TBL_MARCA);

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(POSTS_SELECT_QUERY, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    String novoCelular = new String();
                    novoCelular = cursor.getString(cursor.getColumnIndex(MARCA));
                    posts.add(novoCelular);
                } while(cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.d("Error while trying to get posts from database", "erro");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return posts;
    }

    public Cursor getListContents() {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor data = db.rawQuery("SELECT marca, modelo, Celular.marcaId " +
                "FROM Marca " +
                "INNER JOIN Celular on Marca.marcaId = Celular.marcaId" , null);
        return  data;
    }
}