package com.employee.cinderella.cinternalemp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.employee.cinderella.cinternalemp.AuthActivity.MyPREFERENCES;

public class PasswordActivity extends AppCompatActivity {

    String emp_id,dep_id;
    EditText pwd_current, pwd_new , pwd_new_again;
    Button btnConfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);


        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar12);
        mToolbar.setTitle("Password");
        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //SET UI
        pwd_current = findViewById(R.id.editTextCurrentPwd);
        pwd_new = findViewById(R.id.editTextnewPwd1);
        pwd_new_again = findViewById(R.id.editTextnewPwd2);
        btnConfirm = findViewById(R.id.button7);

        //get data
        SharedPreferences preferences = PasswordActivity.this.getSharedPreferences(AuthActivity.MyPREFERENCES, Context.MODE_PRIVATE);
        dep_id = preferences.getString(AuthActivity.Department_id, "1");
        emp_id = preferences.getString("emp_id", "1");

        TextInputLayout newb_pwd_input = (TextInputLayout) findViewById(R.id.newb_pwd_input);
        newb_pwd_input.setError(null);

        TextInputLayout newa_pwd_input = (TextInputLayout) findViewById(R.id.newa_pwd_input);
        newa_pwd_input.setError(null);

        TextInputLayout current_pwd_input = (TextInputLayout) findViewById(R.id.current_pwd_input);
        current_pwd_input.setError(null);

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ( (pwd_current.getText().toString().trim().length() > 0) &&
                        (pwd_new.getText().toString().trim().length() > 0) &&
                        (pwd_new_again.getText().toString().trim().length() > 0) ){

                   if(pwd_new.getText().toString().trim().length() >= 6){
                       newa_pwd_input.setError(null);
                       if(pwd_new.getText().toString().equals(pwd_new_again.getText().toString())){

                           newb_pwd_input.setError(null);
                           VerifyIfPwdIsCorrect(pwd_current.getText().toString(),pwd_new.getText().toString(),current_pwd_input);
                       }else{
                           newb_pwd_input.setError("new password is not equal");
                       }

                   }else{
                       newa_pwd_input.setError("minimum 6 characters");
                   }

                }else{
                    Toast.makeText(PasswordActivity.this, "Please fill all data", Toast.LENGTH_SHORT).show();
                }
            }
        });


        pwd_new.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {

            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(pwd_new.getText().toString().trim().length() < 6){
                    newa_pwd_input.setError("minimum 6 characters");
                }else{
                    newa_pwd_input.setError(null);
                }

                if(!pwd_new.getText().toString().equals(pwd_new_again.getText().toString())){
                    newb_pwd_input.setError("new password is not equal");
                }else{
                    newb_pwd_input.setError(null);
                }

            }
        });

        pwd_new_again.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {

            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!pwd_new.getText().toString().equals(pwd_new_again.getText().toString())){
                    newb_pwd_input.setError("new password is not equal");
                }else{
                    newb_pwd_input.setError(null);
                }

            }
        });
    }

    public void VerifyIfPwdIsCorrect(String currentPwd,String newPwd,TextInputLayout current_pwd_input){
        final String   URL =  "http://"+ WSadressIP.WSIP+":5000/cinternal/VerifyCurrentPwd";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray array = new JSONArray(response);
                           if(array.length()==1){
                               current_pwd_input.setError(null);
                               updatePassword(newPwd);
                           }else{
                               current_pwd_input.setError("Current Password is wrong");
                           }


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

                params.put("employee_id",emp_id);
                params.put("pwd",currentPwd);
                return params;
            }
        };;
        Volley.newRequestQueue(PasswordActivity.this).add(stringRequest);
    }

    public void updatePassword(String newPwd){
        final String   URL =  "http://"+ WSadressIP.WSIP+":5000/cinternal/updateEmployeePwd";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                     Toast.makeText(PasswordActivity.this, "Password Updated", Toast.LENGTH_SHORT).show();
                     finish();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(PasswordActivity.this, "You are NOT allowed to modify your password. please contact Admin", Toast.LENGTH_SHORT).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Content-Type","application/x-www-form-urlencoded");

                params.put("employee_id",emp_id);
                params.put("newPwd",newPwd);
                return params;
            }
        };;
        Volley.newRequestQueue(PasswordActivity.this).add(stringRequest);
    }
}
