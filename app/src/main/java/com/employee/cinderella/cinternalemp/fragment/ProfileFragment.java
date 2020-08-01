package com.employee.cinderella.cinternalemp.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.employee.cinderella.cinternalemp.AuthActivity;
import com.employee.cinderella.cinternalemp.EditProfileActivity;
import com.employee.cinderella.cinternalemp.ExperienceActivity;
import com.employee.cinderella.cinternalemp.Holidays;
import com.employee.cinderella.cinternalemp.PasswordActivity;
import com.employee.cinderella.cinternalemp.R;
import com.employee.cinderella.cinternalemp.service.MySingleton;
import com.employee.cinderella.cinternalemp.utils.WSadressIP;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;


public class ProfileFragment extends Fragment {
    String full_name, department_name, emp_id, emp_position, emp_email, emp_phonework,  img ;
    TextView txfull_name, txdepartment_name, txemp_position, txemp_email, txemp_phonework, txemp_address;
    ImageView profile_img;
  //  Button btnEdit, btnHolidays, btnSkills;
    LinearLayout goHolidays , goSkills, goEdit, goPwd;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profile, container, false);
        ///////////////////////////////////////////// SETTING CONECTION WITH UI
        txfull_name = v.findViewById(R.id.textViewName);
        txdepartment_name = v.findViewById(R.id.textViewDep);
        txemp_position = v.findViewById(R.id.textViewPosition);
        txemp_email = v.findViewById(R.id.textViewEmail);
        txemp_phonework = v.findViewById(R.id.textView101);

        profile_img = v.findViewById(R.id.imageViewPr);
       // btnEdit = v.findViewById(R.id.buttonEdit);
        //btnHolidays = v.findViewById(R.id.buttonHolidays);


        goHolidays = v.findViewById(R.id.goHolidays);
        goSkills = v.findViewById(R.id.goSkills);
        goEdit = v.findViewById(R.id.goEdit);
        goPwd = v.findViewById(R.id.goPassword);
        ///////////////////////////////////////////// SETTING DATA IN UI ELEMENTS
        txfull_name.setText(full_name);

        if(department_name != null && !department_name.isEmpty() && !department_name.equals("null")){
            txdepartment_name.setText(department_name);
        }else{
            txdepartment_name.setText("_");
        }

        if(emp_position != null && !emp_position.isEmpty() && !emp_position.equals("null")){
            txemp_position.setText(emp_position);
        }else{
            txemp_position.setText("_");
        }

        if(emp_email != null && !emp_email.isEmpty() && !emp_email.equals("null")){
            txemp_email.setText(emp_email);
        }else{
            txemp_email.setText("_");
        }

        if(emp_phonework != null && !emp_phonework.isEmpty() && !emp_phonework.equals("null")){
            txemp_phonework.setText(emp_phonework);
        }else{
            txemp_phonework.setText("_");
        }

        //IMAGE SET
       // String theImage_URL = "http://"+ WSadressIP.WSIP +":5000/"+full_name.replace(" ", "")+".jpeg";
        String theImage_URL = "http://"+ WSadressIP.WSIP+":5000/"+emp_id+".jpeg";
        ImageLoader imageLoader = MySingleton.getInstance(getContext()).getImageLoader();

        imageLoader.get(theImage_URL, new ImageLoader.ImageListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                profile_img.setImageResource(R.drawable.profile);
            }

            @Override
            public void onResponse(ImageLoader.ImageContainer response, boolean arg1) {
                if (response.getBitmap() != null) {

                    if(img.equals("0")){
                        profile_img.setImageBitmap(response.getBitmap());
                    }else{
                        profile_img.setImageBitmap(stringToBitmap(img));

                    }


                }
            }
        });

        ///////////////////////////////////////////// SETTING BUTTON CLICK
      /*  btnEdit.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getContext(), EditProfileActivity.class);
                        startActivity(intent);

                    }
                }
        );

        btnHolidays.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getContext(), Holidays.class);
                        startActivity(intent);

                    }
                }
        ); */

        goHolidays.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getContext(), Holidays.class);
                        startActivity(intent);

                    }
                }
        );

        goEdit.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getContext(), EditProfileActivity.class);
                        startActivity(intent);

                    }
                }
        );

        goPwd.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getContext(), PasswordActivity.class);
                        startActivity(intent);

                    }
                }
        );

        goSkills.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getContext(), ExperienceActivity.class);
                        startActivity(intent);

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
        emp_position = preferences.getString(AuthActivity.Position, "Account Manager");
        emp_email = preferences.getString(AuthActivity.Email, "ac@cinderella.com");
        emp_phonework = preferences.getString(AuthActivity.Workphone, "676312991");
        img = preferences.getString("img", "0");

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
