package com.campuscamara.app.frontend;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.campuscamara.app.R;
import com.campuscamara.app.model.BbddHandler;
import com.campuscamara.app.model.Libro;
import com.google.firebase.annotations.concurrent.Background;

import java.util.concurrent.ExecutionException;

public class LibroDetallesAct extends AppCompatActivity {

    private EditText edtTitulo;
    private EditText edtAutor;
    private EditText edtGenero;
    private EditText edtTipo;
    private ImageView imagen;
    private Libro libro;
    private BbddHandler bbddHandler;
    private Button btnmodificar;
    private Button btnGuardar;
    private ImageButton buttonBorrar;
    private Intent intent;
    private Drawable fondoEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_libro_detalles);
        intent = getIntent();
        String id = intent.getStringExtra("libroid");
        edtTitulo = findViewById(R.id.editTextTitulo);
        edtAutor = findViewById(R.id.editTextAutor);
        edtGenero = findViewById(R.id.editTextGenero);
        edtTipo = findViewById(R.id.editTextTipo);
        imagen = findViewById(R.id.imageView2);
        btnmodificar=findViewById(R.id.btnModificar);
        btnGuardar=findViewById(R.id.btnGuardar);
        btnGuardar.setVisibility(View.INVISIBLE);
        fondoEditText=edtTitulo.getBackground();
        buttonBorrar=findViewById(R.id.btnBorrar);
        buttonBorrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TaskEliminarLibro taskEliminarLibro=new TaskEliminarLibro(id);
                taskEliminarLibro.execute();
                Intent intent1=new Intent(getApplicationContext(), ActivityLibros.class);
                startActivity(intent1);
            }
        });
        btnmodificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtTitulo.setFocusableInTouchMode(true);
                edtTitulo.setBackground(fondoEditText);
                edtAutor.setFocusableInTouchMode(true);
                edtAutor.setBackground(fondoEditText);
                edtGenero.setFocusableInTouchMode(true);
                edtGenero.setBackground(fondoEditText);
                edtTipo.setBackground(fondoEditText);
                edtTipo.setFocusableInTouchMode(true);
                buttonBorrar.setEnabled(false);
                buttonBorrar.setVisibility(View.INVISIBLE);
                btnGuardar.setEnabled(true);
                btnGuardar.setVisibility(View.VISIBLE);
                btnmodificar.setVisibility(View.INVISIBLE);
            }
        });
        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!edtTitulo.getText().toString().isEmpty() && !edtAutor.getText().toString().isEmpty()
                && !edtGenero.getText().toString().isEmpty() && !edtTipo.getText().toString().isEmpty()){
                    bbddHandler.modificarLibro(id,"titulo",edtTitulo.getText().toString().trim());
                    bbddHandler.modificarLibro(id,"autor",edtAutor.getText().toString().trim());
                    bbddHandler.modificarLibro(id,"genero",edtGenero.getText().toString().trim());
                    bbddHandler.modificarLibro(id,"tipo",edtTipo.getText().toString().trim());
                    Toast.makeText(getApplicationContext(),"Modificado Correctamente",Toast.LENGTH_LONG).show();
                    btnGuardar.setVisibility(View.INVISIBLE);
                    btnmodificar.setVisibility(View.VISIBLE);
                    buttonBorrar.setEnabled(true);
                    buttonBorrar.setVisibility(View.VISIBLE);

                }else{
                    Toast.makeText(getApplicationContext(),"Algunos de los campos estan vacios",Toast.LENGTH_LONG).show();
                }
            }
        });

        TaskLibroId task=new TaskLibroId(id);
        task.execute();
    }
    void disableInput(EditText editText) {
        editText.setFocusable(false);
        editText.setFocusableInTouchMode(false);
        editText.setBackground(null);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(intent.getStringExtra("activity")==null) {
            Intent intent = new Intent(getApplicationContext(), ActivityLibros.class);
            startActivity(intent);
        }else if(intent.getStringExtra("activity").equals("prestamo")){
            Intent intent1=new Intent(getApplicationContext(), PrestamoActivity.class);
            startActivity(intent1);
        }else{
            Intent intent1=new Intent(getApplicationContext(), AniadirPrestamoAct.class);
            startActivity(intent1);
        }
    }

    private class TaskLibroId extends AsyncTask<Void, Void, Void> {
            private String id;

            public TaskLibroId(String id) {
                this.id = id;
            }

            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    bbddHandler = new BbddHandler(getResources().getString(R.string.RutaServidor));
                    libro = bbddHandler.buscarporId(id);
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void unused) {
                if(libro!=null) {
                    edtTitulo.setText(libro.getTitulo());
                    edtAutor.setText(libro.getAutor());
                    edtGenero.setText(libro.getGenero());
                    edtTipo.setText(libro.getTipo());
                    Glide.with(getApplicationContext()).load(libro.getCoverurl()).placeholder(R.drawable.placeholder).into(imagen);
                    disableInput(edtTitulo);
                    disableInput(edtAutor);
                    disableInput(edtGenero);
                    disableInput(edtTipo);
                }else{
                    Toast.makeText(getApplicationContext(),"Ha ocurrido un error inesperado, recargue la pagina",Toast.LENGTH_LONG).show();
                }
            }
        }
        private class TaskEliminarLibro extends AsyncTask<Void,Void,Void>{
            private String id;
            public TaskEliminarLibro(String id){
                this.id=id;
            }

            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    bbddHandler.eliminarLibro(id);
                } catch (ExecutionException e) {
                    throw new RuntimeException(e);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void unused) {
                super.onPostExecute(unused);
                Toast.makeText(getApplicationContext(),"Borrado Correctamente",Toast.LENGTH_LONG).show();
            }
        }
}