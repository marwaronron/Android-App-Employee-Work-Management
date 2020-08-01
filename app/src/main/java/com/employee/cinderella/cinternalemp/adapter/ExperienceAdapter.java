package com.employee.cinderella.cinternalemp.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.employee.cinderella.cinternalemp.AuthActivity;
import com.employee.cinderella.cinternalemp.ExperienceActivity;
import com.employee.cinderella.cinternalemp.MainActivity;
import com.employee.cinderella.cinternalemp.R;
import com.employee.cinderella.cinternalemp.model.ExperienceModel;
import com.employee.cinderella.cinternalemp.model.Holiday;
import com.employee.cinderella.cinternalemp.utils.WSadressIP;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

public class ExperienceAdapter extends ArrayAdapter<ExperienceModel> {
    Context context;
    int resource;

    String emp_id ;

    public   ExperienceAdapter (@NonNull Context context, int resource, @NonNull List<ExperienceModel> objects) {
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
        TextView txt_skill = (TextView) v.findViewById(R.id.textView103);
        TextView txt_nbTaskDone = (TextView) v.findViewById(R.id.textView104);
        Button btn_delete = (Button) v.findViewById(R.id.button25);


        //2: SET VALUES
        txt_skill.setText(getItem(position).getSkill());

        txt_nbTaskDone.setText("Tasks Done: "+getItem(position).getNbTasksDone());

        if(!getItem(position).getNbTasksDone().equals("Not yet")){
            btn_delete.setVisibility(View.GONE);
        }

        SharedPreferences preferences = getContext().getSharedPreferences(AuthActivity.MyPREFERENCES, Context.MODE_PRIVATE);
        emp_id = preferences.getString("emp_id", "1");

        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // if(!getItem(position).getNbTasksDone().equals("Not yet")){
                    AlertDialog diaBox = AskOption(String.valueOf(getItem(position).getSkill()));
                    diaBox.show();
                //}
            }
        });

        return v;
    }

    private AlertDialog AskOption(String skill)
    {
        AlertDialog mySkillDialogBox = new AlertDialog.Builder(getContext())
                // set message, title, and icon
                .setTitle("Delete")
                .setMessage("Do you want to Delete")
                .setIcon(R.drawable.delete)

                .setPositiveButton("Delete My Skill", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        //your deleting code
                        deleteMySkill(skill);
                        dialog.dismiss();
                    }

                })
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();

                    }
                })
                .create();

        return mySkillDialogBox;
    }

    public void deleteMySkill(String skill){
        final String   URL =  "http://"+ WSadressIP.WSIP+":5000/cinternal/EmployeeDeleteHisSkill";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getContext(),"Skill deleted!", Toast.LENGTH_LONG).show();
                        //card.setVisibility(View.GONE);
                        ((ExperienceActivity)context).finish();

                        Intent intent = new Intent(context, ExperienceActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);

                        ((ExperienceActivity)context).startActivity(intent);
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
                params.put("employee_id",emp_id);
                params.put("skill",skill);
                return params;
            }
        };;
        Volley.newRequestQueue(getContext()).add(stringRequest);
    }
}
