package com.example.aplicaciongestiontiendasgestor.Adaptadores;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.example.aplicaciongestiontiendasgestor.Modelos.Tiendas;
import com.example.aplicaciongestiontiendasgestor.R;

import java.util.ArrayList;

public class AdaptadorTiendas extends RecyclerView.Adapter<AdaptadorTiendas.AdaptadorViewHolder> {
    private ArrayList<Tiendas> listaTiendas = new ArrayList<>();
    private Context context;
    private ProgressDialog dialog;

    public AdaptadorTiendas(ArrayList<Tiendas> listaTiendas, Context context) {
        this.listaTiendas = listaTiendas;
        this.context = context;
    }

    @NonNull
    @Override
    public AdaptadorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tiendas, parent, false);
        AdaptadorViewHolder avh = new AdaptadorViewHolder(itemView);
        return avh;
    }

    @Override
    public void onBindViewHolder(@NonNull AdaptadorViewHolder holder, int position) {
        Tiendas t = listaTiendas.get(position);
        holder.nombreTienda.setText(t.getNombre());
        holder.bt_edit_tienda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle b = new Bundle();
                b.putSerializable("tienda", t);
                Navigation.findNavController(v).navigate(R.id.action_nav_slideshow_to_modificarTiendaFragment, b);

            }
        });
        holder.bt_eliminar_tienda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialogo = new AlertDialog.Builder(context);
                dialogo.setTitle("¿Estas seguro de eliminar la tienda?");
                dialogo.setMessage("La tienda se eliminará definitivamente");
                dialogo.setCancelable(false);
                dialogo.setPositiveButton("Borrar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogo1, int id) {
                        eliminarTienda(t);
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
                b.putSerializable("tienda", t);
                Navigation.findNavController(v).navigate(R.id.action_nav_slideshow_to_infoTiendas, b);
            }
        });
    }

    private void eliminarTienda(Tiendas t) {
        dialog = new ProgressDialog(context);
        dialog.setMessage("Borrando tienda");
        dialog.show();
        String url = "http://matfranvictor.atwebpages.com/borrarTienda.php?idTienda=" + t.getIdTienda();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.contains("1")) {
                    dialog.hide();
                    Toast.makeText(context, "Tienda eliminada", Toast.LENGTH_LONG).show();
                    listaTiendas.remove(t);
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
        return listaTiendas.size();
    }

    public class AdaptadorViewHolder extends RecyclerView.ViewHolder {
        private ConstraintLayout cs;
        private TextView nombreTienda;
        private ImageButton bt_edit_tienda, bt_eliminar_tienda;

        public AdaptadorViewHolder(@NonNull View itemView) {
            super(itemView);
            nombreTienda = itemView.findViewById(R.id.nombreTienda);
            bt_edit_tienda = itemView.findViewById(R.id.bt_edit_tienda);
            bt_eliminar_tienda = itemView.findViewById(R.id.bt_eliminar_tienda);
            cs = itemView.findViewById(R.id.filaTienda);
        }
    }
}