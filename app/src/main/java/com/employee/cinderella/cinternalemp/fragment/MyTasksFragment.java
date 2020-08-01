package com.employee.cinderella.cinternalemp.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.employee.cinderella.cinternalemp.AuthActivity;
import com.employee.cinderella.cinternalemp.R;
import com.employee.cinderella.cinternalemp.adapter.MyTasksAdapter;
import com.employee.cinderella.cinternalemp.model.Task;
import com.employee.cinderella.cinternalemp.utils.WSadressIP;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MyTasksFragment extends Fragment {

    String dep_id, emp_id;
    EditText mtSearch;
    ListView listView;
    List<Task> lstcc;
    //search list
    List<Task> lstcs;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_my_tasks, container, false);

        mtSearch = v.findViewById(R.id.mtSearch5);

        listView = (ListView) v.findViewById(R.id.lvMyTasks);
        lstcc = new ArrayList<>();

        mtSearch.setOnKeyListener(
                new View.OnKeyListener() {
                    @Override
                    public boolean onKey(View v, int keyCode, KeyEvent event) {

                        searchTaskByTitle();

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

        dep_id = preferences.getString(AuthActivity.Department_id, "1");
        emp_id = preferences.getString("emp_id", "1");

    }

    public void loadMyTasks(){
        final String   URL =  "http://"+ WSadressIP.WSIP+":5000/cinternal/getAllMyTasks";
        lstcc = new ArrayList<>();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONArray array = new JSONArray(response);

                            for (int i = 0; i < array.length(); i++) {

                                JSONObject product = array.getJSONObject(i);

                                lstcc.add(new Task(
                                      product.getInt("id"),
                                      product.getString("title"),
                                      product.getString("start_date"),
                                      product.getString("deadline_date"),
                                      product.getString("finish_date"),
                                      product.getString("status"),
                                      product.getString("project_name"),
                                      product.getString("employee_id"),
                                      product.getString("skill_name")
                                ));

                            }

                            MyTasksAdapter adapter = new MyTasksAdapter(getContext() ,R.layout.task_project_row,lstcc );
                            adapter.notifyDataSetChanged();
                            listView.setAdapter(adapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(getContext(), "you have No Tasks today", Toast.LENGTH_SHORT).show();
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

    public void searchTaskByTitle(){
        String s = mtSearch.getText().toString();

        if(s.length() > 0) {
            lstcs = new ArrayList<>();
            for (Task i : lstcc) {
                if (i.getTitle().toUpperCase().contains(s.toUpperCase())) {
                    lstcs.add(new Task(i.getId(),
                            i.getTitle(),
                            i.getStart_date(),
                            i.getDeadline_date(),
                            i.getFinish_date(),
                            i.getStatus(),
                            i.getProject_id(),
                            i.getEmployee_id(),
                            i.getSkill()

                    ));
                }
            }

            MyTasksAdapter adapter = new MyTasksAdapter(getContext() ,R.layout.task_project_row,lstcs );
            adapter.notifyDataSetChanged();
            listView.setAdapter(adapter);
        }else{
            MyTasksAdapter adapter = new MyTasksAdapter(getContext() ,R.layout.task_project_row,lstcc );
            adapter.notifyDataSetChanged();
            listView.setAdapter(adapter);
        }
    }

    @Override
    public void onResume() {

        super.onResume();
        loadMyTasks();
    }

}
