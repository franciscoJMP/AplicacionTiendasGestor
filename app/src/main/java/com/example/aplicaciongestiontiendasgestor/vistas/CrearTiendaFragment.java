package com.example.aplicaciongestiontiendasgestor.vistas;

import android.os.Bundle;


import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
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
import com.example.aplicaciongestiontiendasgestor.R;


public class CrearTiendaFragment extends Fragment {

    private EditText nombreTienda,descripcionTienda;
    private Button bCrear;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_crear_tienda, container, false);

        nombreTienda=root.findViewById(R.id.tv_nombre_tienda);
        descripcionTienda=root.findViewById(R.id.tv_Descipcion);
        bCrear = root.findViewById(R.id.bt_guardar_tienda);

        bCrear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                crearTienda();
            }
        });

        return root;
    }

    private void crearTienda() {
        if (!nombreTienda.getText().toString().isEmpty() && !descripcionTienda.getText().toString().isEmpty()) {
            String nombreAux = "\"" + nombreTienda.getText().toString() + "\"";
            String descripcion = "\"" + descripcionTienda.getText().toString() + "\"";
            String url = "http://matfranvictor.atwebpages.com/insertarTienda.php?local=" + nombreAux + "&descripcion=" + descripcion;
            Log.e("espa",url);
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    bCrear.setEnabled(false);
                    if (response.contains("1")) {
                        Toast.makeText(getContext(), "Tienda creada con exito", Toast.LENGTH_LONG).show();
                        Navigation.findNavController(getView()).navigate(R.id.action_fragmentCrearTienda_to_nav_slideshow);
                        bCrear.setEnabled(true);
                    } else {
                        Toast.makeText(getContext(), "Fallo al crear la tienda", Toast.LENGTH_LONG).show();
                    }
                }

            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });
            Volley.newRequestQueue(getContext()).add(stringRequest);
        }
    }




}