package com.employee.cinderella.cinternalemp.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.employee.cinderella.cinternalemp.AddPoll;
import com.employee.cinderella.cinternalemp.AuthActivity;
import com.employee.cinderella.cinternalemp.NotifsActivity;
import com.employee.cinderella.cinternalemp.R;
import com.employee.cinderella.cinternalemp.adapter.MyMeetingsAdapter;
import com.employee.cinderella.cinternalemp.adapter.MyTasksAdapter;
import com.employee.cinderella.cinternalemp.adapter.NotifsAdapter;
import com.employee.cinderella.cinternalemp.adapter.TodayMeetingAdapter;
import com.employee.cinderella.cinternalemp.adapter.TodayTaskAdapter;
import com.employee.cinderella.cinternalemp.model.Meeting;
import com.employee.cinderella.cinternalemp.model.NotifisModel;
import com.employee.cinderella.cinternalemp.model.Task;
import com.employee.cinderella.cinternalemp.service.MySingleton;

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

import static com.employee.cinderella.cinternalemp.AuthActivity.MyPREFERENCES;



public class TodayFragment extends Fragment {

    String full_name , department_name , emp_id, emp_IP , img ;
    TextView txFull_name , txDepartment, txt_today, txt_notifs;
    ImageView profile_img, imageViewBackground , img_notif;
    Button btnCheck;
    String ip_able = "";


    public static final String EXTERNAL_IP = "externalIpKey";


    ListView lv;
    List<Task> lstcc;

