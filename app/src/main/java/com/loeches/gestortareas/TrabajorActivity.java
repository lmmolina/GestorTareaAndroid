package com.loeches.gestortareas;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.loeches.gestortareas.conexionBD.DatabaseHelper;
import com.loeches.gestortareas.daoImplement.TareaDAOImp;
import com.loeches.gestortareas.daoImplement.TrabajadorDAOImp;
import com.loeches.gestortareas.daoInterface.TareaDAO;
import com.loeches.gestortareas.daoInterface.TrabajadorDAO;
import com.loeches.gestortareas.modelos.Trabajador;

public class TrabajorActivity extends AppCompatActivity {
    TrabajadorDAO datosTra;
    TareaDAO datosTar;

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

        Intent intent = getIntent();
        final String dni = intent.getStringExtra("DNI");

        DatabaseHelper db = new DatabaseHelper(this);
        datosTra = new TrabajadorDAOImp(db.getWritableDatabase());
        datosTar = new TareaDAOImp(db.getWritableDatabase());

        if (dni != null) {
            Trabajador tra = datosTra.ObtenerTrabajador(dni);
            tnombre.setText(tra.getNombre());
            tdni.setText(tra.getDNI());
            tdni.setEnabled(false);
            tsalario.setText(tra.getSalarioHora() + "€");
            ttelefono.setText(tra.getTelefono());
            temail.setText(tra.getCorreo());
        }

        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nombre = tnombre.getText().toString();
                String ndni = tdni.getText().toString();
                double salario = Double.parseDouble(tsalario.getText().toString());
                String telefono = ttelefono.getText().toString();
                String correo = temail.getText().toString();
                Trabajador t = new Trabajador(nombre, salario, ndni, telefono, correo);
                if (dni != null) {
                    datosTra.ActualizarTrabajador(t);
                } else {
                    datosTra.InsertarTrabajador(t);
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
    }
}