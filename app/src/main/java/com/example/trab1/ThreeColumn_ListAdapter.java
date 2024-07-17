package com.example.trab1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

class ThreeColumn_ListAdapter extends ArrayAdapter {
    private LayoutInflater mInflater;
    private ArrayList<Celular> celulares;
    private int mViewResourceId;

    public ThreeColumn_ListAdapter(Context context, int textViewResourceId, ArrayList<Celular> celulares) {
        super(context, textViewResourceId, celulares);
        this.celulares = celulares;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mViewResourceId = textViewResourceId;
    }

    public View getView(int position, View convertView, ViewGroup parents) {
        convertView = mInflater.inflate(mViewResourceId, null);

        Celular celular = celulares.get(position);

        if (celular != null) {
            TextView marca_ = (TextView) convertView.findViewById(R.id.marca_);
            TextView modelo_ = (TextView) convertView.findViewById(R.id.modelo_);
            TextView marcaId_ = (TextView) convertView.findViewById(R.id.marcaId_);

            if (marca_ != null) {
                marca_.setText(celular.getMarca_());
            }

            if (modelo_ != null) {
                modelo_.setText(celular.getModelo_());
            }

            if (marcaId_ != null) {
                marcaId_.setText(celular.getMarcaId_());
            }
        }
        return convertView;
    }
}