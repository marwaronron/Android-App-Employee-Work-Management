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
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.employee.cinderella.cinternalemp.utils.WSadressIP;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class EditReportOrderActivity extends AppCompatActivity {
    String  emp_id, dep_id ,order_db_id, order_report_id,order_report_text;
    EditText edit_report;
    Button btn_edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_report_order);


        //get data
        SharedPreferences preferences = EditReportOrderActivity.this.getSharedPreferences(AuthActivity.MyPREFERENCES, Context.MODE_PRIVATE);
        dep_id = preferences.getString(AuthActivity.Department_id, "1");
        emp_id = preferences.getString("emp_id", "1");

        order_db_id = this.getIntent().getStringExtra("order_db_id");
        order_report_id = this.getIntent().getStringExtra("order_report_id");
        order_report_text = this.getIntent().getStringExtra("order_report_text");

        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar11);
        mToolbar.setTitle("Edit Comment / Report");
        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                Intent intent = new Intent(EditReportOrderActivity.this , OrderSingle.class);
                intent.putExtra("order_db_id", order_db_id);
                startActivity(intent);
            }
        });

        edit_report = findViewById(R.id.editText17);
        btn_edit = findViewById(R.id.button22);

        edit_report.setText(order_report_text);

        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edit_report.getText().length() > 0 && edit_report.getText().length() <= 500){
                    editReportToOrder(order_report_id, edit_report.getText().toString());

                }else{
                    if(edit_report.getText().length() < 1){
                        Toast.makeText(EditReportOrderActivity.this,"Please fill Text ",Toast.LENGTH_LONG).show();
                    }else  if(edit_report.getText().length() > 501){
                        Toast.makeText(EditReportOrderActivity.this,"Text Maximun 500 characters",Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }

    public void editReportToOrder(String report_id, String report){
        final String   URL =  "http://"+ WSadressIP.WSIP+":5000/cinternal/editMyOrderReport";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        finish();
                        Intent intent = new Intent(EditReportOrderActivity.this , OrderSingle.class);
                        intent.putExtra("order_db_id", order_db_id);
                        startActivity(intent);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(EditReportOrderActivity.this,"You are Currently Unabel To Comment. Please Contact Admin",Toast.LENGTH_LONG).show();

                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Content-Type","application/x-www-form-urlencoded");

               // params.put("id_of_order",order_db_id);
               // params.put("employee_id",emp_id);
                params.put("report_id",report_id);
                String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
                params.put("date",date);
                params.put("report",report);
                return params;
            }
        };;
        Volley.newRequestQueue(EditReportOrderActivity.this).add(stringRequest);
    }
}
