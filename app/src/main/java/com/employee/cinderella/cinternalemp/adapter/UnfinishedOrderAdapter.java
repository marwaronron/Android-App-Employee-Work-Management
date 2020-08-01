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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.employee.cinderella.cinternalemp.AuthActivity;
import com.employee.cinderella.cinternalemp.OrderSingle;
import com.employee.cinderella.cinternalemp.ProjectSingle;
import com.employee.cinderella.cinternalemp.R;
import com.employee.cinderella.cinternalemp.model.OrderClient;
import com.employee.cinderella.cinternalemp.model.Project;
import com.employee.cinderella.cinternalemp.utils.WSadressIP;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;

public class UnfinishedOrderAdapter extends ArrayAdapter<OrderClient> {
    Context context;
    int resource;


    public UnfinishedOrderAdapter(@NonNull Context context, int resource, @NonNull List<OrderClient> objects) {
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

        TextView txt_order_id = (TextView) v.findViewById(R.id.textView67);
        TextView txt_prod_id = (TextView) v.findViewById(R.id.textView68);
        TextView txt_Date = (TextView) v.findViewById(R.id.textView69);



        CardView card = v.findViewById(R.id.unfinishedOrderCard);





        txt_order_id.setText(getItem(position).getOrder_code());
        txt_prod_id.setText(getItem(position).getProd_code());
        txt_Date.setText(getItem(position).getDate());


      /*  String close_date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        String close_time = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date());
        String drag_date = getItem(position).getDrag_date();
        String drag_time = getItem(position).getDrag_time();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        try {
            Date endDate = simpleDateFormat.parse(close_date+" "+close_time);
            Date startDate = simpleDateFormat.parse(drag_date+" "+drag_time);
            long different = endDate.getTime() - startDate.getTime();

            long secondsInMilli = 1000;
            long minutesInMilli = secondsInMilli * 60;
            long hoursInMilli = minutesInMilli * 60;
            long daysInMilli = hoursInMilli * 24;

           long elapsedDays = different / daysInMilli;
            different = different % daysInMilli;

            long elapsedHours = different / hoursInMilli;
            different = different % hoursInMilli;

            long elapsedMinutes = different / minutesInMilli;
            different = different % minutesInMilli;

            //long elapsedSeconds = different / secondsInMilli;
            //Log.v("tt","days= "+elapsedDays+" Hours= "+elapsedHours+" Minutes= "+ elapsedMinutes);
            Log.v("tt"," Minutes= "+ elapsedMinutes);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        */




        SharedPreferences preferences = getContext().getSharedPreferences(AuthActivity.MyPREFERENCES, Context.MODE_PRIVATE);
        String emp_id = preferences.getString("emp_id", "1");



        if(getItem(position).getDrag().equals("0") ||(getItem(position).getDrag().equals("1")&& (!getItem(position).getEmployee().equals(emp_id)))){

        }else    if(getItem(position).getDrag().equals("1")){

        }


        card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    Intent intent = new Intent(getContext(), OrderSingle.class);
                    intent.putExtra("order_db_id", String.valueOf(getItem(position).getId()));
                    context.startActivity(intent);

            }
        });


        return v;
    }

    public void closeOrder(String id, String note, String drag_date, String drag_time){
       // Toast.makeText(getContext(),"okkkkk",Toast.LENGTH_LONG).show();

        final String   URL =  "http://"+ WSadressIP.WSIP+":5000/cinternal/EmployeeCloseOrder";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getContext(),"Order Closed",Toast.LENGTH_LONG).show();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(getContext(),"Order Need to be Verified with admin",Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Content-Type","application/x-www-form-urlencoded");
                String close_date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
                String close_time = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date());
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                long elapsedMinutes=0;
                try {
                    Date endDate = simpleDateFormat.parse(close_date+" "+close_time);
                    Date startDate = simpleDateFormat.parse(drag_date+" "+drag_time);
                    long different = endDate.getTime() - startDate.getTime();

                    long secondsInMilli = 1000;
                    long minutesInMilli = secondsInMilli * 60;

                    elapsedMinutes = different / minutesInMilli;

                    Log.v("tt"," Minutes= "+ elapsedMinutes);

                } catch (ParseException e) {
                    e.printStackTrace();
                }
                params.put("id",id);
                params.put("close_date",close_date);
                params.put("close_time",close_time);
                params.put("note",note);
                params.put("difference",String.valueOf(elapsedMinutes));
                return params;
            }
        };;
        Volley.newRequestQueue(getContext()).add(stringRequest);
    }

    private AlertDialog AskOption(String id, String note,EditText et_addNote, Button btn_confirm, Button btn_cancel, String drag_date, String drag_time)
    {
        AlertDialog myBoxB = new AlertDialog.Builder(getContext())
                // set message, title, and icon
                .setTitle("Close Order "+drag_time)
                .setMessage("Do you want to Close this Order")
                .setIcon(R.drawable.purchase1)

                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        //your deleting code
                        closeOrder(id,note,drag_date,drag_time);
                        dialog.dismiss();
                    }

                })
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        et_addNote.setVisibility(View.GONE);
                        btn_confirm.setVisibility(View.GONE);
                        btn_cancel.setVisibility(View.GONE);
                        dialog.dismiss();

                    }
                })
                .create();

        return myBoxB;
    }
}