package com.employee.cinderella.cinternalemp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.employee.cinderella.cinternalemp.model.Employee;
import com.employee.cinderella.cinternalemp.utils.WSadressIP;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AddSkill extends AppCompatActivity {

    String  emp_id, dep_id ;
    LinearLayout featuresTable;
    ArrayList<CheckBox> arrayCheckBox;
    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_skill);

        //SET TOP BAR
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar16);
        mToolbar.setTitle("Request Skill");
        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //get data
        SharedPreferences preferences = AddSkill.this.getSharedPreferences(AuthActivity.MyPREFERENCES, Context.MODE_PRIVATE);
        dep_id = preferences.getString(AuthActivity.Department_id, "1");
        emp_id = preferences.getString("emp_id", "1");


        featuresTable = (LinearLayout) findViewById(R.id.my_layout_skills);
        getSkills();

        arrayCheckBox = new ArrayList<>();


        btn = findViewById(R.id.button_skill_add);



    }

    public void getSkills(){
        final String   URL =  "http://"+ WSadressIP.WSIP+":5000/cinternal/getEmployeeNONSkill";
        arrayCheckBox = new ArrayList<>();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray array = new JSONArray(response);

                            for (int i = 0; i < array.length(); i++) {
                                JSONObject product = array.getJSONObject(i);

                                CheckBox feature1 = new CheckBox(AddSkill.this);
                                feature1.setText(product.getString("skill_name"));
                                arrayCheckBox.add(feature1 );
                                featuresTable.addView(feature1 );


                            }
                            btn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Log.v("mine",  "Button is checked");
                                    final int[] xl = {0};
                                    for (int j = 0; j < arrayCheckBox.size(); j++) {

                                        if(arrayCheckBox.get(j).isChecked()){
                                            xl[0]++;

                                            Log.v("mine", arrayCheckBox.get(j).getText() + " is checked");
                                            addSkillToEmployee(arrayCheckBox.get(j).getText().toString());
                                        }
                                      /*  arrayCheckBox.get(j).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                            @Override
                                            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                                            }
                                        });*/
                                    }
                                    if(xl[0] == 0){
                                        Toast.makeText(getApplicationContext(), "Select minimum one skill", Toast.LENGTH_SHORT).show();
                                    }
                                }

                            });

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Toast.makeText(getContext(), "you have No Tasks today", Toast.LENGTH_SHORT).show();

                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Content-Type","application/x-www-form-urlencoded");
                params.put("employee_id",emp_id);
                return params;
            }
        };;
        Volley.newRequestQueue(AddSkill.this).add(stringRequest);
    }

    public void addSkillToEmployee(String skill){
        final String   URL =  "http://"+ WSadressIP.WSIP+":5000/cinternal/addEmployeeNewSkill";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        finish();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Toast.makeText(getContext(), "you have No Tasks today", Toast.LENGTH_SHORT).show();

                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Content-Type","application/x-www-form-urlencoded");
                params.put("employee_id",emp_id);
                params.put("skill",skill);
                return params;
            }
        };;
        Volley.newRequestQueue(AddSkill.this).add(stringRequest);
    }
}
