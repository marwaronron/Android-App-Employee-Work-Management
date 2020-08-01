package com.employee.cinderella.cinternalemp.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;


import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.employee.cinderella.cinternalemp.AuthActivity;

import com.employee.cinderella.cinternalemp.model.Task;
import com.employee.cinderella.cinternalemp.utils.WSadressIP;

import com.employee.cinderella.cinternalemp.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;



public class TodayTaskAdapter extends ArrayAdapter<Task> {
     Context context;
    int resource;

    String emp_id ;

    public TodayTaskAdapter(@NonNull Context context, int resource, @NonNull List<Task> objects) {
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

        TextView txt_title = v.findViewById(R.id.textView);
        TextView txt_project = v.findViewById(R.id.textView5);
        TextView txt_Start = v.findViewById(R.id.textView7);
        TextView txt_Deadline = v.findViewById(R.id.textView8);
        CheckBox checkBox = v.findViewById(R.id.checkBox);
        CardView cardImage = v.findViewById(R.id.cardImgTask);
        CardView cv = v.findViewById(R.id.cardViewTaskToday);
       // ImageView img = v.findViewById(R.id.imageView35);




        if(getItem(position).getStatus().equals("3")){
            txt_title.setText("TO DO: "+getItem(position).getTitle());
            checkBox.setText("DOING ?");
            cardImage.setBackgroundColor(ContextCompat.getColor(context, R.color.atodo));
            txt_title.setTextColor(ContextCompat.getColor(context, R.color.atodo));
          //  img.setImageResource(R.drawable.tasknotdone);
        }else if(getItem(position).getStatus().equals("2")){
            txt_title.setText("DOING: "+getItem(position).getTitle());
            checkBox.setText("DONE ?");
            cardImage.setBackgroundColor(ContextCompat.getColor(context, R.color.adoing));
            txt_title.setTextColor(ContextCompat.getColor(context, R.color.adoing));
            //img.setImageResource(R.drawable.tasknotdone);
        } else if(getItem(position).getStatus().equals("1")){
            txt_title.setText("DONE: "+getItem(position).getTitle());
            checkBox.setVisibility(v.GONE);
            cardImage.setBackgroundColor(ContextCompat.getColor(context, R.color.adone));
            txt_title.setTextColor(ContextCompat.getColor(context, R.color.adone));
          //  img.setImageResource(R.drawable.task);
        }

        txt_project.setText("Project: "+getItem(position).getProject_id());
        txt_Start.setText("Duration: "+getItem(position).getStart_date()+" _ ");
        txt_Deadline.setText(getItem(position).getDeadline_date());


        try {
            String today = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            Date dateDeadline = formatter.parse(getItem(position).getDeadline_date());
            Date dateToday = formatter.parse(today);
            if(dateDeadline.compareTo(dateToday) <= 0){
                if(getItem(position).getStatus().equals("2")){ // 2 === DOING
                    cv.setOutlineAmbientShadowColor(ContextCompat.getColor(context, R.color.triadica));
                    cv.setOutlineSpotShadowColor(ContextCompat.getColor(context, R.color.triadica));
                    txt_Deadline.setTextColor(ContextCompat.getColor(context, R.color.red));
                    txt_Deadline.setTextColor(ContextCompat.getColor(context, R.color.red));
                    txt_Deadline.setTypeface(txt_Deadline.getTypeface(), Typeface.BOLD);
                }else  if(getItem(position).getStatus().equals("3")) { // 3 === TO DO
                    cv.setOutlineAmbientShadowColor(ContextCompat.getColor(context, R.color.triadica));
                    cv.setOutlineSpotShadowColor(ContextCompat.getColor(context, R.color.triadica));
                    txt_Deadline.setTextColor(ContextCompat.getColor(context, R.color.red));
                    txt_Deadline.setTextColor(ContextCompat.getColor(context, R.color.red));
                    txt_Deadline.setTypeface(txt_Deadline.getTypeface(), Typeface.BOLD);
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        //checkbox
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // TODO Auto-generated method stub

                if(isChecked){
                    if(checkBox.getText().toString().equals("DOING ?")) {
                        updateMyTask(getItem(position).getEmployee_id(), getItem(position).getId(),"2");
                        checkBox.setText("DONE ?");
                        txt_title.setText("DOING: "+getItem(position).getTitle());
                        cardImage.setBackgroundColor(ContextCompat.getColor(context, R.color.adoing));
                        txt_title.setTextColor(ContextCompat.getColor(context, R.color.adoing));
                      //  img.setImageResource(R.drawable.tasknotdone);
                    }else if (checkBox.getText().toString().equals("DONE ?")) {
                        updateMyTask(getItem(position).getEmployee_id(), getItem(position).getId(),"1");
                        checkBox.setVisibility(View.GONE);
                        txt_title.setText("DONE: "+getItem(position).getTitle());
                        cardImage.setBackgroundColor(ContextCompat.getColor(context, R.color.adone));
                        txt_title.setTextColor(ContextCompat.getColor(context, R.color.adone));
                       // img.setImageResource(R.drawable.task);
                    }
                }
            }
        });
        return v;
    }

  private void updateMyTask(final String emp_id, final int task_id , final String status){
       final String task_id_string = Integer.toString(task_id);
       final String   URL =  "http://"+ WSadressIP.WSIP+":5000/cinternal/EmployeeUpdateTaskSelf";

       StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
               new Response.Listener<String>() {
                   @Override
                   public void onResponse(String response) {


                   }
               },
               new Response.ErrorListener() {
                   @Override
                   public void onErrorResponse(VolleyError error) {

                       Log.d("info","************ NOT FOUND");
                   }
               }){
           @Override
           protected Map<String, String> getParams() throws AuthFailureError {
               Map<String, String> params = new HashMap<>();
               params.put("Content-Type","application/x-www-form-urlencoded");
               String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
               params.put("employee_id",emp_id);
               params.put("finish_date",date);
               params.put("status",status);
               params.put("task_id",task_id_string);

               return params;
           }
       };;
       Volley.newRequestQueue(getContext()).add(stringRequest);
  }


}
