package com.example.diploma;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import dmax.dialog.SpotsDialog;
import es.dmoral.toasty.Toasty;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class RegistrationActivity extends AppCompatActivity {

    IAPI iapi;
    CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        iapi = RetrofitClient.getInstance().create(IAPI.class);
    }

    public void SignUp(View view){
        EditText fullname = (EditText)findViewById(R.id.fullname);
        EditText address = (EditText)findViewById(R.id.address);
        EditText birthdate = (EditText)findViewById(R.id.birthdate);
        EditText login = (EditText)findViewById(R.id.login);
        EditText password = (EditText)findViewById(R.id.password);
        EditText password2 = (EditText)findViewById(R.id.password2);
        EditText phone = (EditText)findViewById(R.id.phone);
        EditText email = (EditText)findViewById(R.id.email);

        if(login.getText().toString().trim().equals("") || password.getText().toString().trim().equals("") ||
                address.getText().toString().trim().equals("") || birthdate.getText().toString().trim().equals("") ||
                fullname.getText().toString().trim().equals("") || phone.getText().toString().trim().equals("") ||
                email.getText().toString().trim().equals("")){
            //Toast.makeText(this, "Заполните все поля", Toast.LENGTH_SHORT).show();
            Toasty.error(this, "Заполните все поля!", Toast.LENGTH_SHORT, true).show();
        }
        else if(!password.getText().toString().trim().matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{5,15}$")){
            //Toast.makeText(this, "Проблемы с паролем", Toast.LENGTH_SHORT).show();
            Toasty.error(this, "Проблемы с паролем!", Toast.LENGTH_SHORT, true).show();
        }
        else if(!login.getText().toString().trim().matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{5,15}$")) {
            //Toast.makeText(this, "Проблемы с логином", Toast.LENGTH_SHORT).show();
            Toasty.error(this, "Проблемы с логином!", Toast.LENGTH_SHORT, true).show();
        }
        else if(!email.getText().toString().trim().matches("^([\\w-\\.]+){1,64}@([\\w&&[^_]]+){2,255}.[a-z]{2,}$")){
            //Toast.makeText(this, "Проблемы с email", Toast.LENGTH_SHORT).show();
            Toasty.error(this, "Проблемы с email!", Toast.LENGTH_SHORT, true).show();
        }
        //else if(!birthdate.getText().toString().trim().matches("[0-9]{1,2}(/|-)[0-9]{1,2}(/|-)[0-9]{4}"));
        else if(!birthdate.getText().toString().trim().matches("^[0-9]{1,2}(.)[0-9]{1,2}(.)[0-9]{4}$")){ //matches("^(0[1-9]|1[0-2])(.)(0[1-9]|1\\d|2\\d|3[01])(.)(19|20)\\d{2}$")){
            //Toast.makeText(this, "Проблемы с email", Toast.LENGTH_SHORT).show();
            Toasty.error(this, "Проблемы с датой рождения!", Toast.LENGTH_SHORT, true).show();
        }
        else if(!password.getText().toString().equals(password2.getText().toString())) {
            //Toast.makeText(this, "Пароли должны совпадать", Toast.LENGTH_SHORT).show();
            Toasty.error(this, "Пароли должны совпадать!", Toast.LENGTH_SHORT, true).show();
        }
        else{
            try{

                final AlertDialog dialog = new SpotsDialog.Builder()
                        .setContext(RegistrationActivity.this)
                        .build();
                dialog.show();

                int userID = UniqueID();
                int patientID = userID;
                Patient patient = new Patient(userID,
                        patientID,
                        birthdate.getText().toString(),
                        address.getText().toString(),
                        phone.getText().toString(),
                        email.getText().toString()
                        );

                User user = new User(userID,
                        fullname.getText().toString(),
                        login.getText().toString(),
                        encrypt(password.getText().toString().getBytes(), ("0123000000000215").getBytes()),
                        "Patient");

                user.Patients.add(0,patient);

                compositeDisposable.add(iapi.registerUser(user)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<String>() {
                            @Override
                            public void accept(String s) throws Exception {
                                if(!s.equals("User is existing!"))
                                {
                                    finish();
                                }
                                //Toast.makeText(RegistrationActivity.this, s, Toast.LENGTH_SHORT).show();
                                Toasty.success(RegistrationActivity.this, s.replace("\"",""), Toast.LENGTH_SHORT, true).show();
                                dialog.dismiss();
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                dialog.dismiss();
                                //Toast.makeText(RegisterPage.this, throwable.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }));


                //Toast.makeText(getApplicationContext(), "Вы успешно зарегестрированы!", Toast.LENGTH_SHORT).show();
                //Toasty.success(this, "Врач успешно удален!", Toast.LENGTH_SHORT, true).show();
            }
            catch (Exception e){
                e.printStackTrace();
            }
//            Intent intent = new Intent(this, LoginActivity.class);
//            startActivity(intent);
        }
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

    public int UniqueID(){
        //return Integer.getInteger(UUID.randomUUID().toString());
        Date now = new Date();
        int id = Integer.parseInt(new SimpleDateFormat("MMddHHmmss",  Locale.US).format(now));
        //int id = Integer.getInteger(new SimpleDateFormat("MMddHHmmss",  Locale.US).format(now));
        return id;
    }
}
