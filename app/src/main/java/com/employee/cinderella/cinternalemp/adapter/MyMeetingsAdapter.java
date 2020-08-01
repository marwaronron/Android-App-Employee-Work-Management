package com.employee.cinderella.cinternalemp.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.employee.cinderella.cinternalemp.AuthActivity;
import com.employee.cinderella.cinternalemp.MeetingSingle;
import com.employee.cinderella.cinternalemp.ProjectSingle;
import com.employee.cinderella.cinternalemp.R;
import com.employee.cinderella.cinternalemp.model.Meeting;
import com.employee.cinderella.cinternalemp.model.Task;
import com.employee.cinderella.cinternalemp.utils.WSadressIP;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

public class MyMeetingsAdapter extends ArrayAdapter<Meeting> {
    Context context;
    int resource;
    String what;

    String emp_id ;

    public MyMeetingsAdapter(@NonNull Context context, int resource, @NonNull List<Meeting> objects, String what) {
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

        TextView txt_topic = (TextView) v.findViewById(R.id.textViewMeetingTopic);
        TextView txt_time = (TextView) v.findViewById(R.id.textViewMeetingStart);
        TextView txt_location = (TextView) v.findViewById(R.id.textViewMeetingLocation);
        TextView txt_attendance = v.findViewById(R.id.textView56);
        TextView txt_date = v.findViewById(R.id.textView91);

        txt_topic.setText( getItem(position).getTopic());
        txt_date.setText( getItem(position).getDay());
        txt_time.setText(getItem(position).getStart_hour()+" _ "+getItem(position).getEnd_hour());

        if(getItem(position).getLocation().isEmpty() || getItem(position).getLocation().equals("")||getItem(position).getLocation().equals("null")){
            txt_location.setText("Office");
        }else{
            txt_location.setText(getItem(position).getLocation());
        }



        if(getItem(position).getIs_present().isEmpty() || getItem(position).getIs_present().equals("")){
            txt_attendance.setText("Pending Confirmation");
            txt_attendance.setTextColor(ContextCompat.getColor(getContext(), R.color.color_meeting));
        }else if(getItem(position).getIs_present().equals("p")){
            txt_attendance.setText("Present");
        }else{
            txt_attendance.setText("Absent");
        }



        CardView cv = (CardView) v.findViewById(R.id.cardViewMeetingToday);


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

        SharedPreferences preferences = getContext().getSharedPreferences(AuthActivity.MyPREFERENCES, Context.MODE_PRIVATE);
        emp_id = preferences.getString("emp_id", "1");

        return v;
    }


}
