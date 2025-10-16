package com.campuscamara.app.frontend;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.SearchView;
import android.widget.Spinner;

import com.campuscamara.app.R;
import com.campuscamara.app.model.BbddHandler;
import com.campuscamara.app.model.Libro;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;


public class ActivityLibros extends AppCompatActivity implements SearchView.OnQueryTextListener {
    private List<Libro> listaLibros;
    private BbddHandler bbddHandler;
    private SearchView searchView;
    private FloatingActionButton button;
    private RecyclerAdapter recyclerAdapter;
    private Spinner spinner;

    private String filtroActual="titulo"; //titulo filtro default

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_libros);
        listaLibros = new ArrayList<>();
        bbddHandler = new BbddHandler(getResources().getString(R.string.RutaServidor));
        button=findViewById(R.id.buttonadd);
        searchView=findViewById(R.id.searchview);
        searchView.setOnQueryTextListener(this);
        spinner=findViewById(R.id.spinnerFiltro);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), AniadirLibroActivity.class);
                startActivity(intent);
            }
        });
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                filtroActual = parent.getItemAtPosition(position).toString().toLowerCase();
                searchView.setQueryHint("Buscar por " + filtroActual);
                if(filtroActual.equals("título")){
                    filtroActual="titulo";
                }
                if(filtroActual.equals("género")){
                    filtroActual="genero";
                }
                // cambia el hint del buscador según el filtro
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                filtroActual="titulo";
            }
        });
        TasklistaLibros tasklistaLibros = new TasklistaLibros();
        tasklistaLibros.execute();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(getApplicationContext(), MenuPrincipal.class);
        startActivity(intent);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (recyclerAdapter != null) {
            switch (filtroActual.toLowerCase()) {
                case "autor":
                    recyclerAdapter.filtrarAutor(newText);
                    break;
                case "genero":
                    recyclerAdapter.filtrarGenero(newText);
                    break;
                default:
                    recyclerAdapter.filtrarTitulo(newText);
                    break;
            }
        }
        return false;
    }
    private class TasklistaLibros extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                listaLibros = bbddHandler.getLibros();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            recyclerAdapter=new RecyclerAdapter(listaLibros, getApplicationContext());
            RecyclerView recyclerView = findViewById(R.id.recylcerview);
            recyclerView.setHasFixedSize(true);
            recyclerView.setAdapter(recyclerAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            recyclerAdapter.updateCover();
        }
    }

}
