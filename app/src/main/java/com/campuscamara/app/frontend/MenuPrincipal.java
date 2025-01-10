package com.campuscamara.app.frontend;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.campuscamara.app.R;
import com.campuscamara.app.model.Prestamo;
import com.campuscamara.app.model.BbddHandler;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;

import java.util.concurrent.ExecutionException;

public class MenuPrincipal extends AppCompatActivity {
    private boolean esPersonal;
    private BbddHandler bbddHandler;

    private TextView textoTotalLibros;

    private TextView textoTotalPrestamos;

    private Toolbar toolbar;

    private DrawerLayout drawerLayout;

    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_principal);
        Intent intent = getIntent();
        bbddHandler = new BbddHandler(getResources().getString(R.string.RutaServidor));
        esPersonal = intent.getBooleanExtra("esPersonal", false);
        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(findViewById(R.id.toolbar));
        drawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView=findViewById(R.id.navigation_view);
        HandlerMenu handlerMenu=new HandlerMenu(getApplicationContext(),drawerLayout);
        navigationView.setNavigationItemSelectedListener(handlerMenu);
        textoTotalLibros = findViewById(R.id.textoTotalLibros);
        textoTotalLibros.setGravity(Gravity.CENTER);
        textoTotalPrestamos=findViewById(R.id.textoTotalPrestamos);
        textoTotalPrestamos.setGravity(Gravity.CENTER);
        AsynctaskLibros asynctaskLibros=new AsynctaskLibros();
        asynctaskLibros.execute();
    }
    private class AsynctaskLibros extends AsyncTask<Void,Void, Long[]> {
     private boolean flag;
        @Override
        protected Long[] doInBackground(Void... voids) {
            Long[] numLibros=new Long[2];
            BbddHandler bbddHandler1=new BbddHandler(getResources().getString(R.string.RutaServidor));
            Task<DataSnapshot> task=bbddHandler1.getDatabaseReference().child("libros").get();
            Task<DataSnapshot> task1=bbddHandler1.getDatabaseReference().child("prestamos").get();
            try {
                Tasks.await(task);
                Tasks.await(task1);
            } catch (ExecutionException  | InterruptedException e) {
                flag=true;
            }
            numLibros[0]=task.getResult().getChildrenCount();
            numLibros[1]=task1.getResult().getChildrenCount();
            return numLibros;
        }

        @Override
        protected void onPostExecute(Long[] aLong) {
            super.onPostExecute(aLong);
            if(!flag) {
                textoTotalLibros.append("\n" + aLong[0]);
                textoTotalPrestamos.append("\n"+aLong[1]);
            }else{
                Toast.makeText(getApplicationContext(),"Fallo al contar los libros totales",Toast.LENGTH_LONG).show();
            }
        }
    }
}