package com.employee.cinderella.cinternalemp.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.employee.cinderella.cinternalemp.ProjectSingle;
import com.employee.cinderella.cinternalemp.R;
import com.employee.cinderella.cinternalemp.model.Project;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

public class ProjectsAdapter extends ArrayAdapter<Project> {
    Context context;
    int resource;

    public ProjectsAdapter(@NonNull Context context, int resource, @NonNull List<Project> objects) {
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

        TextView txt_title = (TextView) v.findViewById(R.id.textView9);
        TextView txt_Start = (TextView) v.findViewById(R.id.textView10);
        final TextView txt_Status = (TextView) v.findViewById(R.id.textView12);


        txt_title.setText(getItem(position).getName());
        txt_Start.setText(getItem(position).getStart_date()+" _ "+getItem(position).getDeadline_date());


        String dayDifference="";

        try {
            String today = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            Date dateToday = formatter.parse(today);

            Date dateStart = null;
            dateStart = formatter.parse(getItem(position).getStart_date());

            Date dateDeadline = null;
            dateDeadline = formatter.parse(getItem(position).getDeadline_date());



            long difference_upcoming =  dateStart.getTime() - dateToday.getTime();


            long difference_deadline =  dateDeadline.getTime() - dateToday.getTime();



            if(getItem(position).getStatus().equals("closed")){
                //closed

                Date dateFinish = null;
                dateFinish = formatter.parse(getItem(position).getFinish_date());

                long difference = dateDeadline.getTime() - dateFinish.getTime();

                long differenceB = Math.abs(difference);
                long differenceDates = differenceB / (24 * 60 * 60 * 1000);
               // dayDifference = Long.toString(differenceDates);

                if(difference >=0){
                    dayDifference = "Closed "+differenceDates+" days before Deadline";
                    txt_Status.setText("Closed "+differenceDates+" days before Deadline");
                    txt_Status.setTextColor(ContextCompat.getColor(context, R.color.color_department));
                }else{
                    dayDifference = "Closed "+differenceDates+" days after Deadline";
                    txt_Status.setText("Closed "+differenceDates+" days after Deadline");
                    txt_Status.setTextColor(ContextCompat.getColor(context, R.color.color_task));
                }


            }else if (difference_upcoming > 0){
                //upcoming
                long difference = Math.abs(dateStart.getTime() - dateToday.getTime());
                long differenceDates = difference / (24 * 60 * 60 * 1000);

                dayDifference = "Upcoming In "+differenceDates +" days";
                txt_Status.setText("Upcoming in "+differenceDates +" days");
                txt_Status.setTextColor(ContextCompat.getColor(context, R.color.color_request));
            }else if( (difference_upcoming <= 0) && (difference_deadline >= 0) ){
                //current
                long difference = Math.abs(dateDeadline.getTime() - dateToday.getTime());
                long differenceDates = difference / (24 * 60 * 60 * 1000);

                dayDifference = "Current, deadline in "+differenceDates+" days";

                txt_Status.setText("Current, deadline in "+differenceDates+" days");
                txt_Status.setTextColor(ContextCompat.getColor(context, R.color.adoing));
            }else if( (difference_upcoming <= 0) && (difference_deadline < 0) ){
                //overdue
                long difference = Math.abs(dateDeadline.getTime() - dateToday.getTime());
                long differenceDates = difference / (24 * 60 * 60 * 1000);

                dayDifference = "Overdue, delay of "+differenceDates+" days";
                txt_Status.setText("Overdue, delay of "+differenceDates+" days");
                txt_Status.setTextColor(ContextCompat.getColor(context, R.color.color_meeting));
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }





        CardView cv = (CardView) v.findViewById(R.id.overduecard);
        String finalDayDifference = dayDifference;
        cv.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                // item clicked

                Intent intent = new Intent(getContext(), ProjectSingle.class);
                intent.putExtra("project_id", getItem(position).getId());
                intent.putExtra("project_title", getItem(position).getName());
                intent.putExtra("project_desc", getItem(position).getDescription());
                intent.putExtra("project_start", getItem(position).getStart_date());
                intent.putExtra("project_deadline", getItem(position).getDeadline_date());
                intent.putExtra("project_stat",  finalDayDifference );
                context.startActivity(intent);
                Log.v("test","%%%%%%%%%+ " +getItem(position).getId()+" // "+getItem(position).getName());
            }
        });

        return v;
    }
}
