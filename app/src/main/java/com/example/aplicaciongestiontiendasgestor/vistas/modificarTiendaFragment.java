package com.example.aplicaciongestiontiendasgestor.vistas;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

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
import com.example.aplicaciongestiontiendasgestor.Modelos.Tiendas;

import com.example.aplicaciongestiontiendasgestor.R;


public class modificarTiendaFragment extends Fragment {


    private Tiendas tienda;
    private EditText tv_nombre_tienda,tv_descripcion;
    private Button bt_guardar_tienda;
    private String nombreAux;
    private String descripcionAux;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_modificar_tienda, container, false);

        Bundle b = getArguments();
        if (b != null) {
            tienda = (Tiendas) b.getSerializable("tienda");
        }

        tv_nombre_tienda=root.findViewById(R.id.tv_nombre_tienda);
        tv_descripcion=root.findViewById(R.id.tv_Descipcion);
        bt_guardar_tienda=root.findViewById(R.id.bt_guardar_tienda);

        tv_nombre_tienda.setText(tienda.getNombre());
        tv_descripcion.setText(tienda.getDescripcion());
        
        bt_guardar_tienda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modificarTienda();
            }
        });

        
        return root;

    }

    private void modificarTienda() {

        bt_guardar_tienda.setEnabled(false);
        String nombre = tv_nombre_tienda.getText().toString();
        String descripcion = tv_descripcion.getText().toString();

        if (nombre.isEmpty()) {
            nombreAux = null;
        }else{
            nombreAux = "\"" + nombre + "\"";
        }
        if (descripcion.isEmpty()) {
            descripcionAux = null;
        }else{
            descripcionAux = "\"" + descripcion + "\"";
        }

        String url = "http://matfranvictor.atwebpages.com/actualizaTienda.php?idTienda="+tienda.getIdTienda()+"&local="+nombreAux+"&descripcion=" + descripcionAux ;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                bt_guardar_tienda.setEnabled(true);
                if (response.contains("1")) {
                    Navigation.findNavController(getView()).navigate(R.id.action_modificarTiendaFragment_to_nav_slideshow);
                } else {
                    Toast.makeText(getContext(),"Fallo al modificar la tienda",Toast.LENGTH_SHORT).show();
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