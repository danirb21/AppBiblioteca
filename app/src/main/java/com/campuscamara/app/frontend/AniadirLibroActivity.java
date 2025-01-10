package com.campuscamara.app.frontend;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.campuscamara.app.R;
import com.campuscamara.app.model.BbddHandler;
import com.campuscamara.app.model.Libro;

public class AniadirLibroActivity extends AppCompatActivity {
    private Button btnGuardar;
    private EditText edtTitulo;
    private EditText edtAutor;
    private EditText edtGenero;
    private EditText edtTipo;
    private BbddHandler bbddHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aniadir_libro);
        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
        getWindow().setAttributes(layoutParams);
        bbddHandler=new BbddHandler(getResources().getString(R.string.RutaServidor));
        edtTitulo = findViewById(R.id.edtTitulo);
        edtAutor = findViewById(R.id.edtAutor);
        edtGenero = findViewById(R.id.edtGenero);
        edtTipo = findViewById(R.id.edtTipo);
        btnGuardar = findViewById(R.id.btnGuardar);
        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!edtTitulo.getText().toString().isEmpty() && !edtAutor.getText().toString().isEmpty() && !edtGenero.getText().toString().isEmpty()
                        && !edtTipo.getText().toString().isEmpty()) {
                    showDialog();
                } else {
                    Toast.makeText(getApplicationContext(), "Algunos de los campos estan vacios", Toast.LENGTH_LONG).show();
                }

            }
        });

    }

    public void showDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);

        dialog.setMessage("¿Estas Seguro?")
                .setTitle("Confirmacion");
        dialog.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                bbddHandler.aniadirLibro(new Libro(edtTitulo.getText().toString().trim(), edtAutor.getText().toString().trim(), edtGenero.getText().toString().trim()
                        , edtTipo.getText().toString().trim()));
                Intent intent = new Intent(getApplicationContext(), ActivityLibros.class);
                startActivity(intent);

            }
        });
        dialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        dialog.show();

    }
}