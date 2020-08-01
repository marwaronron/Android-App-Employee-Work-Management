package com.employee.cinderella.cinternalemp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.employee.cinderella.cinternalemp.adapter.ReportOrderAdapter;
import com.employee.cinderella.cinternalemp.adapter.TaskProjectAdapter;
import com.employee.cinderella.cinternalemp.model.ReportOrder;
import com.employee.cinderella.cinternalemp.model.Task;
import com.employee.cinderella.cinternalemp.transition.OrderCloseActivity;
import com.employee.cinderella.cinternalemp.utils.UIUtils;
import com.employee.cinderella.cinternalemp.utils.WSadressIP;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class OrderSingle extends AppCompatActivity {

    String  emp_id, dep_id ,order_db_id ,drag_date, drag_time;
    TextView txt_order_code, txt_prod_code, txt_acceptance, txt_internal_logs, txt_client_name, txt_country, txt_email,
    txt_phone, txt_ip_adress, txt_extra_details, txt_date, txt_employee, txt_drag_date_time;
    EditText edit_note;
    Button btn_drag, btn_close , btn_add_report;
    CardView cardClose;
    ListView lvReports;
    List<ReportOrder> lstccReports ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_single);


        //SET TOP BAR
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar9);
        mToolbar.setTitle("Client Order");
        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //get data
        SharedPreferences preferences = OrderSingle.this.getSharedPreferences(AuthActivity.MyPREFERENCES, Context.MODE_PRIVATE);
        dep_id = preferences.getString(AuthActivity.Department_id, "1");
        emp_id = preferences.getString("emp_id", "1");

        order_db_id = this.getIntent().getStringExtra("order_db_id");
        Log.v("%%","%%%%%%%%%%% id="+order_db_id);

        //set UI
        txt_order_code = findViewById(R.id.textView65);
        txt_prod_code = findViewById(R.id.textView66);
        txt_acceptance = findViewById(R.id.textView70);
        txt_internal_logs = findViewById(R.id.textView71);
        txt_client_name = findViewById(R.id.textView72);
        txt_country = findViewById(R.id.textView73);
        txt_email = findViewById(R.id.textView74);
        txt_phone = findViewById(R.id.textView75);
        txt_ip_adress = findViewById(R.id.textView76);
        txt_extra_details = findViewById(R.id.textView77);
        txt_date = findViewById(R.id.textView78);
        txt_employee = findViewById(R.id.textView79);
        txt_drag_date_time = findViewById(R.id.textView80);
        edit_note = findViewById(R.id.editText15);
        btn_drag = findViewById(R.id.button23);
        btn_close = findViewById(R.id.button24);
        cardClose = findViewById(R.id.cardClose);
        btn_add_report = findViewById(R.id.button18);
        lvReports = findViewById(R.id.lvReportOrder);
        lstccReports = new ArrayList<>();


        loadOrderData(order_db_id);


        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog diaBox = AskOption(order_db_id, edit_note.getText().toString(),drag_date, drag_time);
                diaBox.show();
            }
        });

        btn_drag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog diaBox = AskOptionDrag(order_db_id);
                diaBox.show();
            }
        });

        fillReportsList(order_db_id);

        btn_add_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
               Intent intent = new Intent(OrderSingle.this , AddReportOrderActivity.class);
                intent.putExtra("order_db_id", order_db_id);
                intent.putExtra("order_real_id",  txt_order_code.getText().toString());
                startActivity(intent);
            }
        });

    }

    public void loadOrderData(String order_db_id){
        final String   URL =  "http://"+ WSadressIP.WSIP+":5000/cinternal/getOrderByDbId";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray array = new JSONArray(response);

                            JSONObject product = array.getJSONObject(0);
                            txt_order_code.setText("Order id: "+product.getString("order_code"));
                            txt_prod_code.setText("Prod: "+product.getString("prod_code"));
                            txt_acceptance.setText("Bank Acceptance: "+product.getString("acceptance"));
                            txt_internal_logs.setText("Internal Logs: "+product.getString("internal_logs"));
                            txt_client_name.setText("Client Name: "+product.getString("client_name"));
                            txt_country.setText("Country: "+product.getString("country"));
                            txt_email.setText("Email: "+product.getString("email"));
                            txt_phone.setText("Phone: "+product.getString("phone"));
                            txt_ip_adress.setText("IP Adress: "+product.getString("ip_adress"));
                            txt_extra_details.setText("Extra Details: "+product.getString("extra_details"));
                            txt_date.setText("Date: "+product.getString("date"));

                            if(product.getString("drag").equals("0") ||(product.getString("drag").equals("1")&& (!product.getString("employee").equals(emp_id)))){
                                btn_close.setVisibility(View.GONE);
                                edit_note.setVisibility(View.GONE);
                                txt_employee.setVisibility(View.GONE);
                                txt_drag_date_time.setVisibility(View.GONE);
                                cardClose.setVisibility(View.GONE);

                            }else if(product.getString("drag").equals("1")){
                                btn_drag.setVisibility(View.GONE);
                                txt_employee.setText("You Dragged this Order: ");
                                txt_drag_date_time.setText("On "+product.getString("drag_date")+" at "+product.getString("drag_time") );
                            }else {
                                btn_drag.setVisibility(View.GONE);
                                btn_close.setVisibility(View.GONE);
                                edit_note.setVisibility(View.GONE);
                                txt_employee.setVisibility(View.GONE);
                                txt_drag_date_time.setVisibility(View.GONE);
                                cardClose.setVisibility(View.GONE);
                            }

                            drag_date = product.getString("drag_date");
                            drag_time = product.getString("drag_time");
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

                params.put("id",order_db_id);

                return params;
            }
        };;
        Volley.newRequestQueue(OrderSingle.this).add(stringRequest);
    }

    private AlertDialog AskOption(String id, String note, String drag_date, String drag_time)
    {
        AlertDialog myBoxC = new AlertDialog.Builder(OrderSingle.this)
                // set message, title, and icon
                .setTitle("Complete Order ")
                .setMessage("Do you want to set this Order as Complete?")
                .setIcon(R.drawable.purchase1)

                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        //your deleting code
                        closeOrder(id,note ,drag_date, drag_time);
                        dialog.dismiss();
                    }

                })
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                    }
                })
                .create();

        return myBoxC;
    }

    public void closeOrder(String id, String note, String drag_date, String drag_time){
        final String   URL =  "http://"+ WSadressIP.WSIP+":5000/cinternal/EmployeeCloseOrder";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(OrderSingle.this,"Order Closed",Toast.LENGTH_LONG).show();

                        finish();
                       // Intent intentb = new Intent(OrderSingle.this, OrderCloseActivity.class);
                       //startActivity(intentb);

                     /*   ((MainActivity)getApplicationContext()).finish();
                        Intent intent = new Intent("com.employee.cinderella.cinternalemp.MainActivity");

                        intent.putExtra("redirectToOrders", "redirectToOrders");
                        ((MainActivity)getApplicationContext()).startActivity(intent);*/
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(OrderSingle.this,"Order Need to be Verified with admin",Toast.LENGTH_LONG).show();
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
        Volley.newRequestQueue(OrderSingle.this).add(stringRequest);
    }

    private AlertDialog AskOptionDrag(String id)
    {
        AlertDialog myBoxD = new AlertDialog.Builder(OrderSingle.this)
                // set message, title, and icon
                .setTitle("Drag Order ")
                .setMessage("Do you want to Drag this Order")
                .setIcon(R.drawable.orderpending)

                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        //your deleting code
                        dragOrder(id);
                        dialog.dismiss();
                    }

                })
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                    }
                })
                .create();

        return myBoxD;
    }

    public void dragOrder(String id){
        final String   URL =  "http://"+ WSadressIP.WSIP+":5000/cinternal/EmployeeDragOrder";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(OrderSingle.this,"Order Dragged To be Done !",Toast.LENGTH_LONG).show();

                        finish();
                      /*  ((MainActivity)getApplicationContext()).finish();
                        Intent intent = new Intent("com.employee.cinderella.cinternalemp.MainActivity");

                        intent.putExtra("redirectToOrders", "redirectToOrders");
                        ((MainActivity)getApplicationContext()).startActivity(intent);*/
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(OrderSingle.this,"Order Need to be Verified with admin",Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Content-Type","application/x-www-form-urlencoded");
                String drag_date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
                String drag_time = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date());

                params.put("id",id);
                params.put("drag_date",drag_date);
                params.put("drag_time",drag_time);
                params.put("employee",emp_id);

                return params;
            }
        };;
        Volley.newRequestQueue(OrderSingle.this).add(stringRequest);
    }

    public void fillReportsList(String order_db_id){
        final String   URL =  "http://"+ WSadressIP.WSIP+":5000/cinternal/getOrderReportsByOrderId";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray array = new JSONArray(response);
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject product = array.getJSONObject(i);

                                lstccReports.add(new ReportOrder(
                                        product.getInt("id"),
                                        product.getString("full_name"),
                                        product.getString("report"),
                                        product.getString("date"),
                                        product.getString("emp_id"),
                                        product.getString("id_of_order")


                                ));

                            }
                            ReportOrderAdapter adapter = new ReportOrderAdapter(OrderSingle.this ,R.layout.report_order_row,lstccReports );
                            adapter.notifyDataSetChanged();
                            lvReports.setAdapter(adapter);
                            UIUtils.setListViewHeightBasedOnItems(lvReports);


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
                params.put("order_id",order_db_id);
                return params;
            }
        };;
        Volley.newRequestQueue(OrderSingle.this).add(stringRequest);
    }
}
