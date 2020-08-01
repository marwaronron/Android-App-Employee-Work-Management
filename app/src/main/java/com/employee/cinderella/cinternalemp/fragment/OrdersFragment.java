package com.employee.cinderella.cinternalemp.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.employee.cinderella.cinternalemp.AuthActivity;
import com.employee.cinderella.cinternalemp.R;
import com.employee.cinderella.cinternalemp.adapter.PollAdapter;
import com.employee.cinderella.cinternalemp.adapter.UnfinishedOrderAdapter;
import com.employee.cinderella.cinternalemp.model.OrderClient;
import com.employee.cinderella.cinternalemp.model.Poll;
import com.employee.cinderella.cinternalemp.utils.UIUtils;
import com.employee.cinderella.cinternalemp.utils.WSadressIP;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class OrdersFragment extends Fragment {

    String  emp_id, dep_id ;
    ListView lvOrderUnfinished;
    List<OrderClient> lstccOrderUnfinished;
    TextView  txt_title;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v  = inflater.inflate(R.layout.fragment_orders, container, false);


        txt_title = v.findViewById(R.id.titleOrders);
        lvOrderUnfinished = v.findViewById(R.id.lvOrderUnfinished);

        //SET LIST OF Unfinished orders
        lstccOrderUnfinished = new ArrayList<>();
      //  loadMyUnfinishedOrders();
        return  v;
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
        if (getArguments() != null) {

        }

        SharedPreferences preferences = this.getActivity().getSharedPreferences(AuthActivity.MyPREFERENCES, Context.MODE_PRIVATE);
        dep_id = preferences.getString(AuthActivity.Department_id, "1");
        emp_id = preferences.getString("emp_id", "1");
    }


    public void  loadMyUnfinishedOrders(){
        final String   URL =  "http://"+ WSadressIP.WSIP+":5000/cinternal/unfinishedOrder";
        lstccOrderUnfinished = new ArrayList<>();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONArray array = new JSONArray(response);
                            if( array.length() >0){
                                for (int i = 0; i < array.length(); i++) {

                                    JSONObject question = array.getJSONObject(i);


                                    lstccOrderUnfinished.add(new OrderClient(
                                            question.getInt("id"),
                                            question.getString("order_code"),
                                            question.getString("prod_code"),
                                            question.getString("acceptance"),
                                            question.getString("internal_logs"),
                                            question.getString("client_name"),
                                            question.getString("country"),
                                            question.getString("email"),
                                            question.getString("phone"),
                                            question.getString("ip_adress"),
                                            question.getString("extra_details"),
                                            question.getString("drag"),
                                            question.getString("date"),
                                            question.getString("employee"),
                                            question.getString("drag_date"),
                                            question.getString("drag_time"),
                                            question.getString("close_date"),
                                            question.getString("close_time"),
                                            question.getString("note"),
                                            question.getString("department")

                                    ));

                                }

                                if(array.getJSONObject(0).getString("drag").equals("0")||(array.getJSONObject(0).getString("drag").equals("1") && !(array.getJSONObject(0).getString("employee").equals(emp_id)))){
                                    txt_title.setText("Delayed Orders");
                                }else if(array.getJSONObject(0).getString("drag").equals("1")){
                                    txt_title.setText("Currently Working on");
                                }else{
                                  //  txt_title.setText("Clients Orders (0)");
                                }


                                    UnfinishedOrderAdapter adapter = new UnfinishedOrderAdapter(getContext() ,R.layout.order_unfinished_row,lstccOrderUnfinished );
                                    adapter.notifyDataSetChanged();
                                    lvOrderUnfinished.setAdapter(adapter);


                                //UIUtils.setListViewHeightBasedOnItems(lvOrderUnfinished);
                            }else{
                               // txt_title.setText("Clients Orders (0)");
                            }



                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                       txt_title.setText("Clients Orders ");
                       lstccOrderUnfinished = new ArrayList<>();
                        UnfinishedOrderAdapter adapter = new UnfinishedOrderAdapter(getContext() ,R.layout.order_unfinished_row,lstccOrderUnfinished );
                        adapter.notifyDataSetChanged();
                        lvOrderUnfinished.setAdapter(adapter);

                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Content-Type","application/x-www-form-urlencoded");
                params.put("dep_id",dep_id);
                params.put("emp_id",emp_id);
                return params;
            }
        };;
        Volley.newRequestQueue(getContext()).add(stringRequest);
    }

    @Override
    public void onResume() {

        super.onResume();
        loadMyUnfinishedOrders();
    }


}
