package com.example.aplicaciongestiontiendasgestor.vistas;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.aplicaciongestiontiendasgestor.Modelos.Usuarios;
import com.example.aplicaciongestiontiendasgestor.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class InfoUsuariosFragment extends Fragment {

    private Usuarios usuario;
    private TextView t1, t2, t3, t4;
    private Button b_infoPedidos;
    private SwipeRefreshLayout swiperefresh;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();
        View root = inflater.inflate(R.layout.fragment_info_usuarios, container, false);
        Bundle b = getArguments();
        swiperefresh = root.findViewById(R.id.swiperefresh);
        swiperefresh.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        cargarDatosUsuario();
                        swiperefresh.setRefreshing(false);
                    }
                }
        );

        if (b != null) {
            usuario = (Usuarios) b.getSerializable("usu");
        }

        t1 = root.findViewById(R.id.textView);
        t2 = root.findViewById(R.id.textView2);
        t3 = root.findViewById(R.id.textView3);
        t4 = root.findViewById(R.id.textView4);
        b_infoPedidos=root.findViewById(R.id.b_infoPedidos);

        t1.setText(usuario.getNombre());
        t2.setText(usuario.getApellidos());
        t3.setText(usuario.getCorreo());
        t4.setText(""+usuario.getSaldo());

        b_infoPedidos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle b = new Bundle();
                b.putSerializable("usuarios",usuario);
                Navigation.findNavController(v).navigate(R.id.action_infoUsuariosFragment_to_listaPedidos,b);
            }
        });

        return root;
    }

    private void cargarDatosUsuario() {
        String correoAux = "\"" + usuario.getCorreo() + "\"";

        String url = "http://matfranvictor.atwebpages.com/consultarUsuario.php?correo=" + correoAux;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = new JSONObject(jsonArray.getString(i));

                        t1.setText(jsonObject.get("nombre").toString());
                        t2.setText(jsonObject.get("apellidos").toString());
                        t3.setText(jsonObject.get("correo").toString());
                        t4.setText(""+Float.parseFloat(jsonObject.get("saldo").toString()));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
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