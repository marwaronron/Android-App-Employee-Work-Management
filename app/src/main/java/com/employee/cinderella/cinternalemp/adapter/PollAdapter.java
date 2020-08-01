package com.employee.cinderella.cinternalemp.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.employee.cinderella.cinternalemp.AuthActivity;
import com.employee.cinderella.cinternalemp.MainActivity;
import com.employee.cinderella.cinternalemp.ProjectSingle;
import com.employee.cinderella.cinternalemp.R;
import com.employee.cinderella.cinternalemp.model.Poll;
import com.employee.cinderella.cinternalemp.utils.WSadressIP;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

public class PollAdapter extends ArrayAdapter<Poll> {
    Context context;
    int resource;

    public  PollAdapter(@NonNull Context context, int resource, @NonNull List<Poll> objects) {
        super(context, resource, objects);

        this.context = context;
        this.resource = resource;
    }

    @RequiresApi(api = 28)
    @SuppressLint("ResourceAsColor")
    @NonNull
    @Override
    public View getView(final int position, View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        final View v = inflater.inflate(resource, parent, false);

        //1: SET UI
        TextView txt_question = (TextView) v.findViewById(R.id.textView31);
        TextView txt_by_day = (TextView) v.findViewById(R.id.textView30);
        TextView txt_department = (TextView) v.findViewById(R.id.textView29);
        RadioButton rb1 = v.findViewById(R.id.radioButtonPoll1);
        RadioButton rb2 = v.findViewById(R.id.radioButtonPoll2);
        RadioButton rb3 = v.findViewById(R.id.radioButtonPoll3);
        RadioButton rb4 = v.findViewById(R.id.radioButtonPoll4);
        RadioGroup rg = v.findViewById(R.id.radioGroupPoll);
        TextView option_a_votes = v.findViewById(R.id.textViewPoll1);
        TextView option_b_votes = v.findViewById(R.id.textViewPoll2);
        TextView option_c_votes = v.findViewById(R.id.textViewPoll3);
        TextView option_d_votes = v.findViewById(R.id.textViewPoll4);
        Button btnConfirm = v.findViewById(R.id.button8);

        //2: SET QUESTION + ASKED BY +DAY + INCLUDE
        txt_question.setText(getItem(position).getQuestion());
        txt_by_day.setText(getItem(position).getEmployee()+", "+getItem(position).getDay());
        if(!getItem(position).getInclude().equals("all_company")){
            txt_department.setText("MY DEP");
            txt_department.setTextColor(ContextCompat.getColor(context, R.color.colorAccentLight));
        }

        //3: initialize all vote as 3 until call webservice
        option_a_votes.setText("0 votes");
        option_b_votes.setText("0 votes");
        option_c_votes.setText("0 votes");
        option_d_votes.setText("0 votes");


        //4: SET QUESTION OPTIONS
        rb1.setText(getItem(position).getOptiona());
        rb2.setText(getItem(position).getOptionb());
        if(!getItem(position).getOptionc().equals("#ok#")){
            rb3.setText(getItem(position).getOptionc());
        }else{
            rb3.setVisibility(View.GONE);
            option_c_votes.setVisibility(View.GONE);
        }
        if(!getItem(position).getOptiond().equals("#ok#")){
            rb4.setText(getItem(position).getOptiond());
        }else{
            rb4.setVisibility(View.GONE);
            option_d_votes.setVisibility(View.GONE);
        }

        //5: SET MY VOTE ANSWER
        getMyAnswerIfExist(String.valueOf(getItem(position).getId()),position,rb1,rb2,rb3,rb4,btnConfirm);

        //6: GET EMPLOYEE ID (to be used in next webservices)
        SharedPreferences preferences = getContext().getSharedPreferences(AuthActivity.MyPREFERENCES, Context.MODE_PRIVATE);
        String emp_id = preferences.getString("emp_id", "1");

        //7: if employee want to vote/change his vote
       // btnConfirm.setVisibility(View.GONE);
        btnConfirm.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.softgray));
        btnConfirm.setTextColor(ContextCompat.getColor(context, R.color.darker_gray));
        btnConfirm.setClickable(false);
        btnConfirm.setEnabled(false);
        rg.setOnCheckedChangeListener((group, checkedId) -> {
           // btnConfirm.setVisibility(View.VISIBLE);
            btnConfirm.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.colorPrimary));
            btnConfirm.setTextColor(ContextCompat.getColor(context, R.color.white));
            btnConfirm.setClickable(true);
            btnConfirm.setEnabled(true);
            RadioButton rb=(RadioButton)v.findViewById(checkedId);
            btnConfirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(rb.getText().equals(getItem(position).getOptiona())){
                        updateMyVote(getItem(position).getOptiona(),getItem(position).getAnswer(),emp_id,String.valueOf(getItem(position).getId()));
                    }else if(rb.getText().equals(getItem(position).getOptionb())){
                        updateMyVote(getItem(position).getOptionb(),getItem(position).getAnswer(),emp_id,String.valueOf(getItem(position).getId()));
                    }else if(rb.getText().equals(getItem(position).getOptionc())){
                        updateMyVote(getItem(position).getOptionc(),getItem(position).getAnswer(),emp_id,String.valueOf(getItem(position).getId()));
                    }else if(rb.getText().equals(getItem(position).getOptiond())){
                        updateMyVote(getItem(position).getOptiond(),getItem(position).getAnswer(),emp_id,String.valueOf(getItem(position).getId()));
                    }
                }
            });

        });

        //8: get number of votes of each option
        getVotes(getItem(position).getOptiona(),option_a_votes,
                getItem(position).getOptionb(),option_b_votes,
                getItem(position).getOptionc(),option_c_votes,
                getItem(position).getOptiond(),option_d_votes,
                String.valueOf(getItem(position).getId()));
        return v;
    }


    public void  getVotes(String id_a,TextView option_a_votes,String id_b,TextView option_b_votes,String id_c,TextView option_c_votes,String id_d,TextView option_d_votes, String question_id){
        final String   URL =  "http://"+ WSadressIP.WSIP+":5000/cinternal/getNumberOfVotesPerOption";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONArray array = new JSONArray(response);
                            for (int i = 0; i < array.length(); i++) {

                                JSONObject product = array.getJSONObject(i);

                                if(id_a.equals(product.getString("option_selected"))){
                                    if(product.getString("nb_votes").length() >0){
                                        option_a_votes.setText(product.getString("nb_votes")+ " votes");
                                    }else{
                                        option_a_votes.setText("0");
                                    }

                                }else if(id_b.equals(product.getString("option_selected"))){
                                    if(product.getString("nb_votes").length() >0){
                                        option_b_votes.setText(product.getString("nb_votes")+ " votes");
                                    }else{
                                        option_b_votes.setText("0");
                                    }

                                }else  if(id_c.equals(product.getString("option_selected"))){
                                    if(product.getString("nb_votes").length() >0){
                                        option_c_votes.setText(product.getString("nb_votes")+ " votes");
                                    }else{
                                        option_c_votes.setText("0");
                                    }

                                }else   if(id_d.equals(product.getString("option_selected"))){
                                    if(product.getString("nb_votes").length() >0){
                                        option_d_votes.setText(product.getString("nb_votes")+ "votes");
                                    }else{
                                        option_d_votes.setText("0");
                                    }

                                }

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
                params.put("question_id",question_id);
                return params;
            }
        };;
        Volley.newRequestQueue(getContext()).add(stringRequest);
    }

    public void updateMyVote(String new_vote_id,String old_vote_id,String emp_id, String question_id){
        final String   URL =  "http://"+ WSadressIP.WSIP+":5000/cinternal/employeeUpdateOrAddVote";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(old_vote_id.equals("#ok#")){
                            Toast.makeText(getContext(), "Your vote is added", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(getContext(), "your vote is update", Toast.LENGTH_SHORT).show();
                        }

                       ((MainActivity)context).finish();
                        Intent intent = new Intent("com.employee.cinderella.cinternalemp.MainActivity");

                        intent.putExtra("redirectToPolls", "redirectToPolls");
                        ((MainActivity)context).startActivity(intent);
                       // ((MainActivity)context).overridePendingTransition(0, 0);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Log.v("XX","%%%%%%%%%%%% no");
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Content-Type","application/x-www-form-urlencoded");
                params.put("new_vote_id",new_vote_id);
                params.put("old_vote_id",old_vote_id);
                params.put("emp_id",emp_id);
                params.put("question_id",question_id);
                return params;
            }
        };;
        Volley.newRequestQueue(getContext()).add(stringRequest);
    }

    public void getMyAnswerIfExist(String question_id,int position,RadioButton rb1,RadioButton rb2,RadioButton rb3,RadioButton rb4, Button btnConfirm ){
        final String   URL =  "http://"+ WSadressIP.WSIP+":5000/cinternal/getPollsEmployeeAnswer";
        //final String[] answer = new String[1];
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONArray arrayB = new JSONArray(response);

                            if(arrayB.length() >0){
                                JSONObject object_answer = arrayB.getJSONObject(0);

                                getItem(position).setAnswer(object_answer.getString("option_selected"));

                                    if(getItem(position).getAnswer().equals(getItem(position).getOptiona())){
                                        rb1.setChecked(true);

                                    }else if (getItem(position).getAnswer().equals(getItem(position).getOptionb())){
                                        rb2.setChecked(true);

                                    }else if(getItem(position).getAnswer().equals(getItem(position).getOptionc())){
                                        rb3.setChecked(true);

                                    }else if (getItem(position).getAnswer().equals(getItem(position).getOptiond())){
                                        rb4.setChecked(true);

                                    }
                               // btnConfirm.setVisibility(View.GONE);
                                btnConfirm.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.softgray));
                                btnConfirm.setTextColor(ContextCompat.getColor(context, R.color.darker_gray));
                                btnConfirm.setClickable(false);
                                btnConfirm.setEnabled(false);

                            }else{
                                getItem(position).setAnswer("#ok#");
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
                SharedPreferences preferences = getContext().getSharedPreferences(AuthActivity.MyPREFERENCES, Context.MODE_PRIVATE);
                String emp_id = preferences.getString("emp_id", "1");
                params.put("question_id", question_id);
                params.put("emp_id", emp_id);
                return params;
            }
        };;
        Volley.newRequestQueue(getContext()).add(stringRequest);
    }


}

