package com.example.aplicaciongestiontiendasgestor.Adaptadores;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.example.aplicaciongestiontiendasgestor.Modelos.Productos;
import com.example.aplicaciongestiontiendasgestor.R;

import java.util.ArrayList;

public class AdaptadorProductos extends RecyclerView.Adapter<AdaptadorProductos.AdaptadorViewHolder> {
    private ArrayList<Productos> listaProductos = new ArrayList<>();
    private Context context;
    private ProgressDialog dialog;


    public AdaptadorProductos(ArrayList<Productos> listaProductos, Context context) {
        this.listaProductos = listaProductos;
        this.context = context;
    }


    @NonNull
    @Override
    public AdaptadorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_productos,parent,false);
        AdaptadorViewHolder avh=new AdaptadorViewHolder(itemView);
        return avh;
    }

    @Override
    public void onBindViewHolder(@NonNull AdaptadorViewHolder holder, int position) {
        Productos p = listaProductos.get(position);
        holder.nombreProducto.setText(p.getNombre());
        holder.modificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle b = new Bundle();
                b.putSerializable("product",p);
                Navigation.findNavController(v).navigate(R.id.action_nav_gallery_to_modificarProductosFragment,b);
            }
        });
        holder.borrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialogo = new AlertDialog.Builder(context);
                dialogo.setTitle("¿Estas seguro de eliminar este prodcuto?");
                dialogo.setMessage("El producto se eliminara definitivamente");
                dialogo.setCancelable(false);
                dialogo.setPositiveButton("Borrar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogo1, int id) {
                        eliminarProducto(p);
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
                b.putSerializable("product", p);
                Navigation.findNavController(v).navigate(R.id.action_nav_gallery_to_infoProductoFragment,b);
            }
        });




    }

    private void eliminarProducto(Productos p) {

        dialog = new ProgressDialog(context);
        dialog.setMessage("Borrando producto...");
        dialog.show();
        String url = "http://matfranvictor.atwebpages.com/borrarProducto.php?idProducto=" + p.getIdProducto();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.contains("1")) {
                    dialog.hide();
                    Toast.makeText(context, "Producto eliminado", Toast.LENGTH_LONG).show();
                    listaProductos.remove(p);
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
        return listaProductos.size();
    }

    public class AdaptadorViewHolder extends RecyclerView.ViewHolder {
        private ConstraintLayout cs;
        private TextView nombreProducto;
        private ImageButton modificar,borrar;
        public AdaptadorViewHolder(@NonNull View itemView) {
            super(itemView);
            nombreProducto=itemView.findViewById(R.id.nombreProducto);
            cs=itemView.findViewById(R.id.filaProducto);
            modificar=itemView.findViewById(R.id.bt_edit_producto);
            borrar=itemView.findViewById(R.id.bt_eliminar_producto);
        }
    }

}
