package com.employee.cinderella.cinternalemp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;


import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;


import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;


import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.employee.cinderella.cinternalemp.service.MySingleton;
import com.employee.cinderella.cinternalemp.utils.WSadressIP;


import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;


import static com.employee.cinderella.cinternalemp.AuthActivity.MyPREFERENCES;

public class EditProfileActivity extends AppCompatActivity {
    Button btnNewPhoto , btnConfirm;
    private static final int GALLERY_REQUEST_CODE = 100;


    ImageView selectedImageView;
    public  String photoUpdate ="";


    String full_name , department_name , emp_id, emp_position , emp_email , emp_phonework, img;
    EditText edEmail , edPhone , edAddress;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        //SET TOP BAR
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("Edit Profile");
        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //GET DATA
        SharedPreferences preferences = EditProfileActivity.this.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        full_name = preferences.getString(AuthActivity.Name, "xxxl");
        department_name = preferences.getString(AuthActivity.Department_Name, "Undefined");
        emp_id = preferences.getString("emp_id", "1");
        emp_position = preferences.getString(AuthActivity.Position, "Account Manager");
        emp_email = preferences.getString(AuthActivity.Email, "ac@cinderella.com");
        emp_phonework = preferences.getString(AuthActivity.Workphone, "676312991");
        img = preferences.getString("img", "0");
        //emp_address =preferences.getString(AuthActivity.Addressx, "carrer de llobregat,bcn");

        //SET UI
        edEmail = findViewById(R.id.editTextEdEmail);
        edPhone = findViewById(R.id.editTextEDPhone);
        //edAddress = findViewById(R.id.editTextEdAddress);

        //SET THIS DATA INSIDE UI
        edEmail.setText(emp_email);
        edPhone.setText(emp_phonework);
        //edAddress.setText(emp_address);

        //set default image
        selectedImageView = findViewById(R.id.selectedImageView);
       // String theImage_URL = "http://"+ WSadressIP.WSIP+":5000/"+full_name.replace(" ", "")+".jpeg";
        String theImage_URL = "http://"+ WSadressIP.WSIP+":5000/"+emp_id+".jpeg";

       ImageLoader imageLoader = MySingleton.getInstance(EditProfileActivity.this).getImageLoader();

        imageLoader.get(theImage_URL, new ImageLoader.ImageListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                selectedImageView.setImageResource(R.drawable.profile);
            }

