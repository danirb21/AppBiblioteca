package com.campuscamara.app.frontend;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.campuscamara.app.R;
import com.campuscamara.app.model.BbddHandler;
import com.campuscamara.app.model.Usuario;

public class Login extends AppCompatActivity {
    private boolean esPersonal=true;

    private EditText usuariotexto;
    private EditText contraseniatext;
    private BbddHandler bbddHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        RadioButton radioButton=findViewById(R.id.radioPersonal);
        RadioButton radioButton1=findViewById(R.id.radioCliente);
        usuariotexto=findViewById(R.id.edtUsuario);
        contraseniatext=findViewById(R.id.edtContraseña);
        radioButton.setEnabled(false);
        radioButton1.setEnabled(false);
        if(esPersonal){
            radioButton.setChecked(true);
        }else{
            radioButton1.setChecked(true);
        }
        Button btIniciarSesion=findViewById(R.id.btnIniciarSesion);
        btIniciarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!usuariotexto.getText().toString().trim().isEmpty() && !contraseniatext.getText().toString().trim().isEmpty()) {
                    Usuario usuario = new Usuario(usuariotexto.getText().toString().trim(), contraseniatext.getText().toString().trim(), esPersonal);
                    TaskCheckUser taskCheckUser = new TaskCheckUser();
                    taskCheckUser.execute(usuario);
                }else{
                    Toast.makeText(getApplicationContext(),"Algunos de los campos esta vacio, por favor rellenelos",Toast.LENGTH_LONG).show();
                }
                //Task task=new Task();
                //task.execute();
            }
        });
    }
    //CONTEMPLAR POSIBILIDAD DE PANTALLA DE REGISTRO DE USUARIO.
    private class Task extends AsyncTask<Void,Void,Boolean>{

        @Override
        protected Boolean doInBackground(Void... voids) {
            Boolean bol=true;
            bbddHandler=new BbddHandler(getResources().getString(R.string.RutaServidor));
            try {
                bbddHandler.newUser(new Usuario(usuariotexto.getText().toString(),contraseniatext.getText().toString(),esPersonal));
            } catch (Exception e) {
                bol=false;
                e.printStackTrace();
            }
            return bol;
        }
    }
    private class TaskCheckUser extends AsyncTask<Usuario, Boolean, Boolean>{

        @Override
        protected Boolean doInBackground(Usuario... usuarios) {
            bbddHandler=new BbddHandler(getResources().getString(R.string.RutaServidor));
            return bbddHandler.checkuser(usuarios[0]);
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            if(aBoolean) {
                Intent intent1 = new Intent(getApplicationContext(), MenuPrincipal.class);
                intent1.putExtra("esPersonal", esPersonal);
                startActivity(intent1);
            }else{
                Toast.makeText(getApplicationContext(),"Usuario Incorrecto Vuelva a Intentarlo",Toast.LENGTH_LONG).show();
            }
        }
    }

}