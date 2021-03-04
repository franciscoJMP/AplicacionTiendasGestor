package com.example.aplicaciongestiontiendasgestor.vistas;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

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

import com.example.aplicaciongestiontiendasgestor.R;


public class CrearUsuariosFragment extends Fragment {

    private EditText tEmail, tPassword, tNombre, tApellidos, tTipo;
    private Button bRegistrarse;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_crear_usuarios, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();

        tEmail = root.findViewById(R.id.tEmail);
        tPassword = root.findViewById(R.id.tPassword);
        tNombre = root.findViewById(R.id.tNombre);
        tApellidos = root.findViewById(R.id.tApellidos);
        tTipo = root.findViewById(R.id.tTipo);
        bRegistrarse = root.findViewById(R.id.bRegistrarse);

        bRegistrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                comprobarEmail();
            }
        });



        return root;
    }

    public void comprobarEmail() {
        if (!tEmail.getText().toString().isEmpty() && !tPassword.getText().toString().isEmpty() && !tNombre.getText().toString().isEmpty() && !tApellidos.getText().toString().isEmpty()) {
            String correoAux = "\"" + tEmail.getText().toString() + "\"";
            String url = "http://matfranvictor.atwebpages.com/comprobarUsuario.php?correo=" + correoAux;

            StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    bRegistrarse.setEnabled(false);
                    if (response.contains("1")) {
                        Toast.makeText(getContext(), "El email existe", Toast.LENGTH_LONG).show();
                        bRegistrarse.setEnabled(true);
                    } else if (response.contains("0")) {
                        registrarse();
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

    private void registrarse() {
        String correoAux = tEmail.getText().toString();
        String passAux = tPassword.getText().toString();
        String nombreAux = tNombre.getText().toString();
        String apellidosAux = tApellidos.getText().toString();
        String tipoAux = tTipo.getText().toString();
        String url = "http://matfranvictor.atwebpages.com/registrarse.php?correo=" + correoAux + "&password=" + passAux + "&nombre=" + nombreAux + "&apellidos=" + apellidosAux + "&tipo=" + tipoAux;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                bRegistrarse.setEnabled(false);
                if (response.contains("Correcto")) {
                    Toast.makeText(getContext(), "Usuario creado con exito", Toast.LENGTH_LONG).show();
                    bRegistrarse.setEnabled(true);
                } else {
                    Toast.makeText(getContext(), "Fallo en las creedenciales", Toast.LENGTH_LONG).show();
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