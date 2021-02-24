package com.example.aplicaciongestiontiendasgestor.vistas;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.aplicaciongestiontiendasgestor.Adaptadores.AdaptadorLineasPedido;
import com.example.aplicaciongestiontiendasgestor.Adaptadores.AdaptadorPedidos;
import com.example.aplicaciongestiontiendasgestor.Modelos.LineasPedidos;
import com.example.aplicaciongestiontiendasgestor.Modelos.Pedidos;
import com.example.aplicaciongestiontiendasgestor.Modelos.Usuarios;
import com.example.aplicaciongestiontiendasgestor.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class lineaPedidoFragment extends Fragment {

    private ProgressDialog dialog;
    private ArrayList<LineasPedidos> listaLineas = new ArrayList<>();
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;

    private RecyclerView rv;
    private Pedidos product;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_linea_pedido, container, false);

        rv = root.findViewById(R.id.rv_lineas);

        layoutManager=new LinearLayoutManager(getActivity());
        rv.setLayoutManager(layoutManager);

        Bundle b = getArguments();

        if (b != null) {
            product = (Pedidos) b.getSerializable("product");
        }

        cargarLineas();
        return root;
    }

    private void cargarLineas() {
        dialog = new ProgressDialog(getContext());
        dialog.setMessage("Cargando lineas de pedido");
        dialog.show();
        String url = "http://matfranvictor.atwebpages.com/listarLineasPedido.php?idPedido="+product.getIdPedido();
        listaLineas.clear();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = new JSONObject(jsonArray.getString(i));
                        LineasPedidos lp = new LineasPedidos();
                        lp.setIdLinea(Integer.parseInt(jsonObject.get("idLinea").toString()));
                        lp.setIdPedido(Integer.parseInt(jsonObject.get("idPedido").toString()));
                        lp.setIdProducto(Integer.parseInt(jsonObject.get("idProducto").toString()));
                        lp.setCantidad(Integer.parseInt(jsonObject.get("cantidad").toString()));
                        listaLineas.add(lp);
                    }
                    if (listaLineas.size() > 0) {
                        adapter = new AdaptadorLineasPedido(listaLineas, getContext(),product);
                        rv.setAdapter(adapter);
                        dialog.hide();
                        adapter.notifyDataSetChanged();
                    } else {
                        dialog.hide();
                        Toast.makeText(getContext(),"No hay lineas de pedido",Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.hide();
            }
        });
        Volley.newRequestQueue(getContext()).add(stringRequest);


    }
}