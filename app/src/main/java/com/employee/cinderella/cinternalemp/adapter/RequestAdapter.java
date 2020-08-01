package com.employee.cinderella.cinternalemp.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.employee.cinderella.cinternalemp.MainActivity;
import com.employee.cinderella.cinternalemp.R;
import com.employee.cinderella.cinternalemp.model.Employee;
import com.employee.cinderella.cinternalemp.model.RequestModel;
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
import androidx.core.content.ContextCompat;

public class RequestAdapter extends ArrayAdapter<RequestModel> {
    Context context;
    int resource;

    public RequestAdapter(@NonNull Context context, int resource, @NonNull List<RequestModel> objects) {
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


        TextView txt_title = (TextView) v.findViewById(R.id.textView48);
        TextView txt_desc = (TextView) v.findViewById(R.id.textView49);
        TextView txt_status = (TextView) v.findViewById(R.id.textView50);
        TextView txt_type_date = (TextView) v.findViewById(R.id.textView52);
        TextView txt_date = (TextView) v.findViewById(R.id.textView98);

        Button btn_delete = v.findViewById(R.id.button17);
        CardView card = v.findViewById(R.id.cardViewMyRequest);
        ImageView img = v.findViewById(R.id.imageView28);

        if(getItem(position).getType().equals("1")){
            txt_type_date.setText("Repot Bugs/issues, "+ getItem(position).getPrivacy());
            img.setImageDrawable(getContext().getDrawable(R.drawable.bug));
        }else if(getItem(position).getType().equals("2")){
            txt_type_date.setText("Suggest New Project, "+ getItem(position).getPrivacy());
            img.setImageDrawable(getContext().getDrawable(R.drawable.idea));
        }else if(getItem(position).getType().equals("3")){
            txt_type_date.setText("Request API, "+ getItem(position).getPrivacy());
            img.setImageDrawable(getContext().getDrawable(R.drawable.api));
        }else if(getItem(position).getType().equals("4")){
            txt_type_date.setText("Request Office Material, "+ getItem(position).getPrivacy());
            img.setImageDrawable(getContext().getDrawable(R.drawable.material));
        }

        txt_title.setText(getItem(position).getTitle());
        txt_desc.setText(getItem(position).getDesc());
        txt_status.setText(getItem(position).getStatus());
        txt_date.setText(getItem(position).getDate());


        if(getItem(position).getStatus().equals("CLOSED")){
            txt_status.setTextColor(ContextCompat.getColor(context, R.color.colorAccentLight));
          //  txt_status.setBackgroundColor(ContextCompat.getColor(context, R.color.complementary));
            btn_delete.setVisibility(View.GONE);
        }

        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getItem(position).getStatus().equals("PENDING")){
                    AlertDialog diaBox = AskOption(String.valueOf(getItem(position).getId()),card);
                    diaBox.show();
                }
            }
        });



        return v;
    }

    public void deleteMyRequest(String req_id, CardView card){
        final String   URL =  "http://"+ WSadressIP.WSIP+":5000/cinternal/EmployeeDeleteHisRequest";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getContext(),"Request deleted!", Toast.LENGTH_LONG).show();
                        //card.setVisibility(View.GONE);
                        ((MainActivity)context).finish();

                        Intent intent = new Intent("com.employee.cinderella.cinternalemp.MainActivity");

                        intent.putExtra("redirectToRequests", "redirectToRequests");
                        ((MainActivity)context).startActivity(intent);
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
                params.put("request_id",req_id);
                return params;
            }
        };;
        Volley.newRequestQueue(getContext()).add(stringRequest);
    }

    private AlertDialog AskOption(String req_id, CardView card)
    {
        AlertDialog myQuittingDialogBox = new AlertDialog.Builder(getContext())
                // set message, title, and icon
                .setTitle("Delete")
                .setMessage("Do you want to Delete")
                .setIcon(R.drawable.delete)

                .setPositiveButton("Delete My Request", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        //your deleting code
                        deleteMyRequest(req_id, card);
                        dialog.dismiss();
                    }

                })
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();

                    }
                })
                .create();

        return myQuittingDialogBox;
    }
}
