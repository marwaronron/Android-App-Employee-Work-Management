package com.employee.cinderella.cinternalemp.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
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
import com.employee.cinderella.cinternalemp.MainActivity;
import com.employee.cinderella.cinternalemp.R;
import com.employee.cinderella.cinternalemp.model.Task;
import com.employee.cinderella.cinternalemp.utils.WSadressIP;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

public class MyTasksAdapter extends ArrayAdapter<Task> {
    Context context;
    int resource;


    public MyTasksAdapter(@NonNull Context context, int resource, @NonNull List<Task> objects) {
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

        // TextView txt_title = (TextView) v.findViewById(R.id.textViewTaskTitle);
        TextView txt_type = (TextView) v.findViewById(R.id.textViewTaskType);
        TextView txt_project = (TextView) v.findViewById(R.id.textViewTaskEmployee);
        TextView txt_skill = (TextView) v.findViewById(R.id.textViewTaskSkill);

        TextView txt_Start = (TextView) v.findViewById(R.id.textViewTaskStart);
        TextView txt_Deadline = (TextView) v.findViewById(R.id.textViewTaskDeadline);

        CardView cv = v.findViewById(R.id.cardViewTaskProj);
        final LinearLayout layout = v.findViewById(R.id.taskstatuscolor2);

        CheckBox chbx = v.findViewById(R.id.checkBox2);


        SharedPreferences preferences = getContext().getSharedPreferences(AuthActivity.MyPREFERENCES, Context.MODE_PRIVATE);
        String emp_id = preferences.getString("emp_id", "1");

        txt_project.setText("Project: "+getItem(position).getProject_id());
      //  txt_project.setTextColor(ContextCompat.getColor(context, R.color.colorAccent));
        txt_project.setTypeface(txt_Deadline.getTypeface(), Typeface.BOLD);
        txt_skill.setText("Task Skill: "+getItem(position).getSkill());
        txt_Start.setText("Duration: "+getItem(position).getStart_date()+" _ ");
        txt_Deadline.setText(getItem(position).getDeadline_date());



        //txt_title.setText(getItem(position).getTitle());
        if(getItem(position).getStatus().equals("3")){
            txt_type.setText("TO DO: "+getItem(position).getTitle());
            chbx.setText("DOING ?");
            layout.setBackgroundColor(ContextCompat.getColor(context, R.color.atodo));
            txt_type.setTextColor(ContextCompat.getColor(context, R.color.atodo));
        }else if(getItem(position).getStatus().equals("2")){
            txt_type.setText("DOING: "+getItem(position).getTitle());
            chbx.setText("DONE ?");
            layout.setBackgroundColor(ContextCompat.getColor(context, R.color.adoing));
            txt_type.setTextColor(ContextCompat.getColor(context, R.color.adoing));
        } else if(getItem(position).getStatus().equals("1")){
            txt_type.setText("DONE: "+getItem(position).getTitle());
            chbx.setVisibility(v.GONE);
            layout.setBackgroundColor(ContextCompat.getColor(context, R.color.adone));
            txt_type.setTextColor(ContextCompat.getColor(context, R.color.adone));
        }

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

                    //  layout.setBackgroundColor(ContextCompat.getColor(context, R.color.red));
                }else  if(getItem(position).getStatus().equals("3")) { // 3 === TO DO

                    //
                    cv.setOutlineAmbientShadowColor(ContextCompat.getColor(context, R.color.triadica));
                    cv.setOutlineSpotShadowColor(ContextCompat.getColor(context, R.color.triadica));
                    txt_Deadline.setTextColor(ContextCompat.getColor(context, R.color.red));

                    txt_Deadline.setTextColor(ContextCompat.getColor(context, R.color.red));
                    txt_Deadline.setTypeface(txt_Deadline.getTypeface(), Typeface.BOLD);

                    // layout.setBackgroundColor(ContextCompat.getColor(context, R.color.red));
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }


        //checkbox
        chbx.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // TODO Auto-generated method stub

                if(isChecked){
                    if(chbx.getText().toString().equals("DOING ?")) {
                        updateMyTask(getItem(position).getEmployee_id(), getItem(position).getId(),"2");
                        chbx.setText("DONE ?");
                        txt_type.setText("DOING: "+getItem(position).getTitle());
                        layout.setBackgroundColor(ContextCompat.getColor(context, R.color.adoing));
                        txt_type.setTextColor(ContextCompat.getColor(context, R.color.adoing));
                    }else if (chbx.getText().toString().equals("DONE ?")) {
                        updateMyTask(getItem(position).getEmployee_id(), getItem(position).getId(),"1");
                        chbx.setVisibility(View.GONE);
                        txt_type.setText("DONE: "+getItem(position).getTitle());
                        layout.setBackgroundColor(ContextCompat.getColor(context, R.color.adone));
                        txt_type.setTextColor(ContextCompat.getColor(context, R.color.adone));
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

                        ((MainActivity)context).finish();
                        Intent intent = new Intent("com.employee.cinderella.cinternalemp.MainActivity");

                        intent.putExtra("redirectToTasks", "redirectToTasks");
                        ((MainActivity)context).startActivity(intent);
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
                params.put("task_id",task_id_string);
                params.put("employee_id",emp_id);
                params.put("finish_date",date);
                params.put("status",status);
                return params;
            }
        };;
        Volley.newRequestQueue(getContext()).add(stringRequest);
    }
}
