package com.example.diploma;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Base64;
import android.util.JsonReader;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.security.MessageDigest;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import dmax.dialog.SpotsDialog;
import es.dmoral.toasty.Toasty;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import com.google.gson.Gson;

public class LoginActivity extends AppCompatActivity {

    IAPI iapi;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    public static int user_enterence_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        iapi = RetrofitClient.getInstance().create(IAPI.class);
    }

    public void GoToRegistrationForm(View view){
        Intent intent = new Intent(this, RegistrationActivity.class);
        startActivity(intent);
    }

    public void SignIn(View view){

        final EditText login = (EditText)findViewById(R.id.signin_login);
        final EditText password = (EditText)findViewById(R.id.signin_password);

        final Intent doctor = new Intent(this, DoctorActivity.class);
        final Intent admin = new Intent(this, AdminActivity.class);
        final Intent patient = new Intent(this, MainActivity.class);

        final AlertDialog dialog = new SpotsDialog.Builder()
                .setContext(LoginActivity.this)
                .build();
        dialog.show();


        if(login.getText().toString().trim().equals("") && password.getText().toString().trim().equals(""))
        {
            Toasty.error(this, "Поля логина и пароля пустые!", Toast.LENGTH_SHORT, true).show();
            dialog.dismiss();
//            Intent intent = new Intent(this, LoginActivity.class);
//            startActivity(intent);
        }else{

            try {
                User user = new User(0, "", login.getText().toString().trim(), encrypt(password.getText().toString().getBytes(), ("0123000000000215").getBytes()), "");

                compositeDisposable.add(iapi.loginUser(user)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<String>() {
                            @Override
                            public void accept(String s) throws Exception {
                                //Toast.makeText(LoginPage.this, "Success!", Toast.LENGTH_SHORT).show();
                                //LoginActivity loginPage = new LoginActivity();
                                //loginPage.user_enterence_id = Integer.parseInt(s); //Integer.getInteger(s);
                                //Log.d("tag2", "login page     " + user_enterence_id);

                                if(s.equals("\"Wrong password!\"")){
                                    Toasty.error(getApplication(), "Неверный пароль!", Toast.LENGTH_SHORT, true).show();
                                }else if (s.equals("\"User not existing!\"")){
                                    Toasty.error(getApplication(), "Такого пользователя не существует!", Toast.LENGTH_SHORT, true).show();
                                }else{
                                    User user = new Gson().fromJson(s, User.class);

                                    user_enterence_id = user.UserId;

                                    if(user.Role.equals("Doctor")){
                                        startActivity(doctor);
                                    }else if (user.Role.equals("Patient")){
                                        startActivity(patient);
                                    }else if(user.Role.equals("Admin")){
                                        startActivity(admin);
                                    }
                                }

                                dialog.dismiss();
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                dialog.dismiss();
                                //attempt to invoke virtual method
                                //Toast.makeText(LoginActivity.this, throwable.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }));
            }catch(Exception e){

            }

            login.setText("");
            password.setText("");


        }




//        if(login.getText().toString().trim().equals("doctor")){
//            Intent intent = new Intent(this, DoctorActivity.class);
//            startActivity(intent);
//        }else if(login.getText().toString().trim().equals("admin")){
//            Intent intent = new Intent(this, AdminActivity.class);
//            startActivity(intent);
//        }else{
//            Intent intent = new Intent(this, MainActivity.class);
//            startActivity(intent);
//        }

    }


    private static String encrypt(byte[] key, byte[] clear) throws Exception
    {
        MessageDigest md = MessageDigest.getInstance("md5");
        byte[] digestOfPassword = md.digest(key);

        SecretKeySpec skeySpec = new SecretKeySpec(digestOfPassword, "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS7Padding");
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
        byte[] encrypted = cipher.doFinal(clear);
        return Base64.encodeToString(encrypted,Base64.DEFAULT);
    }
}
