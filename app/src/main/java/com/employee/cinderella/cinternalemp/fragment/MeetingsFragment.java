package com.employee.cinderella.cinternalemp.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.employee.cinderella.cinternalemp.AddRequestActivity;
import com.employee.cinderella.cinternalemp.AuthActivity;
import com.employee.cinderella.cinternalemp.MeetingSingle;
import com.employee.cinderella.cinternalemp.R;
import com.employee.cinderella.cinternalemp.adapter.MyMeetingsAdapter;
import com.employee.cinderella.cinternalemp.model.Meeting;

import com.employee.cinderella.cinternalemp.utils.WSadressIP;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MeetingsFragment extends Fragment {

    String dep_id, emp_id;
    EditText mtSearch;
    ListView listView;
    List<Meeting> lstcc;
    //search list
    List<Meeting> lstcs;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_meetings, container, false);

        mtSearch = v.findViewById(R.id.mtSearch);

        listView = (ListView) v.findViewById(R.id.lvMeetingsAll);
        lstcc = new ArrayList<>();

        mtSearch.setOnKeyListener(
                new View.OnKeyListener() {
                    @Override
                    public boolean onKey(View v, int keyCode, KeyEvent event) {

                        searchMeetingByDate();

                        return true;
                    }
                }
        );

       // loadMeetings();



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

    public void loadMeetings(){
        final String   URL =  "http://"+ WSadressIP.WSIP+":5000/cinternal/getMyMeetings";

        lstcc = new ArrayList<>();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray array = new JSONArray(response);

                            for (int i = 0; i < array.length(); i++) {

                                //getting product object from json array
                                JSONObject product = array.getJSONObject(i);

                                //adding the product to product list
                                lstcc.add(new Meeting(
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


                            MyMeetingsAdapter adapterMeeting = new MyMeetingsAdapter(getContext() ,R.layout.meetings_row,lstcc ,"all");
                            adapterMeeting.notifyDataSetChanged();
                            listView.setAdapter(adapterMeeting);




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

    public void searchMeetingByDate(){
        String s = mtSearch.getText().toString();

        if(s.length() > 0) {
            lstcs = new ArrayList<>();
            for (Meeting i : lstcc) {
                if (i.getDay().toUpperCase().contains(s.toUpperCase())) {
                    lstcs.add(new Meeting(i.getId(),
                            i.getDay(),
                            i.getStart_hour(),
                            i.getEnd_hour(),
                            i.getTopic(),
                            i.getAgenda(),
                            i.getDecision(),
                            i.getLocation(),
                            i.getIs_present()

                    ));
                }
            }

            MyMeetingsAdapter adapterMeeting = new MyMeetingsAdapter(getContext() ,R.layout.meetings_row,lstcs ,"all");
            adapterMeeting.notifyDataSetChanged();
            listView.setAdapter(adapterMeeting);
        }else{
            MyMeetingsAdapter adapterMeeting = new MyMeetingsAdapter(getContext() ,R.layout.meetings_row,lstcc ,"all");
            adapterMeeting.notifyDataSetChanged();
            listView.setAdapter(adapterMeeting);
        }

    }

    @Override
    public void onResume() {

        super.onResume();
        loadMeetings();
    }

}
