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
import android.widget.RadioButton;
import android.widget.RadioGroup;
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

import java.util.HashMap;
import java.util.Map;

public class AddPoll extends AppCompatActivity {

    String  emp_id, dep_id ;
    RadioGroup radioGroup;
    EditText editTxt_question, editTxt_option_a,editTxt_option_b, editTxt_option_c, editTxt_option_d;
    Button btnAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_poll);

        //SET TOP BAR
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar3);
        mToolbar.setTitle(getString(R.string.app_name));
        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //get data
        SharedPreferences preferences = AddPoll.this.getSharedPreferences(AuthActivity.MyPREFERENCES, Context.MODE_PRIVATE);
        dep_id = preferences.getString(AuthActivity.Department_id, "1");
        emp_id = preferences.getString("emp_id", "1");

        //set ui
        radioGroup = findViewById(R.id.radioGroupAddPoll);
        editTxt_question = findViewById(R.id.editText5);
        editTxt_option_a = findViewById(R.id.editText6);
        editTxt_option_b = findViewById(R.id.editText7);
        editTxt_option_c = findViewById(R.id.editText8);
        editTxt_option_d = findViewById(R.id.editText9);
        btnAdd = findViewById(R.id.button9);

       // btnAdd.setVisibility(View.GONE);
        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {

            RadioButton rb = (RadioButton)findViewById(checkedId);

         //   btnAdd.setVisibility(View.VISIBLE);

        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!(editTxt_question.getText().toString().trim().length() == 0) &&  !(editTxt_option_a.getText().toString().trim().length() == 0) && !(editTxt_option_b.getText().toString().trim().length() == 0)){
                    if( !(editTxt_option_d.getText().toString().trim().length() == 0) && (editTxt_option_c.getText().toString().trim().length() == 0)){
                        Toast.makeText(AddPoll.this, "if u want to fill 4th option then please fill 3rd option.", Toast.LENGTH_SHORT).show();
                    }else{
                        int selectedId = radioGroup.getCheckedRadioButtonId();
                        RadioButton rb = (RadioButton) findViewById(selectedId);
                        if(rb.getText().toString().isEmpty()){
                            Toast.makeText(AddPoll.this, "Please select for who is the poll", Toast.LENGTH_SHORT).show();
                        }else{
                            //add
                            String opt_c;
                            String opt_d;
                            if(editTxt_option_d.getText().toString().trim().length() == 0){
                                opt_d="";
                            }else{
                                opt_d = editTxt_option_d.getText().toString().trim();
                            }
                            if(editTxt_option_c.getText().toString().trim().length() == 0){
                                opt_c="";
                            }else{
                                opt_c = editTxt_option_c.getText().toString().trim();
                            }
                            String include;
                            if(rb.getText().toString().equals("All Company")){
                                include="all_company";
                            }else{
                                include = dep_id;
                            }
                            if( (opt_c.equals(""))&&(opt_d.equals(""))){
                                if(editTxt_option_a.getText().toString().trim().equals(editTxt_option_b.getText().toString().trim())){
                                    Toast.makeText(AddPoll.this, "Options must be different", Toast.LENGTH_SHORT).show();
                                }else{
                                    addPoll(include,
                                            editTxt_question.getText().toString().trim(),
                                            editTxt_option_a.getText().toString().trim(),
                                            editTxt_option_b.getText().toString().trim(),
                                            opt_c,
                                            opt_d,
                                            emp_id);
                                }
                            }else if((!opt_c.equals(""))&&(opt_d.equals(""))){
                                if( (editTxt_option_a.getText().toString().trim().equals(editTxt_option_b.getText().toString().trim()))
                                        ||(editTxt_option_a.getText().toString().trim().equals(editTxt_option_c.getText().toString().trim()))
                                        ||(editTxt_option_b.getText().toString().trim().equals(editTxt_option_c.getText().toString().trim()))){
                                    Toast.makeText(AddPoll.this, "Options must be different", Toast.LENGTH_SHORT).show();
                                }else{
                                    addPoll(include,
                                            editTxt_question.getText().toString().trim(),
                                            editTxt_option_a.getText().toString().trim(),
                                            editTxt_option_b.getText().toString().trim(),
                                            opt_c,
                                            opt_d,
                                            emp_id);
                                }

                            }else if((!opt_c.equals(""))&&(!opt_d.equals(""))){
                                if( (editTxt_option_a.getText().toString().trim().equals(editTxt_option_b.getText().toString().trim()))
                                        ||(editTxt_option_a.getText().toString().trim().equals(editTxt_option_c.getText().toString().trim()))
                                        ||(editTxt_option_a.getText().toString().trim().equals(editTxt_option_d.getText().toString().trim()))
                                        ||(editTxt_option_b.getText().toString().trim().equals(editTxt_option_c.getText().toString().trim()))
                                        ||(editTxt_option_b.getText().toString().trim().equals(editTxt_option_d.getText().toString().trim()))
                                        ||(editTxt_option_c.getText().toString().trim().equals(editTxt_option_d.getText().toString().trim()))){
                                    Toast.makeText(AddPoll.this, "Options must be different", Toast.LENGTH_SHORT).show();
                                }else{
                                    addPoll(include,
                                            editTxt_question.getText().toString().trim(),
                                            editTxt_option_a.getText().toString().trim(),
                                            editTxt_option_b.getText().toString().trim(),
                                            opt_c,
                                            opt_d,
                                            emp_id);
                                }

                            }else{
                                //
                            }


                        }
                    }

                }else{
                    Toast.makeText(AddPoll.this, "Please add question and Minimum 2 options", Toast.LENGTH_SHORT).show();

                }

            }
        });


        /*int selectedId = radioGroup.getCheckedRadioButtonId();
        RadioButton rb = (RadioButton) findViewById(selectedId);
        rb.getText();*/

    }

    public void addPoll(String include,String question,String option_a, String option_b, String option_c,String option_d,String emp_id){
        final String   URL =  "http://"+ WSadressIP.WSIP+":5000/cinternal/addPoll";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Toast.makeText(AddPoll.this, "Poll successfully added", Toast.LENGTH_SHORT).show();

                       /* ((MainActivity)getApplicationContext()).finish();
                        Intent intent = new Intent("com.employee.cinderella.cinternalemp.MainActivity");
                        intent.putExtra("redirectToPolls", "redirectToPolls");
                        ((MainActivity)getApplicationContext()).startActivity(intent);*/
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
                params.put("question",question);
                params.put("include",include);
                params.put("option_a",option_a);
                params.put("option_b",option_b);
                params.put("option_c",option_c);
                params.put("option_d",option_d);
                params.put("emp_id",emp_id);
                return params;
            }
        };;
        Volley.newRequestQueue(AddPoll.this).add(stringRequest);
    }


}
