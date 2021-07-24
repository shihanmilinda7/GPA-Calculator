package com.example.connfirebase;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class Analysis_Result_ListAdapter extends ArrayAdapter<String> {
    private final Activity context;
    private final String[] ResultType;
    private final String[] Percentage;

    public Analysis_Result_ListAdapter(Activity context, String[] ResultType, String[] Percentage) {
        super(context, R.layout.sub_final_analysis_list, ResultType);
        this.context = context;
        this.ResultType = ResultType;
        this.Percentage = Percentage;
    }

    public View getView(int position, View view, ViewGroup parent) {

        LayoutInflater inflater = context.getLayoutInflater();

        View rowView = inflater.inflate(R.layout.sub_final_analysis_list, null, true);
        TextView resultType = (TextView) rowView.findViewById(R.id.ResultType);
        TextView percentage = (TextView) rowView.findViewById(R.id.Prasantage);

        resultType.setText(ResultType[position]);
        percentage.setText(Percentage[position]);
        return rowView;
    }
}


