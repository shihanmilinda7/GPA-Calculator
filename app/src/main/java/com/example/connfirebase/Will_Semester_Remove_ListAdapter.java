package com.example.connfirebase;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;



public class Will_Semester_Remove_ListAdapter extends BaseAdapter {
    Context context;
    ArrayList<String> subName;
    ArrayList<String> subCode;
    ArrayList<String> subCredit;
    ArrayList<String> subResult;
    LayoutInflater inflter;

    public Will_Semester_Remove_ListAdapter(Context applicationContext, ArrayList<String> subName, ArrayList<String> subCredit, ArrayList<String> subCode, ArrayList<String> subResult) {
        this.context = applicationContext;
        this.subName = subName;
        this.subCredit = subCredit;
        this.subCode = subCode;
        this.subResult = subResult;
        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return subName.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflter.inflate(R.layout.will_semster_remove_list, null);
        //ImageView icon = (ImageView) view.findViewById(R.id.imageView);
        TextView spCode = (TextView) view.findViewById(R.id.spCode);
        TextView spName = (TextView) view.findViewById(R.id.spName);
        TextView spCredit = (TextView) view.findViewById(R.id.spCredit);
        TextView spResult = (TextView) view.findViewById(R.id.spResult);
        spCredit.setText(subCredit.get(i));
        spName.setText(subName.get(i));
        spCode.setText(subCode.get(i));
        spResult.setText(subResult.get(i));
        return view;
    }
}