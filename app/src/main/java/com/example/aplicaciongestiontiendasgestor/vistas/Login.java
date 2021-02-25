package com.example.aplicaciongestiontiendasgestor.vistas;


import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
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

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class Login extends Fragment {
    private Button acceso;
    private EditText email, password;
    private ProgressDialog dialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    private void cargarComponentes(View root) {
        acceso = root.findViewById(R.id.bt_loginLogin);
        email = root.findViewById(R.id.usuCorreo);
        password = root.findViewById(R.id.usuPass);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_login, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        cargarComponentes(root);

        acceso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                acceso.setEnabled(false);
                String correo = email.getText().toString();
                String pass = getMD5(password.getText().toString());
                comprobarCredenciales(correo,pass,root);
            }
        });

        return root;
    }

    private void comprobarCredenciales(String correo, String pass, View root) {
        dialog = new ProgressDialog(getContext());
        dialog.setMessage("Accediendo...");
        String correoAux = "\"" +correo+ "\"";
        String passAux = "\""+pass+"\"";
        String url = "http://matfranvictor.atwebpages.com/loginAdmin.php?correo=" + correoAux+"&pass="+passAux;
        Log.e("Ha",url);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.contains("1")){
                    Bundle b = new Bundle();
                    b.putString("e",correo);
                    Navigation.findNavController(root).navigate(R.id.action_login_to_nav_home,b);
                }else{
                    Toast.makeText(getContext(),"Fallo en las creedenciales",Toast.LENGTH_LONG).show();
                    acceso.setEnabled(true);
                    dialog.hide();
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Mensaje de error a la hora de consultar
                dialog.hide();
            }
        });
        Volley.newRequestQueue(getContext()).add(stringRequest);
    }

    //Funcion para convertir un String a md5(Nos sirve tambien para el registrar)

    private String getMD5(final String s){
        try {
            MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();
            StringBuilder hexString = new StringBuilder();
            for (int i = 0; i < messageDigest.length; i++) {
                String h = Integer.toHexString(0xFF & messageDigest[i]);
                while (h.length() < 2) {
                    h = "0" + h;
                }
                hexString.append(h);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            Log.e("MD5", "md5() NoSuchAlgorithmException: " + e.getMessage());
        }
        return "";
    }




}