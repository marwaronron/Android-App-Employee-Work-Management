package com.employee.cinderella.cinternalemp.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import com.employee.cinderella.cinternalemp.AuthActivity;
import com.employee.cinderella.cinternalemp.MyFirebaseMessagingService;
import com.employee.cinderella.cinternalemp.R;
import com.employee.cinderella.cinternalemp.adapter.CalendarAdapter;
import com.employee.cinderella.cinternalemp.adapter.PollAdapter;
import com.employee.cinderella.cinternalemp.model.CalendarModel;
import com.employee.cinderella.cinternalemp.model.Poll;
import com.employee.cinderella.cinternalemp.utils.UIUtils;
import com.employee.cinderella.cinternalemp.utils.WSadressIP;
import com.skyhope.eventcalenderlibrary.CalenderEvent;
import com.skyhope.eventcalenderlibrary.listener.CalenderDayClickListener;
import com.skyhope.eventcalenderlibrary.model.DayContainerModel;
import com.skyhope.eventcalenderlibrary.model.Event;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;


import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import static com.firebase.ui.auth.AuthUI.getApplicationContext;


public class CalendarFragment extends Fragment {

    String dep_id, emp_id;

    ListView lvCalendarProjects;
    List<CalendarModel> lstccCalendarProjects;

    ListView lvCalendarTasks;
    List<CalendarModel> lstccCalendarTasks;

    ListView lvCalendarMeetings;
    List<CalendarModel> lstccCalendarMeetings;

    ListView lvCalendarHolidays;
    List<CalendarModel> lstccCalendarHolidays;

