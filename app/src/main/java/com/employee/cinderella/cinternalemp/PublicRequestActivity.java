package com.employee.cinderella.cinternalemp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.employee.cinderella.cinternalemp.adapter.RequestAdapter;
import com.employee.cinderella.cinternalemp.adapter.RequestPublicAdapter;
import com.employee.cinderella.cinternalemp.model.Meeting;
import com.employee.cinderella.cinternalemp.model.RequestModel;
import com.employee.cinderella.cinternalemp.model.Task;
import com.employee.cinderella.cinternalemp.utils.WSadressIP;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.view.View.VISIBLE;

public class PublicRequestActivity extends AppCompatActivity {


    ListView lvReqPending;
    List<RequestModel> lstccReqPending;


    String  emp_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_public_request);


        //SET TOP BAR
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar8);
        mToolbar.setTitle("Public Requests");
        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //SET UI
        lvReqPending =  findViewById(R.id.lv_public_requests_pending);


        //get data
        SharedPreferences preferences = PublicRequestActivity.this.getSharedPreferences(AuthActivity.MyPREFERENCES, Context.MODE_PRIVATE);
        emp_id = preferences.getString("emp_id", "1");

        //SET list views
        lstccReqPending = new ArrayList<>();
        loadReqPending();



    }


    public void loadReqPending(){
        final String   URL =  "http://"+ WSadressIP.WSIP+":5000/cinternal/getPublicRequestsByOthers";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONArray array = new JSONArray(response);
                            for (int i = 0; i < array.length(); i++) {

                                JSONObject question = array.getJSONObject(i);

                                lstccReqPending.add(new RequestModel(
                                        question.getInt("id"),
                                        question.getString("type"),
                                        question.getString("title"),
                                        question.getString("description"),
                                        question.getString("full_name"),
                                        question.getString("status"),
                                        question.getString("date"),
                                        question.getString("privacy")
                                ));

                            }

                            RequestPublicAdapter adapter = new RequestPublicAdapter(PublicRequestActivity.this ,R.layout.request_public_others_row,lstccReqPending );
                            adapter.notifyDataSetChanged();
                            lvReqPending.setAdapter(adapter);


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
                params.put("emp_id",emp_id);
                return params;
            }
        };;
        Volley.newRequestQueue(PublicRequestActivity.this).add(stringRequest);
    }

}
