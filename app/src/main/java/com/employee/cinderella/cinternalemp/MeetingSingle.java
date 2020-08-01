package com.employee.cinderella.cinternalemp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.employee.cinderella.cinternalemp.adapter.MeetingAttendeesAdapter;
import com.employee.cinderella.cinternalemp.adapter.TaskProjectAdapter;
import com.employee.cinderella.cinternalemp.model.Employee;
import com.employee.cinderella.cinternalemp.model.Meeting;
import com.employee.cinderella.cinternalemp.model.Task;
import com.employee.cinderella.cinternalemp.utils.UIUtils;
import com.employee.cinderella.cinternalemp.utils.WSadressIP;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MeetingSingle extends AppCompatActivity {
    String  emp_id, dep_id ;
    TextView txt_topic_day, txt_time, txt_location, txt_Present_absent, txt_agenda, txt_decision, absentReason, txt_date;
    LinearLayout confirmationBox  ;
    CardView ConfirmationHeader,ParticipatingHeader;
    EditText editTxt_absentReson, editTxt_absentReason2;
    Button btn_reasonAbsent, btnConfirmPA;
    RadioGroup radioGroup;
    ImageView absenceImage;
TextInputLayout reasonb_text_input , reasona_text_input;
    ListView lv;
    List<Employee> lstcc ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting_single);

        String what = this.getIntent().getStringExtra("what");
        //SET TOP BAR
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar2);
        mToolbar.setTitle("My Meeting");
        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();

            }
        });

        //get data
        SharedPreferences preferences = MeetingSingle.this.getSharedPreferences(AuthActivity.MyPREFERENCES, Context.MODE_PRIVATE);
        dep_id = preferences.getString(AuthActivity.Department_id, "1");
        emp_id = preferences.getString("emp_id", "1");

        //set ui
        txt_topic_day = findViewById(R.id.textViewMeetingTopic2);
        txt_time = findViewById(R.id.textViewMeetingTime2);
        txt_location = findViewById(R.id.textViewMeetingLocation2);
        txt_Present_absent = findViewById(R.id.textView21);
        txt_agenda = findViewById(R.id.textView23);
        txt_decision = findViewById(R.id.textView25);
       // txt_date = findViewById(R.id.textView95);
        confirmationBox = findViewById(R.id.confirmationBox);
        ConfirmationHeader = findViewById(R.id.ConfirmationHeader);
        absentReason = findViewById(R.id.textView26);
        editTxt_absentReson = findViewById(R.id.editText4);
        reasona_text_input = findViewById(R.id.reasona_text_input);
        btn_reasonAbsent = findViewById(R.id.button6);
        absenceImage  = findViewById(R.id.imageView26 );

        //IF PENDING CONFIRMATION
        editTxt_absentReason2 = findViewById(R.id.editText3);
        reasonb_text_input = findViewById(R.id.reasonb_text_input);
        radioGroup = findViewById(R.id.radioGroup1);
        btnConfirmPA = findViewById(R.id.button5);
        editTxt_absentReason2.setVisibility(View.GONE);
        reasonb_text_input.setVisibility(View.GONE);

        btnConfirmPA.setVisibility(View.GONE);

        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {

            RadioButton rb=(RadioButton)findViewById(checkedId);
            //textViewChoice.setText("You Selected " + rb.getText());
            //Toast.makeText(getApplicationContext(), rb.getText(), Toast.LENGTH_SHORT).show();
            if(rb.getText().equals("Present")){
                editTxt_absentReason2.setVisibility(View.GONE);
                reasonb_text_input.setVisibility(View.GONE);

                btnConfirmPA.setVisibility(View.VISIBLE);
            }else if(rb.getText().equals("Absent")){
                editTxt_absentReason2.setVisibility(View.VISIBLE);
                reasonb_text_input.setVisibility(View.VISIBLE);

                btnConfirmPA.setVisibility(View.VISIBLE);
            }
        });




        //set ui data
        txt_topic_day.setText(this.getIntent().getStringExtra("meeting_topic"));
    //   txt_date.setText(this.getIntent().getStringExtra("meeting_day"));
        txt_time.setText(this.getIntent().getStringExtra("meeting_day")+" from "+this.getIntent().getStringExtra("meeting_start_hour")+" until "+this.getIntent().getStringExtra("meeting_end_hour"));
        txt_location.setText(this.getIntent().getStringExtra("meeting_location"));
        if(this.getIntent().getStringExtra("meeting_location").isEmpty() || this.getIntent().getStringExtra("meeting_location").equals("")||this.getIntent().getStringExtra("meeting_location").equals("null")){
            txt_location.setText("Office");
        }else{
            txt_location.setText(this.getIntent().getStringExtra("meeting_location"));
        }
        if(this.getIntent().getStringExtra("meeting_agenda").isEmpty() || this.getIntent().getStringExtra("meeting_agenda").equals("null") || this.getIntent().getStringExtra("meeting_agenda").equals("")){
            txt_agenda.setText("NOT added yet.");
        }else{
            txt_agenda.setText(this.getIntent().getStringExtra("meeting_agenda"));
        }
        if(this.getIntent().getStringExtra("meeting_decision").isEmpty() || this.getIntent().getStringExtra("meeting_decision").equals("null") | this.getIntent().getStringExtra("meeting_decision").equals("")){
            txt_decision.setText("NOT added yet.");
        }else{
            txt_decision.setText(this.getIntent().getStringExtra("meeting_decision"));
        }
        setPresentAbsent(this.getIntent().getStringExtra("meeting_id"));
        String meeting_id = this.getIntent().getStringExtra("meeting_id");


        //IF PENDING CONFIRMATION & CLICK BTN CONFIRM
        btnConfirmPA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selected_id = radioGroup.getCheckedRadioButtonId();
                RadioButton Rb1 = (RadioButton) findViewById(selected_id);
                if(Rb1.getText().equals("Present")){
                    updateAttendance("p","",meeting_id);
                  //  Toast.makeText(MeetingSingle.this,"Your Attendance status is successfully clarified", Toast.LENGTH_SHORT).show();
                    finish();
                    startActivity(getIntent()); // !!!!!!!! in case make error take it off
                    overridePendingTransition(0, 0);
                }else if(Rb1.getText().equals("Absent")){
                    if(editTxt_absentReason2.getText().equals("") || editTxt_absentReason2.getText().length() == 0 ){
                        Toast.makeText(MeetingSingle.this,"please add your reason", Toast.LENGTH_SHORT).show();
                    }else{
                        updateAttendance("a",editTxt_absentReason2.getText().toString(),meeting_id);
                       // Toast.makeText(MeetingSingle.this,"Your Attendance status is successfully clarified", Toast.LENGTH_SHORT).show();
                        finish();
                        startActivity(getIntent()); // !!!!!!!! in case make error take it off
                        overridePendingTransition(0, 0);
                    }

                }

            }
        });
        //editTxt_absentReson
        btn_reasonAbsent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editTxt_absentReson.getText().equals("") || editTxt_absentReson.getText().length() == 0 ){
                    Toast.makeText(MeetingSingle.this,"please add your reason", Toast.LENGTH_SHORT).show();
                }else{
                    updateAttendance("a",editTxt_absentReson.getText().toString(),meeting_id);
                    Toast.makeText(MeetingSingle.this,"Your Attendance status is successfully clarified", Toast.LENGTH_SHORT).show();
                    finish();
                    startActivity(getIntent()); // !!!!!!!! in case make error take it off
                    overridePendingTransition(0, 0);
                }
            }
        });

        //SET attendees list
        lv =  findViewById(R.id.participating);
        lstcc = new ArrayList<>();
        setAttendeesList(meeting_id);
    }

    public void setPresentAbsent(String meeting_id){
        final String   URL =  "http://"+ WSadressIP.WSIP+":5000/cinternal/getMeetingGroupByEmp";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray array = new JSONArray(response);

                            JSONObject product = array.getJSONObject(0);

                            if(product.getString("is_present").isEmpty() || product.getString("is_present").equals("")){
                                ConfirmationHeader.setVisibility(View.VISIBLE);
                                txt_Present_absent.setText("Pending Confirmation");
                                txt_Present_absent.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.color_meeting));
                                absentReason.setText("Attendance");
                               // absentReason.setVisibility(View.GONE);
                                editTxt_absentReson.setVisibility(View.GONE);
                                btn_reasonAbsent.setVisibility(View.GONE);
                                absenceImage.setVisibility(View.GONE);

                                reasona_text_input.setVisibility(View.GONE);

                            }else{
                                if(product.getString("is_present").equals("p")){
                                    txt_Present_absent.setText("Present");
                                    txt_Present_absent.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.color_department));
                                    absentReason.setText("Attendance");
                                   // absentReason.setVisibility(View.GONE);
                                    editTxt_absentReson.setVisibility(View.GONE);
                                    btn_reasonAbsent.setVisibility(View.GONE);
                                    absenceImage.setVisibility(View.GONE);

                                    reasona_text_input.setVisibility(View.GONE);
                                }else if(product.getString("is_present").equals("a")){
                                    txt_Present_absent.setText("Absent");
                                    txt_Present_absent.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.color_meeting));
                                    if( !product.getString("note").equals("null") && !product.getString("note").isEmpty() && !product.getString("note").equals("")){

                                        //show reason of absent
                                       // absentReason.setText("Reason: "+product.getString("note"));
                                        absentReason.setText("Attendance");
                                        editTxt_absentReson.setVisibility(View.GONE);
                                        btn_reasonAbsent.setVisibility(View.GONE);
                                        absenceImage.setVisibility(View.GONE);

                                        reasona_text_input.setVisibility(View.GONE);
                                    }else{
                                        // add reason of absent
                                        absentReason.setText("Please add your Absence reason");
                                        absentReason.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.red));
                                        editTxt_absentReson.setVisibility(View.VISIBLE);
                                        btn_reasonAbsent.setVisibility(View.VISIBLE);
                                        absenceImage.setVisibility(View.VISIBLE);

                                        reasona_text_input.setVisibility(View.VISIBLE);

                                    }
                                }

                                ConfirmationHeader.setVisibility(View.GONE);
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {


                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Content-Type","application/x-www-form-urlencoded");
                params.put("meeting_id",meeting_id);
                params.put("employee_id",emp_id);
                return params;
            }
        };;
        Volley.newRequestQueue(MeetingSingle.this).add(stringRequest);
    }

    public void updateAttendance(String is_present, String note, String meeting_id){
        final String   URL =  "http://"+ WSadressIP.WSIP+":5000/cinternal/updateMeetingAttendanceByEmp";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {



                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {


                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Content-Type","application/x-www-form-urlencoded");
                params.put("meeting_id",meeting_id);
                params.put("employee_id",emp_id);
                params.put("is_present",is_present);
                params.put("note",note);
                return params;
            }
        };;
        Volley.newRequestQueue(MeetingSingle.this).add(stringRequest);
    }
    public void setAttendeesList(String meeting_id){
        final String   URL =  "http://"+ WSadressIP.WSIP+":5000/cinternal/meetingAttendeesNames";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray array = new JSONArray(response);
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject product = array.getJSONObject(i);

                                lstcc.add(new Employee(
                                        product.getString("full_name")
                                ));

                            }
                            MeetingAttendeesAdapter adapter = new MeetingAttendeesAdapter(MeetingSingle.this ,R.layout.meeting_attendees_row,lstcc );
                            adapter.notifyDataSetChanged();
                            // UIUtils.setListViewHeightBasedOnItems(lvCurrent);
                            lv.setAdapter(adapter);
                            UIUtils.setListViewHeightBasedOnItems(lv);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        ParticipatingHeader.setVisibility(View.GONE);
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Content-Type","application/x-www-form-urlencoded");
                params.put("meeting_id",meeting_id);
                return params;
            }
        };;
        Volley.newRequestQueue(MeetingSingle.this).add(stringRequest);
    }
}
