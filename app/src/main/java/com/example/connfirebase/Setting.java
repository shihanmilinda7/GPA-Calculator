package com.example.connfirebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.jetbrains.annotations.NotNull;

import es.dmoral.toasty.Toasty;

public class Setting extends AppCompatActivity {

    private EditText password1, password2, oldpassword;
    private TextView update;
    String email;
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting);
        assert getSupportActionBar() != null;   //null check
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        oldpassword = findViewById(R.id.oldpassword);
        password1 = findViewById(R.id.password1);
        password2 = findViewById(R.id.password2);

        update = findViewById(R.id.update);
        email = getIntent().getStringExtra("email");

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String txt_password = oldpassword.getText().toString();
                String txt_password1 = password1.getText().toString();
                String txt_password2 = password2.getText().toString();



                if(txt_password1.isEmpty()||txt_password2.isEmpty()||txt_password.isEmpty()){
                    Toasty.error(Setting.this, "Cannot empty", Toast.LENGTH_SHORT).show();
                }else{
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    AuthCredential credential = EmailAuthProvider.getCredential(email, txt_password);
                    if(txt_password1.equals(txt_password2)){
                        if(txt_password1.length()>5){
                            user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull @NotNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        user.updatePassword(txt_password1).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Toasty.success(Setting.this, "Updated Successfully", Toast.LENGTH_SHORT).show();
                                                } else {
                                                    Toasty.error(Setting.this, "Update Fail", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                    } else {
                                        Toasty.error(Setting.this, "Incorrect old password", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }else{
                            Toasty.error(Setting.this, "Password should grater than 5 char", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toasty.error(Setting.this, "Password not equal", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}