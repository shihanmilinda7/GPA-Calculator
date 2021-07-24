package com.example.connfirebase;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class Done_Semesters_Result_ListAdapter extends ArrayAdapter<String> {
    private final Activity context;
    ArrayList<String> subCode;
    ArrayList<String> subName;
    ArrayList<String> subResult;

    public Done_Semesters_Result_ListAdapter(Activity context, ArrayList<String> subCode, ArrayList<String> subName, ArrayList<String> subResult) {
        super(context, R.layout.done_sub_result_list, subCode);
        this.context = context;
        this.subCode = subCode;
        this.subName = subName;
        this.subResult = subResult;
    }

    public View getView(int position, View view, ViewGroup parent) {

        LayoutInflater inflater = context.getLayoutInflater();

        View rowView = inflater.inflate(R.layout.done_sub_result_list, null, true);
        TextView sCode = (TextView) rowView.findViewById(R.id.sCode);
        TextView sName = (TextView) rowView.findViewById(R.id.sName);
        TextView sResult = (TextView) rowView.findViewById(R.id.sResult);

        sCode.setText(subCode.get(position));
        sName.setText(subName.get(position));
        sResult.setText(subResult.get(position));
        return rowView;
    }
}


