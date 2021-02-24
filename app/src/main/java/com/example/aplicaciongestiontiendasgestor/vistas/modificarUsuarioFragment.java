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
import com.example.aplicaciongestiontiendasgestor.Modelos.Usuarios;
import com.example.aplicaciongestiontiendasgestor.R;


public class modificarUsuarioFragment extends Fragment {

    EditText e1, e2, e3;
    private Button b1;
    private String nombre,apellidos,saldo;
    private String nombreAux,apellidosAux,saldoAux;
    private Usuarios usuario;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_modificar_usuario, container, false);
        Bundle b = getArguments();
        if (b != null) {
            usuario = (Usuarios) b.getSerializable("usu");
        }
        e1 = root.findViewById(R.id.editTextTextPersonName);
        e2 = root.findViewById(R.id.editTextTextPersonName2);
        e3 = root.findViewById(R.id.editTextTextPersonName3);

        e1.setText(usuario.getNombre());
        e2.setText(usuario.getApellidos());
        e3.setText(""+usuario.getSaldo());


        b1 = root.findViewById(R.id.button2);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actualizarPerfil();
            }
        });


        return root;
    }


    private void actualizarPerfil() {
        b1.setEnabled(false);
        nombre = e1.getText().toString();
        apellidos = e2.getText().toString();
        saldo = e3.getText().toString();

        if (nombre.isEmpty()) {
            nombreAux = null;
        }else{
            nombreAux = "\"" + nombre + "\"";
        }
        if (apellidos.isEmpty()) {
            apellidosAux = null;
        }else{
            apellidosAux = "\"" + apellidos + "\"";
        }

        if (saldo.isEmpty()) {
            saldoAux = null;
        }else{
            saldoAux = saldo;
        }


        String cooreoAux = "\"" + usuario.getCorreo() + "\"";
        String url = "http://matfranvictor.atwebpages.com/actualizarUsuario.php?correo="+cooreoAux+"&nombre="+nombreAux+"&apellidos=" + apellidosAux + "&saldo=" + saldoAux;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                b1.setEnabled(true);
                if (response.contains("Correcto")) {
                    Toast.makeText(getContext(),"Correcto",Toast.LENGTH_SHORT).show();
                    Navigation.findNavController(getView()).navigate(R.id.action_modificarUsuarioFragment_to_nav_home);
                } else {
                    Toast.makeText(getContext(),"Fallido",Toast.LENGTH_SHORT).show();
                    // acceso.setEnabled(true);
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