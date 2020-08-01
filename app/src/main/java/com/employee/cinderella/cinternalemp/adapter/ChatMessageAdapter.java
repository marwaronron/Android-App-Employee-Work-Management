package com.employee.cinderella.cinternalemp.adapter;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.employee.cinderella.cinternalemp.AuthActivity;
import com.employee.cinderella.cinternalemp.R;
import com.employee.cinderella.cinternalemp.model.ChatMessage;
import com.employee.cinderella.cinternalemp.model.Task;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;


public class ChatMessageAdapter extends ArrayAdapter<ChatMessage> {

    Context context;
    int resource;

    String emp_full_name ;

   /* public ChatMessageAdapter(Context context, int resource, List<ChatMessage> objects) {
        super(context, resource, objects);

    }*/
    public ChatMessageAdapter(@NonNull Context context, int resource, @NonNull List<ChatMessage> objects) {
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

        ImageView photoProfil = (ImageView) v.findViewById(R.id.photoProfil);
        TextView txt_message = (TextView) v.findViewById(R.id.mainmessage);
        TextView txt_employee_sender = (TextView) v.findViewById(R.id.nameOfSender);
        TextView txt_msgDatetime = (TextView) v.findViewById(R.id.msgDatetime);

        CardView card = v.findViewById(R.id.messageCard);
        LinearLayout lay = v.findViewById(R.id.idForMsg);
        SharedPreferences preferences = getContext().getSharedPreferences(AuthActivity.MyPREFERENCES, Context.MODE_PRIVATE);
        emp_full_name = preferences.getString("nameKey", "1");

        ChatMessage newMessage = getItem(position);

        boolean photoExist = newMessage.getPhotoUrl() != null;
        if (photoExist ) {

            txt_message.setVisibility(View.GONE);
            photoProfil.setVisibility(View.VISIBLE);
            Glide.with(photoProfil.getContext())
                    .load(newMessage.getPhotoUrl())
                    .into(photoProfil);
        } else {
            txt_message.setVisibility(View.VISIBLE);
            photoProfil.setVisibility(View.GONE);

            txt_message.setText(newMessage.getText());
        }
        txt_employee_sender.setText(newMessage.getName());
        txt_msgDatetime.setText(newMessage.getTime());
        //Log.d("XX","%%%%%%%%%%%%%% "+txt_employee_sender.getText().toString()+" %%%%% "+emp_full_name);
        String[] array = {"#6600cc", "#6666ff", "#00ccff" , "#00cc66", "#ff9900" , "#cc0000" , "#cc0066" , "#ff3399" , "#990099" , "#cc00cc"};
        String randomStr = array[new Random().nextInt(array.length)];



        if(txt_employee_sender.getText().toString().equals(emp_full_name)){
           // card.setCardBackgroundColor(ContextCompat.getColor(context, R.color.doing));
            card.setBackground(ContextCompat.getDrawable(context,R.drawable.gradient_my_message));
           lay.setGravity(Gravity.END);
           txt_message.setTextColor(ContextCompat.getColor(context, R.color.white));
           txt_employee_sender.setTextColor(ContextCompat.getColor(context, R.color.white));
            txt_msgDatetime.setTextColor(ContextCompat.getColor(context, R.color.white));
        }else{
            card.setBackground(ContextCompat.getDrawable(context,R.drawable.gradient_other_message));
            txt_employee_sender.setTextColor(Color.parseColor(randomStr));
        }




        return v;
    }
  /*  @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.chat_message_row, parent, false);
        }

        ImageView photoProfil = (ImageView) convertView.findViewById(R.id.photoProfil);
        TextView txt_message = (TextView) convertView.findViewById(R.id.mainmessage);
        TextView txt_employee_sender = (TextView) convertView.findViewById(R.id.nameOfSender);


        ChatMessage newMessage = getItem(position);

        boolean photoExist = newMessage.getPhotoUrl() != null;
        if (photoExist ) {
            txt_message.setVisibility(View.GONE);
            photoProfil.setVisibility(View.VISIBLE);
            Glide.with(photoProfil.getContext())
                    .load(newMessage.getPhotoUrl())
                    .into(photoProfil);
        } else {
            txt_message.setVisibility(View.VISIBLE);
            photoProfil.setVisibility(View.GONE);

            txt_message.setText(newMessage.getText());
        }
        txt_employee_sender.setText(newMessage.getName());


        return convertView;
    }*/
}

