package com.example.aplicaciongestiontiendasgestor.vistas;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.aplicaciongestiontiendasgestor.Adaptadores.AdaptadorUsuarios;
import com.example.aplicaciongestiontiendasgestor.MainActivity;
import com.example.aplicaciongestiontiendasgestor.Modelos.Usuarios;
import com.example.aplicaciongestiontiendasgestor.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class UsuariosFragment extends Fragment {

    private LinearLayoutManager layoutManager;
    private RecyclerView rv;
    private ProgressDialog dialog;
    private ArrayList<Usuarios> listaUsuario = new ArrayList<>();
    private RecyclerView.Adapter adapter;
    private Button bCrear;
    private SwipeRefreshLayout swiperefresh;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_usuarios, container, false);
        bCrear = root.findViewById(R.id.button);

        bCrear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(root).navigate(R.id.action_nav_home_to_crearUsuariosFragment);
            }
        });



        Bundle b = getArguments();

        if (b != null) {
            String ema = b.getString("e");
            ((MainActivity) getActivity()).mostrarDatosUsuario(ema);
        }
        layoutManager = new LinearLayoutManager(getContext());
        rv = root.findViewById(R.id.recyclerUsuarios);
        rv.setLayoutManager(layoutManager);
        swiperefresh = root.findViewById(R.id.swiperefresh);
        swiperefresh.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        cargarUsuarios();
                        swiperefresh.setRefreshing(false);
                    }
                }
        );
        cargarUsuarios();
        return root;
    }

    private void cargarUsuarios() {
        dialog = new ProgressDialog(getContext());
        dialog.setMessage("Cargando Usuarios");
        dialog.show();
        String url = "http://matfranvictor.atwebpages.com/listadoUsuarios.php";
        listaUsuario.clear();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = new JSONObject(jsonArray.getString(i));
                        Usuarios u = new Usuarios();
                        u.setNombre(jsonObject.get("nombre").toString());
                        u.setCorreo(jsonObject.get("correo").toString());
                        u.setApellidos(jsonObject.get("apellidos").toString());
                        u.setSaldo(Float.parseFloat(jsonObject.get("saldo").toString()));
                        u.setIdUsuario(Integer.parseInt(jsonObject.get("idUsuario").toString()));
                        u.setTipo(jsonObject.get("tipo").toString());
                        listaUsuario.add(u);

                    }
                    if (listaUsuario.size() > 0) {
                        adapter = new AdaptadorUsuarios(listaUsuario, getContext());
                        rv.setAdapter(adapter);
                        dialog.hide();
                        adapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(getContext(),"No hay usuarios",Toast.LENGTH_SHORT).show();
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


    public void mvp_notifyDataSetChanged()
    {
        adapter.notifyDataSetChanged();

    }

}