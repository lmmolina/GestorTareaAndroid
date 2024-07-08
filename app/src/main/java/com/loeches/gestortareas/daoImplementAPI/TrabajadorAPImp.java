package com.loeches.gestortareas.daoImplementAPI;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loeches.gestortareas.daoInterface.TrabajadorDAO;
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

public class TrabajadorAPImp implements TrabajadorDAO {
    private OkHttpClient cliente;
    private Gson gson;

    public TrabajadorAPImp() {
        cliente = new OkHttpClient();
        gson = new Gson();
    }

    @Override
    public void InsertarTrabajador(Trabajador t) {
        RequestBody body =
                RequestBody.create(gson.toJson(t),
                        MediaType.get("application/json; charset=utf-8"));

        Request peticion = new Request.Builder()
                .url("http://localhost:8080/api/trabajadores/add")
                .post(body)
                .build();

        try {
            cliente.newCall(peticion).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void EliminarTrabajador(String DNI) {
        RequestBody body = new FormBody.Builder()
                .add("DNI", DNI)
                .build();

        Request peticion = new Request.Builder()
                .url("http://localhost:8080/api/trabajadores")
                .delete(body)
                .build();

        try {
            cliente.newCall(peticion).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void ActualizarTrabajador(Trabajador t) {
        RequestBody body =
                RequestBody.create(gson.toJson(t),
                        MediaType.get("application/json; charset=utf-8"));

        Request peticion = new Request.Builder()
                .url("http://localhost:8080/api/trabajadores")
                .put(body)
                .build();

        try {
            cliente.newCall(peticion).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Trabajador ObtenerTrabajador(String DNI) {
        return null;
    }

    @Override
    public List<Trabajador> ObtenerTrabajadores() {
        return null;
    }

    public Future<Trabajador> ObtenerTrabajadorF(String DNI) {
        CompletableFuture<Trabajador> fTrab = new CompletableFuture<>();
        RequestBody body = new FormBody.Builder()
                .add("DNI", DNI)
                .build();
        Request peticion = new Request.Builder()
                .url("http://localhost:8080/api/trabajadores")
                .post(body)
                .build();
        cliente.newCall(peticion).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
                fTrab.completeExceptionally(e);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    Trabajador tra = gson.fromJson(response.body().string(), Trabajador.class);
                    fTrab.complete(tra);
                } else {
                    fTrab.completeExceptionally(new Exception("Error al recibir los datos"));
                }
            }
        });
        return fTrab;
    }

    public Future<List<Trabajador>> ObtenerTrabajadoresF() {
        CompletableFuture<List<Trabajador>> fTrab = new CompletableFuture<>();
        Request peticion = new Request.Builder()
                .url("http://localhost:8080/api/trabajadores")
                .get()
                .build();
        cliente.newCall(peticion).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
                fTrab.completeExceptionally(e);
            }
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    Type listType = new TypeToken<List<Trabajador>>() {
                    }.getType();
                    List<Trabajador> tra = gson.fromJson(response.body().string(), listType);
                    fTrab.complete(tra);
                } else {
                    fTrab.completeExceptionally(new Exception("Error al recibir los datos"));
                }
            }
        });
        return fTrab;
    }
}
