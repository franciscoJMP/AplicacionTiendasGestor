package com.example.aplicaciongestiontiendasgestor.vistas;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.aplicaciongestiontiendasgestor.Modelos.Tiendas;
import com.example.aplicaciongestiontiendasgestor.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class CrearProductoFragment extends Fragment {
    private Spinner sp_tiendas;
    private ArrayList<Tiendas> listaTiendas=new ArrayList<>();
    private ArrayList<String> listaTiendasFormateada=new ArrayList<>();
    private int idTienda;
    private EditText nombreProducto,cantidad,precio;
    private Button crearProducto;


    public static CrearProductoFragment newInstance(String param1, String param2) {
        CrearProductoFragment fragment = new CrearProductoFragment();
        return fragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_crear_producto, container, false);
        sp_tiendas=view.findViewById(R.id.sp_tiendas);
        nombreProducto=view.findViewById(R.id.tv_nombre_producto);
        cantidad=view.findViewById(R.id.tv_cantidad_producto);
        precio=view.findViewById(R.id.tv_precio_producto);
        crearProducto=view.findViewById(R.id.bt_guardar_producto);
        cargarTiendas();

        sp_tiendas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
               String textoTienda=sp_tiendas.getItemAtPosition(sp_tiendas.getSelectedItemPosition()).toString();
               String aux[]=textoTienda.replace(" ","").split("-");
               idTienda=Integer.parseInt(aux[0]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        crearProducto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guardarProducto();
            }
        });
        return view;
    }



    private void cargarTiendas() {
        String url = "http://matfranvictor.atwebpages.com/tienda.php";

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
                        for(Tiendas t:listaTiendas){
                            listaTiendasFormateada.add(t.getIdTienda()+" - "+t.getNombre());
                        }
                        sp_tiendas.setAdapter(new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item,listaTiendasFormateada));
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
    private void guardarProducto() {
        String url = "http://matfranvictor.atwebpages.com/insertarProducto.php?nombre="
                + "\""+nombreProducto.getText().toString()+"\""
                + "&cantidad="
                + cantidad.getText().toString()
                + "&precio="
                + precio.getText().toString()
                + "&idTienda="
                + idTienda;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.contains("1")) {
                    Navigation.findNavController(getView()).navigate(R.id.action_crearProductoFragment_to_nav_gallery);

                } else {
                    Toast.makeText(getContext(), "Error al crear el producto", Toast.LENGTH_LONG).show();
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