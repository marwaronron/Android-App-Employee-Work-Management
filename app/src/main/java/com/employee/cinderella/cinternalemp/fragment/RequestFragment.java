package com.employee.cinderella.cinternalemp.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.employee.cinderella.cinternalemp.PublicRequestActivity;
import com.employee.cinderella.cinternalemp.R;
import com.employee.cinderella.cinternalemp.adapter.RequestAdapter;
import com.employee.cinderella.cinternalemp.model.RequestModel;
import com.employee.cinderella.cinternalemp.utils.WSadressIP;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;


public class RequestFragment extends Fragment {
    String emp_id;
  //  Button gotToAddReqBtn;
    ListView lvRequest;
    List<RequestModel> lstccRequest;

    List<RequestModel> lstcs;
    EditText mtSearch;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_request, container, false);

       /* gotToAddReqBtn = v.findViewById(R.id.buttonAddRequest);
        gotToAddReqBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), AddRequestActivity.class);
                startActivity(intent);
            }
        });*/

        FloatingActionButton btn_floatAction = (FloatingActionButton) v.findViewById(R.id.floating_action_button2);

        btn_floatAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), AddRequestActivity.class);
                startActivity(intent);

            }
        });

        CardView card = v.findViewById(R.id.consultPublic);
                 card.setOnClickListener(new View.OnClickListener() {
                     @Override
                     public void onClick(View v) {
                         Intent intent = new Intent(getContext(), PublicRequestActivity.class);
                         startActivity(intent);
                     }
                 });
        mtSearch = v.findViewById(R.id.mtSearch2);
        lvRequest = v.findViewById(R.id.lv_my_requests);
        //SET LIST OF POLLS
        lstccRequest = new ArrayList<>();
       // loadMyRequests();

        mtSearch.setOnKeyListener(
                new View.OnKeyListener() {
                    @Override
                    public boolean onKey(View v, int keyCode, KeyEvent event) {

                        searchRequestByTitle();

                        return true;
                    }
                }
        );

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
        emp_id = preferences.getString("emp_id", "1");

    }

    public void  loadMyRequests(){
        final String   URL =  "http://"+ WSadressIP.WSIP+":5000/cinternal/getRequestsByEmployee";
        lstccRequest = new ArrayList<>();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONArray array = new JSONArray(response);
                            for (int i = 0; i < array.length(); i++) {

                                JSONObject question = array.getJSONObject(i);

                                lstccRequest.add(new RequestModel(
                                        question.getInt("id"),
                                        question.getString("type"),
                                        question.getString("title"),
                                        question.getString("description"),
                                        question.getString("send_by"),
                                        question.getString("status"),
                                        question.getString("date"),
                                        question.getString("privacy")
                                ));

                            }

                            RequestAdapter adapter = new RequestAdapter(getContext() ,R.layout.request_row,lstccRequest );
                            adapter.notifyDataSetChanged();
                            lvRequest.setAdapter(adapter);


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
                params.put("emp_id",emp_id);
                return params;
            }
        };;
        Volley.newRequestQueue(getContext()).add(stringRequest);
    }

    public void searchRequestByTitle(){
        String s = mtSearch.getText().toString();

        if(s.length() > 0) {
            lstcs = new ArrayList<>();
            for (RequestModel i : lstccRequest) {
                if (i.getTitle().toUpperCase().contains(s.toUpperCase())) {
                    lstcs.add(new RequestModel(i.getId(),
                            i.getType(),
                            i.getTitle(),
                            i.getDesc(),
                            i.getEmployee_id(),
                            i.getStatus(),
                            i.getDate(),
                            i.getPrivacy()
                    ));
                }
            }

            RequestAdapter adapter = new RequestAdapter(getContext() ,R.layout.request_row,lstcs );
            adapter.notifyDataSetChanged();
            lvRequest.setAdapter(adapter);
        }else{
            RequestAdapter adapter = new RequestAdapter(getContext() ,R.layout.request_row,lstccRequest );
            adapter.notifyDataSetChanged();
            lvRequest.setAdapter(adapter);
        }

    }

    @Override
    public void onResume() {

        super.onResume();
        loadMyRequests();
    }

}
