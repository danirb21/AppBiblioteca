package com.campuscamara.app.frontend;

import static org.apache.commons.lang3.StringUtils.isNumeric;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.campuscamara.app.R;
import com.campuscamara.app.model.BbddHandler;
import com.campuscamara.app.model.Cliente;

import java.util.Arrays;
import java.util.regex.Pattern;

public class CrearClienteAct extends AppCompatActivity {
    private EditText editTextNombre;
    private EditText editTextApellidos;
    private EditText editTextDni;
    private EditText editTextTelefono;
    private Button btnCrearCliente;
    private BbddHandler bbddHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_cliente);
        editTextNombre=findViewById(R.id.editTextNombre);
        editTextApellidos=findViewById(R.id.editTextApellidos);
        editTextDni=findViewById(R.id.editTextDni);
        editTextTelefono=findViewById(R.id.editTextTelefono);
        btnCrearCliente=findViewById(R.id.btnCrearCliente);
        bbddHandler=new BbddHandler(getResources().getString(R.string.RutaServidor));
        btnCrearCliente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String nombre = editTextNombre.getText().toString().trim();
                    String apellidos = editTextApellidos.getText().toString().trim();
                    String dni = editTextDni.getText().toString().trim();
                    long telefono = Long.parseLong(editTextTelefono.getText().toString().trim());
                    if (!nombre.isEmpty() && !apellidos.isEmpty() && validarDNI(dni)){
                        bbddHandler.aniadirCliente(new Cliente(nombre,apellidos,dni,telefono));
                        Intent intent=new Intent(getApplicationContext(), AniadirPrestamoAct.class);
                        startActivity(intent);
                    }else{
                        Toast.makeText(getApplicationContext(),"Formato Incorrecto", Toast.LENGTH_LONG).show();
                    }
                }catch(NumberFormatException nfe){
                    Toast.makeText(getApplicationContext(),"Formato de telefono incorrecto",Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    public  boolean validarDNI(String dni) {
          final Pattern REGEXP = Pattern.compile("[0-9]{8}[A-Z]");
          final String DIGITO_CONTROL = "TRWAGMYFPDXBNJZSQVHLCKE";
          final String[] INVALIDOS = new String[] { "00000000T", "00000001R", "99999999R" };
        return Arrays.binarySearch(INVALIDOS, dni) < 0
                && REGEXP.matcher(dni).matches()
                && dni.charAt(8) == DIGITO_CONTROL.charAt(Integer.parseInt(dni.substring(0, 8)) % 23);
    }

}