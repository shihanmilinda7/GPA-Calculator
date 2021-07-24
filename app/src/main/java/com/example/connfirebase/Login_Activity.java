package com.example.connfirebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import es.dmoral.toasty.Toasty;

public class Login_Activity extends AppCompatActivity {
    ImageView passVis;//password visible/invisible
    private EditText email, password;
    String trimEmail;
    private FirebaseAuth auth;
    CardView login;
    TextView Register;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        login = findViewById(R.id.login);
        auth = FirebaseAuth.getInstance();//The entry point for accessing a Firebase Database.
        passVis = (ImageView) findViewById(R.id.passVis);
        Register = findViewById(R.id.Register);
        /////////////////////PASSWORD VISIBLE/INVISIBLE/////////////////////////////////////////////////////////////
        passVis.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    ////////////WHEN PRESS////////////////////////////////
                    password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    passVis.setImageResource(R.drawable.show);
                }
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    ////////////////////WHEN NOT PRESS/////////////////////////////////////////
                    password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    passVis.setImageResource(R.drawable.hide);
                }
                return true;
            }
        });

        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toasty.error(Login_Activity.this, "You cannot register\n Please contact ITU", Toast.LENGTH_SHORT).show();
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String txt_email = email.getText().toString();
                String txt_password = password.getText().toString();

                //String txt_email = "2021s00005@gmail.com";
                //String txt_password = "123456";
                if (txt_password.isEmpty()||txt_email.isEmpty()) {
                    Toasty.error(Login_Activity.this, "Email/Password cannot empty", Toast.LENGTH_SHORT).show();
                } else {
                    if (isEmailValid(txt_email)) {
                        int index = txt_email.indexOf('@');
                        trimEmail = txt_email.substring(0, index);
                        loginUser(txt_email, txt_password);
                        password.setText(null);
                    } else {

                        Toasty.error(Login_Activity.this, "Enter Valid Email", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
      /////////////////check register or not////////////////////
    private void loginUser(String email, String password) {
        /////////////////////////CHECK AUTH///////////////////////////
        auth.signInWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                Toasty.success(Login_Activity.this, "Logging Successfully", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Login_Activity.this, Profile_Activity.class);
                intent.putExtra("userName", trimEmail);
                intent.putExtra("email", email);
                startActivity(intent);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toasty.error(Login_Activity.this, "Incorrect Email/Password", Toast.LENGTH_SHORT).show();
            }
        });
    }

    boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }


}