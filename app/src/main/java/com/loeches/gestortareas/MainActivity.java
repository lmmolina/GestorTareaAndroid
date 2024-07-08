package com.loeches.gestortareas;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Space;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.loeches.gestortareas.conexionBD.ConexionMySQL;
import com.loeches.gestortareas.conexionBD.DatabaseHelper;
import com.loeches.gestortareas.daoImplementMySQL.TrabajadorMyImp;
import com.loeches.gestortareas.daoImplementSQLite.TrabajadorDAOImp;
import com.loeches.gestortareas.daoInterface.TrabajadorDAO;
import com.loeches.gestortareas.modelos.Trabajador;

import java.sql.SQLException;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    TableLayout tl;
    TrabajadorDAO datos;
    //datos = new TrabajadorDAOImp(db.getWritableDatabase());
    TextView promedio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        tl = findViewById(R.id.tlTrabajadores);
        FloatingActionButton b = findViewById(R.id.bAdicionar);
        promedio = findViewById(R.id.tPromedio);
        Button aumentar = findViewById(R.id.bAumento);

        EstablecerConexion();

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), TrabajorActivity.class);
                startActivity(intent);
            }
        });
        aumentar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (datos instanceof TrabajadorDAOImp) {
                    ((TrabajadorDAOImp) datos).AumentarSalario(20, 30);
                    onResume();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        MostrarTrabajadores(datos.ObtenerTrabajadores());
        MostrarPromedioSalario();
    }

    public void MostrarPromedioSalario() {
        if (datos instanceof TrabajadorDAOImp) {
            double prom = ((TrabajadorDAOImp) datos).PromedioSalario();
            prom = Math.round(prom * 100) / 100.0;
            promedio.setText("El salario promedio es " + prom + "€.");
        }
    }

    public void MostrarTrabajadores(List<Trabajador> trabajadores) {
        tl.removeAllViews();
        TableRow header = new TableRow(this);
        TextView t1 = new TextView(this);
        TableRow.LayoutParams parametros =
                new TableRow.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1);
        t1.setLayoutParams(parametros);
        t1.setText("DNI");
        t1.setTypeface(null, Typeface.BOLD);
        parametros = new TableRow.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 2);
        TextView t2 = new TextView(this);
        t2.setLayoutParams(parametros);
        t2.setText("Nombre");
        t2.setTypeface(null, Typeface.BOLD);
        parametros = new TableRow.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1);
        TextView t3 = new TextView(this);
        t3.setLayoutParams(parametros);
        t3.setText("Salario Hora");
        t3.setTypeface(null, Typeface.BOLD);
        parametros = new TableRow.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1);
        Space t4 = new Space(this);
        t4.setLayoutParams(parametros);
        header.addView(t1);
        header.addView(t2);
        header.addView(t3);
        header.addView(t4);
        tl.addView(header);

        for (Trabajador t : trabajadores) {
            TableRow fila = new TableRow(this);
            parametros =
                    new TableRow.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1);
            t1 = new TextView(this);
            t1.setLayoutParams(parametros);
            t1.setText(t.getDNI());
            parametros =
                    new TableRow.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 2);
            t2 = new TextView(this);
            t2.setLayoutParams(parametros);
            t2.setText(t.getNombre());
            parametros =
                    new TableRow.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1);
            t3 = new TextView(this);
            t3.setLayoutParams(parametros);
            t3.setText(t.getSalarioHora() + "€");
            parametros =
                    new TableRow.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1);
            Button b = new Button(this);
            b.setLayoutParams(parametros);
            b.setText("VER");

            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), TrabajorActivity.class);
                    intent.putExtra("DNI", t.getDNI());
                    startActivity(intent);
                }
            });

            fila.addView(t1);
            fila.addView(t2);
            fila.addView(t3);
            fila.addView(b);
            tl.addView(fila);
        }
    }

    public void EstablecerConexion() {
        try {
            ConexionMySQL conMy = new ConexionMySQL();
            datos = new TrabajadorMyImp(conMy);
            Toast.makeText(this, "MySQL Conectado", Toast.LENGTH_LONG).show();
            return;
        } catch (Exception e) {
            e.printStackTrace();
        }

        DatabaseHelper db = new DatabaseHelper(this);
        datos = new TrabajadorDAOImp(db.getWritableDatabase());
        Toast.makeText(this, "SQLite Conectado", Toast.LENGTH_LONG).show();

    }

    public void ExportarDatos(){

    }
}