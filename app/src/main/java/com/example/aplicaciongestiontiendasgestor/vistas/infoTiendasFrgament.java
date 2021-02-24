package com.example.aplicaciongestiontiendasgestor.vistas;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.aplicaciongestiontiendasgestor.Modelos.Tiendas;

import com.example.aplicaciongestiontiendasgestor.R;


public class infoTiendasFrgament extends Fragment {

    TextView nombre,descripcion;
    private Tiendas tienda;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_info_tiendas, container, false);

        Bundle b = getArguments();

        if (b != null) {
            tienda = (Tiendas) b.getSerializable("tienda");
        }

        nombre=root.findViewById(R.id.textView);
        descripcion=root.findViewById(R.id.textView4);


        nombre.setText(tienda.getNombre());
        descripcion.setText(tienda.getDescripcion());

        return root;
    }
}