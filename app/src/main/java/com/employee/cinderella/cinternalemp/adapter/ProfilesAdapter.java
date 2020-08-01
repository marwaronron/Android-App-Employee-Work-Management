package com.employee.cinderella.cinternalemp.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.employee.cinderella.cinternalemp.ProjectSingle;
import com.employee.cinderella.cinternalemp.R;
import com.employee.cinderella.cinternalemp.model.Employee;
import com.employee.cinderella.cinternalemp.model.Project;
import com.employee.cinderella.cinternalemp.service.MySingleton;
import com.employee.cinderella.cinternalemp.utils.WSadressIP;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;

public class ProfilesAdapter extends ArrayAdapter<Employee> {
    Context context;
    int resource;

    public ProfilesAdapter(@NonNull Context context, int resource, @NonNull List<Employee> objects) {
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

        TextView txt_full_name = (TextView) v.findViewById(R.id.textView36);
        TextView txt_department_name = (TextView) v.findViewById(R.id.textView38);
        TextView txt_position = (TextView) v.findViewById(R.id.textView39);
        ImageView profile_img = v.findViewById(R.id.imageView11);


       // txt_title.setText("ProjectSingle: "+getItem(position).getName());
        txt_full_name.setText(getItem(position).getFull_name());
        if(getItem(position).getDepartment_id().equals("")  || getItem(position).getDepartment_id().equals("null") || getItem(position).getDepartment_id().isEmpty()){
            txt_department_name.setText("");
        }else{
            txt_department_name.setText("Department: "+getItem(position).getDepartment_id());
        }
        //txt_department_name.setText(getItem(position).getDepartment_id());
        txt_position.setText(getItem(position).getPosition());

        //SEt profile image
      //  String theImage_URL = "http://"+ WSadressIP.WSIP +":5000/"+getItem(position).getFull_name().replace(" ", "")+".jpeg";
        String theImage_URL = "http://"+ WSadressIP.WSIP+":5000/"+getItem(position).getId()+".jpeg";
        ImageLoader imageLoader = MySingleton.getInstance(getContext()).getImageLoader();

        imageLoader.get(theImage_URL, new ImageLoader.ImageListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                profile_img.setImageResource(R.drawable.profile);
            }

            @Override
            public void onResponse(ImageLoader.ImageContainer response, boolean arg1) {
                if (response.getBitmap() != null) {

                    if(profile_img.equals("0")){
                        profile_img.setImageResource(R.drawable.profile);
                    }else{
                        profile_img.setImageBitmap(response.getBitmap());

                    }

                }
            }
        });



        return v;
    }

    private static Bitmap stringToBitmap(String encodedString) {
        try {
            byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }
}

