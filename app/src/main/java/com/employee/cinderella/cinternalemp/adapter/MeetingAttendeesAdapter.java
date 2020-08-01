package com.employee.cinderella.cinternalemp.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.employee.cinderella.cinternalemp.MeetingSingle;
import com.employee.cinderella.cinternalemp.R;
import com.employee.cinderella.cinternalemp.model.Employee;
import com.employee.cinderella.cinternalemp.model.Meeting;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;

public class MeetingAttendeesAdapter extends ArrayAdapter<Employee> {
    Context context;
    int resource;

    String emp_id ;

    public MeetingAttendeesAdapter(@NonNull Context context, int resource, @NonNull List<Employee> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
    }

    @RequiresApi(api = 28)
    @SuppressLint("ResourceAsColor")
    @NonNull
    @Override
    public View getView(final int position, View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        final View v = inflater.inflate(resource, parent, false);
        TextView txt_full_name = (TextView) v.findViewById(R.id.textView28);
        txt_full_name.setText(getItem(position).getFull_name());


        return v;
    }
}
