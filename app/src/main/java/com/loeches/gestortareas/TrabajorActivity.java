package com.loeches.gestortareas;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.loeches.gestortareas.conexionBD.DatabaseHelper;
import com.loeches.gestortareas.daoImplementSQLite.TareaDAOImp;
import com.loeches.gestortareas.daoImplementSQLite.TrabajadorDAOImp;
import com.loeches.gestortareas.daoInterface.TareaDAO;
import com.loeches.gestortareas.daoInterface.TrabajadorDAO;
import com.loeches.gestortareas.modelos.Tarea;
import com.loeches.gestortareas.modelos.Trabajador;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class TrabajorActivity extends AppCompatActivity {
    TrabajadorDAO datosTra;
    TareaDAO datosTar;
    List<Tarea> tareas;
    LinearLayout ltareas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_trabajor);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        EditText tnombre = findViewById(R.id.tNombre);
        EditText tdni = findViewById(R.id.tDNI);
        EditText tsalario = findViewById(R.id.tSalario);
        EditText ttelefono = findViewById(R.id.tTelefono);
        EditText temail = findViewById(R.id.tEmail);
        Button guardar = findViewById(R.id.bGuardar);
        Button eliminar = findViewById(R.id.bEliminar);
        Button generarTareas = findViewById(R.id.bTareas);
        ltareas = findViewById(R.id.llTareas);

        Intent intent = getIntent();
        final String dni = intent.getStringExtra("DNI");

        DatabaseHelper db = new DatabaseHelper(this);
        datosTra = new TrabajadorDAOImp(db.getWritableDatabase());
        datosTar = new TareaDAOImp(db.getWritableDatabase());

        tareas = new ArrayList<>();
        if (dni != null) {
            Trabajador tra = datosTra.ObtenerTrabajador(dni);
            tnombre.setText(tra.getNombre());
            tdni.setText(tra.getDNI());
            tdni.setEnabled(false);
            tsalario.setText(tra.getSalarioHora() + "€");
            ttelefono.setText(tra.getTelefono());
            temail.setText(tra.getCorreo());
            datosTar.ObtenerTareas(tra);
            tareas.addAll(tra.getTareas());
            MostrarTareas();
        }

        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nombre = tnombre.getText().toString();
                String ndni = tdni.getText().toString();
                double salario = Double.parseDouble(
                        tsalario.getText().toString().replace("€", ""));
                String telefono = ttelefono.getText().toString();
                String correo = temail.getText().toString();
                Trabajador t = new Trabajador(nombre, salario, ndni, telefono, correo);
                if (dni != null) {
                    datosTra.ActualizarTrabajador(t);
                } else {
                    datosTra.InsertarTrabajador(t);
                }
                for (Tarea tar : tareas) {
                    if (tar.getId() == -1) {
                        datosTar.AgregarTarea(tar, t);
                    }
                }
                finish();
            }
        });
        eliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dni != null) {
                    datosTra.EliminarTrabajador(dni);
                }
                finish();
            }
        });

        generarTareas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < 5; i++) {
                    tareas.add(
                            new Tarea(LocalDate.now().plusDays(i),
                                    LocalTime.of(8, 0),
                                    LocalTime.of(15, 0),
                                    "Un lugar",
                                    "Lo que tienes que hacer"));
                }
                MostrarTareas();
            }
        });
    }

    public void MostrarTareas() {
        ltareas.removeAllViews();
        for (Tarea t : tareas) {
            LinearLayout ll = new LinearLayout(this);
            ll.setOrientation(LinearLayout.HORIZONTAL);
            TextView tv = new TextView(this);
            tv.setText(
                    t.getFecha().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) +
                            " " + t.getHoraInicio().format(DateTimeFormatter.ISO_LOCAL_TIME) +
                            " " + t.getLugar());
            Button b = new Button(this);
            b.setText("Eliminar");

            ll.addView(tv);
            ll.addView(b);
            ltareas.addView(ll);

            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    datosTar.EliminarTarea(t);
                    tareas.remove(t);
                    MostrarTareas();
                }
            });
        }
    }
}