package com.employee.cinderella.cinternalemp.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.employee.cinderella.cinternalemp.MeetingSingle;
import com.employee.cinderella.cinternalemp.R;
import com.employee.cinderella.cinternalemp.model.Meeting;
import com.employee.cinderella.cinternalemp.model.Task;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

public class TodayMeetingAdapter extends ArrayAdapter<Meeting> {
    Context context;
    int resource;

    String emp_id ;
    String what;

    public TodayMeetingAdapter(@NonNull Context context, int resource, @NonNull List<Meeting> objects, String what) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.what = what;


    }

    @RequiresApi(api = 28)
    @SuppressLint("ResourceAsColor")
    @NonNull
    @Override
    public View getView(final int position, View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        final View v = inflater.inflate(resource, parent, false);

        TextView txt_title = v.findViewById(R.id.textView);
        TextView txt_location = v.findViewById(R.id.textView5);
        TextView txt_Start = v.findViewById(R.id.textView7);
        TextView txt_Deadline = v.findViewById(R.id.textView8);
        CheckBox checkBox = v.findViewById(R.id.checkBox);
        CardView cardImage = v.findViewById(R.id.cardImgTask);
        CardView cv = v.findViewById(R.id.cardViewTaskToday);
       // ImageView img = v.findViewById(R.id.imageView35);


        checkBox.setVisibility(View.GONE);
        cardImage.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimaryLight));
        txt_title.setTextColor(ContextCompat.getColor(context, R.color.colorPrimaryLight));
        //img.setImageResource(R.drawable.meeting2);

        txt_title.setText("MEETING: " +getItem(position).getTopic());
        if(getItem(position).getLocation().isEmpty() || getItem(position).getLocation().equals("")||getItem(position).getLocation().equals("null")){
            txt_location.setText("Location: Office");
        }else{
            txt_location.setText("Location: " +getItem(position).getLocation());
        }

        txt_Start.setText("Time: "+getItem(position).getStart_hour()+" _ ");
        txt_Deadline.setText(getItem(position).getEnd_hour());

        cv.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                // item clicked
                Intent intent = new Intent(getContext(), MeetingSingle.class);
                intent.putExtra("meeting_id", String.valueOf(getItem(position).getId())); //IMPORTANT
                intent.putExtra("meeting_day", getItem(position).getDay());
                intent.putExtra("meeting_start_hour", getItem(position).getStart_hour());
                intent.putExtra("meeting_end_hour", getItem(position).getEnd_hour());
                intent.putExtra("meeting_topic", getItem(position).getTopic());
                intent.putExtra("meeting_agenda", getItem(position).getAgenda());
                intent.putExtra("meeting_decision", getItem(position).getDecision());
                intent.putExtra("meeting_location", getItem(position).getLocation());

                intent.putExtra("what", what);
                context.startActivity(intent);

            }
        });

        return v;
    }
}
