package com.example.connfirebase;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.ListView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Analysis_Sub_Final extends AppCompatActivity {

    String[] subjectNameSplit;
    ListView listView;

    PieChart pieChart;
    PieData pieData;
    List<PieEntry> pieEntryList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sub_final_analysis);
        listView = findViewById(R.id.forcast_list1);
        assert getSupportActionBar() != null;   //null check
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final ArrayList<String> resultCount = new ArrayList<>();
        final String ResultType[] = {"A+, A, A-", "B+, B, B-", "C+, C, C-", "D+, D, D-", "F", "AB, Medical"};

        final Float result_cnt[] = new Float[6];
        Arrays.fill(result_cnt, new Float(0.0));
        final String result_cnt_S[] = new String[6];

        final Float result_cnt_1[] = new Float[3];
        Arrays.fill(result_cnt_1, new Float(0.0));
        final String result_cnt_S_1[] = new String[3];

        final String subjectName = getIntent().getStringExtra("subjectName");
        final String semName = getIntent().getStringExtra("semName");
        subjectNameSplit = subjectName.split(":");

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Students");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot requestSnapshot : dataSnapshot.getChildren()) {
                    if (requestSnapshot.child("Result").child(semName).child(subjectNameSplit[0]).exists()) {
                        DataSnapshot pSnapshot = requestSnapshot.child("Result").child(semName).child(subjectNameSplit[0]);
                        String allResult = pSnapshot.child("SubResult").getValue(String.class);
                        resultCount.add(allResult);

                        Analysis_Result_Count resultCountForecast = new Analysis_Result_Count();
                        resultCountForecast.count(allResult, result_cnt);
                        resultCountForecast.count_1(allResult, result_cnt_1);
                    } else {
                        System.out.println("");
                    }
                }
                for (int i = 0; i < result_cnt.length; i++) {
                    float cnt = (result_cnt[i]) * 100 / resultCount.size();
                    String cntS = String.format("%.1f", cnt);
                    result_cnt_S[i] = cntS + "%";
                }
                for (int i = 0; i < result_cnt_1.length; i++) {
                    float cnt = (result_cnt_1[i]) * 100 / resultCount.size();
                    String cntS = String.format("%.1f", cnt);
                    result_cnt_S_1[i] = cntS + "%";
                }

                ////////////////////////////////////////////////////////////////////////////////
                pieChart = findViewById(R.id.pieChart);
                pieChart.setDrawHoleEnabled(true);
                pieChart.setUsePercentValues(true);
                pieChart.setEntryLabelTextSize(12);
                pieChart.setEntryLabelColor(Color.BLACK);
                pieChart.setCenterText(subjectNameSplit[0] + "\n" + subjectNameSplit[1]);
                pieChart.setCenterTextSize(16);
                pieChart.setCenterTextColor(Color.MAGENTA);
                pieChart.getDescription().setEnabled(false);

                Legend legend = pieChart.getLegend();
                legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
                legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
                legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
                legend.setDrawInside(false);
                legend.setEnabled(true);

                pieEntryList.add(new PieEntry(result_cnt_1[0], "Pass"));
                pieEntryList.add(new PieEntry(result_cnt_1[1], "Repeat"));
                pieEntryList.add(new PieEntry(result_cnt_1[2], "Fail"));

                PieDataSet pieDataSet = new PieDataSet(pieEntryList, "Result");
                pieDataSet.setColors(ColorTemplate.JOYFUL_COLORS);
                pieData = new PieData(pieDataSet);
                pieData.setDrawValues(true);
                pieData.setValueFormatter(new PercentFormatter(pieChart));
                pieData.setValueTextSize(16f);
                pieData.setValueTextColor(Color.BLACK);
                
                pieChart.setData(pieData);
                pieChart.invalidate();
                ////////////////////////////////////////////////////////////////////////////////
                final Analysis_Result_ListAdapter adapter = new Analysis_Result_ListAdapter(Analysis_Sub_Final.this, ResultType, result_cnt_S);
                listView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                throw databaseError.toException(); // don't ignore errors
            }
        });
    }
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}