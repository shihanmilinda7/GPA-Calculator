package com.example.connfirebase;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import es.dmoral.toasty.Toasty;

public class Will_Semesters extends AppCompatActivity {

    ListView listView;
    String item1, FutResult;
    String creditSum, gpvSum, passedSem;
    TextView gpa, remove;
    double subjectCredit;
    float curCreditSum, curGpvSum, gpvSum1, creditSum1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.will_semster);
        assert getSupportActionBar() != null;   //null check
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        gpa = findViewById(R.id.curGpa);
        remove = findViewById(R.id.removeSub);



        final String userName = getIntent().getStringExtra("userName");
        creditSum = getIntent().getStringExtra("creditSum");
        gpvSum = getIntent().getStringExtra("gpvSum");
        curCreditSum = Float.parseFloat(creditSum);
        curGpvSum = Float.parseFloat(gpvSum);
        gpa.setText(String.format("GPA - %.2f", curGpvSum / curCreditSum));

        final ArrayList<String> doneSemester = new ArrayList<>();
        final ArrayList<String> WillDoSemster = new ArrayList<>();

        final ArrayList<String> selectedSubject = new ArrayList<>();
        final ArrayList<String> resultDetailsList = new ArrayList<>();

        final ArrayList<String> subList = new ArrayList<>();

        final ArrayList<Double> subCredit_1 = new ArrayList<>();
        final ArrayList<Double> subGpv_1 = new ArrayList<>();

        final ArrayList<String> subCode = new ArrayList<>();
        final ArrayList<String> subName = new ArrayList<>();
        final ArrayList<String> subCredit = new ArrayList<>();
        final ArrayList<String> subResult = new ArrayList<>();

        subCredit_1.add((double) curCreditSum);
        subGpv_1.add((double) curGpvSum);

        listView = findViewById(R.id.list2);

        resultDetailsList.add("A+");
        resultDetailsList.add("A");
        resultDetailsList.add("A-");
        resultDetailsList.add("B+");
        resultDetailsList.add("B");
        resultDetailsList.add("B-");
        resultDetailsList.add("C+");
        resultDetailsList.add("C");
        resultDetailsList.add("C-");
        resultDetailsList.add("D+");
        resultDetailsList.add("D");
        resultDetailsList.add("D-");
        resultDetailsList.add("F");

        /////////////////CHECK SEMESTER ALREADY DONE OR NOT START//////////////////////////////////

        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.will_semster_list, R.id.semList, WillDoSemster);

        listView.setAdapter(adapter);

        final DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference().child("Students").child(userName).child("Result");
        reference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                doneSemester.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    passedSem = snapshot.getKey().toString();
                    doneSemester.add(passedSem);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
