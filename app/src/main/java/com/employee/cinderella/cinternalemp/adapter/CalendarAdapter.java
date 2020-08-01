package com.employee.cinderella.cinternalemp.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.employee.cinderella.cinternalemp.ProjectSingle;
import com.employee.cinderella.cinternalemp.R;
import com.employee.cinderella.cinternalemp.model.CalendarModel;
import com.employee.cinderella.cinternalemp.model.Project;
import com.employee.cinderella.cinternalemp.model.RequestModel;
import com.employee.cinderella.cinternalemp.utils.UIUtils;
import com.employee.cinderella.cinternalemp.utils.WSadressIP;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

public class CalendarAdapter extends ArrayAdapter<CalendarModel> {
    Context context;
    int resource;

    public CalendarAdapter(@NonNull Context context, int resource, @NonNull List<CalendarModel> objects) {
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

        Log.d("CAL", ">>>>>>>>>  OK"+getItem(position).getTitle());


        TextView txt_title = (TextView) v.findViewById(R.id.textView61);
        TextView txt_times = (TextView) v.findViewById(R.id.textView62);
        TextView txt_status = (TextView) v.findViewById(R.id.textView63);
        LinearLayout layout = v.findViewById(R.id.calendarstatuscolor);
        CardView card = v.findViewById(R.id.cardViewCalendar);


        txt_title.setText(getItem(position).getTitle());
        txt_times.setText("From: "+getItem(position).getStart()+" To: "+getItem(position).getEnd());
        txt_status.setText(getItem(position).getStatus());

        if(getItem(position).getType().equals("project")){
            layout.setBackgroundColor(ContextCompat.getColor(context, R.color.orange));
        }else if(getItem(position).getType().equals("task")){
            layout.setBackgroundColor(ContextCompat.getColor(context, R.color.otherbleu));
           /* if(getItem(position).getStatus().equals("1")){
                txt_status.setText("DONE");
            }else if(getItem(position).getStatus().equals("2")){
                txt_status.setText("DOING");
            }else if(getItem(position).getStatus().equals("3")){
                txt_status.setText("TO DO");
            }*/

        }else if(getItem(position).getType().equals("meeting")){
            layout.setBackgroundColor(ContextCompat.getColor(context, R.color.othergreen));
        }else if(getItem(position).getType().equals("holiday")){
            layout.setBackgroundColor(ContextCompat.getColor(context, R.color.darkbleu));
        }

     /*   card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getItem(position).getType().equals("project")){

                }
            }
        });*/

        return v;
    }


}
