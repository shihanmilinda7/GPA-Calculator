package com.example.connfirebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Analysis_Sub_Details extends AppCompatActivity {
    ListView listView;
    String[] subjectNameSplit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sub_details_analysis);
        listView = findViewById(R.id.forcastList);
        assert getSupportActionBar() != null;   //null check
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final ArrayList<String> subList = new ArrayList<>();
        final ArrayList<String> subjectCode = new ArrayList<>();
        final ArrayList<String> subjectName = new ArrayList<>();
        final String semName = getIntent().getStringExtra("semForcast");

        final Analysis_Sub_ListAdapter adapter = new Analysis_Sub_ListAdapter(
                Analysis_Sub_Details.this, subjectCode, subjectName);


        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Subject").child(semName);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                subjectCode.clear();
                subjectName.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String subName = snapshot.getKey().toString();
                    subjectNameSplit = subName.split(":");
                    subjectCode.add(subjectNameSplit[0]);
                    subjectName.add(subjectNameSplit[1]);
                    subList.add(subName);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Toast.makeText(Analysis_Sub_Details.this, "position", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Analysis_Sub_Details.this, Analysis_Sub_Final.class);
                String subjectName = subList.get(+position);
                intent.putExtra("subjectName", subjectName);
                intent.putExtra("semName", semName);
                startActivity(intent);
            }
        });
    }
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}