////////////////////////////////GET SUBJECT DETAILS START/////////////////////////////////////////////
        final DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference().child("Subject");
        reference2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                WillDoSemster.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String result = snapshot.getKey().toString();
                    WillDoSemster.add(result);
                    WillDoSemster.removeAll(doneSemester);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        ////////////////////////////GET SUBJECT DETAILS END/////////////////////////////////////////////
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                String selectedItem = (String) listView.getItemAtPosition(position);
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(Will_Semesters.this, R.style.CustomAlertDialog);

                View titleView = getLayoutInflater().inflate(R.layout.will_semster_spinner_title, null);
                TextView title = titleView.findViewById(R.id.titleDialogBox);
                title.setText(selectedItem);
                alertDialog.setCustomTitle(titleView);
                View holder = View.inflate(Will_Semesters.this, R.layout.will_semster_spinner, null);
                TextView buttonAdd = holder.findViewById(R.id.btn_yes);
                alertDialog.setView(holder);
                Spinner spinner1 = (Spinner) holder.findViewById(R.id.subject);
                Spinner spinner2 = (Spinner) holder.findViewById(R.id.result);

                buttonAdd.setOnClickListener(new View.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onClick(View v) {
                        if (selectedSubject.contains(item1)) {
                            Toasty.warning(Will_Semesters.this, "Already Selected", Toast.LENGTH_SHORT).show();
                            creditSum1 = subCredit_1.stream().mapToInt(Double::intValue).sum();
                            gpvSum1 = subGpv_1.stream().mapToInt(Double::intValue).sum();

                            gpa.setText(String.format("GPA - %.2f", gpvSum1 / creditSum1));
                        } else {
                            SubjectSelection();
                        }
                    }

                    @RequiresApi(api = Build.VERSION_CODES.N)
                    public void SubjectSelection() {
                        subCredit_1.add((double) subjectCredit);
                        selectedSubject.add(item1);
                        String[] subjectNameSplit_1;
                        String[] subjectNameSplit_2;
                        subjectNameSplit_1 = item1.split("ct: ");
                        subjectNameSplit_2 = subjectNameSplit_1[1].split(":");
                        subCode.add(subjectNameSplit_2[0]);
                        subCredit.add(String.valueOf(subjectCredit));
                        subName.add(subjectNameSplit_2[1]);
                        subResult.add(FutResult);

                        Gpa_Calculation gpa_calculation = new Gpa_Calculation();
                        subGpv_1.add(gpa_calculation.calculation(FutResult, subjectCredit));


                        creditSum1 = subCredit_1.stream().mapToInt(Double::intValue).sum();
                        gpvSum1 = subGpv_1.stream().mapToInt(Double::intValue).sum();

                        gpa.setText(String.format("GPA - %.2f", gpvSum1 / creditSum1));
                        Toasty.info(Will_Semesters.this, "GPA updated", Toast.LENGTH_SHORT).show();
                    }
                });

                alertDialog.show();
                /////////////SELECT SUBJECT START/////////////////////
                final ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(Will_Semesters.this, android.R.layout.simple_spinner_item, subList);
                final ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(Will_Semesters.this, android.R.layout.simple_spinner_item, resultDetailsList);


                // Drop down layout style - list view with radio button
                adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner1.setAdapter(adapter1);

                adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner2.setAdapter(adapter2);

                spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        item1 = adapterView.getItemAtPosition(i).toString();
                        char crdt = item1.charAt(8);
                        subjectCredit = Double.parseDouble(String.valueOf(crdt));
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });

                spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        FutResult = adapterView.getItemAtPosition(i).toString();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });

                final DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Subject").child(selectedItem);
                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        subList.clear();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            String subName = snapshot.getKey().toString();
                            String credit = snapshot.getValue().toString();
                            subList.add("Credit: " + credit + " Subject: " + subName);
                        }
                        adapter1.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                ///////////////SELECT SUBJECT END//////////////////////////////
            }
        });
        //////////////////////CHECK SEMESTER ALREADY DONE OR NOT END////////////////////////////////////
        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toasty.info(Will_Semesters.this, "Selected Subjects", Toast.LENGTH_SHORT).show();
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(Will_Semesters.this, R.style.CustomAlertDialog);
                View titleView = getLayoutInflater().inflate(R.layout.will_semster_spinner_title, null);
                TextView title = titleView.findViewById(R.id.titleDialogBox);
                title.setText("Tap Courses For Remove");
                alertDialog.setCustomTitle(titleView);

                View holder = View.inflate(Will_Semesters.this, R.layout.will_semster_remove_list, null);
                Will_Semester_Remove_ListAdapter dataAdapter = new Will_Semester_Remove_ListAdapter(Will_Semesters.this, subName, subCredit, subCode, subResult);
                alertDialog.setAdapter(dataAdapter, new DialogInterface.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int removeCredit = which + 1;
                        selectedSubject.remove(which);
                        subCode.remove(which);
                        subResult.remove(which);
                        subName.remove(which);
                        subCredit.remove(which);

                        subCredit_1.remove(removeCredit);
                        subGpv_1.remove(removeCredit);
                        Toasty.error(Will_Semesters.this, "Removed", Toast.LENGTH_LONG).show();
                        float creditSum1 = subCredit_1.stream().mapToInt(Double::intValue).sum();
                        float gpvSum = subGpv_1.stream().mapToInt(Double::intValue).sum();
                        gpa.setText(String.format("GPA - %.2f", gpvSum / creditSum1));
                    }
                });
                alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //if user select "No", just cancel this dialog and continue with app
                        dialog.cancel();
                    }
                });
                AlertDialog alert = alertDialog.create();
                alert.show();
            }
        });
    }
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}