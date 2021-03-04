package com.example.aplicaciongestiontiendasgestor.vistas;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.aplicaciongestiontiendasgestor.Adaptadores.AdaptadorTiendas;
import com.example.aplicaciongestiontiendasgestor.Modelos.Tiendas;
import com.example.aplicaciongestiontiendasgestor.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class TiendasFragment extends Fragment {
    private RecyclerView rv;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;
    private ArrayList<Tiendas> listaTiendas=new ArrayList<>();
    private ProgressDialog dialog;
    private Button crearTienda;

    public static TiendasFragment newInstance() {
        TiendasFragment fragment = new TiendasFragment();
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();
        View root = inflater.inflate(R.layout.fragment_tiendas, container, false);
        rv=root.findViewById(R.id.rv_tiendas);
        crearTienda=root.findViewById(R.id.crearTienda);
        crearTienda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_nav_slideshow_to_fragmentCrearTienda);
            }
        });
        return root;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        layoutManager=new LinearLayoutManager(getActivity());
        rv.setLayoutManager(layoutManager);

        cargarTiendas();
    }

    private void cargarTiendas() {
        dialog = new ProgressDialog(getContext());
        dialog.setMessage("Cargando datos tiendas");
        dialog.setCancelable(false);
        dialog.show();
        String url = "http://matfranvictor.atwebpages.com/tienda.php";
        listaTiendas.clear();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for(int i=0;i<jsonArray.length();i++){
                        JSONObject jsonObject = new JSONObject(jsonArray.getString(i));
                        Tiendas t = new Tiendas();
                        t.setIdTienda(Integer.parseInt(jsonObject.get("idTienda").toString()));
                        t.setNombre(jsonObject.get("Local").toString());
                        t.setDescripcion(jsonObject.get("Descripcion").toString());
                        listaTiendas.add(t);

                    }
                    if(listaTiendas.size()>0){
                        adapter = new AdaptadorTiendas(listaTiendas, getContext());
                        rv.setAdapter(adapter);
                        dialog.hide();
                    }else{

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