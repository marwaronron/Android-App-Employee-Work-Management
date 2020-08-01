package com.employee.cinderella.cinternalemp.fragment;

import android.content.Context;
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
import com.employee.cinderella.cinternalemp.AuthActivity;
import com.employee.cinderella.cinternalemp.R;
import com.employee.cinderella.cinternalemp.adapter.ProjectsAdapter;
import com.employee.cinderella.cinternalemp.model.Project;
import com.employee.cinderella.cinternalemp.utils.WSadressIP;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;


public class ProjectsFragment extends Fragment {
    String  emp_id, dep_id ;

    EditText mtSearch;
    ListView lvProjects;
    List<Project> lstccProjects;

    List<Project> lstcs;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_projects, container, false);


        lvProjects =  v.findViewById(R.id.lvprojectsAll);
        lstccProjects = new ArrayList<>();
        setAllProjectsData(dep_id);

        mtSearch = v.findViewById(R.id.mtSearch4);
        mtSearch.setOnKeyListener(
                new View.OnKeyListener() {
                    @Override
                    public boolean onKey(View v, int keyCode, KeyEvent event) {

                        searchProjectByName();

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

    public void setAllProjectsData(String dep_id){
        final String   URL =  "http://"+ WSadressIP.WSIP+":5000/cinternal/myDepartmentProjects";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray array = new JSONArray(response);


                            for (int i = 0; i < array.length(); i++) {
                                JSONObject product = array.getJSONObject(i);

                                lstccProjects.add(new Project(
                                        product.getInt("id"),
                                        product.getString("name"),
                                        product.getString("description"),
                                        product.getString("start_date"),
                                        product.getString("deadline_date"),
                                        product.getString("finish_date"),
                                        product.getString("stat"),
                                        product.getString("department_id")
                                ));

                            }


                            ProjectsAdapter adapter = new ProjectsAdapter(getContext() ,R.layout.projects_row,lstccProjects );
                            adapter.notifyDataSetChanged();
                            lvProjects.setAdapter(adapter);
                           // UIUtils.setListViewHeightBasedOnItems(lvProjects);

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
                params.put("department_id",dep_id);
                return params;
            }
        };;
        Volley.newRequestQueue(getContext()).add(stringRequest);

    }

    public void searchProjectByName(){
        String s = mtSearch.getText().toString();

        if(s.length() > 0) {
            lstcs = new ArrayList<>();
            for (Project i : lstccProjects) {
                if (i.getName().toUpperCase().contains(s.toUpperCase())) {
                    lstcs.add(new Project(i.getId(),
                            i.getName(),
                            i.getDescription(),
                            i.getStart_date(),
                            i.getDeadline_date(),
                            i.getFinish_date(),
                            i.getStatus(),
                            i.getDepartment_id()
                    ));
                }
            }

            ProjectsAdapter adapter = new ProjectsAdapter(getContext() ,R.layout.projects_row,lstcs );
            adapter.notifyDataSetChanged();
            lvProjects.setAdapter(adapter);
        }else{
            ProjectsAdapter adapter = new ProjectsAdapter(getContext() ,R.layout.projects_row,lstccProjects );
            adapter.notifyDataSetChanged();
            lvProjects.setAdapter(adapter);
        }

    }
}
