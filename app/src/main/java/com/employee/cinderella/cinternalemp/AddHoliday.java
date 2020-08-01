package com.employee.cinderella.cinternalemp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
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
import com.employee.cinderella.cinternalemp.adapter.HolidaysAdapter;
import com.employee.cinderella.cinternalemp.model.Holiday;
import com.employee.cinderella.cinternalemp.utils.WSadressIP;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class AddHoliday extends AppCompatActivity {
    EditText editTxt_type, editTxt_start, editTxt_end;
    Button btnConfirm;
    String  emp_id, dep_id ;
    TextView txt_nbDays;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_holiday);

        //SET TOP BAR
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar4);
        mToolbar.setTitle("Request Holiday");
        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //SET UI
        editTxt_type = findViewById(R.id.editText10);
        editTxt_start = findViewById(R.id.editText11);
        editTxt_end = findViewById(R.id.editText12);
        btnConfirm = findViewById(R.id.button10);
        txt_nbDays = findViewById(R.id.textView33);

        //get data
        SharedPreferences preferences = AddHoliday.this.getSharedPreferences(AuthActivity.MyPREFERENCES, Context.MODE_PRIVATE);
        dep_id = preferences.getString(AuthActivity.Department_id, "1");
        emp_id = preferences.getString("emp_id", "1");

          editTxt_end.addTextChangedListener(new TextWatcher() {
              @Override
              public void beforeTextChanged(CharSequence s, int start, int count, int after) {

              }

              @Override
              public void onTextChanged(CharSequence s, int start, int before, int count) {


              }

              @Override
              public void afterTextChanged(Editable s) {
                  setDifference(editTxt_start.getText().toString(),editTxt_end.getText().toString(),txt_nbDays);

              }

          });


        editTxt_start.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


            }

            @Override
            public void afterTextChanged(Editable s) {
                setDifference(editTxt_start.getText().toString(),editTxt_end.getText().toString(),txt_nbDays);

            }

        });

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!editTxt_type.getText().toString().equals("") && !editTxt_start.getText().toString().equals("") && !editTxt_end.getText().toString().equals("")){
                   // String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
                    if(isValidFormat(editTxt_start.getText().toString())&& isValidFormat(editTxt_end.getText().toString())){

                        if(correctDates(editTxt_start.getText().toString(),editTxt_end.getText().toString())){
                            if(isInFuture(editTxt_start.getText().toString())){
                                // Pass to webservice
                                RequestHoliday(emp_id,editTxt_type.getText().toString(),editTxt_start.getText().toString(),editTxt_end.getText().toString());
                            }else{
                                //you can't add holiday in the past
                                Toast.makeText(AddHoliday.this, "You can NOT request holidays in the past!", Toast.LENGTH_SHORT).show();
                            }

                        }else{
                            //end date must be same or after start date
                            Toast.makeText(AddHoliday.this, "end day can NOT be before start day", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        //please fill date in the correct format
                        Toast.makeText(AddHoliday.this, "Please fill dates in this format yyyy-mm-dd", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    //please fill tall data
                    Toast.makeText(AddHoliday.this, "Please fill all data", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    public static boolean isValidFormat( String value) {
        Date date = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            date = sdf.parse(value);
            if (!value.equals(sdf.format(date))) {
                date = null;
            }
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        return date != null;
    }

    public boolean correctDates(String start_date, String end_date){
        boolean pass = false;
        try{
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            Date date1 = formatter.parse(start_date);


            Date date2 = formatter.parse( end_date);

            if (date1.compareTo(date2)<=0)
            {   //date 2 is greater than date 1
                pass = true;


            }
        }catch (ParseException e1){
            e1.printStackTrace();
        }
        return pass;
    }

    public boolean isInFuture(String start_date){
        boolean pass = false;
        try{
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            Date date1 = formatter.parse(start_date);
            String today = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
            Date dateToday = formatter.parse(today);

            if(date1.compareTo(dateToday) >=0){
                pass = true;
            }

        }catch (ParseException e1){
            e1.printStackTrace();
        }
        return pass;
    }

    public void RequestHoliday(String emp_id,String type, String start_date, String end_date){
        final String   URL =  "http://"+ WSadressIP.WSIP+":5000/cinternal/AddHolidayByEmployee";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                        JSONArray array = new JSONArray(response);
                            JSONObject product = array.getJSONObject(0);
                            if(product.getString("resultX").equals("NO")){
                                Toast.makeText(AddHoliday.this, "You already have a requested holiday in the same periode", Toast.LENGTH_SHORT).show();
                            }else if(product.getString("resultX").equals("YES")){
                                Toast.makeText(AddHoliday.this, "Holiday successfully added", Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent();
                                intent.putExtra("key", "value");
                                setResult(RESULT_OK, intent);
                                finish();
                               // Intent intent = new Intent(AddHoliday.this,Holidays.class);
                                //startActivity(intent);
                            }else{

                            }
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
                params.put("type",type);
                params.put("start_date",start_date);
                params.put("end_date",end_date);
                return params;
            }
        };;
        Volley.newRequestQueue(AddHoliday.this).add(stringRequest);
    }

    public void setDifference(String start,String end, TextView txt_nbDays){

        if(isValidFormat(editTxt_start.getText().toString())&& isValidFormat(editTxt_end.getText().toString())){

            if(correctDates(editTxt_start.getText().toString(),editTxt_end.getText().toString())){
                if(isInFuture(editTxt_start.getText().toString())){
                    // Pass to webservice
                    String dayDifference="";
                    try {

                        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                        Date dateStart = null;
                        dateStart = formatter.parse(start);

                        Date dateEnd = null;
                        dateEnd = formatter.parse(end);

                        //Comparing dates
                        long difference = Math.abs(dateEnd.getTime() - dateStart.getTime() );
                        long differenceDates = difference / (24 * 60 * 60 * 1000);

                        //Convert long to String
                        dayDifference = Long.toString(differenceDates + 1);

                        txt_nbDays.setText("Number Of Requested Days: "+dayDifference );

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                }else{
                    txt_nbDays.setText("" );
                }
            }else{
                txt_nbDays.setText("" );
            }
        }else{
            txt_nbDays.setText("" );
        }


    }
}
