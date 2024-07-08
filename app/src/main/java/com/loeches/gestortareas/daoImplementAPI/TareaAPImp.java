package com.loeches.gestortareas.daoImplementAPI;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loeches.gestortareas.daoInterface.TareaDAO;
import com.loeches.gestortareas.modelos.Tarea;
import com.loeches.gestortareas.modelos.Trabajador;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class TareaAPImp implements TareaDAO {

    private OkHttpClient cliente;
    private Gson gson;

    public TareaAPImp() {
        cliente = new OkHttpClient();
        gson = new Gson();
    }

    @Override
    public void AgregarTarea(Tarea ta, Trabajador tr) {
        RequestBody body =
                RequestBody.create(gson.toJson(ta),
                        MediaType.get("application/json; charset=utf-8"));

        Request peticion = new Request.Builder()
                .url("http://localhost:8080/api/tareas?DNI=" + tr.getDNI())
                .post(body)
                .build();

        try {
            cliente.newCall(peticion).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void EliminarTarea(Tarea ta) {
        Request peticion = new Request.Builder()
                .url("http://localhost:8080/api/tareas/" + ta.getId())
                .delete()
                .build();
        try {
            cliente.newCall(peticion).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public Future ObtenerTareasF(Trabajador tr) {
        CompletableFuture f = new CompletableFuture();
        RequestBody body = new FormBody.Builder()
                .add("DNI", tr.getDNI())
                .build();
        Request peticion = new Request.Builder()
                .url("http://localhost:8080/api/tareas")
                .post(body)
                .build();
        cliente.newCall(peticion).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
                f.completeExceptionally(e);
            }
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    Type listType = new TypeToken<List<Tarea>>() {
                    }.getType();
                    List<Tarea> tra = gson.fromJson(response.body().string(), listType);
                    tr.getTareas().clear();
                    tr.getTareas().addAll(tra);
                    f.complete(tra);
                } else {
                    f.completeExceptionally(new Exception("Error al recibir los datos"));
                }
            }
        });
        return f;
    }
}
