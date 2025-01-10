package com.campuscamara.app.frontend;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.widget.TextView;

import com.campuscamara.app.R;
import com.campuscamara.app.model.BbddHandler;
import com.campuscamara.app.model.Cliente;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class ClienteDetalles extends AppCompatActivity {

    private Cliente cliente;
    private String uidCliente;
    private TextView tvnombre;
    private TextView tvApellido;
    private TextView tvDni;
    private TextView tvTelefono;
    private BbddHandler bbddHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cliente_detalles);
        Intent intent=getIntent();
        uidCliente=intent.getStringExtra("clienteUid");
        bbddHandler=new BbddHandler(getResources().getString(R.string.RutaServidor));
        TaskClientes taskClientes=new TaskClientes();
        taskClientes.execute();

    }
    private class TaskClientes extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                cliente=bbddHandler.getCliente(uidCliente);
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
               e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            tvnombre=findViewById(R.id.tvNombre);
            tvApellido=findViewById(R.id.tvApellidos);
            tvDni=findViewById(R.id.tvDni);
            tvTelefono=findViewById(R.id.tvTelefono);
            tvnombre.setText(cliente.getNombre());
            tvApellido.setText(cliente.getApellidos());
            tvDni.setText(cliente.getDni());
            tvTelefono.setText(""+cliente.getTelefono());
        }
    }


}