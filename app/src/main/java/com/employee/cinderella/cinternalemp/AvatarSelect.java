package com.employee.cinderella.cinternalemp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class AvatarSelect extends AppCompatActivity {

    ImageView avatarImg001, avatarImg002, avatarImg003,
            avatarImg004, avatarImg005, avatarImg006,
            avatarImg007, avatarImg008, avatarImg009;
    CardView avatarCircle001, avatarCircle002, avatarCircle003 ,
            avatarCircle004, avatarCircle005, avatarCircle006 ,
            avatarCircle007, avatarCircle008, avatarCircle009 ,
    avatarCard001,avatarCard002,avatarCard003;
    Button btnUpdate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_avatar_select);



        //SET TOP BAR
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar13);
        mToolbar.setTitle("Select Avatar");
        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        avatarImg001 = findViewById(R.id.avatarImg001);
        avatarImg002 = findViewById(R.id.avatarImg002);
        avatarImg003 = findViewById(R.id.avatarImg003);
        avatarImg004 = findViewById(R.id.avatarImg004);
        avatarImg005 = findViewById(R.id.avatarImg005);
        avatarImg006 = findViewById(R.id.avatarImg006);
        avatarImg007 = findViewById(R.id.avatarImg007);
        avatarImg008 = findViewById(R.id.avatarImg008);
        avatarImg009 = findViewById(R.id.avatarImg009);

        avatarCircle001 = findViewById(R.id.avatarCircle001);
        avatarCircle002 = findViewById(R.id.avatarCircle002);
        avatarCircle003 = findViewById(R.id.avatarCircle003);
        avatarCircle004 = findViewById(R.id.avatarCircle004);
        avatarCircle005 = findViewById(R.id.avatarCircle005);
        avatarCircle006 = findViewById(R.id.avatarCircle006);
        avatarCircle007 = findViewById(R.id.avatarCircle007);
        avatarCircle008 = findViewById(R.id.avatarCircle008);
        avatarCircle009 = findViewById(R.id.avatarCircle009);

        btnUpdate = findViewById(R.id.button26);
        btnUpdate.setEnabled(false);

        final String[] value = {""};

        avatarImg001.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                avatarCircle001.setCardBackgroundColor(ContextCompat.getColor(AvatarSelect.this, R.color.colorPrimary));
                avatarCircle002.setCardBackgroundColor(ContextCompat.getColor(AvatarSelect.this, R.color.white));
                avatarCircle003.setCardBackgroundColor(ContextCompat.getColor(AvatarSelect.this, R.color.white));
                avatarCircle004.setCardBackgroundColor(ContextCompat.getColor(AvatarSelect.this, R.color.white));
                avatarCircle005.setCardBackgroundColor(ContextCompat.getColor(AvatarSelect.this, R.color.white));
                avatarCircle006.setCardBackgroundColor(ContextCompat.getColor(AvatarSelect.this, R.color.white));
                avatarCircle007.setCardBackgroundColor(ContextCompat.getColor(AvatarSelect.this, R.color.white));
                avatarCircle008.setCardBackgroundColor(ContextCompat.getColor(AvatarSelect.this, R.color.white));
                avatarCircle009.setCardBackgroundColor(ContextCompat.getColor(AvatarSelect.this, R.color.white));

                value[0] ="avatarImg001";
                btnUpdate.setEnabled(true);

            }
        });

        avatarImg002.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                avatarCircle001.setCardBackgroundColor(ContextCompat.getColor(AvatarSelect.this, R.color.white));
                avatarCircle002.setCardBackgroundColor(ContextCompat.getColor(AvatarSelect.this, R.color.colorPrimary));
                avatarCircle003.setCardBackgroundColor(ContextCompat.getColor(AvatarSelect.this, R.color.white));
                avatarCircle004.setCardBackgroundColor(ContextCompat.getColor(AvatarSelect.this, R.color.white));
                avatarCircle005.setCardBackgroundColor(ContextCompat.getColor(AvatarSelect.this, R.color.white));
                avatarCircle006.setCardBackgroundColor(ContextCompat.getColor(AvatarSelect.this, R.color.white));
                avatarCircle007.setCardBackgroundColor(ContextCompat.getColor(AvatarSelect.this, R.color.white));
                avatarCircle008.setCardBackgroundColor(ContextCompat.getColor(AvatarSelect.this, R.color.white));
                avatarCircle009.setCardBackgroundColor(ContextCompat.getColor(AvatarSelect.this, R.color.white));

                value[0] ="avatarImg002";
                btnUpdate.setEnabled(true);

            }
        });

        avatarImg003.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                avatarCircle001.setCardBackgroundColor(ContextCompat.getColor(AvatarSelect.this, R.color.white));
                avatarCircle002.setCardBackgroundColor(ContextCompat.getColor(AvatarSelect.this, R.color.white));
                avatarCircle003.setCardBackgroundColor(ContextCompat.getColor(AvatarSelect.this, R.color.colorPrimary));
                avatarCircle004.setCardBackgroundColor(ContextCompat.getColor(AvatarSelect.this, R.color.white));
                avatarCircle005.setCardBackgroundColor(ContextCompat.getColor(AvatarSelect.this, R.color.white));
                avatarCircle006.setCardBackgroundColor(ContextCompat.getColor(AvatarSelect.this, R.color.white));
                avatarCircle007.setCardBackgroundColor(ContextCompat.getColor(AvatarSelect.this, R.color.white));
                avatarCircle008.setCardBackgroundColor(ContextCompat.getColor(AvatarSelect.this, R.color.white));
                avatarCircle009.setCardBackgroundColor(ContextCompat.getColor(AvatarSelect.this, R.color.white));

                value[0] ="avatarImg003";
                btnUpdate.setEnabled(true);
            }
        });

        avatarImg004.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                avatarCircle001.setCardBackgroundColor(ContextCompat.getColor(AvatarSelect.this, R.color.white));
                avatarCircle002.setCardBackgroundColor(ContextCompat.getColor(AvatarSelect.this, R.color.white));
                avatarCircle003.setCardBackgroundColor(ContextCompat.getColor(AvatarSelect.this, R.color.white));
                avatarCircle004.setCardBackgroundColor(ContextCompat.getColor(AvatarSelect.this, R.color.colorPrimary));
                avatarCircle005.setCardBackgroundColor(ContextCompat.getColor(AvatarSelect.this, R.color.white));
                avatarCircle006.setCardBackgroundColor(ContextCompat.getColor(AvatarSelect.this, R.color.white));
                avatarCircle007.setCardBackgroundColor(ContextCompat.getColor(AvatarSelect.this, R.color.white));
                avatarCircle008.setCardBackgroundColor(ContextCompat.getColor(AvatarSelect.this, R.color.white));
                avatarCircle009.setCardBackgroundColor(ContextCompat.getColor(AvatarSelect.this, R.color.white));

                value[0] ="avatarImg004";
                btnUpdate.setEnabled(true);
            }
        });
        avatarImg005.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                avatarCircle001.setCardBackgroundColor(ContextCompat.getColor(AvatarSelect.this, R.color.white));
                avatarCircle002.setCardBackgroundColor(ContextCompat.getColor(AvatarSelect.this, R.color.white));
                avatarCircle003.setCardBackgroundColor(ContextCompat.getColor(AvatarSelect.this, R.color.white));
                avatarCircle004.setCardBackgroundColor(ContextCompat.getColor(AvatarSelect.this, R.color.white));
                avatarCircle005.setCardBackgroundColor(ContextCompat.getColor(AvatarSelect.this, R.color.colorPrimary));
                avatarCircle006.setCardBackgroundColor(ContextCompat.getColor(AvatarSelect.this, R.color.white));
                avatarCircle007.setCardBackgroundColor(ContextCompat.getColor(AvatarSelect.this, R.color.white));
                avatarCircle008.setCardBackgroundColor(ContextCompat.getColor(AvatarSelect.this, R.color.white));
                avatarCircle009.setCardBackgroundColor(ContextCompat.getColor(AvatarSelect.this, R.color.white));

                value[0] ="avatarImg005";
                btnUpdate.setEnabled(true);
            }
        });

        avatarImg006.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                avatarCircle001.setCardBackgroundColor(ContextCompat.getColor(AvatarSelect.this, R.color.white));
                avatarCircle002.setCardBackgroundColor(ContextCompat.getColor(AvatarSelect.this, R.color.white));
                avatarCircle003.setCardBackgroundColor(ContextCompat.getColor(AvatarSelect.this, R.color.white));
                avatarCircle004.setCardBackgroundColor(ContextCompat.getColor(AvatarSelect.this, R.color.white));
                avatarCircle005.setCardBackgroundColor(ContextCompat.getColor(AvatarSelect.this, R.color.white));
                avatarCircle006.setCardBackgroundColor(ContextCompat.getColor(AvatarSelect.this, R.color.colorPrimary));
                avatarCircle007.setCardBackgroundColor(ContextCompat.getColor(AvatarSelect.this, R.color.white));
                avatarCircle008.setCardBackgroundColor(ContextCompat.getColor(AvatarSelect.this, R.color.white));
                avatarCircle009.setCardBackgroundColor(ContextCompat.getColor(AvatarSelect.this, R.color.white));

                value[0] ="avatarImg006";
                btnUpdate.setEnabled(true);
            }
        });

        avatarImg007.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                avatarCircle001.setCardBackgroundColor(ContextCompat.getColor(AvatarSelect.this, R.color.white));
                avatarCircle002.setCardBackgroundColor(ContextCompat.getColor(AvatarSelect.this, R.color.white));
                avatarCircle003.setCardBackgroundColor(ContextCompat.getColor(AvatarSelect.this, R.color.white));
                avatarCircle004.setCardBackgroundColor(ContextCompat.getColor(AvatarSelect.this, R.color.white));
                avatarCircle005.setCardBackgroundColor(ContextCompat.getColor(AvatarSelect.this, R.color.white));
                avatarCircle006.setCardBackgroundColor(ContextCompat.getColor(AvatarSelect.this, R.color.white));
                avatarCircle007.setCardBackgroundColor(ContextCompat.getColor(AvatarSelect.this, R.color.colorPrimary));
                avatarCircle008.setCardBackgroundColor(ContextCompat.getColor(AvatarSelect.this, R.color.white));
                avatarCircle009.setCardBackgroundColor(ContextCompat.getColor(AvatarSelect.this, R.color.white));

                value[0] ="avatarImg007";
                btnUpdate.setEnabled(true);
            }
        });

        avatarImg008.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                avatarCircle001.setCardBackgroundColor(ContextCompat.getColor(AvatarSelect.this, R.color.white));
                avatarCircle002.setCardBackgroundColor(ContextCompat.getColor(AvatarSelect.this, R.color.white));
                avatarCircle003.setCardBackgroundColor(ContextCompat.getColor(AvatarSelect.this, R.color.white));
                avatarCircle004.setCardBackgroundColor(ContextCompat.getColor(AvatarSelect.this, R.color.white));
                avatarCircle005.setCardBackgroundColor(ContextCompat.getColor(AvatarSelect.this, R.color.white));
                avatarCircle006.setCardBackgroundColor(ContextCompat.getColor(AvatarSelect.this, R.color.white));
                avatarCircle007.setCardBackgroundColor(ContextCompat.getColor(AvatarSelect.this, R.color.white));
                avatarCircle008.setCardBackgroundColor(ContextCompat.getColor(AvatarSelect.this, R.color.colorPrimary));
                avatarCircle009.setCardBackgroundColor(ContextCompat.getColor(AvatarSelect.this, R.color.white));

                value[0] ="avatarImg008";
                btnUpdate.setEnabled(true);
            }
        });

        avatarImg009.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                avatarCircle001.setCardBackgroundColor(ContextCompat.getColor(AvatarSelect.this, R.color.white));
                avatarCircle002.setCardBackgroundColor(ContextCompat.getColor(AvatarSelect.this, R.color.white));
                avatarCircle003.setCardBackgroundColor(ContextCompat.getColor(AvatarSelect.this, R.color.white));
                avatarCircle004.setCardBackgroundColor(ContextCompat.getColor(AvatarSelect.this, R.color.white));
                avatarCircle005.setCardBackgroundColor(ContextCompat.getColor(AvatarSelect.this, R.color.white));
                avatarCircle006.setCardBackgroundColor(ContextCompat.getColor(AvatarSelect.this, R.color.white));
                avatarCircle007.setCardBackgroundColor(ContextCompat.getColor(AvatarSelect.this, R.color.white));
                avatarCircle008.setCardBackgroundColor(ContextCompat.getColor(AvatarSelect.this, R.color.white));
                avatarCircle009.setCardBackgroundColor(ContextCompat.getColor(AvatarSelect.this, R.color.colorPrimary));

                value[0] ="avatarImg009";
                btnUpdate.setEnabled(true);
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AvatarSelect.this,EditProfileActivity.class);
                intent.putExtra("key", value[0]);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

      /*  Intent intent = new Intent();
        intent.putExtra("key", "value");
        setResult(RESULT_OK, intent);
        finish();*/
    }
}
