package com.example.connfirebase;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Done_Semesters_Result extends AppCompatActivity {
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.done_sub_result);
        assert getSupportActionBar() != null;   //null check
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        listView = findViewById(R.id.result_list);

        final ArrayList<String> subCode = new ArrayList<>();
        final ArrayList<String> subName = new ArrayList<>();
        final ArrayList<String> subResult = new ArrayList<>();

        final Done_Semesters_Result_ListAdapter adapter = new Done_Semesters_Result_ListAdapter(Done_Semesters_Result.this, subCode, subName, subResult);
        listView.setAdapter(adapter);

        final String userName = getIntent().getStringExtra("userName");
        final String semester = getIntent().getStringExtra("semester");

        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Students").child(userName).child("Result").child(semester);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                subResult.clear();
                subCode.clear();
                subName.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    subCode.add(snapshot.getKey().toString());
                    subName.add(snapshot.child("SubName").getValue().toString());
                    subResult.add(snapshot.child("SubResult").getValue().toString());
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}