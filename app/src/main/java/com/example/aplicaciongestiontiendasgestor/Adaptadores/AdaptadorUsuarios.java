package com.example.aplicaciongestiontiendasgestor.Adaptadores;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.aplicaciongestiontiendasgestor.Modelos.Usuarios;
import com.example.aplicaciongestiontiendasgestor.R;

import java.util.ArrayList;

public class AdaptadorUsuarios extends RecyclerView.Adapter<AdaptadorUsuarios.AdaptadorViewHolder> {
    private ArrayList<Usuarios> listaUsuarios = new ArrayList<>();
    private Context context;
    private ProgressDialog dialog;


    public AdaptadorUsuarios(ArrayList<Usuarios> listaUsuarios, Context context) {
        this.listaUsuarios = listaUsuarios;
        this.context = context;
    }


    @NonNull
    @Override
    public AdaptadorUsuarios.AdaptadorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_item_usuarios, parent, false);
        AdaptadorViewHolder avh = new AdaptadorViewHolder(itemView);
        return avh;
    }

    @Override
    public void onBindViewHolder(@NonNull AdaptadorUsuarios.AdaptadorViewHolder holder, int position) {
        Usuarios u = listaUsuarios.get(position);
        holder.userName.setText(u.getNombre());
        holder.bModificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle b = new Bundle();
                b.putSerializable("usu", u);
                Navigation.findNavController(v).navigate(R.id.action_nav_home_to_modificarUsuarioFragment, b);
            }
        });


        holder.bEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialogo = new AlertDialog.Builder(context);
                dialogo.setTitle("¿Estas seguro de eliminar el usuario?");
                dialogo.setMessage("El usuario se eliminara definitivamente");
                dialogo.setCancelable(false);
                dialogo.setPositiveButton("Borrar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogo1, int id) {
                        eliminarUsuario(u);
                    }
                });
                dialogo.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogo1, int id) {
                        //codigo
                    }
                });
                dialogo.show();
            }
        });


        holder.cs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle b = new Bundle();
                b.putSerializable("usu", u);
                Navigation.findNavController(v).navigate(R.id.action_nav_home_to_infoUsuariosFragment, b);
            }
        });
    }

    private void eliminarUsuario(Usuarios u) {

        dialog = new ProgressDialog(context);
        dialog.setMessage("Borrando usuario");
        dialog.show();
        String idUsuario = "\"" + u.getIdUsuario() + "\"";
        String url = "http://matfranvictor.atwebpages.com/eliminarUsuario.php?idUsuario=" + idUsuario;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.contains("Correcto")) {
                    dialog.hide();
                    Toast.makeText(context, "Usuario eliminado con exito", Toast.LENGTH_LONG).show();
                    listaUsuarios.remove(u);
                    notifyDataSetChanged();
                } else {
                    dialog.hide();
                    Toast.makeText(context, "Fallo al eliminar", Toast.LENGTH_LONG).show();
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.hide();
            }
        });
        Volley.newRequestQueue(context).add(stringRequest);


    }

    @Override
    public int getItemCount() {
        return listaUsuarios.size();
    }

    public class AdaptadorViewHolder extends RecyclerView.ViewHolder {
        private ConstraintLayout cs;
        private TextView userName;
        private ImageButton bEliminar, bModificar;

        private RecyclerView recyclerUsuarios;

        public AdaptadorViewHolder(@NonNull View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.nombreUsuario);
            cs = itemView.findViewById(R.id.filaProducto);
            bEliminar = itemView.findViewById(R.id.bt_eliminar_usu);
            bModificar = itemView.findViewById(R.id.bt_edit_usu);
        }
    }

}