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
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.employee.cinderella.cinternalemp.AddHoliday;
import com.employee.cinderella.cinternalemp.AuthActivity;
import com.employee.cinderella.cinternalemp.Holidays;
import com.employee.cinderella.cinternalemp.MainActivity;
import com.employee.cinderella.cinternalemp.R;
import com.employee.cinderella.cinternalemp.model.Holiday;
import com.employee.cinderella.cinternalemp.model.Poll;
import com.employee.cinderella.cinternalemp.utils.WSadressIP;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

public class HolidaysAdapter  extends ArrayAdapter<Holiday> {
    Context context;
    int resource;

    public  HolidaysAdapter (@NonNull Context context, int resource, @NonNull List<Holiday> objects) {
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
        TextView txt_type = (TextView) v.findViewById(R.id.textViewHolidayType);
        TextView txt_status =  (TextView) v.findViewById(R.id.textViewHolidayStatus);
        TextView txt_from_to = (TextView) v.findViewById(R.id.textViewHolidayFromTo);
        TextView txt_to = (TextView) v.findViewById(R.id.textView34);
       // final LinearLayout layout =  v.findViewById(R.id.holidayStatusColor);
        Button btnCancel = v.findViewById(R.id.button10Cancel);
      //  Button btnDelete = v.findViewById(R.id.button11Delete);
        CardView card  = v.findViewById(R.id.cardViewHoliday1);

        //2: SET VALUES
        txt_type.setText(getItem(position).getType());
        txt_status.setText(getItem(position).getStatus());
        txt_from_to.setText(getItem(position).getStart_date());
        txt_to.setText(getItem(position).getEnd_date());

        if(getItem(position).getStatus().equals("APPROVED")){
            btnCancel.setVisibility(View.GONE);
          //  btnDelete.setVisibility(View.GONE);
            //layout.setBackgroundColor(ContextCompat.getColor(context, R.color.doing));
            txt_status.setTextColor(ContextCompat.getColor(context, R.color.complementary));
        }else if(getItem(position).getStatus().equals("PENDING")){
            //btnCancel.setVisibility(View.GONE);
           // btnDelete.setVisibility(View.GONE);
            //layout.setBackgroundColor(ContextCompat.getColor(context, R.color.darkorange));
            txt_status.setTextColor(ContextCompat.getColor(context, R.color.triadicb));
        }else if(getItem(position).getStatus().equals("REJECTED")){
           // btnCancel.setVisibility(View.GONE);
            //btnDelete.setVisibility(View.GONE);
            //layout.setBackgroundColor(ContextCompat.getColor(context, R.color.red));
            txt_status.setTextColor(ContextCompat.getColor(context, R.color.triadica));
        }

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // deleteHoliday(String.valueOf(getItem(position).getId()),card);

                AlertDialog cancelBox = AskOptionCancel(String.valueOf(getItem(position).getId()),card);
                cancelBox.show();
            }
        });

      /*  btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // deleteHoliday(String.valueOf(getItem(position).getId()),card);

                AlertDialog diaBox = AskOption(String.valueOf(getItem(position).getId()),card);
                diaBox.show();
            }
        }); */
        return v;
    }

    public void deleteHoliday(String holiday_id,CardView card){
        final String   URL =  "http://"+ WSadressIP.WSIP+":5000/cinternal/deleteByEmployee";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        //card.setVisibility(View.GONE);

                        ((Holidays)context).finish();
                        Intent intent = new Intent(getContext(), Holidays.class);
                        ((Holidays)context).startActivity(intent);
                        ((Holidays)context).overridePendingTransition(0, 0);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Toast.makeText(getContext(), "you have No Tasks today", Toast.LENGTH_SHORT).show();

                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Content-Type","application/x-www-form-urlencoded");
                params.put("holiday_id",holiday_id);
                return params;
            }
        };;
        Volley.newRequestQueue(getContext()).add(stringRequest);
    }

    private AlertDialog AskOption(String id, CardView mainCard)
    {
        AlertDialog myBoxF = new AlertDialog.Builder(getContext())
                // set message, title, and icon
                .setTitle("Delete Holiday ")
                .setMessage("Do you want to Delete this Holiday?")
                .setIcon(R.drawable.delete)

                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        deleteHoliday(id,mainCard);
                        dialog.dismiss();
                    }

                })
                .setNegativeButton("Keep", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                    }
                })
                .create();

        return myBoxF;
    }

    private AlertDialog AskOptionCancel(String id, CardView mainCard)
    {
        AlertDialog myBoxG = new AlertDialog.Builder(getContext())
                // set message, title, and icon
                .setTitle("Cancel Holiday Request")
                .setMessage("Do you want to Cancel Holiday Request?")
                .setIcon(R.drawable.cancel)

                .setPositiveButton("Cancel", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        deleteHoliday(id,mainCard);
                        dialog.dismiss();
                    }

                })
                .setNegativeButton("Keep", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                    }
                })
                .create();

        return myBoxG;
    }
}
