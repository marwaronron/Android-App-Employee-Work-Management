package com.employee.cinderella.cinternalemp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.employee.cinderella.cinternalemp.adapter.NotifsAdapter;
import com.employee.cinderella.cinternalemp.adapter.TodayTaskAdapter;
import com.employee.cinderella.cinternalemp.model.NotifisModel;
import com.employee.cinderella.cinternalemp.model.Task;
import com.employee.cinderella.cinternalemp.utils.UIUtils;
import com.employee.cinderella.cinternalemp.utils.WSadressIP;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class NotifsActivity extends AppCompatActivity {

    String  emp_id, dep_id ;

    ListView lv;
    List<NotifisModel> lstcc;

    ImageView img_no_notifs;
    TextView txt_no_notifs;
    RelativeLayout boxRelativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifs);

        //SET TOP BAR
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar15);
        mToolbar.setTitle("Notifications");
        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //get data
        SharedPreferences preferences = NotifsActivity.this.getSharedPreferences(AuthActivity.MyPREFERENCES, Context.MODE_PRIVATE);
        dep_id = preferences.getString(AuthActivity.Department_id, "1");
        emp_id = preferences.getString("emp_id", "1");

        //3: set list view
        lv =  findViewById(R.id.List_view_mast_notifications);
        lstcc = new ArrayList<>();
        loadLastNotifications();
        setNotificationsAsSeen();

        //Set Case if 0 notifs
        img_no_notifs = findViewById(R.id.imageView13);
        txt_no_notifs = findViewById(R.id.textView108);
        img_no_notifs.setVisibility(View.GONE);
        txt_no_notifs.setVisibility(View.GONE);
        boxRelativeLayout = findViewById(R.id.boxRelativeLayout);
        boxRelativeLayout.setVisibility(View.GONE);
    }
    public void  loadLastNotifications(){
        final String   URL =  "http://"+ WSadressIP.WSIP+":5000/cinternal/getEmployeeNotifications";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {

                            JSONArray array = new JSONArray(response);
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject product = array.getJSONObject(i);
                                lstcc.add(new NotifisModel(
                                        product.getInt("id"),
                                        product.getString("title"),
                                        product.getString("topic"),
                                        product.getString("date"),
                                        product.getString("time"),
                                        product.getString("seen"),
                                        product.getString("type"),
                                        product.getString("employee_id")

                                ));
                            }

                            NotifsAdapter adapter = new NotifsAdapter(NotifsActivity.this ,R.layout.notifs_row,lstcc );
                            adapter.notifyDataSetChanged();
                            lv.setAdapter(adapter);

                           // UIUtils.setListViewHeightBasedOnItems(lv);

                            if(lstcc.size() ==0 ) {
                                img_no_notifs.setVisibility(View.VISIBLE);
                                txt_no_notifs.setVisibility(View.VISIBLE);
                                boxRelativeLayout.setVisibility(View.VISIBLE);
                                lv.setVisibility(View.GONE);

                               // imageViewBackground.setVisibility(View.VISIBLE);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {


                        //Toast.makeText(getContext(), "you have No Tasks today", Toast.LENGTH_SHORT).show();
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


        Volley.newRequestQueue(NotifsActivity.this).add(stringRequest);
    }

    public void  setNotificationsAsSeen(){
        final String   URL =  "http://"+ WSadressIP.WSIP+":5000/cinternal/setNotificationsAsSeen";
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
                params.put("employee_id",emp_id);
                return params;
            }
        };;

        Volley.newRequestQueue(NotifsActivity.this).add(stringRequest);
    }
}
