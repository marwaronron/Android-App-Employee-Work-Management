package com.employee.cinderella.cinternalemp.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.employee.cinderella.cinternalemp.R;
import com.employee.cinderella.cinternalemp.model.RequestModel;
import com.employee.cinderella.cinternalemp.utils.WSadressIP;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

public class RequestPublicAdapter extends ArrayAdapter<RequestModel> {
    Context context;
    int resource;

    public RequestPublicAdapter(@NonNull Context context, int resource, @NonNull List<RequestModel> objects) {
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


        TextView txt_title = (TextView) v.findViewById(R.id.textView53);
        TextView txt_desc = (TextView) v.findViewById(R.id.textView105);
        TextView txt_by_person = (TextView) v.findViewById(R.id.textView54);
        TextView txt_type_date = (TextView) v.findViewById(R.id.textView51);

        ImageView img = v.findViewById(R.id.imageView14);

        if(getItem(position).getType().equals("1")){
            txt_by_person.setText("Repot Bugs/issues, "+getItem(position).getEmployee_id());
            img.setImageDrawable(getContext().getDrawable(R.drawable.bug));
        }else if(getItem(position).getType().equals("2")){
            txt_by_person.setText("Suggest New Project, "+getItem(position).getEmployee_id());
            img.setImageDrawable(getContext().getDrawable(R.drawable.idea));
        }else if(getItem(position).getType().equals("3")){
            txt_by_person.setText("Request API, "+getItem(position).getEmployee_id());
            img.setImageDrawable(getContext().getDrawable(R.drawable.api));
        }else if(getItem(position).getType().equals("4")){
            txt_by_person.setText("Request Office Material, "+getItem(position).getEmployee_id());
            img.setImageDrawable(getContext().getDrawable(R.drawable.material));
        }

        txt_title.setText(getItem(position).getTitle());
        txt_desc.setText(getItem(position).getStatus());
        if(getItem(position).getStatus().equals("CLOSED")){
            txt_desc.setTextColor(ContextCompat.getColor(context, R.color.colorAccentLight));
        }
        txt_type_date.setText(getItem(position).getDate());

        return v;
    }

}
