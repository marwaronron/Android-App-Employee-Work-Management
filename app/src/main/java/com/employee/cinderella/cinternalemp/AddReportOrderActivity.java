package com.employee.cinderella.cinternalemp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.employee.cinderella.cinternalemp.utils.WSadressIP;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class AddReportOrderActivity extends AppCompatActivity {

    String  emp_id, dep_id ,order_db_id, order_real_id;
    TextView title;
    EditText comment;
    Button btnConfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_report_order);


        //get data
        SharedPreferences preferences = AddReportOrderActivity.this.getSharedPreferences(AuthActivity.MyPREFERENCES, Context.MODE_PRIVATE);
        dep_id = preferences.getString(AuthActivity.Department_id, "1");
        emp_id = preferences.getString("emp_id", "1");

        order_db_id = this.getIntent().getStringExtra("order_db_id");
        order_real_id = this.getIntent().getStringExtra("order_real_id");

        //SET TOP BAR
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar10);
        mToolbar.setTitle("Add Comment / Report");
        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                Intent intent = new Intent(AddReportOrderActivity.this , OrderSingle.class);
                intent.putExtra("order_db_id", order_db_id);
                startActivity(intent);
            }
        });

        title = findViewById(R.id.textView89);
        comment = findViewById(R.id.editText16);
        btnConfirm = findViewById(R.id.button19);

        title.setText("Comment/Report Order "+order_real_id);
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(comment.getText().length() > 0 && comment.getText().length() <= 500){
                    addReportToOrder(order_db_id, comment.getText().toString());

                }else{
                    if(comment.getText().length() < 1){
                        Toast.makeText(AddReportOrderActivity.this,"Please fill Text ",Toast.LENGTH_LONG).show();
                    }else  if(comment.getText().length() > 501){
                        Toast.makeText(AddReportOrderActivity.this,"Text Maximun 500 characters",Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }

    public void addReportToOrder(String order_db_id, String report){
        final String   URL =  "http://"+ WSadressIP.WSIP+":5000/cinternal/addOrderReport";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                            finish();
                            Intent intent = new Intent(AddReportOrderActivity.this , OrderSingle.class);
                            intent.putExtra("order_db_id", order_db_id);
                            startActivity(intent);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(AddReportOrderActivity.this,"You are Currently Unabel To Comment. Please Contact Admin",Toast.LENGTH_LONG).show();

                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Content-Type","application/x-www-form-urlencoded");

                params.put("id_of_order",order_db_id);
                params.put("employee_id",emp_id);
                String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
                params.put("date",date);
                params.put("report",report);
                return params;
            }
        };;
        Volley.newRequestQueue(AddReportOrderActivity.this).add(stringRequest);
    }



}
