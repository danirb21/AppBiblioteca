package com.campuscamara.app.frontend;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CalendarView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.campuscamara.app.R;
import com.campuscamara.app.model.BbddHandler;
import com.campuscamara.app.model.Prestamo;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.materialswitch.MaterialSwitch;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class PrestamoActivity extends AppCompatActivity {
    private ImageButton button;
    private PrestamoAdapter prestamoAdapter;
    private BbddHandler bbddHandler;
    private List<Prestamo> prestamos;
    private CalendarView calendarView;
    private FloatingActionButton btnAniadir;
    //private long selectedDatemilis;
    private String selectedDate;
    private TextView fechaFiltrada;
    private MaterialSwitch switch1;
    private MaterialSwitch switch2;
    //private boolean esFechaPrestamo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_prestamo);
        button = findViewById(R.id.btnFecha);
        btnAniadir=findViewById(R.id.botonaniadirPrestamo);
        fechaFiltrada=findViewById(R.id.fechafiltrada);
        prestamos = new ArrayList<>();
        //selectedDatemilis=-1;
        bbddHandler = new BbddHandler(getResources().getString(R.string.RutaServidor));
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDatePickerDialog();
            }
        });
         switch1 = findViewById(R.id.switch1);
         switch2 = findViewById(R.id.switch2);
        switch1.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                switch2.setChecked(false);
                filterLoansByDate(false);
            }else if(!isChecked && !switch2.isChecked()){
                prestamoAdapter.updateList(prestamos);
            }
        });

        switch2.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                switch1.setChecked(false);
                filterLoansByDate(true);
            }else if(!isChecked && !switch2.isChecked()){
                prestamoAdapter.updateList(prestamos);
            }
        });
        btnAniadir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), AniadirPrestamoAct.class);
                startActivity(intent);
            }
        });
        TaskPrestamos taskPrestamos = new TaskPrestamos();
        taskPrestamos.execute();
    }
    private void openDatePickerDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_calendar, null);
        builder.setView(dialogView);
        calendarView = dialogView.findViewById(R.id.calendarView);
        //calendarView.setDate(selectedDatemilis);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                selectedDate = dayOfMonth + "/" + (month + 1) + "/" + year;
            }
        });
        builder.setPositiveButton("Seleccionar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                boolean bol;
                if (switch1.isChecked() || switch2.isChecked()) {
                    if (switch2.isChecked()) {
                        bol = true;
                    } else {
                        bol = false;
                    }
                    fechaFiltrada.setText(selectedDate);
                    filterLoansByDate(bol);
                }
            }
        });
        builder.setNegativeButton("Cancelar", (dialog, which) -> dialog.dismiss());
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void filterLoansByDate(boolean bol) {
        // Filter your loan list by the selected date
        List<Prestamo> filteredList = new ArrayList<>();
        if (bol) {
            for (Prestamo prestamo : prestamos) {
                if (prestamo.getFechaDevolucion().equals(selectedDate)) {
                    filteredList.add(prestamo);
                }
            }
            prestamoAdapter.updateList(filteredList);
        } else if(!bol) {
            for (Prestamo prestamo : prestamos) {
                if (prestamo.getFechaPrestamo().equals(selectedDate)) {
                    filteredList.add(prestamo);
                }
            }
            prestamoAdapter.updateList(filteredList);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent=new Intent(getApplicationContext(), MenuPrincipal.class);
        startActivity(intent);
    }

    private class TaskPrestamos extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                prestamos = bbddHandler.getPrestamos();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            prestamoAdapter = new PrestamoAdapter(prestamos, getApplicationContext());
            RecyclerView recyclerView = findViewById(R.id.recyclerView);
            recyclerView.setHasFixedSize(true);
            recyclerView.setAdapter(prestamoAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(PrestamoActivity.this));

        }
    }

}