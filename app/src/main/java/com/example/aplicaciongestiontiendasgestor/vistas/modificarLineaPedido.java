package com.example.aplicaciongestiontiendasgestor.vistas;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;



import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.aplicaciongestiontiendasgestor.Modelos.LineasPedidos;

import com.example.aplicaciongestiontiendasgestor.R;



public class modificarLineaPedido extends Fragment {


    private LineasPedidos lineasPedido;
    private TextView tv_cantidad_mod;
    private Button bt_modificar_producto;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle b = getArguments();
        if (b != null) {
            lineasPedido = (LineasPedidos) b.getSerializable("lineaPedido");
        }

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();
        View root = inflater.inflate(R.layout.fragment_modificar_pedido, container, false);

        tv_cantidad_mod = root.findViewById(R.id.tv_cantidad_mod);
        bt_modificar_producto = root.findViewById(R.id.bt_modificar_producto);

        bt_modificar_producto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!tv_cantidad_mod.getText().toString().isEmpty()) {
                    modificarLinea();
                } else {
                    Toast.makeText(getContext(), "Cantidad vacia", Toast.LENGTH_SHORT).show();
                }
            }
        });


        return root;
    }


    private void modificarLinea() {
        bt_modificar_producto.setEnabled(false);
        String url = "http://matfranvictor.atwebpages.com/actualizarLinea.php?idLinea=" + lineasPedido.getIdLinea() + "&idPedido=" + lineasPedido.getIdPedido() + "&idProducto=" + lineasPedido.getIdProducto()
                + "&cantidadOld=" + lineasPedido.getCantidad() + "&cantidadNew=" + Integer.parseInt(tv_cantidad_mod.getText().toString());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.contains("1")) {
                    bt_modificar_producto.setEnabled(true);
                    Toast.makeText(getContext(), "Actualizado", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Fallo al modificar", Toast.LENGTH_SHORT).show();
                    bt_modificar_producto.setEnabled(true);
                }


            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                bt_modificar_producto.setEnabled(true);
            }
        });
        Volley.newRequestQueue(getContext()).add(stringRequest);
    }


}