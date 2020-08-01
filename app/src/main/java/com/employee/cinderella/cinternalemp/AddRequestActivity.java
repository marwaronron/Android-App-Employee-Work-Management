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
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.employee.cinderella.cinternalemp.utils.WSadressIP;

import java.util.HashMap;
import java.util.Map;

public class AddRequestActivity extends AppCompatActivity {

    String  emp_id, dep_id ;
    Button sendReqBtn;
    RadioGroup rGRequest;
    EditText txt_title, txt_description;
    Switch mySwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_request);

        //SET TOP BAR
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar7);
        mToolbar.setTitle(getString(R.string.app_name));
        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //get data
        SharedPreferences preferences = AddRequestActivity.this.getSharedPreferences(AuthActivity.MyPREFERENCES, Context.MODE_PRIVATE);
        dep_id = preferences.getString(AuthActivity.Department_id, "1");
        emp_id = preferences.getString("emp_id", "1");

        //SET UI
        sendReqBtn = findViewById(R.id.button16);
        rGRequest = findViewById(R.id.radioGroupRequest);
        txt_title = findViewById(R.id.editText13);
        txt_description = findViewById(R.id.editText14);
        mySwitch = findViewById(R.id.switch2);
        mySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                   mySwitch.setText("Public");  //To change the text near to switch

                }
                else {
                    mySwitch.setText("Private");  //To change the text near to switch

                }
            }
        });


        //SET btn HIDDEN
        sendReqBtn.setVisibility(View.GONE);

        rGRequest.setOnCheckedChangeListener((group, checkedId) -> {

            RadioButton rb=(RadioButton)findViewById(checkedId);
            //textViewChoice.setText("You Selected " + rb.getText());
            //Toast.makeText(getApplicationContext(), rb.getText(), Toast.LENGTH_SHORT).show();
            if(!rb.getText().toString().isEmpty()){

                sendReqBtn.setVisibility(View.VISIBLE);
            }
        });



        sendReqBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!(txt_title.getText().toString().trim().length() == 0) && !(txt_description.getText().toString().trim().length() == 0)){
                    int selectedId = rGRequest.getCheckedRadioButtonId();
                    RadioButton rb = (RadioButton) findViewById(selectedId);
                    if(rb.getText().toString().isEmpty()){
                        Log.v("XX","++++++++++++++=+++ 1");
                        Toast.makeText(AddRequestActivity.this, "Please select the Request type", Toast.LENGTH_SHORT).show();
                    }else{
                        Log.v("XX","++++++++++++++=+++ 3");
                        if(rb.getText().toString().equals(getResources().getString(R.string.req_1))){
                            addNewRequest("1",
                                    txt_title.getText().toString().trim(),
                                    txt_description.getText().toString().trim(),
                                    emp_id,
                                    mySwitch.getText().toString());
                        }else if(rb.getText().toString().equals(getResources().getString(R.string.req_2))){
                            addNewRequest("2",
                                    txt_title.getText().toString().trim(),
                                    txt_description.getText().toString().trim(),
                                    emp_id,
                                    mySwitch.getText().toString());
                        }else if(rb.getText().toString().equals(getResources().getString(R.string.req_3))){
                            addNewRequest("3",
                                    txt_title.getText().toString().trim(),
                                    txt_description.getText().toString().trim(),
                                    emp_id,
                                    mySwitch.getText().toString());
                        }else if(rb.getText().toString().equals(getResources().getString(R.string.req_4))){
                            addNewRequest("4",
                                    txt_title.getText().toString().trim(),
                                    txt_description.getText().toString().trim(),
                                    emp_id,
                                    mySwitch.getText().toString());
                        }else{
                            Log.v("XX","++++++++++++++=+++ 2");
                        }

                    }
                }else{
                    Toast.makeText(AddRequestActivity.this, "Please fill all data", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void addNewRequest(String type, String title, String desc, String emp_id, String privacy){
        final String   URL =  "http://"+ WSadressIP.WSIP+":5000/cinternal/addRequest";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Toast.makeText(AddRequestActivity.this, "Request successfully sent", Toast.LENGTH_SHORT).show();

                      /*  ((MainActivity)getApplicationContext()).finish();
                        Intent intent = new Intent("com.employee.cinderella.cinternalemp.MainActivity");
                        intent.putExtra("redirectToRequests", "redirectToRequests");
                        ((MainActivity)getApplicationContext()).startActivity(intent); */
                      finish();
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
                params.put("type",type);
                params.put("title",title);
                params.put("description",desc);
                params.put("emp_id",emp_id);
                params.put("privacy",privacy);


                return params;
            }
        };;
        Volley.newRequestQueue(AddRequestActivity.this).add(stringRequest);
    }
}
