package com.employee.cinderella.cinternalemp.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.employee.cinderella.cinternalemp.AddReportOrderActivity;
import com.employee.cinderella.cinternalemp.AuthActivity;
import com.employee.cinderella.cinternalemp.EditReportOrderActivity;
import com.employee.cinderella.cinternalemp.OrderSingle;
import com.employee.cinderella.cinternalemp.R;
import com.employee.cinderella.cinternalemp.model.Employee;
import com.employee.cinderella.cinternalemp.model.ReportOrder;
import com.employee.cinderella.cinternalemp.service.MySingleton;
import com.employee.cinderella.cinternalemp.utils.WSadressIP;

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

public class ReportOrderAdapter extends ArrayAdapter<ReportOrder> {
    Context context;
    int resource;

    public ReportOrderAdapter(@NonNull Context context, int resource, @NonNull List<ReportOrder> objects) {
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

       // TextView txt_full_name = (TextView) v.findViewById(R.id.textView85);
        TextView txt_date = (TextView) v.findViewById(R.id.textView87);
        TextView txt_report = (TextView) v.findViewById(R.id.textView86);

        Integer xl = getItem(position).getReport_txt().length();

        if(xl < 45){
            txt_report.setHeight(60);
        }else if(xl >= 45 && xl < 90){
            txt_report.setHeight(100);
        }else if(xl >= 90 && xl < 135){
            txt_report.setHeight(180);
        }else if(xl >= 135 && xl < 180){
            txt_report.setHeight(220);
        }else if(xl >= 180 && xl < 255){
            txt_report.setHeight(320);
        }else if(xl >= 225 && xl < 270){
            txt_report.setHeight(360);
        }else if(xl >= 270 && xl < 315){
            txt_report.setHeight(420);
        }else if(xl >= 315 && xl < 360){
            txt_report.setHeight(460);
        }else if(xl >= 360 && xl < 405){
            txt_report.setHeight(520);
        }else if(xl >= 405 && xl < 450){
            txt_report.setHeight(560);
        }else if(xl >= 450 && xl < 540){
            txt_report.setHeight(620);
        }else if(xl >= 540 && xl < 585){
            txt_report.setHeight(660);
        }else if(xl >= 585 && xl < 630){
            txt_report.setHeight(720);
        }else if(xl >= 630 && xl < 675){
            txt_report.setHeight(860);
        }else{
            txt_report.setHeight(1200);
        }

       // txt_full_name.setText("Posted By "+getItem(position).getEmployee_name()+" ");
        txt_date.setText(getItem(position).getEmployee_name()+", "+getItem(position).getDate()+".");
        txt_report.setText(getItem(position).getReport_txt());


        Button btn_edit = v.findViewById(R.id.button20);
        Button  btn_delete = v.findViewById(R.id.button21);
        CardView mainCard =  v.findViewById(R.id.repOrdCard);

        SharedPreferences preferences = getContext().getSharedPreferences(AuthActivity.MyPREFERENCES, Context.MODE_PRIVATE);
        String emp_id = preferences.getString("emp_id", "1");

        if(!getItem(position).getEmp_id().equals(emp_id)){

            btn_edit.setVisibility(View.GONE);
            btn_delete.setVisibility(View.GONE);
        }

        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ( (OrderSingle)getContext()).finish();
                Intent intent = new Intent(getContext() , EditReportOrderActivity.class);
                intent.putExtra("order_report_id", String.valueOf(getItem(position).getId()));
                intent.putExtra("order_report_text", getItem(position).getReport_txt());
                intent.putExtra("order_db_id", getItem(position).getOrder_db_id());
                ( (OrderSingle)getContext()).startActivity(intent);
            }
        });

        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v("%%","MMMMMMMMMMMM");
                //deleteMyReport(String.valueOf(getItem(position).getId()),mainCard);
                AlertDialog diaBox = AskOption(String.valueOf(getItem(position).getId()),mainCard);
                diaBox.show();
            }
        });

        return v;
    }

    public void deleteMyReport(String id, CardView mainCard){
        final String   URL =  "http://"+ WSadressIP.WSIP+":5000/cinternal/deleteMyOrderReport";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        mainCard.setVisibility(View.GONE);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(),"You are Currently Unabel To Delete Comment. Please Contact Admin",Toast.LENGTH_LONG).show();

                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Content-Type","application/x-www-form-urlencoded");
                params.put("report_id",id);

                return params;
            }
        };;
        Volley.newRequestQueue(getContext()).add(stringRequest);
    }

    private AlertDialog AskOption(String id, CardView mainCard)
    {  Log.v("%%","MMMMMMMMMMMM 2");
        AlertDialog myBoxEE = new AlertDialog.Builder(getContext())
                // set message, title, and icon
                .setTitle("Close Order ")
                .setMessage("Do you want to Close this Order")
                .setIcon(R.drawable.delete)

                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        deleteMyReport(id,mainCard);
                        dialog.dismiss();
                    }

                })
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                    }
                })
                .create();

        return myBoxEE;
    }
}

