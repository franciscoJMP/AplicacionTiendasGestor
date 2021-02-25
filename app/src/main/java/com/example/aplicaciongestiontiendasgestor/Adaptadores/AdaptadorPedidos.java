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
import com.example.aplicaciongestiontiendasgestor.Modelos.Usuarios;
import com.example.aplicaciongestiontiendasgestor.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AdaptadorPedidos extends RecyclerView.Adapter<AdaptadorPedidos.AdaptadorViewHolder> {
    private ArrayList<Pedidos> listaPedidos = new ArrayList<>();
    private Context context;
    private Usuarios usuario;
    private ProgressDialog dialog;
    private float total;


    public AdaptadorPedidos(ArrayList<Pedidos> listaPedidos, Context context, Usuarios usuario) {
        this.listaPedidos = listaPedidos;
        this.context = context;
        this.usuario=usuario;
    }


    @NonNull
    @Override
    public AdaptadorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView;
        if (viewType == 1){
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pagado,parent,false);
        }else{
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_no_pagado,parent,false);
        }

        AdaptadorViewHolder avh=new AdaptadorViewHolder(itemView);
        return avh;

    }

    @Override
    public void onBindViewHolder(@NonNull AdaptadorViewHolder holder, int position) {
        Pedidos p = listaPedidos.get(position);
        holder.name.setText(""+p.getIdPedido());
        holder.date.setText(p.getFechaPedido());
        if(p.getPagado()==0){
            holder.modificar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder dialogo = new AlertDialog.Builder(context);
                    dialogo.setTitle("¿Estas seguro de marcar como pagado el pedido?");
                    dialogo.setMessage("El pedido se marcara como pagado");
                    dialogo.setCancelable(false);
                    dialogo.setPositiveButton("Actualizar", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogo1, int id) {

                            obtenerTotal(p,usuario);
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
            holder.borrar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder dialogo = new AlertDialog.Builder(context);
                    dialogo.setTitle("¿Estas seguro de eliminar este pedido?");
                    dialogo.setMessage("El pedido y todas sus lineas desapareceran,¿Esta seguro?");
                    dialogo.setCancelable(false);
                    dialogo.setPositiveButton("Borrar", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogo1, int id) {
                            eliminarPedido(p);
                        }
                    });
                    dialogo.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogo1, int id) {

                        }
                    });
                    dialogo.show();
                }

            });
        }else{
            holder.modificar.setVisibility(View.INVISIBLE);
            holder.borrar.setVisibility(View.INVISIBLE);
        }

        holder.cl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle b = new Bundle();
                b.putSerializable("product", p);
                Navigation.findNavController(v).navigate(R.id.action_listaPedidos_to_lineaPedido,b);
            }
        });
    }

    private void obtenerTotal(Pedidos p, Usuarios usuario) {
        String url =  "http://matfranvictor.atwebpages.com/consultarTotal.php?idPedido="+p.getIdPedido();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONArray jsonArray = null;
                try {
                    jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = new JSONObject(jsonArray.getString(i));
                        total = Float.parseFloat(jsonObject.get("totalPedido").toString());
                    }
                    actualizarPedido(total,usuario,p);
                } catch (JSONException e) {
                    e.printStackTrace();
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

    private void actualizarPedido(float total, Usuarios us, Pedidos pedidos) {
        if(us.getSaldo()>=total  ){
            dialog = new ProgressDialog(context);
            dialog.setMessage("Actualizando pedido...");
            dialog.setCancelable(false);
            dialog.show();
            String url = "http://matfranvictor.atwebpages.com/actualizarEstadoPedido.php?idPedido=" + pedidos.getIdPedido();

            StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if (response.contains("1")) {
                        dialog.hide();
                        float restante = us.getSaldo() - total;
                        modificarSaldo(restante,us);
                        Toast.makeText(context, "Pedido Actualizado", Toast.LENGTH_LONG).show();

                        notifyDataSetChanged();
                    } else {
                        dialog.hide();
                        Toast.makeText(context, "Pedido no actualizado", Toast.LENGTH_LONG).show();
                    }
                }

            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    dialog.hide();
                }
            });
            Volley.newRequestQueue(context).add(stringRequest);
        }else{
            AlertDialog.Builder dialogo = new AlertDialog.Builder(context);
            dialogo.setTitle("El usuario no tiene saldo");
            dialogo.setMessage("Sin saldo");
            dialogo.setCancelable(false);
            dialogo.setNegativeButton("Aceptar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogo1, int id) {
                    //codigo
                }
            });
            dialogo.show();
        }


    }

    private void modificarSaldo(float restante, Usuarios us) {
        String cooreoAux = "\"" + us.getCorreo()+ "\"";


        if(restante>0){
            restante = (float) (Math.round(restante*100.0)/100.0);
            String url = "http://matfranvictor.atwebpages.com/actualizarUsuario.php?correo="+cooreoAux+"&nombre="+null+"&apellidos=" + null + "&saldo=" + restante;
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                }

            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    dialog.hide();
                }
            });
            Volley.newRequestQueue(context).add(stringRequest);
        }


    }





    private void eliminarPedido(Pedidos p) {

        dialog = new ProgressDialog(context);
        dialog.setMessage("Borrando pedido...");
        dialog.setCancelable(false);
        dialog.show();
        String url = "http://matfranvictor.atwebpages.com/borrarPedido.php?idPedido=" + p.getIdPedido();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.contains("1")) {
                    dialog.hide();
                    Toast.makeText(context, "Pedido eliminado", Toast.LENGTH_LONG).show();
                    listaPedidos.remove(p);
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
        return listaPedidos.size();
    }

    @Override
    public int getItemViewType(int position) {

       return listaPedidos.get(position).getPagado();

    }

    public class AdaptadorViewHolder extends RecyclerView.ViewHolder {
        private ConstraintLayout cl;
        private TextView name,date;
        private ImageButton modificar,borrar;
        public AdaptadorViewHolder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.tvIdPedidos);
            date=itemView.findViewById(R.id.tvFechaPedido);
            cl =itemView.findViewById(R.id.cl);
            modificar=itemView.findViewById(R.id.bt_edit_pedido);
            borrar=itemView.findViewById(R.id.bt_eliminar_pedido);
        }
    }

}