    ListView lvMeeting;
    List<Meeting> lstccMeeting;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_today, container, false);
        txFull_name = v.findViewById(R.id.textViewXW);
        txDepartment = v.findViewById(R.id.textView4);
        profile_img = v.findViewById(R.id.imageView2);
        btnCheck = v.findViewById(R.id.button);
        txt_today = v.findViewById(R.id.textView6);

        img_notif = v.findViewById(R.id.imageView37);
        txt_notifs = v.findViewById(R.id.textView106);

        txFull_name.setText(full_name);
        txDepartment.setText(department_name);
        String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        txt_today.setText(date);


        //Notifs
        img_notif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v("tst","yes");
                Intent intent = new Intent(getContext(), NotifsActivity.class);
                getContext().startActivity(intent);
            }
        });

        //1: IMAGE SET
       // String theImage_URL = "http://"+ WSadressIP.WSIP +":5000/"+full_name.replace(" ", "")+".jpeg";
        String theImage_URL = "http://"+ WSadressIP.WSIP+":5000/"+emp_id+".jpeg";
        ImageLoader imageLoader = MySingleton.getInstance(getContext()).getImageLoader();

        imageLoader.get(theImage_URL, new ImageLoader.ImageListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                profile_img.setImageResource(R.drawable.profile);
            }

            @Override
            public void onResponse(ImageLoader.ImageContainer response, boolean arg1) {
                if (response.getBitmap() != null) {

                    if(img.equals("0")){
                        profile_img.setImageBitmap(response.getBitmap());
                    }else{
                        profile_img.setImageBitmap(stringToBitmap(img));

                    }

                }
            }
        });

        imageViewBackground = v.findViewById(R.id.imageView38);
        imageViewBackground.setVisibility(View.GONE);

        //2: CHECK IN CHECK OUT
        btnCheck.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //////////////////////
                        try {
                            getPublicIPAddress(btnCheck);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                      //  ipEnable();

                       // SharedPreferences pref = getContext().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                      //  emp_IP = pref.getString(EXTERNAL_IP,"000");
                     /*   emp_IP="test";
                        String ip_status = pref.getString("ip_status","1");

                        Log.v("ok","%%%%% ip_status= "+ip_status);

                        if((emp_IP.equals(WSadressIP.EXTERNALIP) && ip_status.equals("1")) || (ip_status.equals("0"))){

                            if(btnCheck.getText().toString().equals("Check In")){
                                insertCheckIn(emp_id,emp_IP);
                            }else if(btnCheck.getText().toString().equals("Check Out")){

                                updateCheckOut(emp_id);
                            }else{
                                btnCheck.setText("Check In");
                            }

                        }else{
                            Log.v("ipadr","%%%%% this device is not in office");
                            Toast.makeText(getContext(), "CHECK IN REQUIRE BEING CONNECTED TO THE OFFICE WIFI." , Toast.LENGTH_SHORT).show();
                        }*/
                        //////////////////////
                    }
                }
        );



        //3: set list view
        lv =  v.findViewById(R.id.List_view_Task_Today);
        lstcc = new ArrayList<>();
        loadTodayTasks();

        //3: Set meetings list view
        lvMeeting = v.findViewById(R.id.lv_meetings);
        lstccMeeting = new ArrayList<>();
        loadTodayMeetings();



        holidayCase(emp_id);
        return v;
    }

    protected FragmentActivity getActivityNonNull() {
        if (super.getActivity() != null) {
            return super.getActivity();
        } else {
            throw new RuntimeException("null returned from getActivity()");
        }
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences preferences = this.getActivity().getSharedPreferences(AuthActivity.MyPREFERENCES, Context.MODE_PRIVATE);
        full_name = preferences.getString(AuthActivity.Name, "xxxl");
        department_name = preferences.getString(AuthActivity.Department_Name, "Undefined");
        emp_id = preferences.getString("emp_id", "1");
        img = preferences.getString("img", "0");

        //SET CHECK IN OUT BUTTON
        checkInOut(emp_id);

        if (getArguments() != null) {

        }

    }

    public void checkInOut(final String emp_id){
        final String   URL =  "http://"+ WSadressIP.WSIP+":5000/cinternal/getDailyCheckByIdDay";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                            try {

                                JSONArray array = new JSONArray(response);
                                JSONObject product = array.getJSONObject(0);
                                int rowsCounter = product.getInt("namesCount");

                                if( Integer.toString(rowsCounter).equals("1")){
                                        btnCheck.setText("Check Out");

                                }else if(Integer.toString(rowsCounter).equals("0")){
                                        btnCheck.setText("Check In");
                                }else{
                                    btnCheck.setText("ok");
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Log.d("info","************ NOT FOUND ??");
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Content-Type","application/x-www-form-urlencoded");
                String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
                params.put("id",emp_id);
                params.put("day",date);

                return params;
            }
        };;


        Volley.newRequestQueue(getContext()).add(stringRequest);
    }

    public static String myIP="";
    public void getPublicIPAddress(Button txt_title) throws Exception {
        final String   URL  = "https://api.ipify.org/?format=json";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            JSONObject product = new JSONObject(response);
                            myIP = product.getString("ip");
                            Log.d("notif","%%%%%%%%%%%%%%%% the ip for this device is: "+ myIP );
                           /* SharedPreferences  sharedpreferences = getContext().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedpreferences.edit();
                            editor.putString(EXTERNAL_IP ,  product.getString("ip"));
                            editor.commit();*/

                            ipEnable(txt_title, myIP );
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
                params.put("Content-Type","application/json");



                return params;
            }
        };;
        Volley.newRequestQueue(getContext()).add(stringRequest);

    }

    public void insertCheckIn(final String emp_id, final String emp_IP){
        final String   URL =  "http://"+ WSadressIP.WSIP+":5000/cinternal/insertDailyCheckIn";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        btnCheck.setText("Check Out");
                        Toast.makeText(getContext(), "CHECK IN SUCCESSFULLY." , Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Log.d("info","************ NOT FOUND");
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Content-Type","application/x-www-form-urlencoded");
                String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

                String currentTime = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date());
                params.put("day",date);
                params.put("entry_hour",currentTime);
                params.put("ip_address",emp_IP);
                params.put("employee_id",emp_id);

                return params;
            }
        };;


        Volley.newRequestQueue(getContext()).add(stringRequest);
    }


    public void updateCheckOut(final String emp_id){
        final String   URL =  "http://"+ WSadressIP.WSIP+":5000/cinternal/updateDailyCheckOut";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Toast.makeText(getContext(), " CHECK OUT SUCCESS!", Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(getContext(), "you are currently unable to logout. PLEASE TRY AFTER 00:00 !", Toast.LENGTH_SHORT).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Content-Type","application/x-www-form-urlencoded");
                String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

                String currentTime = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date());
                params.put("day",date);
                params.put("leave_hour",currentTime);

                params.put("employee_id",emp_id);

                return params;
            }
        };;


        Volley.newRequestQueue(getContext()).add(stringRequest);
    }

    private static Bitmap stringToBitmap(String encodedString) {
        try {
            byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }

    public void  loadTodayTasks(){
        final String   URL =  "http://"+ WSadressIP.WSIP+":5000/cinternal/EmployeeTodayTask";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {

                            JSONArray array = new JSONArray(response);
                            for (int i = 0; i < array.length(); i++) {

                                JSONObject product = array.getJSONObject(i);

                                String Deadline = product.getString("deadline_date");
                                String deadline_date = Deadline.substring(0, 10);
                                String status = product.getString("status");

                                String Start = product.getString("start_date");
                                String Start_date = Start.substring(0, 10);

                                try {
                                    String today = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
                                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                                    Date dateDeadline = formatter.parse(deadline_date);
                                    Date dateToday = formatter.parse(today);
                                    Date dateStart = formatter.parse(Start_date);

                                    if( (dateDeadline.compareTo(dateToday) >= 0 ) ||(dateDeadline.compareTo(dateToday) <= 0 && !status.equals("1") )){
                                        if(dateToday.compareTo(dateStart) >= 0){
                                            lstcc.add(new Task(
                                                    product.getInt("id"),
                                                    product.getString("title"),
                                                    product.getString("start_date"),
                                                    deadline_date,
                                                    product.getString("finish_date"),
                                                    product.getString("status"),
                                                    product.getString("project_name"),
                                                    product.getString("employee_id"),
                                                    product.getString("skill_name")
                                            ));
                                        }
                                    }

                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }

                            }

                            TodayTaskAdapter adapter = new TodayTaskAdapter(getContext() ,R.layout.task_today_row,lstcc );
                            adapter.notifyDataSetChanged();
                            lv.setAdapter(adapter);

                            UIUtils.setListViewHeightBasedOnItems(lv);

                        /*   if(lstcc.size() == 0){
                                imageViewBackground.setVisibility(View.VISIBLE);
                            }*/

                            if( (lstcc.size() ==0 ) && ( lstccMeeting.size()==0 ) ){
                                imageViewBackground.setVisibility(View.VISIBLE);
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
                String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

               // String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
                params.put("day",date);
                params.put("employee_id",emp_id);

                return params;
            }
        };;


        Volley.newRequestQueue(getContext()).add(stringRequest);
    }

    public void loadTodayMeetings(){
        final String   URL =  "http://"+ WSadressIP.WSIP+":5000/cinternal/getMeetingByEmpToday";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            //converting the string to json array object
                            JSONArray array = new JSONArray(response);

                            //traversing through all the object
                            for (int i = 0; i < array.length(); i++) {

                                //getting product object from json array
                                JSONObject product = array.getJSONObject(i);

                                lstccMeeting.add(new Meeting(
                                        product.getInt("id"),
                                        product.getString("day"),
                                        product.getString("start_hour"),
                                        product.getString("end_hour"),
                                        product.getString("topic"),
                                        product.getString("agenda"),
                                        product.getString("decision"),
                                        product.getString("location"),
                                        product.getString("is_present")
                                ));
                            }

                            //creating adapter object and setting it to recyclerview
                            TodayMeetingAdapter adapterMeeting = new TodayMeetingAdapter(getContext() ,R.layout.task_today_row,lstccMeeting ,"today");
                            adapterMeeting.notifyDataSetChanged();
                            lvMeeting.setAdapter(adapterMeeting);
                            UIUtils.setListViewHeightBasedOnItems(lvMeeting);



                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                       // Toast.makeText(getContext(), "you have No Meetings today", Toast.LENGTH_SHORT).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Content-Type","application/x-www-form-urlencoded");
                String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
                params.put("day",date);
                params.put("employee_id",emp_id);

                return params;
            }
        };;

        Volley.newRequestQueue(getContext()).add(stringRequest);
    }

    public void  setUseenNotifications(){
        final String   URL =  "http://"+ WSadressIP.WSIP+":5000/cinternal/getEmployeeNotifications";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {

                            int my_counter = 0;
                            JSONArray array = new JSONArray(response);
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject product = array.getJSONObject(i);
                                if( product.getString("seen").equals("0")){
                                    my_counter = my_counter + 1;
                                }
                            }

                            String my_counter_string = String.valueOf(my_counter);
                            if(my_counter_string.equals("0")){
                                txt_notifs.setVisibility(View.GONE);
                            }else{
                                txt_notifs.setText("+"+my_counter_string);
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
                return params;
            }
        };;


        Volley.newRequestQueue(getContext()).add(stringRequest);
    }

    public void ipEnable(Button txt_title, String url_ip){
        final String   URL =  "http://"+ WSadressIP.WSIP+":5000/cinternal/ipStatus";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {


                            JSONArray array = new JSONArray(response);

                                JSONObject product = array.getJSONObject(0);
                                ip_able = product.getString("ip_status");
                                emp_IP = url_ip;
                              /*  SharedPreferences  sharedpreferences = getContext().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedpreferences.edit();
                                editor.putString("ip_status" , ip_able);
                                editor.commit();

                            SharedPreferences pref = getContext().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);*/
                            //  emp_IP = pref.getString(EXTERNAL_IP,"000");

                            String ip_status = ip_able;

                            Log.v("ok","%%%%% ip_status= "+ip_status+" ");

                            if((emp_IP.equals(WSadressIP.EXTERNALIP) && ip_status.equals("1")) || (ip_status.equals("0"))){

                                if(txt_title.getText().toString().equals("Check In")){
                                    insertCheckIn(emp_id,emp_IP);
                                }else if(txt_title.getText().toString().equals("Check Out")){
                                    updateCheckOut(emp_id);
                                }else{
                                    txt_title.setText("Check In");
                                }

                            }else{
                                Log.v("ipadr","%%%%% this device is not in office");
                                Toast.makeText(getContext(), "CHECK IN/OUT REQUIRE BEING CONNECTED TO THE OFFICE WIFI." , Toast.LENGTH_SHORT).show();
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

                return params;
            }
        };;

        Volley.newRequestQueue(getContext()).add(stringRequest);
    }

    public void holidayCase(final String emp_id){
        final String   URL =  "http://"+ WSadressIP.WSIP+":5000/cinternal/ifEmpInHolidayToday";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        try {

                            JSONArray array = new JSONArray(response);
                            if(array.length()>0){
                                JSONObject product = array.getJSONObject(0);
                                String approved = product.getString("status");
                                if(approved.equals("APPROVED")){
                                    btnCheck.setVisibility(View.GONE);
                                }
                                Log.v("ok","holiday from "+product.getString("start_date")+" until "+product.getString("end_date"));
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Log.d("info","************ NOT FOUND ??");
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Content-Type","application/x-www-form-urlencoded");
                String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
                params.put("id",emp_id);
                params.put("day",date);

                return params;
            }
        };;


        Volley.newRequestQueue(getContext()).add(stringRequest);
    }

    @Override
    public void onResume() {

        super.onResume();
        setUseenNotifications();
    }
}
