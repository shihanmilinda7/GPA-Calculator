package com.example.connfirebase;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class Analysis_Sub_ListAdapter extends ArrayAdapter<String> {
    private final Activity context;
    ArrayList<String> subCode;
    ArrayList<String> subName;


    public Analysis_Sub_ListAdapter(Activity context, ArrayList<String> subCode, ArrayList<String> subName) {
        super(context, R.layout.sub_details_analysist_list, subCode);
        this.context = context;
        this.subCode = subCode;
        this.subName = subName;
    }

    public View getView(int position, View view, ViewGroup parent) {

        LayoutInflater inflater = context.getLayoutInflater();

        View rowView = inflater.inflate(R.layout.sub_details_analysist_list, null, true);
        TextView sCode = (TextView) rowView.findViewById(R.id.subjectCode);
        TextView sName = (TextView) rowView.findViewById(R.id.subjectName);

        sCode.setText(subCode.get(position));
        sName.setText(subName.get(position));
        return rowView;
    }
}


