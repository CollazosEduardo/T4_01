package com.example.vj20231;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.example.vj20231.adapters.NameAdapter;
import com.example.vj20231.entities.User;
import com.example.vj20231.services.UserService;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ListaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista);


        RecyclerView rvLista =  findViewById(R.id.rvListaSimple);
        rvLista.setLayoutManager(new LinearLayoutManager(this));


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://64865fe6a795d24810b7e07b.mockapi.io/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();



        UserService service = retrofit.create(UserService.class);

        Call<List<User>> call = service.getAllUser();// Guardar la appi


        call.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                Log.i("MAIN_APP", String.valueOf(response.code()));
                if(response.isSuccessful()) {
                   List<User> data = response.body();
                    Log.i("MAIN_APP", new Gson().toJson(data));
                    NameAdapter adapter = new NameAdapter(data);
                    rvLista.setAdapter(adapter);
                }
               // llegó una respuesta
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {

            }
        });

    }


    private List<String> data() {
        List<String> names = new ArrayList<>();
        names.add("Luis");
        names.add("Marcos");
        names.add("Alonso");
        names.add("Lionel");
        names.add("Cristiano");
        names.add("Luis");
        names.add("Marcos");
        names.add("Alonso");
        names.add("Lionel");
        names.add("Cristiano");
        names.add("Luis");
        return names;

    }
}