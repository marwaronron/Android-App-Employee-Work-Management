package com.employee.cinderella.cinternalemp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.employee.cinderella.cinternalemp.adapter.ProfilesAdapter;
import com.employee.cinderella.cinternalemp.model.Employee;

import com.employee.cinderella.cinternalemp.utils.UIUtils;
import com.employee.cinderella.cinternalemp.utils.WSadressIP;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CinderellaProfilesActivity extends AppCompatActivity {

    Button goToChat;
    ListView lvProfiles;
    List<Employee> lstccProfiles ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cinderella_profiles);

        //SET TOP BAR
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar6);
        mToolbar.setTitle("Cinderella Chat");
        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CinderellaProfilesActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });


        goToChat = findViewById(R.id.button13);
        goToChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CinderellaProfilesActivity.this, ChattingActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
            }
        });



        // Set list view
        lvProfiles =  findViewById(R.id.lvProfiles);
        lstccProfiles = new ArrayList<>();
        setProfiles();

    }

    public void setProfiles(){
        final String   URL =  "http://"+ WSadressIP.WSIP+":5000/cinternal/getAllActiveEmployees";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray array = new JSONArray(response);



                            for (int i = 0; i < array.length(); i++) {
                                JSONObject product = array.getJSONObject(i);

                                lstccProfiles.add(new Employee(
                                        product.getInt("id"),
                                        product.getString("full_name"),
                                        product.getString("position"),
                                        product.getString("department_name")

                                ));

                            }
                            ProfilesAdapter adapter = new ProfilesAdapter(CinderellaProfilesActivity.this ,R.layout.profiles_employee_row,lstccProfiles );
                            adapter.notifyDataSetChanged();
                            // UIUtils.setListViewHeightBasedOnItems(lvCurrent);
                            lvProfiles.setAdapter(adapter);
                            UIUtils.setListViewHeightBasedOnItems(lvProfiles);

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
                return params;
            }
        };;
        Volley.newRequestQueue(CinderellaProfilesActivity.this).add(stringRequest);
    }
}
