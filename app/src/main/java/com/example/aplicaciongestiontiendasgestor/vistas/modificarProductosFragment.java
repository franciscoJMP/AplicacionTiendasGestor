package com.example.aplicaciongestiontiendasgestor.vistas;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.aplicaciongestiontiendasgestor.Modelos.Productos;
import com.example.aplicaciongestiontiendasgestor.R;


public class modificarProductosFragment extends Fragment {
    private EditText mod_nombre, mod_cantidad, mod_precio;
    private Button modificarProducto;
    private Productos p;

    public modificarProductosFragment() {
    }


    public static modificarProductosFragment newInstance(String param1, String param2) {
        modificarProductosFragment fragment = new modificarProductosFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle b = getArguments();
        if (b != null) {
            p = (Productos) b.getSerializable("product");
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_modificar_productos, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();
        mod_nombre = view.findViewById(R.id.tv_nombre_mod);
        mod_cantidad = view.findViewById(R.id.tv_cantidad_mod);
        mod_precio = view.findViewById(R.id.tv_precio_mod);
        modificarProducto = view.findViewById(R.id.bt_modificar_producto);
        mod_nombre.setText(p.getNombre());
        mod_cantidad.setText(String.valueOf(p.getCantidad()));
        mod_precio.setText(String.valueOf(p.getPrecio()));
        modificarProducto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modifyProduct();
            }
        });
        return view;
    }

    private void modifyProduct() {
        String url = "http://matfranvictor.atwebpages.com/actualizarProducto.php?nombre="
                + "\"" + mod_nombre.getText().toString() + "\""
                + "&cantidad="
                + mod_cantidad.getText().toString()
                +"&idProducto="
                +p.getIdProducto()
                + "&precio="
                + mod_precio.getText().toString();



        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.contains("1")) {
                    Navigation.findNavController(getView()).navigate(R.id.action_modificarProductosFragment_to_nav_gallery);
                } else {
                    Toast.makeText(getContext(), "Error al actualizar el producto", Toast.LENGTH_LONG).show();
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Mensaje de error a la hora de consultar
            }
        });
        Volley.newRequestQueue(getContext()).add(stringRequest);
    }
}
