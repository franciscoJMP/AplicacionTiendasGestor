package com.example.aplicaciongestiontiendasgestor.vistas;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.aplicaciongestiontiendasgestor.Adaptadores.AdaptadorLineasPedido;
import com.example.aplicaciongestiontiendasgestor.Adaptadores.AdaptadorProductos;
import com.example.aplicaciongestiontiendasgestor.Modelos.LineasPedidos;
import com.example.aplicaciongestiontiendasgestor.Modelos.Productos;
import com.example.aplicaciongestiontiendasgestor.Modelos.Usuarios;
import com.example.aplicaciongestiontiendasgestor.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class infoLinea extends Fragment {


    private LineasPedidos lineas;
    private TextView tNombre,tPrecio,tCantidad;
    private ArrayList<Productos> listaProductos =new ArrayList<>();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_info_linea, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();
        Bundle b = getArguments();

        if (b != null) {
            lineas = (LineasPedidos) b.getSerializable("linea");
        }

        tNombre = root.findViewById(R.id.tvNombreProducto);
        tPrecio = root.findViewById(R.id.tvPrecioProducto);
        tCantidad = root.findViewById(R.id.tvCantidad);


        tCantidad.setText(""+lineas.getCantidad());
        cargarProducto();
        return root;
    }

    private void cargarProducto() {
        String url = "http://matfranvictor.atwebpages.com/listarProductoIDProducto.php?producto="+lineas.getIdProducto();
        listaProductos.clear();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for(int i=0;i<jsonArray.length();i++){
                        JSONObject jsonObject = new JSONObject(jsonArray.getString(i));
                        tNombre.setText(jsonObject.get("nombre").toString());
                        tPrecio.setText(""+Float.parseFloat(jsonObject.get("precio").toString()));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
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