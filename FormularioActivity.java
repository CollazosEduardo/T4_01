package com.example.vj20231;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.vj20231.entities.User;
import com.example.vj20231.services.UserService;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FormularioActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario);

        EditText Name = this.findViewById(R.id.editTextName);
        EditText Username = this.findViewById(R.id.editTextUserName);
        EditText Correo = this.findViewById(R.id.editTextEmail);
        Button btnR = this.findViewById(R.id.registrar);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://64865fe6a795d24810b7e07b.mockapi.io/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        UserService service = retrofit.create(UserService.class);

        btnR.setOnClickListener(view -> {

            User user = new User();
            user.name = Name.getText().toString();
            user.username  = Username.getText().toString();
            user.email = Correo.getText().toString();


            Call<User> call = service.create(user);

            call.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                   Log.i("MAIN_APP", String.valueOf(response.code()));
                    if(response.isSuccessful()) {
                        User data = response.body();
               //         Log.i("MAIN_APP", String.valueOf(data.size()));
                        Log.i("MAIN_APP", new Gson().toJson(data));
//                        NameAdapter adapter = new NameAdapter(data);
//                        rvLista.setAdapter(adapter);
                    }
                    // llegó una respuesta
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {

                }
            });

            Intent intent = new Intent(getApplicationContext(), FormUserActivity.class);
            startActivity(intent);
        });

    }
}

