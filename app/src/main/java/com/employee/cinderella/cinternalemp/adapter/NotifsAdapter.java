package com.employee.cinderella.cinternalemp.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.employee.cinderella.cinternalemp.R;
import com.employee.cinderella.cinternalemp.model.NotifisModel;
import com.employee.cinderella.cinternalemp.model.RequestModel;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

public class NotifsAdapter extends ArrayAdapter<NotifisModel> {
    Context context;
    int resource;

    public NotifsAdapter(@NonNull Context context, int resource, @NonNull List<NotifisModel> objects) {
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


        TextView txt_title = (TextView) v.findViewById(R.id.textView_notif_title);
        TextView txt_topic = (TextView) v.findViewById(R.id.textView_notif_topic);
        TextView txt_date_time = (TextView) v.findViewById(R.id.textView55);
       // ImageView img_type = (ImageView) v.findViewById(R.id.imageView_notifs_type);
        CardView card = (CardView) v.findViewById(R.id.card_notif);
        ImageView img_notif = (ImageView) v.findViewById(R.id.imageView_notif_02);
        LinearLayout notifs_layout = (LinearLayout) v.findViewById(R.id.notifs_layout);
        if(getItem(position).getSeen().equals("0")){
            notifs_layout.setBackgroundColor(ContextCompat.getColor(context, R.color.notifs_background));
        }

        if (getItem(position).getType().equals("Department")) {

            //img_type.setImageDrawable(getContext().getDrawable(R.drawable.adepartment));
            card.setCardBackgroundColor(ContextCompat.getColor(context, R.color.color_department));
            img_notif.setImageDrawable(getContext().getDrawable(R.drawable.adepartment2));
        } else if (getItem(position).getType().equals("Task")) {

           // img_type.setImageDrawable(getContext().getDrawable(R.drawable.atask));
            card.setCardBackgroundColor(ContextCompat.getColor(context, R.color.color_task));
            img_notif.setImageDrawable(getContext().getDrawable(R.drawable.task));
        } else if (getItem(position).getType().equals("Holiday")) {

           // img_type.setImageDrawable(getContext().getDrawable(R.drawable.aholiday));
            card.setCardBackgroundColor(ContextCompat.getColor(context, R.color.color_holiday));
            img_notif.setImageDrawable(getContext().getDrawable(R.drawable.aholiday2));
        } else if (getItem(position).getType().equals("Request")) {

            //img_type.setImageDrawable(getContext().getDrawable(R.drawable.arequest));
            card.setCardBackgroundColor(ContextCompat.getColor(context, R.color.color_request));
            img_notif.setImageDrawable(getContext().getDrawable(R.drawable.request2));
        } else if (getItem(position).getType().equals("Project")) {

           // img_type.setImageDrawable(getContext().getDrawable(R.drawable.aproject));
            card.setCardBackgroundColor(ContextCompat.getColor(context, R.color.color_project));
            img_notif.setImageDrawable(getContext().getDrawable(R.drawable.projects));
        } else if (getItem(position).getType().equals("Meeting")) {

           // img_type.setImageDrawable(getContext().getDrawable(R.drawable.ameeting2));
            card.setCardBackgroundColor(ContextCompat.getColor(context, R.color.color_meeting));
            img_notif.setImageDrawable(getContext().getDrawable(R.drawable.meeting2));
        } else if (getItem(position).getType().equals("Poll")) {

           // img_type.setImageDrawable(getContext().getDrawable(R.drawable.apoll));
            card.setCardBackgroundColor(ContextCompat.getColor(context, R.color.color_poll));
            img_notif.setImageDrawable(getContext().getDrawable(R.drawable.poll));
        } else if (getItem(position).getType().equals("Order")) {

           // img_type.setImageDrawable(getContext().getDrawable(R.drawable.aorder));
            card.setCardBackgroundColor(ContextCompat.getColor(context, R.color.color_order));
            img_notif.setImageDrawable(getContext().getDrawable(R.drawable.purchase));
        } else {

            //img_type.setImageDrawable(getContext().getDrawable(R.drawable.notification2));
            //card.setBackgroundColor(ContextCompat.getColor(context, R.color.softgray));
            card.setCardBackgroundColor(ContextCompat.getColor(context, R.color.softgray));
            img_notif.setImageDrawable(getContext().getDrawable(R.drawable.notifications));
        }

        txt_title.setText(getItem(position).getTitle());
        txt_topic.setText(getItem(position).getTopic());
        txt_date_time.setText(getItem(position).getDate() + " at " + getItem(position).getTime());


        return v;
    }
}