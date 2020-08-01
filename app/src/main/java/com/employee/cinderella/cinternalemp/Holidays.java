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
import android.widget.ListView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.employee.cinderella.cinternalemp.adapter.HolidaysAdapter;
import com.employee.cinderella.cinternalemp.adapter.TaskProjectAdapter;
import com.employee.cinderella.cinternalemp.model.Holiday;
import com.employee.cinderella.cinternalemp.model.Task;
import com.employee.cinderella.cinternalemp.utils.UIUtils;
import com.employee.cinderella.cinternalemp.utils.WSadressIP;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Holidays extends AppCompatActivity {

    String  emp_id, dep_id ;

    ListView lvHolidays;
    List<Holiday> lstccHolidays ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_holidays);

        //SET TOP BAR
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar3);
        mToolbar.setTitle("My Holidays");
        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //get data
        SharedPreferences preferences = Holidays.this.getSharedPreferences(AuthActivity.MyPREFERENCES, Context.MODE_PRIVATE);
        dep_id = preferences.getString(AuthActivity.Department_id, "1");
        emp_id = preferences.getString("emp_id", "1");

        lvHolidays =  findViewById(R.id.lvHolidays);
        lstccHolidays = new ArrayList<>();
        setHolidays(emp_id);



        FloatingActionButton btn_floatAction = (FloatingActionButton) findViewById(R.id.floating_action_button3);

        btn_floatAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Holidays.this,AddHoliday.class);
               // startActivity(intent);
                startActivityForResult(intent, 1);
            }
        });
    }
    public void setHolidays(String emp_id){
        final String   URL =  "http://"+ WSadressIP.WSIP+":5000/cinternal/getHolidaysByEmployee";
        lstccHolidays = new ArrayList<>();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray array = new JSONArray(response);
                            //txtTodo.setText("This Project have: "+array.length()+" Tasks" );

                            for (int i = 0; i < array.length(); i++) {
                                JSONObject product = array.getJSONObject(i);

                                lstccHolidays.add(new Holiday(
                                        product.getInt("id"),
                                        product.getString("type"),
                                        product.getString("start_date"),
                                        product.getString("end_date"),
                                        product.getString("status"),
                                        product.getString("reason"),
                                        product.getString("employee_id")
                                ));

                            }
                            HolidaysAdapter adapter = new HolidaysAdapter(Holidays.this ,R.layout.holiday_row,lstccHolidays );
                            adapter.notifyDataSetChanged();
                            // UIUtils.setListViewHeightBasedOnItems(lvCurrent);
                            lvHolidays.setAdapter(adapter);
                            //UIUtils.setListViewHeightBasedOnItems(lvHolidays);

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
        Volley.newRequestQueue(Holidays.this).add(stringRequest);
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if(resultCode == RESULT_OK) {
                String strEditText = data.getStringExtra("key");

                setHolidays(emp_id);
            }
        }
    }
}
