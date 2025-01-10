package com.campuscamara.app.frontend;

import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.content.AsyncTaskLoader;

import android.content.AsyncQueryHandler;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.campuscamara.app.R;
import com.campuscamara.app.model.BbddHandler;
import com.campuscamara.app.model.Cliente;
import com.campuscamara.app.model.Libro;
import com.campuscamara.app.model.Prestamo;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class AniadirPrestamoAct extends AppCompatActivity {
    private Spinner spinnerLibro;
    private Spinner spinnerCliente;
    private ChipGroup chipGroup;
    private Button btnGuardar;
    private BbddHandler bbddHandler;
    private EditText edtFechaPrest;
    private EditText edtFechaDev;
    private List<Libro> libroSeleccionados;
    private String idCliente;
    private List<Cliente> listaClientes;
    private List<Libro> listaLibros;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aniadir_prestamo);
        spinnerLibro=findViewById(R.id.spinnerLibro);
        spinnerCliente=findViewById(R.id.spinnerCliente);
        chipGroup=findViewById(R.id.chipGroup);
        libroSeleccionados=new ArrayList<>();
        edtFechaPrest=findViewById(R.id.edtFechaPrestamo);
        edtFechaDev=findViewById(R.id.edtFechaDevolucion);
        btnGuardar=findViewById(R.id.btnsave);
        bbddHandler=new BbddHandler(getResources().getString(R.string.RutaServidor));
        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fechaPrestamo=edtFechaPrest.getText().toString().trim();
                String fechaDevolucion=edtFechaDev.getText().toString().trim();
                if(!fechaPrestamo.isEmpty() && isValidDate(fechaPrestamo) && !fechaDevolucion.isEmpty() && isValidDate(fechaDevolucion)) {
                    if(!libroSeleccionados.isEmpty() && idCliente!=null) {
                        bbddHandler.aniadirPrestamo(new Prestamo(fechaPrestamo, fechaDevolucion, idCliente, toListId(libroSeleccionados)));
                        Intent intent = new Intent(getApplicationContext(), PrestamoActivity.class);
                        startActivity(intent);
                        Toast.makeText(getApplicationContext(),"Añadido Correctamente",Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(getApplicationContext(),"Ningun libro o cliente seleccionado",Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(getApplicationContext(),"Formato incorrecto de fecha debe ser d/m/yyyy",Toast.LENGTH_LONG).show();
                }
            }
        });
        Task task=new Task();
        task.execute();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent=new Intent(getApplicationContext(),PrestamoActivity.class);
        startActivity(intent);
    }

    private class Task extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... voids) {
             bbddHandler=new BbddHandler(getResources().getString(R.string.RutaServidor));
            try {
                listaClientes = bbddHandler.getClientes();
                listaLibros = bbddHandler.getLibros();
            }catch(ExecutionException ex){
                ex.printStackTrace();
            }catch(InterruptedException ie){
                ie.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            String[] nombreClientes = new String[listaClientes.size()+2];
            String[] nombreLibros=new String[listaLibros.size()+1];
            nombreLibros[0]="";
            nombreClientes[0]="";
            nombreClientes[1]="Crear Nuevo Cliente";
            if(listaClientes!=null) {
                for(int i=2;i<=listaClientes.size()+1;i++){
                    nombreClientes[i]=listaClientes.get(i-2).getNombre()+" "+listaClientes.get(i-2).getApellidos();
                }
            }
            if(listaLibros!=null){
                for(int j=1;j<=listaLibros.size();j++){
                    nombreLibros[j]=listaLibros.get(j-1).getTitulo();
                }
            }
            bindSpinnerClientes(nombreClientes);
            bindSpinnerLibros(nombreLibros);
        }
    }
    public void bindSpinnerLibros(String[] nombreLibros){
        ArrayAdapter<String> arrayAdapter=new ArrayAdapter<>(getApplicationContext(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,nombreLibros);
        spinnerLibro.setAdapter(arrayAdapter);
        spinnerLibro.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Chip chip = new Chip(chipGroup.getContext());
                if(position!=0) {
                    chip.setText(listaLibros.get(position-1).getTitulo());
                    libroSeleccionados.add(listaLibros.get(position-1));
                    chip.setCloseIconVisible(true);
                    chip.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(getApplicationContext(), LibroDetallesAct.class);
                            intent.putExtra("libroid", listaLibros.get(position-1).getId());
                            intent.putExtra("activity", "prestamoaniadir");
                            startActivity(intent);
                        }
                    });
                    chip.setOnCloseIconClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            chipGroup.removeView(chip);
                            for (int i = 0; i < libroSeleccionados.size(); i++) {
                                if (chip.getText().toString().equals(libroSeleccionados.get(i).getTitulo())) {
                                    libroSeleccionados.remove(i);
                                }
                            }
                        }
                    });
                    chipGroup.addView(chip);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    public void bindSpinnerClientes(String[] nombreClientes) {
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getApplicationContext(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, nombreClientes);
        spinnerCliente.setAdapter(arrayAdapter);
        spinnerCliente.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position!=1 ) {
                    if(position!=0) {
                        idCliente = listaClientes.get(position - 2).getId();
                    }
                }else{
                    Intent intent=new Intent(getApplicationContext(), CrearClienteAct.class);
                    intent.putExtra("clienteid",listaClientes.get(position).getId());
                    startActivity(intent);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    public List<String> toListId(List<Libro> libroList){
        List<String> lista=new ArrayList<>();
        for(Libro l:libroList){
            lista.add(l.getId());
        }
        return lista;
    }
    public static boolean isValidDate(String date) {
        String datePattern = "^([1-9]|[12][0-9]|3[01])/([1-9]|1[0-2])/\\d{4}$";
        return date.matches(datePattern);
    }
}