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
import com.employee.cinderella.cinternalemp.AddPoll;
import com.employee.cinderella.cinternalemp.AuthActivity;
import com.employee.cinderella.cinternalemp.R;
import com.employee.cinderella.cinternalemp.adapter.PollAdapter;
import com.employee.cinderella.cinternalemp.model.Poll;
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
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import static com.employee.cinderella.cinternalemp.AuthActivity.Department_id;


public class PollFragment extends Fragment {
    String full_name , department_name , emp_id, dep_id ;
    ListView lvPoll;
    List<Poll> lstccPoll;
    //search list
    List<Poll> lstcs;
    EditText mtSearch;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_poll, container, false);

        //SET UI

        lvPoll =  v.findViewById(R.id.lvPoll);

        FloatingActionButton btn_floatAction =
                (FloatingActionButton) v.findViewById(R.id.floating_action_button);

        btn_floatAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), AddPoll.class);
                getContext().startActivity(intent);

            }
        });

        mtSearch = v.findViewById(R.id.editText20);

        //SET LIST OF POLLS
        lstccPoll = new ArrayList<>();
        //loadMyPolls(); // methode moved to in resume

        mtSearch.setOnKeyListener(
                new View.OnKeyListener() {
                    @Override
                    public boolean onKey(View v, int keyCode, KeyEvent event) {

                        searchPollByQuestion();

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
        full_name = preferences.getString(AuthActivity.Name, "xxxl");
        department_name = preferences.getString(AuthActivity.Department_Name, "Undefined");
        emp_id = preferences.getString("emp_id", "1");
        dep_id = preferences.getString(Department_id, "1");

    }

    public void  loadMyPolls(){
        final String   URL =  "http://"+ WSadressIP.WSIP+":5000/cinternal/getPollsForEmployee";
        lstccPoll = new ArrayList<>();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONArray array = new JSONArray(response);
                            for (int i = 0; i < array.length(); i++) {

                                JSONObject question = array.getJSONObject(i);

                                String full_name;
                                if(question.getString("full_name").equals("null")){
                                    full_name = "dismissal";
                                }else{
                                    full_name = question.getString("full_name");
                                }

                                String option_c = "";
                                if(!question.getString("option_c").equals("null") && !question.getString("option_c").isEmpty() && !question.getString("option_c").equals("")){
                                    option_c = question.getString("option_c");
                                }else{
                                    option_c ="#ok#";
                                }

                                String option_d = "";
                                if(!question.getString("option_d").equals("null") && !question.getString("option_d").isEmpty() && !question.getString("option_d").equals("")){
                                    option_d = question.getString("option_d");
                                }else{
                                    option_d ="#ok#";
                                }
                                lstccPoll.add(new Poll(
                                        question.getInt("id"),
                                        full_name,
                                        question.getString("day"),
                                        question.getString("question"),
                                        question.getString("include"),
                                        question.getString("option_a"),
                                        question.getString("option_b"),
                                        option_c,
                                        option_d,
                                        "#ok#"
                                ));

                            }


                            PollAdapter adapter = new PollAdapter(getContext() ,R.layout.poll_row,lstccPoll );
                            adapter.notifyDataSetChanged();
                            //UIUtils.setListViewHeightBasedOnItems(lvPoll);
                            lvPoll.setAdapter(adapter);


                           // pollsNumber.setText("Polls ("+String.valueOf(array.length())+")");

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


    public void searchPollByQuestion(){
        String s = mtSearch.getText().toString();

        if(s.length() > 0) {
            lstcs = new ArrayList<>();
            for (Poll i : lstccPoll) {
                if (i.getQuestion().toUpperCase().contains(s.toUpperCase())) {
                    lstcs.add(new Poll(i.getId(),
                            i.getEmployee(),
                            i.getDay(),
                            i.getQuestion(),
                            i.getInclude(),
                            i.getOptiona(),
                            i.getOptionb(),
                            i.getOptionc(),
                            i.getOptiond(),
                            i.getAnswer()

                    ));
                }
            }

            PollAdapter adapter = new PollAdapter(getContext() ,R.layout.poll_row,lstcs );
            adapter.notifyDataSetChanged();
            //UIUtils.setListViewHeightBasedOnItems(lvPoll);
            lvPoll.setAdapter(adapter);
        }else{
            PollAdapter adapter = new PollAdapter(getContext() ,R.layout.poll_row,lstccPoll );
            adapter.notifyDataSetChanged();
            //UIUtils.setListViewHeightBasedOnItems(lvPoll);
            lvPoll.setAdapter(adapter);
        }
    }

    @Override
    public void onResume() {

        super.onResume();
        loadMyPolls();

    }
}
