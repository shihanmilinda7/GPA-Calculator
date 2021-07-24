package com.example.connfirebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import es.dmoral.toasty.Toasty;

public class Analysis_Sem_Details extends AppCompatActivity {

    String semForcast;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sem_details_analysis);
        Spinner spinner1 = (Spinner) findViewById(R.id.semester);
        assert getSupportActionBar() != null;   //null check
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final ArrayList<String> semList = new ArrayList<>();
        final ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(Analysis_Sem_Details.this, android.R.layout.simple_spinner_item, semList);

        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adapter1);
        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Subject");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                semList.clear();
                semList.add("Choose a Semester");
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String semName = snapshot.getKey().toString();
                    semList.add(semName);
                }
                adapter1.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        ////////////////////////////////////////
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i > 0) {
                    semForcast = adapterView.getItemAtPosition(i).toString();
                    Toasty.success(adapterView.getContext(), semForcast, Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(Analysis_Sem_Details.this, Analysis_Sub_Details.class);
                    intent.putExtra("semForcast", semForcast);
                    startActivity(intent);
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}