            @Override
            public void onResponse(ImageLoader.ImageContainer response, boolean arg1) {
                if (response.getBitmap() != null) {

                    if(img.equals("0")){
                        selectedImageView.setImageBitmap(response.getBitmap());
                    }else{
                        selectedImageView.setImageBitmap(stringToBitmap(img));

                    }
                    //selectedImageView.setImageBitmap(response.getBitmap());

                }
            }
        });



        //BUTTON GALLERY PHOTOS
        btnNewPhoto = findViewById(R.id.buttonNewPhoto);
        btnNewPhoto.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        /*Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(Intent.createChooser(intent, "Select Picture"), GALLERY_REQUEST_CODE);*/
                         //int RESULT_GALLERY = 0;

                        /*Intent galleryIntent = new Intent(
                                Intent.ACTION_PICK,
                                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(galleryIntent ,GALLERY_REQUEST_CODE);*/

                        AlertDialog diaBox = AskOption();
                        diaBox.show();
                    }
                }
        );
        selectedImageView = findViewById(R.id.selectedImageView);
        btnConfirm = findViewById(R.id.button4);
        btnConfirm.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                         if(!photoUpdate.equals("")){
                             if((edEmail.getText().toString().length() >0) && (edPhone.getText().toString().length() >0) ){
                                    if(determine.equals("yes")){
                                        sendImage(photoUpdate ,edEmail.getText().toString() , edPhone.getText().toString());
                                    }else{
                                        Toast.makeText(getApplicationContext(),"image size should not pass 80KB", Toast.LENGTH_LONG).show();
                                    }
                             }else{
                                 Toast.makeText(getApplicationContext(),"Fill All Data Please", Toast.LENGTH_LONG).show();
                             }
                         }else{
                             if((edEmail.getText().toString().length()>0) && (edPhone.getText().toString().length() >0) ){
                                 sendImage("0" ,edEmail.getText().toString() , edPhone.getText().toString());
                             }else{
                                 Toast.makeText(getApplicationContext(),"Fill All Data Please", Toast.LENGTH_LONG).show();
                             }
                         }

                    }
                }
        );
    }

    public static int getOrientation(Context context, Uri photoUri) {
        /* it's on the external media. */
        Cursor cursor = context.getContentResolver().query(photoUri,
                new String[] { MediaStore.Images.ImageColumns.ORIENTATION }, null, null, null);

        if (cursor.getCount() != 1) {
            return -1;
        }

        cursor.moveToFirst();
        return cursor.getInt(0);
    }

    /*  public Bitmap getBitmap(Uri contentURI) throws IOException {
         Bitmap  myBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentURI);

         return myBitmap;
     }
     public Bitmap testtest(Uri contentURI) throws IOException {


         Bitmap myBitmap = getBitmap(contentURI);

         try {
             ExifInterface exif = new ExifInterface(getRealPathFromURI(contentURI,this));
             int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
             Log.d("EXIF", "Exif: " + orientation);
             Matrix matrix = new Matrix();
             if (orientation == 6) {
                 matrix.postRotate(90);
             }
             else if (orientation == 3) {
                 matrix.postRotate(180);
             }
             else if (orientation == 8) {
                 matrix.postRotate(270);
             }
             myBitmap = Bitmap.createBitmap(myBitmap, 0, 0, myBitmap.getWidth(), myBitmap.getHeight(), matrix, true); // rotating bitmap
         }
         catch (Exception e) {

         }

         return myBitmap;
     }
     private static int getExifOrientation(String src) throws IOException {
         int orientation = 1;

         ExifInterface exif = new ExifInterface(src);
         String orientationString=exif.getAttribute(ExifInterface.TAG_ORIENTATION);
         try {
             orientation = Integer.parseInt(orientationString);
         }
         catch(NumberFormatException e){}

         return orientation;
     }
     public static Bitmap rotateBitmap(String src, Bitmap bitmap) {
         try {
             int orientation = getExifOrientation(src);

             if (orientation == 1) {
                 return bitmap;
             }

             Matrix matrix = new Matrix();
             switch (orientation) {
                 case 2:
                     matrix.setScale(-1, 1);
                     break;
                 case 3:
                     matrix.setRotate(180);
                     break;
                 case 4:
                     matrix.setRotate(180);
                     matrix.postScale(-1, 1);
                     break;
                 case 5:
                     matrix.setRotate(90);
                     matrix.postScale(-1, 1);
                     break;
                 case 6:
                     matrix.setRotate(90);
                     break;
                 case 7:
                     matrix.setRotate(-90);
                     matrix.postScale(-1, 1);
                     break;
                 case 8:
                     matrix.setRotate(-90);
                     break;
                 default:
                     return bitmap;
             }

             try {
                 Bitmap oriented = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                 bitmap.recycle();
                 return oriented;
             } catch (OutOfMemoryError e) {
                 e.printStackTrace();
                 return bitmap;
             }
         } catch (IOException e) {
             e.printStackTrace();
         }

         return bitmap;
     }*/
