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
import com.example.aplicaciongestiontiendasgestor.Adaptadores.AdaptadorProductos;
import com.example.aplicaciongestiontiendasgestor.Modelos.Productos;

import com.example.aplicaciongestiontiendasgestor.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;






import java.util.ArrayList;

public class ProductosFragment extends Fragment {

    private ArrayList<Productos> listaProductos =new ArrayList<>();
    private ProgressDialog dialog;
    private RecyclerView rv;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;
    private Button crearProducto;



    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_productos, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();
        rv = root.findViewById(R.id.rvProductos);
        crearProducto=root.findViewById(R.id.crearProducto);

        crearProducto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_nav_gallery_to_crearProductoFragment);
            }
        });

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        layoutManager=new LinearLayoutManager(getActivity());
        rv.setLayoutManager(layoutManager);
        cargarProductos();
    }

    private void cargarProductos() {
        dialog = new ProgressDialog(getContext());
        dialog.setMessage("Cargando Productos");
        dialog.setCancelable(false);
        dialog.show();
        String url = "http://matfranvictor.atwebpages.com/productos.php";
        listaProductos.clear();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for(int i=0;i<jsonArray.length();i++){
                        JSONObject jsonObject = new JSONObject(jsonArray.getString(i));
                        Productos p = new Productos();
                        p.setIdProducto(Integer.parseInt(jsonObject.get("idProducto").toString()));
                        p.setNombre(jsonObject.get("nombre").toString());
                        p.setCantidad(Integer.parseInt(jsonObject.get("cantidad").toString()));
                        p.setPrecio(Float.parseFloat(jsonObject.get("precio").toString()));
                        p.setIdTienda(Integer.parseInt(jsonObject.get("idTienda").toString()));
                        listaProductos.add(p);

                    }
                    if(listaProductos.size()>0){
                        adapter = new AdaptadorProductos(listaProductos, getContext());
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