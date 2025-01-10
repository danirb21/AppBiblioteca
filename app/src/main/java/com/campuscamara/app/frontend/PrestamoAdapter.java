package com.campuscamara.app.frontend;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.campuscamara.app.R;
import com.campuscamara.app.model.BbddHandler;
import com.campuscamara.app.model.Prestamo;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class PrestamoAdapter extends RecyclerView.Adapter<PrestamoAdapter.PrestamoViewHolder> {
    @NonNull
    private  List<Prestamo> listaPrestamo;

    private List<String> tituloLibros;
    private BbddHandler bbddHandler;
    private LayoutInflater layoutInflater;
    public PrestamoAdapter(List<Prestamo> lista, Context context){
        this.layoutInflater=LayoutInflater.from(context);
        this.listaPrestamo=lista;
        bbddHandler=new BbddHandler(context.getResources().getString(R.string.RutaServidor));

    }
    @Override
    public PrestamoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = layoutInflater.inflate(R.layout.prestamo_element, parent, false);
            return new PrestamoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PrestamoViewHolder holder, int position) {
        Prestamo prestamo = listaPrestamo.get(position);
        holder.chipGroup.removeAllViews();
      //  try {
            for (String id: prestamo.getLibrosIds()) {
                Chip chip = new Chip(holder.chipGroup.getContext());
                TaskNombreLibro taskNombreLibro=new TaskNombreLibro(id,chip);
                taskNombreLibro.execute();
                chip.setFocusable(true);
                chip.setClickable(true);
                chip.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(v.getContext(), LibroDetallesAct.class);
                        intent.putExtra("libroid", id);
                        intent.putExtra("activity","prestamo");
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        v.getContext().startActivity(intent);
                    }
                });
                holder.chipGroup.addView(chip);
            }
        holder.cliente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ClienteDetalles.class);
                intent.putExtra("clienteUid", prestamo.getUidUsuario());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                v.getContext().startActivity(intent);
            }
        });
            holder.btnBorrar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listaPrestamo.remove(prestamo)) {
                        bbddHandler.eliminarPrestamo(prestamo.getId());
                        notifyDataSetChanged();
                        Toast.makeText(v.getContext(), "Borrado Correctamente", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        holder.fechaPrestamo.setText("Fecha Prestamo: " + prestamo.getFechaPrestamo());
        holder.fechaDevolucion.setText("Fecha Devolucion:" + prestamo.getFechaDevolucion());
    }

        @Override
    public int getItemCount() {
        return this.listaPrestamo.size();
    }
    public void updateList(List<Prestamo> newList) {
        listaPrestamo = newList;
        notifyDataSetChanged();
    }
    public static class PrestamoViewHolder extends RecyclerView.ViewHolder {
        private TextView fechaPrestamo;
        private TextView fechaDevolucion;
        private ChipGroup chipGroup;
        private TextView cliente;
        private ImageButton btnBorrar;
        public PrestamoViewHolder(@NonNull View itemView) {
            super(itemView);
            fechaPrestamo=itemView.findViewById(R.id.tvFechaPrestamo);
            fechaDevolucion=itemView.findViewById(R.id.tvFechaDevolucion);
            chipGroup=itemView.findViewById(R.id.chipGroupLibros);
            cliente=itemView.findViewById(R.id.tvCliente);
            btnBorrar=itemView.findViewById(R.id.imageButton);

        }
    }
    private class TaskNombreLibro extends AsyncTask<Void,Void,Void> {
        private Chip chip;
        private String id;
        private String titulo;
        public  TaskNombreLibro(String id, Chip chip){
            this.chip=chip;
            this.id=id;
            titulo="libro";
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                titulo=bbddHandler.buscarporId(id).getTitulo();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
               e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            chip.setText(titulo);

        }
    }
}