String determine = "no";
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
       super.onActivityResult(requestCode, resultCode, data);

       if (resultCode == RESULT_OK && requestCode == GALLERY_REQUEST_CODE) {
            try {
                Uri selectedImage = data.getData();
               // Log.v("ok3",selectedImage.getPath());
                Log.v("rotation","%%%%%%%%%%%% =  "+getOrientation(this,selectedImage));
                InputStream imageStream = getContentResolver().openInputStream(selectedImage);

                if(getOrientation(this,selectedImage) == 90){
                    selectedImageView.setImageBitmap(BitmapFactory.decodeStream(imageStream));
                    selectedImageView.setRotation(selectedImageView.getRotation() + 90);
                }else{
                    selectedImageView.setImageBitmap(BitmapFactory.decodeStream(imageStream));
                }
               // selectedImageView.setImageBitmap(BitmapFactory.decodeStream(imageStream));
                //selectedImageView.setImageBitmap(rotateBitmap(getRealPathFromURI(selectedImage,this) ,testtest(selectedImage) ));
                //selectedImageView.setRotation(selectedImageView.getRotation() + 90);

                Bitmap x = ((BitmapDrawable) selectedImageView.getDrawable()).getBitmap();
                photoUpdate = bitmapToString(x);

               // Log.v("hi","============== "+ photoUpdate);



                Bitmap bitmap = x;
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 0, stream);
                byte[] imageInByte = stream.toByteArray();
                long lengthbmp = imageInByte.length;
                if(lengthbmp < 75000){
                    determine = "yes";
                }
                Log.v("size","+++++++++++++++ "+lengthbmp);

            } catch (IOException exception) {
                exception.printStackTrace();
            }
       }else  if (requestCode == 20) {
           if(resultCode == RESULT_OK) {
               String strKey = data.getStringExtra("key");

               //Toast.makeText(getApplicationContext(),"GOOD 1 "+strKey, Toast.LENGTH_LONG).show();
               //selectedImageView.setImageResource(R.drawable.);
               //selectedImageView.setImageResource(R.drawable.profile);
              if(strKey.equals("avatarImg001")){
                   selectedImageView.setImageResource(R.drawable.avatar001);
              }else if(strKey.equals("avatarImg002")){
                   selectedImageView.setImageResource(R.drawable.avatar002);
              }else if(strKey.equals("avatarImg003")){
                   selectedImageView.setImageResource(R.drawable.avatar003);
              }else if(strKey.equals("avatarImg004")){
                  selectedImageView.setImageResource(R.drawable.avatar004);
              }else if(strKey.equals("avatarImg005")){
                  selectedImageView.setImageResource(R.drawable.avatar005);
              }else if(strKey.equals("avatarImg006")){
                  selectedImageView.setImageResource(R.drawable.avatar006);
              }else if(strKey.equals("avatarImg007")){
                  selectedImageView.setImageResource(R.drawable.avatar007);
              }else if(strKey.equals("avatarImg008")){
                  selectedImageView.setImageResource(R.drawable.avatar008);
              }else if(strKey.equals("avatarImg009")){
                  selectedImageView.setImageResource(R.drawable.avatar009);
              }else{
                  selectedImageView.setImageResource(R.drawable.profile);
              }
               Bitmap x = ((BitmapDrawable) selectedImageView.getDrawable()).getBitmap();
               photoUpdate = bitmapToString(x);
               Bitmap bitmap = x;
               ByteArrayOutputStream stream = new ByteArrayOutputStream();
               bitmap.compress(Bitmap.CompressFormat.JPEG, 0, stream);
               byte[] imageInByte = stream.toByteArray();
               long lengthbmp = imageInByte.length;
               if(lengthbmp < 75000){
                   determine = "yes";
               }
               Log.v("size","+++++++++++++++ "+lengthbmp);
            /*   switch(strKey){
                   case "avatar001":
                       selectedImageView.setImageResource(R.drawable.avatar001);
                       break;
                   case "avatar002":
                       selectedImageView.setImageResource(R.drawable.avatar002);
                       break;
                   case "avatar003":
                       selectedImageView.setImageResource(R.drawable.avatar003);
                       break;
                   default:
                       selectedImageView.setImageResource(R.drawable.profile);
                       break;
               }*/
           }
       }


    }

    private static String bitmapToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        return Base64.encodeToString(b, Base64.DEFAULT);
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
    public void sendImage(final String image, final String smail , final String sphone ){
        //VERIFY IF USER UPDATE HIS PHOTO OR IS KEEPING THE SAME


        final String   URL =  "http://"+ WSadressIP.WSIP+":5000/cinternal/updateEmployeeSelf";


        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Toast.makeText(getApplicationContext(),"Successffuly updated", Toast.LENGTH_LONG).show();
                        SharedPreferences sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putString(AuthActivity.Email,  smail);
                        editor.putString(AuthActivity.Workphone,  sphone);
                        // editor.putString(AuthActivity.Addressx,  saddress);
                        editor.putString("img",image);
                        editor.commit();

                        Intent intent = new Intent(EditProfileActivity.this, MainActivity.class);
                        intent.putExtra("goToProfile", "1");
                        finish();
                        startActivity(intent);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Log.v("hi","mumumumumumumumumummmumumumum");
                        Toast.makeText(getApplicationContext(),"Image size exeed the maximum size. Image size should be less than 80KB", Toast.LENGTH_LONG).show();
                        finish();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Content-Type","application/x-www-form-urlencoded");
                params.put("emp_id",emp_id);
                params.put("full_name",full_name);
                params.put("workphone",sphone);
                params.put("email",smail);
                params.put("image", image);
                return params;
            }
        };;


        Volley.newRequestQueue(EditProfileActivity.this).add(stringRequest);
    }

    private AlertDialog AskOption()
    {
        AlertDialog myPhotoDialogBox = new AlertDialog.Builder(EditProfileActivity.this)
                // set message, title, and icon
                .setTitle("Edit Profile Photo")
                .setMessage("Select Profile Photo From")
                .setIcon(R.drawable.profile)

                .setPositiveButton("My Gallery", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {

                        Intent galleryIntent = new Intent(
                                Intent.ACTION_PICK,
                                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(galleryIntent ,GALLERY_REQUEST_CODE);
                        dialog.dismiss();
                    }

                })
                .setNegativeButton("Default", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        Intent intent = new Intent(EditProfileActivity.this, AvatarSelect.class);
                        // startActivity(intent);
                        startActivityForResult(intent, 20);
                        dialog.dismiss();

                    }
                })
                .create();

        return myPhotoDialogBox;
    }

    
}
