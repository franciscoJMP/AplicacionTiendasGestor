package com.example.aplicaciongestiontiendasgestor.vistas;


import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.aplicaciongestiontiendasgestor.Modelos.Productos;
import com.example.aplicaciongestiontiendasgestor.Modelos.Tiendas;
import com.example.aplicaciongestiontiendasgestor.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class InfoProductoFragment extends Fragment {
    private Productos p;
    private ArrayList<Tiendas> listaTiendas = new ArrayList<>();
    private TextView nombreTienda,cantidad,precio,tienda;


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
        View view = inflater.inflate(R.layout.fragment_info_producto, container, false);
        nombreTienda=view.findViewById(R.id.tvNombreProducto);
        cantidad=view.findViewById(R.id.tvCantidad);
        precio=view.findViewById(R.id.tvPrecioProducto);
        tienda=view.findViewById(R.id.tvTienda);
        nombreTienda.setText(p.getNombre());
        cantidad.setText(String.valueOf(p.getCantidad()));
        precio.setText(String.valueOf(p.getPrecio()));
        cargarTienda();
        return view;
    }

    private void cargarTienda() {
        String url = "http://matfranvictor.atwebpages.com/tiendaProducto.php?tienda="+p.getIdTienda();
        Log.e("hola",url);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for(int i=0;i<jsonArray.length();i++){
                        JSONObject jsonObject = new JSONObject(jsonArray.getString(i));

                        tienda.setText(jsonObject.get("Local").toString());

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