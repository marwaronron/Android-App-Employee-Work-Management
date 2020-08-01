package com.employee.cinderella.cinternalemp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.employee.cinderella.cinternalemp.adapter.ExperienceAdapter;
import com.employee.cinderella.cinternalemp.adapter.HolidaysAdapter;
import com.employee.cinderella.cinternalemp.model.ExperienceModel;
import com.employee.cinderella.cinternalemp.model.Holiday;
import com.employee.cinderella.cinternalemp.utils.WSadressIP;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExperienceActivity extends AppCompatActivity {

    String  emp_id, dep_id ;

    ListView lvExperience;
    List<ExperienceModel> lstccExperience ;

    //ListView lvSkill;
    //List<ExperienceModel> lstccSkill ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_experience);


        //SET TOP BAR
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar14);
        mToolbar.setTitle("Experience");
        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //
        FloatingActionButton btn_floatAction =
                (FloatingActionButton) findViewById(R.id.floating_action_button_skilladd);

        btn_floatAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ExperienceActivity.this, AddSkill.class);
                startActivity(intent);
               // Log.v("x","%%%%%%%%%%%%%MM");

            }
        });

        //get data
        SharedPreferences preferences = ExperienceActivity.this.getSharedPreferences(AuthActivity.MyPREFERENCES, Context.MODE_PRIVATE);
        dep_id = preferences.getString(AuthActivity.Department_id, "1");
        emp_id = preferences.getString("emp_id", "1");


        lvExperience =  findViewById(R.id.lvExperience);
        lstccExperience = new ArrayList<>();

       // lvSkill =  findViewById(R.id.lvSkill);
        //lstccSkill = new ArrayList<>();

    }

    public void setExperience(String emp_id){
        final String   URL =  "http://"+ WSadressIP.WSIP+":5000/cinternal/getEmployeeExperience";
        lstccExperience = new ArrayList<>();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray array = new JSONArray(response);
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject product = array.getJSONObject(i);
                                lstccExperience.add(new ExperienceModel(
                                        product.getString("skill_name"),
                                        product.getString("nb_tasks")
                                ));

                            }
                            ExperienceAdapter adapter = new ExperienceAdapter(ExperienceActivity.this ,R.layout.experience_row,lstccExperience );
                            adapter.notifyDataSetChanged();
                            lvExperience.setAdapter(adapter);

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
        Volley.newRequestQueue(ExperienceActivity.this).add(stringRequest);

    }

    public void setSkill(String emp_id){
        final String   URL =  "http://"+ WSadressIP.WSIP+":5000/cinternal/getEmployeeSkill";
        lstccExperience = new ArrayList<>();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray array = new JSONArray(response);
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject product = array.getJSONObject(i);
                                lstccExperience.add(new ExperienceModel(
                                        product.getString("skill"),
                                        "Not yet"
                                ));
                            }

                            ExperienceAdapter adapter = new ExperienceAdapter(ExperienceActivity.this ,R.layout.experience_row,lstccExperience);
                            adapter.notifyDataSetChanged();
                            lvExperience.setAdapter(adapter);

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
        Volley.newRequestQueue(ExperienceActivity.this).add(stringRequest);
    }

    @Override
    public void onResume() {

        super.onResume();

        setExperience(emp_id);
        setSkill(emp_id);

    }
}
