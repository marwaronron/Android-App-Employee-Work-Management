package com.employee.cinderella.cinternalemp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.employee.cinderella.cinternalemp.adapter.TaskProjectAdapter;
import com.employee.cinderella.cinternalemp.model.Task;
import com.employee.cinderella.cinternalemp.utils.UIUtils;
import com.employee.cinderella.cinternalemp.utils.WSadressIP;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProjectSingle extends AppCompatActivity {

    TextView projectTitle, projectDesc, projectStart,  projectStat, txtTodo, txtDoing ,txtDone;
    CardView cardTodo, cardDoing, cardDone;

    String  emp_id, dep_id ;

    ListView lvTasks;
    List<Task> lstccTasks ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signle_project);

        //SET TOP BAR
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar2);
        mToolbar.setTitle(getString(R.string.app_name));
        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //get data
        SharedPreferences preferences = ProjectSingle.this.getSharedPreferences(AuthActivity.MyPREFERENCES, Context.MODE_PRIVATE);
        dep_id = preferences.getString(AuthActivity.Department_id, "1");
        emp_id = preferences.getString("emp_id", "1");

        //set ui
        projectTitle = findViewById(R.id.textViewProjectTitle);
        projectDesc = findViewById(R.id.textViewProjectDesc);
        projectStart = findViewById(R.id.textViewProjectStart);
        projectStat = findViewById(R.id.textViewProjectStat);
        txtTodo = findViewById(R.id.textViewtodo);


        //set ui data
        projectTitle.setText(this.getIntent().getStringExtra("project_title"));
        projectDesc.setText(this.getIntent().getStringExtra("project_desc"));
        projectStart.setText(this.getIntent().getStringExtra("project_start")+" _ "+this.getIntent().getStringExtra("project_deadline"));

        projectStat.setText(this.getIntent().getStringExtra("project_stat"));


        lvTasks =  findViewById(R.id.tasksProj);
        lstccTasks = new ArrayList<>();
        setTasks(String.valueOf(this.getIntent().getIntExtra("project_id",0)));
        Log.v("proj","§§§§§§§§§§§ "+String.valueOf(this.getIntent().getIntExtra("project_id",0)));
    }

    public void setTasks(String proj_id){
        final String   URL =  "http://"+ WSadressIP.WSIP+":5000/cinternal/getProjectTasks";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray array = new JSONArray(response);
                            txtTodo.setText("This Project have: "+array.length()+" Tasks" );


                            for (int i = 0; i < array.length(); i++) {
                                JSONObject product = array.getJSONObject(i);

                                lstccTasks.add(new Task(
                                        product.getInt("id"),
                                        product.getString("title"),
                                        product.getString("start_date"),
                                        product.getString("deadline_date"),
                                        product.getString("finish_date"),
                                        product.getString("status"),
                                        product.getString("project_id"),
                                        product.getString("employee_id"),
                                        product.getString("skill_name")
                                ));

                            }
                            TaskProjectAdapter adapter = new TaskProjectAdapter(ProjectSingle.this ,R.layout.task_project_row,lstccTasks );
                            adapter.notifyDataSetChanged();
                            // UIUtils.setListViewHeightBasedOnItems(lvCurrent);
                            lvTasks.setAdapter(adapter);
                            UIUtils.setListViewHeightBasedOnItems(lvTasks);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Toast.makeText(getContext(), "you have No Tasks today", Toast.LENGTH_SHORT).show();
                        Log.v("projects","%%%%%%%%% no overdue projects");
                        txtTodo.setText("This project don't have tasks yet");
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Content-Type","application/x-www-form-urlencoded");
                params.put("project_id",proj_id);
                return params;
            }
        };;
        Volley.newRequestQueue(ProjectSingle.this).add(stringRequest);
    }
}
