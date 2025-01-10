package com.campuscamara.app.frontend;

import android.content.Context;
import android.content.Intent;
import android.view.MenuItem;
import com.campuscamara.app.R;
import androidx.annotation.NonNull;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

public class HandlerMenu implements NavigationView.OnNavigationItemSelectedListener {
    private Context context;

    private DrawerLayout drawerLayout;

    public HandlerMenu(Context context, DrawerLayout drawerLayout){
        this.context=context;
        this.drawerLayout=drawerLayout;
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        //int id=getId(item);
       // showFragment(id);
        Intent intent = null;
        if(item.getItemId()==R.id.nav_search){
            intent=new Intent(context,ActivityLibros.class);
        }else if(item.getItemId()==R.id.nav_prestamos){
            intent=new Intent(context, PrestamoActivity.class);
        }else if(item.getItemId()==R.id.logout){
            intent =new Intent(context, Login.class);
        }
        if(intent!=null){
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
        return true;
        //showFragment
    }

  /*  public int getId(MenuItem menuItem) {
        int num;
        if(menuItem.getItemId()==MenuPrincipal.getIdBuscar()){
            num=MenuPrincipal.getIdBuscar();
        }else{
            num=MenuPrincipal.getIdCalendario();
        }
        return num;
    }
   */
}
