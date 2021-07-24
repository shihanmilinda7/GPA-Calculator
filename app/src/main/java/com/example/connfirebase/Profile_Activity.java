package com.example.connfirebase;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Profile_Activity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public DrawerLayout drawerLayout;
    public ActionBarDrawerToggle actionBarDrawerToggle;
    public NavigationView navigationView;
    String userName, email;
    private ImageButton resultBtn, gpaBtn;
    private TextView f_name, reg_no, index_no, phone_no, email_1, gpa;
    ImageView first, second, third;
    TextView studentName, studentRegNo,firstP,secondP,thirdP;
    float curGpaVal, creditSum, gpvSum;

    final ArrayList<Double> subCredit = new ArrayList<>();
    final ArrayList<Double> subGpv = new ArrayList<>();

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_activity);
        setNavigationViewListener();

        drawerLayout = findViewById(R.id.my_drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);

        View view = navigationView.getHeaderView(0);
        studentName = view.findViewById(R.id.studentName);
        studentRegNo = view.findViewById(R.id.studentRegNo);

        ////////Progress Bar/////////////////////////
        firstP = findViewById(R.id.p_Bar_1_Pre);
        secondP = findViewById(R.id.p_Bar_2_Pre);
        thirdP = findViewById(R.id.p_Bar_3_Pre);

        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close);
        // pass the Open and Close toggle for the drawer layout listene to toggle the button
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);// to make the Navigation drawer icon always appear on the action bar

        gpaBtn = findViewById(R.id.gpa);
        resultBtn = findViewById(R.id.result);

        gpa = findViewById(R.id.curGpa);
        f_name = findViewById(R.id.name);
        reg_no = findViewById(R.id.regno);
        index_no = findViewById(R.id.indexno);
        phone_no = findViewById(R.id.phoneno);
        email_1 = findViewById(R.id.email1);

        final ProgressBar progressBar1 = (ProgressBar) findViewById(R.id.p_Bar_1);
        final ProgressBar progressBar2 = (ProgressBar) findViewById(R.id.p_Bar_2);
        final ProgressBar progressBar3 = (ProgressBar) findViewById(R.id.p_Bar_3);

        progressBar1.setVisibility(View.VISIBLE);
        progressBar2.setVisibility(View.VISIBLE);
        progressBar3.setVisibility(View.VISIBLE);

        first = (ImageView) findViewById(R.id.first);
        second = (ImageView) findViewById(R.id.second);
        third = (ImageView) findViewById(R.id.third);

        first.setImageResource(R.drawable.gold);
        second.setImageResource(R.drawable.silver);
        third.setImageResource(R.drawable.browns);

        userName = getIntent().getStringExtra("userName");
        email = getIntent().getStringExtra("email");
        gpaBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Profile_Activity.this, Will_Semesters.class);
                intent.putExtra("creditSum", String.valueOf(creditSum));
                intent.putExtra("gpvSum", String.valueOf(gpvSum));
                intent.putExtra("userName", userName);
                startActivity(intent);
            }
        });

        resultBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Profile_Activity.this, Done_Semesters.class);
                intent.putExtra("userName", userName);
                startActivity(intent);
            }
        });
        ///////////////////////////Retrive firebase data//////////////////////////
        final DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference().child("Students").child(userName).child("Result");
        reference1.addChildEventListener(new ChildEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String result = snapshot.child("SubResult").getValue().toString();
                    String credit = snapshot.child("SubCredit").getValue().toString();
                    double creditInt = Double.parseDouble(credit);
                    subCredit.add(creditInt);

                    Gpa_Calculation gpa_calculation = new Gpa_Calculation();

                    subGpv.add(gpa_calculation.calculation(result, creditInt));
                }

                creditSum = subCredit.stream().mapToInt(Double::intValue).sum();//get credit sum
                gpvSum = subGpv.stream().mapToInt(Double::intValue).sum();//get gpv * credit sum
                curGpaVal = gpvSum / creditSum;

                int proGpaFirst = (int) ((curGpaVal / 3.7) * 100);
                int proGpaSecond = (int) ((curGpaVal / 3.3) * 100);
                int proGpaThird = (int) ((curGpaVal / 3.0) * 100);
                progressBar1.setProgress(proGpaFirst);
                progressBar2.setProgress(proGpaSecond);
                progressBar3.setProgress(proGpaThird);

                if(proGpaFirst>100){
                    firstP.setText("100%");
                }else{
                    firstP.setText(String.valueOf(proGpaFirst)+"%");
                }
                if(proGpaSecond>100){
                    secondP.setText("100%");
                }else{
                    secondP.setText(String.valueOf(proGpaSecond)+"%");
                }
                if(proGpaThird>100){
                    thirdP.setText("100%");
                }else{
                    thirdP.setText(String.valueOf(proGpaThird)+"%");
                }
                gpa.setText("GPA - " + String.format("%.2f", curGpaVal));
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference reference2 = database.getReference().child("Students").child(userName);

        reference2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Get_User_Details getUserDetails = dataSnapshot.getValue(Get_User_Details.class);
                findViewById(R.id.loadingPanel).setVisibility(View.GONE);

                f_name.setText(getUserDetails.getName());
                studentName.setText(getUserDetails.getName());
                reg_no.setText(getUserDetails.getReg());
                studentRegNo.setText(getUserDetails.getReg());
                index_no.setText(getUserDetails.getIndex());
                phone_no.setText(getUserDetails.getPhone());
                email_1.setText(getUserDetails.getEmail());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setNavigationViewListener() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }
/////////////////////////set navigation action//////////////////////////////////////////////////////
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.home_menu:
                Toast.makeText(this, "home_menu", Toast.LENGTH_SHORT).show();
                break;
            case R.id.forecasting_menu:
                Intent intent = new Intent(Profile_Activity.this, Analysis_Sem_Details.class);
                startActivity(intent);
                break;
            case R.id.results_menu:
                Intent intent1 = new Intent(Profile_Activity.this, Done_Semesters.class);
                intent1.putExtra("userName", userName);
                startActivity(intent1);
                break;
            case R.id.settings_menu:
                Intent intent2 = new Intent(Profile_Activity.this, Setting.class);
                intent2.putExtra("email", email);
                startActivity(intent2);
                break;
            case R.id.about_menu:
                Intent intent3 = new Intent(Profile_Activity.this, About.class);
                startActivity(intent3);
                break;
            case R.id.logout:
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(this, R.style.CustomAlertDialog);
                alertDialog.setCancelable(false);
                alertDialog.setMessage("Are You Sure?");
                View alertDialogTitle = getLayoutInflater().inflate(R.layout.logout_title, null);
                alertDialog.setCustomTitle(alertDialogTitle);
                alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //if user pressed "yes", then he is allowed to exit from application
                        finish();
                    }
                });
                alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //if user select "No", just cancel this dialog and continue with app
                        dialog.cancel();
                    }
                });
                AlertDialog alert = alertDialog.create();
                alert.show();
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}