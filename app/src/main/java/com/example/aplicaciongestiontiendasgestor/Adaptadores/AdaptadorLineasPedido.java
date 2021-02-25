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
import com.example.aplicaciongestiontiendasgestor.Modelos.LineasPedidos;
import com.example.aplicaciongestiontiendasgestor.Modelos.Pedidos;
import com.example.aplicaciongestiontiendasgestor.R;

import java.util.ArrayList;

public class AdaptadorLineasPedido extends RecyclerView.Adapter<AdaptadorLineasPedido.AdaptadorViewHolder> {
    private ArrayList<LineasPedidos> listaLineasPedidos = new ArrayList<>();
    private Context context;
    private ProgressDialog dialog;
    private Pedidos p;


    public AdaptadorLineasPedido(ArrayList<LineasPedidos> listaPedidos, Context context,Pedidos p) {
        this.listaLineasPedidos = listaPedidos;
        this.context = context;
        this.p=p;
    }


    @NonNull
    @Override
    public AdaptadorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.itemlineaspedidos, parent, false);
        AdaptadorViewHolder avh = new AdaptadorViewHolder(itemView);
        return avh;
    }

    @Override
    public void onBindViewHolder(@NonNull AdaptadorViewHolder holder, int position) {
        LineasPedidos lp = listaLineasPedidos.get(position);
        holder.idLinea.setText("" + lp.getIdLinea());
        holder.cs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle b = new Bundle();
                b.putSerializable("linea", lp);
                Navigation.findNavController(v).navigate(R.id.action_lineaPedido_to_infoLinea, b);
            }
        });
        if(p.getPagado()==0){
            holder.modificar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle b = new Bundle();
                    b.putSerializable("lineaPedido",lp);
                    Navigation.findNavController(v).navigate(R.id.action_lineaPedido_to_modificarPedido,b);
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
                            eliminarLineasPedido(lp);
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
        }else{
            holder.modificar.setVisibility(View.INVISIBLE);
            holder.borrar.setVisibility(View.INVISIBLE);
        }

    }

    private void eliminarLineasPedido(LineasPedidos lp) {
        dialog = new ProgressDialog(context);
        dialog.setMessage("Borrando linea pedido...");
        dialog.setCancelable(false);
        dialog.show();
        String url = "http://matfranvictor.atwebpages.com/borrarLinea.php?idLinea="+lp.getIdLinea()+"&idPedido="+lp.getIdPedido()+"&idProducto="+lp.getIdProducto()+"&cantidad="+lp.getCantidad();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.contains("1")) {
                    dialog.hide();
                    Toast.makeText(context, "Linea pedido eliminada", Toast.LENGTH_LONG).show();
                    listaLineasPedidos.remove(lp);
                    notifyDataSetChanged();
                } else {
                    dialog.hide();
                    Toast.makeText(context, "Fallo al eliminar la linea", Toast.LENGTH_LONG).show();
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
        return listaLineasPedidos.size();
    }





    public class AdaptadorViewHolder extends RecyclerView.ViewHolder {
        private ConstraintLayout cs;
        private TextView idLinea;
        private ImageButton modificar,borrar;
        public AdaptadorViewHolder(@NonNull View itemView) {
            super(itemView);
            idLinea=itemView.findViewById(R.id.idLinea);
            cs=itemView.findViewById(R.id.lineaPedido);
            modificar=itemView.findViewById(R.id.bt_edit_linea);
            borrar=itemView.findViewById(R.id.bt_eliminar_linea);
        }
    }

}
