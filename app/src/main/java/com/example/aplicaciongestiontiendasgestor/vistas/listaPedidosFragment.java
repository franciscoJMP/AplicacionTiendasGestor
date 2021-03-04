package com.example.aplicaciongestiontiendasgestor.vistas;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.aplicaciongestiontiendasgestor.Adaptadores.AdaptadorPedidos;
import com.example.aplicaciongestiontiendasgestor.Adaptadores.AdaptadorUsuarios;
import com.example.aplicaciongestiontiendasgestor.Modelos.Pedidos;
import com.example.aplicaciongestiontiendasgestor.Modelos.Usuarios;
import com.example.aplicaciongestiontiendasgestor.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class listaPedidosFragment extends Fragment {


    private ProgressDialog dialog;
    private ArrayList<Pedidos> listaPedidos= new ArrayList<>();
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;

    private RecyclerView rv;
    private Usuarios usuario;
    private SwipeRefreshLayout swiperefresh;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();
        View root = inflater.inflate(R.layout.fragment_lista_pedidos, container, false);
        rv=root.findViewById(R.id.rv_pedidos_user);

        Bundle b = getArguments();

        if (b != null) {
            usuario = (Usuarios) b.getSerializable("usuarios");
        }


        swiperefresh = root.findViewById(R.id.swiperefresh);

        swiperefresh.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        cargarPedidos();
                        swiperefresh.setRefreshing(false);
                    }
                }
        );


        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        layoutManager=new LinearLayoutManager(getActivity());
        rv.setLayoutManager(layoutManager);

        cargarPedidos();

    }

    private void cargarPedidos() {
        dialog = new ProgressDialog(getContext());
        dialog.setMessage("Cargando pedidos");
        dialog.setCancelable(false);
        dialog.show();
        String url = "http://matfranvictor.atwebpages.com/listarPedidos.php?idUsuario="+usuario.getIdUsuario();
        listaPedidos.clear();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = new JSONObject(jsonArray.getString(i));
                        Pedidos p = new Pedidos();
                        p.setIdPedido(Integer.parseInt(jsonObject.get("idPedido").toString()));
                        p.setFechaPedido(jsonObject.get("fechaPedido").toString());
                        p.setPagado(Integer.parseInt(jsonObject.get("pagado").toString()));
                        listaPedidos.add(p);

                    }
                    if (listaPedidos.size() > 0) {
                        adapter = new AdaptadorPedidos(listaPedidos, getContext(),usuario);
                        rv.setAdapter(adapter);
                        dialog.hide();
                        adapter.notifyDataSetChanged();
                    } else {
                        dialog.hide();
                        Toast.makeText(getContext(),"No hay pedidos",Toast.LENGTH_SHORT).show();
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