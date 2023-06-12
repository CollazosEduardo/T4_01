package com.example.vj20231;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.vj20231.entities.User;
import com.example.vj20231.services.UserService;

import java.io.ByteArrayOutputStream;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FormUserActivity extends AppCompatActivity {

    private static final int OPEN_CAMERA_REQUEST = 1001;
    private static final int OPEN_GALLERY_REQUEST = 1002;
    private ImageView ivAvatar;
    String urlImage = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_user);

        ivAvatar = findViewById(R.id.ivAvatar);
        Button btnCamera = findViewById(R.id.btnOpenCamera);
        Button btnGallery = findViewById(R.id.btnGallery);
        Button mostrar = this.findViewById(R.id.MostrarForm);
      //  Button registrar = this.findViewById(R.id.RegistrarForm);
       Button apiRegister = this.findViewById(R.id.buttonRegister);

        EditText name=this.findViewById(R.id.editName);
        EditText USERNAME=this.findViewById(R.id.editUsernamee);
        EditText email=this.findViewById(R.id.editEmanil);

        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleOpenCamera();
            }
        });

        apiRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("https://64865fe6a795d24810b7e07b.mockapi.io/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                UserService services = retrofit.create(UserService.class);

                User user = new User();
                user.name=String.valueOf(name.getText());
                user.username=String.valueOf(USERNAME.getText());
                user.email=String.valueOf(email.getText());
                user.img=String.valueOf("https://demo-upn.bit2bittest.com/"+urlImage);

                Call<User> call = services.create(user);


                call.enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        if (response.isSuccessful()) {
                            // La imagen se agregó correctamente a MockAPI
                        } else {
                            // Hubo un error al agregar la imagen a MockAPI
                        }
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        // Error de red o de la API
                    }
                });
            }
        });

        btnGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    openGallery();
                }
                else {
                    String[] permissions = new String[] {Manifest.permission.READ_EXTERNAL_STORAGE};
                    requestPermissions(permissions, 2000);
                }
            }
        });
        mostrar.setOnClickListener(view -> {
//            int sum = numbers.stream().reduce(0, Integer::sum);
//            tvSum.setText(String.valueOf(sum));

            Intent intent = new Intent(getApplicationContext(), ListaActivity.class);
            startActivity(intent);

        });
    //  registrar.setOnClickListener(view -> {
//            int sum = numbers.stream().reduce(0, Integer::sum);
//            tvSum.setText(String.valueOf(sum));

     //       Intent intent = new Intent(getApplicationContext(), FormularioActivity.class);
    //        startActivity(intent);

     //   });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == OPEN_CAMERA_REQUEST && resultCode == RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            ivAvatar.setImageBitmap(photo);


            String base64Image = convertBitmapToBase64(photo);


            System.out.println(base64Image);
            Log.e("hola",base64Image);



            // imprimirImagenEnLog(base64Image);
            Retrofit retrofit123 = new Retrofit.Builder()
                    .baseUrl("https://demo-upn.bit2bittest.com/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            UserService service=retrofit123.create(UserService.class);



            Call<UserService.ImageResponse> call = service.saveImage(new UserService.ImageToSave(base64Image));

            call.enqueue(new Callback<UserService.ImageResponse>() {
                @Override
                public void onResponse(Call<UserService.ImageResponse> call, Response<UserService.ImageResponse> response) {
                    Log.i("Respuesta activa", response.toString());
                    if (response.isSuccessful()) {

                        UserService.ImageResponse imageResponse = response.body();
                        Log.i("Respues", response.toString());
                        urlImage = imageResponse.getUrl();
                        Log.i("Imagen url:", urlImage);

                    } else {

                        Log.e("Error cargar imagen",response.toString());
                    }
                }

                @Override
                public void onFailure(Call<UserService.ImageResponse> call, Throwable t) {
                    // Error de red o de la API
                    Log.i("Respuesta inactiva", "");
                }
            });




        }

        if(requestCode == OPEN_GALLERY_REQUEST && resultCode == RESULT_OK) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };
            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close(); // close cursor

            Bitmap bitmap = BitmapFactory.decodeFile(picturePath);
            ivAvatar.setImageBitmap(bitmap);
        }

    }

    private void handleOpenCamera() {
        if(checkSelfPermission(Manifest.permission.CAMERA)  == PackageManager.PERMISSION_GRANTED)
        {
            // abrir camara
            Log.i("MAIN_APP", "Tiene permisos para abrir la camara");
            abrirCamara();
        } else {
            // solicitar el permiso
            Log.i("MAIN_APP", "No tiene permisos para abrir la camara, solicitando");
            String[] permissions = new String[] {Manifest.permission.CAMERA};
            requestPermissions(permissions, 1000);
        }
    }

    private void abrirCamara() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, OPEN_CAMERA_REQUEST);
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, OPEN_GALLERY_REQUEST);
    }

    private String convertBitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }



}