    TextView txt_selected_day, txt_day_projects, txt_day_tasks, txt_day_meetings, txt_day_holiday;
    CalendarView newCalendar ;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_calendar, container, false);

        //SET UI
        lvCalendarProjects = v.findViewById(R.id.lv_calendar_projects);
        lvCalendarTasks = v.findViewById(R.id.lv_calendar_tasks);
        lvCalendarMeetings = v.findViewById(R.id.lv_calendar_meetings);
        lvCalendarHolidays = v.findViewById(R.id.lv_calendar_holidays);
        txt_selected_day = v.findViewById(R.id.textView64);
        txt_day_projects = v.findViewById(R.id.textView57);
        txt_day_tasks = v.findViewById(R.id.textView58);
        txt_day_meetings = v.findViewById(R.id.textView59);
        txt_day_holiday  = v.findViewById(R.id.textView60);

        //set data
        txt_day_projects.setText("Projects");
        txt_day_tasks.setText("Tasks");
        txt_day_meetings.setText("Meetings");
        txt_day_holiday.setText("Holidays");


        //SET fragment load ( today data)
        String today = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        txt_selected_day.setText("Day: "+today);

        lstccCalendarProjects = new ArrayList<>();
        loadCalendarProjects(today);

        lstccCalendarTasks = new ArrayList<>();
        loadCalendarTasks(today);

        lstccCalendarMeetings = new ArrayList<>();
        loadCalendarMeetings(today);

        lstccCalendarHolidays = new ArrayList<>();
        loadCalendarHolidays(today);


       /* Calendar calendar = Calendar.getInstance();
        CalenderEvent calenderEvent = v.findViewById(R.id.calender_event_id);
        Event event = new Event(calendar.getTimeInMillis(), "",Color.TRANSPARENT);
        calenderEvent.addEvent(event);


        calenderEvent.initCalderItemClickCallback(new CalenderDayClickListener() {
            @Override
            public void onGetDay(DayContainerModel dayContainerModel) {

                    String stringdate = dayContainerModel.getDate();
                    String segments[] = stringdate.split(" ");
                    String year = segments[segments.length - 1];
                    String monthName = segments[segments.length - 2];
                    String day = segments[segments.length - 3];

                    String month="";
                    if(monthName.equals("January")){
                        month="01";
                    }else if(monthName.equals("February")){
                        month="02";
                    }else if(monthName.equals("March")){
                        month="03";
                    }else if(monthName.equals("April")){
                        month="04";
                    }else if(monthName.equals("May")){
                        month="05";
                    }else if(monthName.equals("June")){
                        month="06";
                    }else if(monthName.equals("July")){
                        month="07";
                    }else if(monthName.equals("August")){
                        month="08";
                    }else if(monthName.equals("September")){
                        month="09";
                    }else if(monthName.equals("October")){
                        month="10";
                    }else if(monthName.equals("November")){
                        month="11";
                    }else if(monthName.equals("December")){
                        month="12";
                    }

                    if(day.equals("1")){
                        day="01";
                    }else if(day.equals("2")){
                        day="02";
                    }else if(day.equals("3")){
                        day="03";
                    }else if(day.equals("4")){
                        day="04";
                    }else if(day.equals("5")){
                        day="05";
                    }else if(day.equals("6")){
                        day="06";
                    }else if(day.equals("7")){
                        day="07";
                    }else if(day.equals("8")){
                        day="08";
                    }else if(day.equals("9")){
                        day="09";
                    }else{
                        day = day;
                    }
                    String good_format_date = year+"-"+month+"-"+day;

                    Log.d("CAL", ">>>>>>>>> "+dayContainerModel.getDate()+" >>>>> "+good_format_date);


                    txt_selected_day.setText("Day: "+good_format_date);

                    lstccCalendarProjects = new ArrayList<>();
                    loadCalendarProjects(good_format_date);

                    lstccCalendarTasks = new ArrayList<>();
                    loadCalendarTasks(good_format_date);

                    lstccCalendarMeetings = new ArrayList<>();
                    loadCalendarMeetings(good_format_date);

                    lstccCalendarHolidays = new ArrayList<>();
                    loadCalendarHolidays(good_format_date);

            }
        });
        */

        newCalendar = (CalendarView) v.findViewById(R.id.calendarView);
        newCalendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month,
                                            int dayOfMonth) {
               String day = String.valueOf(dayOfMonth);

                if(day.equals("1")){
                    day="01";
                }else if(day.equals("2")){
                    day="02";
                }else if(day.equals("3")){
                    day="03";
                }else if(day.equals("4")){
                    day="04";
                }else if(day.equals("5")){
                    day="05";
                }else if(day.equals("6")){
                    day="06";
                }else if(day.equals("7")){
                    day="07";
                }else if(day.equals("8")){
                    day="08";
                }else if(day.equals("9")){
                    day="09";
                }else{
                    day = day;
                }

                String fixMonth = String.valueOf(month+1);
                if(fixMonth.equals("1")){
                    fixMonth = "01";
                }else if(fixMonth.equals("2")){
                    fixMonth = "02";
                }else if(fixMonth.equals("3")){
                    fixMonth = "03";
                }else if(fixMonth.equals("4")){
                    fixMonth = "04";
                }else if(fixMonth.equals("5")){
                    fixMonth = "05";
                }else if(fixMonth.equals("6")){
                    fixMonth = "06";
                }else if(fixMonth.equals("7")){
                    fixMonth = "07";
                }else if(fixMonth.equals("8")){
                    fixMonth = "08";
                }else if(fixMonth.equals("9")){
                    fixMonth = "09";
                }
                Toast.makeText(getContext(),year+"-"+fixMonth+"-"+day,Toast.LENGTH_LONG).show();
                String good_format_date = year+"-"+fixMonth+"-"+day;
                txt_selected_day.setText("Day: "+good_format_date);

                lstccCalendarProjects = new ArrayList<>();
                loadCalendarProjects(good_format_date);

                lstccCalendarTasks = new ArrayList<>();
                loadCalendarTasks(good_format_date);

                lstccCalendarMeetings = new ArrayList<>();
                loadCalendarMeetings(good_format_date);

                lstccCalendarHolidays = new ArrayList<>();
                loadCalendarHolidays(good_format_date);
            }
        });

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

        dep_id = preferences.getString(AuthActivity.Department_id, "1");
        emp_id = preferences.getString("emp_id", "1");

    }

    public void loadCalendarProjects(String date){


        final String   URL =  "http://"+ WSadressIP.WSIP+":5000/cinternal/calendarProjects";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {

                            JSONArray array = new JSONArray(response);
                            txt_day_projects.setText("Projects ("+array.length()+")");
                            for (int i = 0; i < array.length(); i++) {

                                JSONObject question = array.getJSONObject(i);


                                lstccCalendarProjects.add(new CalendarModel(
                                        question.getInt("id"),
                                        "project",
                                        question.getString("name"),
                                        question.getString("start_date"),
                                        question.getString("deadline_date"),
                                        question.getString("stat")
                                ));

                            }


                            CalendarAdapter adapter = new CalendarAdapter(getContext() ,R.layout.calendar_row,lstccCalendarProjects );
                            adapter.notifyDataSetChanged();
                            lvCalendarProjects.setAdapter(adapter);
                            UIUtils.setListViewHeightBasedOnItems(lvCalendarProjects);
                            Log.d("CAL", ">>>>>>>>>  OK");

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Log.d("CAL", ">>>>>>>>> FAIL");
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Content-Type","application/x-www-form-urlencoded");
                params.put("department_id",dep_id);
                params.put("date",date);
                return params;
            }
        };;
        Volley.newRequestQueue(getContext()).add(stringRequest);
    }

    public void loadCalendarTasks(String date){
       // txt_selected_day.setText("Day: "+date);

        final String   URL =  "http://"+ WSadressIP.WSIP+":5000/cinternal/calendarTasks";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {

                            JSONArray array = new JSONArray(response);
                            txt_day_tasks.setText("Tasks ("+array.length()+")");
                            for (int i = 0; i < array.length(); i++) {

                                JSONObject question = array.getJSONObject(i);

                                String status ="";
                                if(question.getString("status").equals("1")){
                                    status="Done";
                                }else  if(question.getString("status").equals("2")){
                                    status="Doing";
                                }else  if(question.getString("status").equals("3")){
                                    status="To Do";
                                }else{
                                    status ="";
                                }
                                lstccCalendarTasks.add(new CalendarModel(
                                        question.getInt("id"),
                                        "task",
                                        status+": "+question.getString("title"),
                                        question.getString("start_date"),
                                        question.getString("deadline_date"),
                                        "From project: "+question.getString("project_name")
                                ));
                            }


                            CalendarAdapter adapter = new CalendarAdapter(getContext() ,R.layout.calendar_row,lstccCalendarTasks );
                            adapter.notifyDataSetChanged();
                            lvCalendarTasks.setAdapter(adapter);
                            UIUtils.setListViewHeightBasedOnItems(lvCalendarTasks);
                            Log.d("CAL", ">>>>>>>>>  OK");

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Log.d("CAL", ">>>>>>>>> FAIL");
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Content-Type","application/x-www-form-urlencoded");
                params.put("emp_id",emp_id);
                params.put("date",date);
                return params;
            }
        };;
        Volley.newRequestQueue(getContext()).add(stringRequest);
    }

    public void loadCalendarMeetings(String date){


        final String   URL =  "http://"+ WSadressIP.WSIP+":5000/cinternal/calendarMeetings";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {

                            JSONArray array = new JSONArray(response);
                            txt_day_meetings.setText("Meetings ("+array.length()+")");
                            for (int i = 0; i < array.length(); i++) {

                                JSONObject question = array.getJSONObject(i);
                                String location ="";
                                if(question.getString("location").equals("null") || question.getString("location").equals("") ||question.getString("location").isEmpty()){
                                    location = "office";
                                }else{
                                    location = question.getString("location");
                                }

                                lstccCalendarMeetings.add(new CalendarModel(
                                        question.getInt("id"),
                                        "meeting",
                                        question.getString("topic"),
                                        question.getString("start_hour"),
                                        question.getString("end_hour"),
                                        "Location: "+location
                                ));

                            }


                            CalendarAdapter adapter = new CalendarAdapter(getContext() ,R.layout.calendar_row,lstccCalendarMeetings );
                            adapter.notifyDataSetChanged();
                            lvCalendarMeetings.setAdapter(adapter);
                            UIUtils.setListViewHeightBasedOnItems(lvCalendarMeetings);
                            Log.d("CAL", ">>>>>>>>>  OK");

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Log.d("CAL", ">>>>>>>>> FAIL");
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Content-Type","application/x-www-form-urlencoded");
                params.put("emp_id",emp_id);
                params.put("date",date);
                return params;
            }
        };;
        Volley.newRequestQueue(getContext()).add(stringRequest);
    }

    public void loadCalendarHolidays(String date){


        final String   URL =  "http://"+ WSadressIP.WSIP+":5000/cinternal/calendarHolidays";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {

                            JSONArray array = new JSONArray(response);
                            if(!String.valueOf(array.length()).equals("0")){
                                txt_day_holiday.setText("Holidays ("+array.length()+")");
                            }else{
                                txt_day_holiday.setText("Holidays");
                            }

                            for (int i = 0; i < array.length(); i++) {

                                JSONObject question = array.getJSONObject(i);


                                lstccCalendarHolidays.add(new CalendarModel(
                                        question.getInt("id"),
                                        "holiday",
                                        question.getString("type"),
                                        question.getString("start_date"),
                                        question.getString("end_date"),
                                        "Approved Holidays"
                                ));

                            }


                            CalendarAdapter adapter = new CalendarAdapter(getContext() ,R.layout.calendar_row,lstccCalendarHolidays);
                            adapter.notifyDataSetChanged();
                            lvCalendarHolidays.setAdapter(adapter);
                            UIUtils.setListViewHeightBasedOnItems(lvCalendarHolidays);
                            Log.d("CAL", ">>>>>>>>>  OK");

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Log.d("CAL", ">>>>>>>>> FAIL");
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Content-Type","application/x-www-form-urlencoded");
                params.put("emp_id",emp_id);
                params.put("date",date);
                return params;
            }
        };;
        Volley.newRequestQueue(getContext()).add(stringRequest);
    }
}
