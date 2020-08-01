package com.employee.cinderella.cinternalemp;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.location.LocationManager;

import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.employee.cinderella.cinternalemp.service.MySingleton;
import com.employee.cinderella.cinternalemp.utils.WSadressIP;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;

import java.io.ByteArrayOutputStream;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class AuthActivity extends AppCompatActivity {

    EditText full_name , pwd ;
    Button startBtn;

    public static final String IS_LOGIN = "IsLoggedIn";
    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String Name = "nameKey";
    public static final String Position = "positionKey";
    public static final String Workphone = "workphoneKey";
    public static final String Email = "emailKey";
    public static final String Addressx = "addressKey";
    public static final String Department_id = "departmentIdKey";
    public static final String Department_Name = "departmentNameKey";
    SharedPreferences sharedpreferences;





    private static final int REQUEST_LOCATION = 1;
    LocationManager locationManager;
    String lattitude,longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        //RESET AT MIDNIGHT

            SharedPreferences pref = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

            if(pref.getBoolean(IS_LOGIN, false) ){

                    Intent intent = new Intent(this, MainActivity.class);
                    startActivity(intent);
                    finish();

            } else{
                //1: set UI relations
                full_name =  findViewById(R.id.editText);
                pwd =  findViewById(R.id.editText2);
                startBtn = findViewById(R.id.button2);


                startBtn.setOnClickListener(
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if((!full_name.getText().toString().isEmpty()) ||(!pwd.getText().toString().isEmpty()) ){
                                    VerifyUserExistance(full_name.getText().toString() , pwd.getText().toString());
                                }else{
                                    Toast.makeText(getApplicationContext(),"Fill All Data Please", Toast.LENGTH_LONG).show();
                                }
                            }
                        }
                );
            }


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create channel to show notifications.
            String channelId  = getString(R.string.default_notification_channel_id);
            String channelName = getString(R.string.default_notification_channel_name);
            NotificationManager notificationManager =
                    getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(new NotificationChannel(channelId,
                    channelName, NotificationManager.IMPORTANCE_LOW));
        }
        if (getIntent().getExtras() != null) {
            for (String key : getIntent().getExtras().keySet()) {
                Object value = getIntent().getExtras().get(key);
                Log.d("mwr1", "Key: " + key + " Value: " + value);
            }
        }



    }

    //POST
   public void VerifyUserExistance(final String full_name , final String pwd){
       final String   URL =  "http://"+ WSadressIP.WSIP+":5000/cinternal/VerifyEmployee";

       StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
               new Response.Listener<String>() {
                   @Override
                   public void onResponse(String response) {

                       try {

                           JSONArray array = new JSONArray(response);
                           JSONObject product = array.getJSONObject(0);

                           //2: GET EMPLOYEE DATA
                           product.getString("full_name");

                           sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                           SharedPreferences.Editor editor = sharedpreferences.edit();
                           editor.putString("emp_id",  product.getString("id"));

                           editor.putString(Name,  product.getString("full_name"));
                           editor.putString(Position,  product.getString("position"));
                           editor.putString(Email,  product.getString("email"));
                           editor.putString(Workphone,  product.getString("workphone"));
                           // editor.putString(Addressx,  product.getString("address"));
                           editor.putString(Department_id,  product.getString("department_id"));
                           editor.putBoolean(IS_LOGIN, true);

                           editor.commit();

                           whatever(); //SAVE IMAGE TO SHARED PREF

                           //Update employee row with firebase token
                           updateEmployeeRow(product.getString("id"));

                           //GO TO HOME
                           getDepartmentName(product.getString("department_id"));

                           Toast.makeText(getApplicationContext(),"Account Accepted", Toast.LENGTH_LONG).show();
                           Intent intent = new Intent(AuthActivity.this, MainActivity.class);

                            startActivity(intent);

                       } catch (JSONException e) {
                           e.printStackTrace();
                       }
                   }
               },
               new Response.ErrorListener() {
                   @Override
                   public void onErrorResponse(VolleyError error) {
                       Toast.makeText(getApplicationContext(),"This Account doesn\'t existe. Please verify with your admin", Toast.LENGTH_LONG).show();
                   }
               }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Content-Type","application/x-www-form-urlencoded");

                params.put("full_name",full_name);
                params.put("pwd",pwd);

                return params;
            }
        };;
        Volley.newRequestQueue(AuthActivity.this).add(stringRequest);
    }



   public void  getDepartmentName(final String department_id){
        final String   URL =  "http://"+ WSadressIP.WSIP+":5000/cinternal/getDepartmentById";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {

                            JSONArray array = new JSONArray(response);
                            JSONObject product = array.getJSONObject(0);
                            sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedpreferences.edit();
                            editor.putString(Department_Name,  product.getString("name"));
                            editor.commit();

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

                params.put("id",department_id);
                return params;
            }
        };;
        Volley.newRequestQueue(AuthActivity.this).add(stringRequest);
   }


       /*
        RequestQueue queue = Volley.newRequestQueue(AuthActivity.this);
        JSONObject params = new JSONObject();
        try {
            params.put("full_name", full_name);
            params.put("password", pwd);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest rq = new JsonObjectRequest(Request.Method.POST, URL, params, new Response.Listener<JSONObject>() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Log.d("access_test","******************** level 2");
                    final JSONArray employee = response.getJSONArray("employee");



                        for (int i = 0; i < employee.length(); i++) {

                            JSONObject product = employee.getJSONObject(i);
                            product.getString("full_name");
                            Log.d("Response", "+++++++++++++++++++"+ product.getString("full_name"));

                        }

                } catch (JSONException ex) {
                    ex.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }

        };

        queue.add(rq);
    }*/


   public void whatever(){
           SharedPreferences preferences = AuthActivity.this.getSharedPreferences(AuthActivity.MyPREFERENCES, Context.MODE_PRIVATE);
           String xx = preferences.getString(AuthActivity.Name, "default");
           String theImage_URL = "http://" + WSadressIP.WSIP + ":5000/" + xx + ".jpeg";

           ImageLoader imageLoader = MySingleton.getInstance(AuthActivity.this).getImageLoader();

           imageLoader.get(theImage_URL, new ImageLoader.ImageListener() {
               @Override
               public void onErrorResponse(VolleyError error) {
                   //profile_img.setImageResource(R.drawable.profile);
               }

               @Override
               public void onResponse(ImageLoader.ImageContainer response, boolean arg1) {
                   if (response.getBitmap() != null) {

                       SharedPreferences.Editor editor = sharedpreferences.edit();
                       editor.putString("img",  bitmapToString(response.getBitmap()));
                       editor.commit();
                   }
               }
           });
   }

   private static String bitmapToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 10, baos);
        byte[] b = baos.toByteArray();
        return Base64.encodeToString(b, Base64.DEFAULT);
   }


  public void updateEmployeeRow(String id){

      final String   URL =  "http://"+ WSadressIP.WSIP+":5000/cinternal/updateEmployeeToken";

      // Get token
      // [START retrieve_current_token]

      FirebaseInstanceId.getInstance().getInstanceId()
              .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                  @Override
                  public void onComplete(@NonNull Task<InstanceIdResult> task) {
                      if (!task.isSuccessful()) {
                          Log.w("mrw1", "getInstanceId failed", task.getException());

                          return;
                      }

                      // Get new Instance ID token
                      String token = task.getResult().getToken();

                      // Log and toast
                     // String msg = getString(R.string.msg_token_fmt, token);
                      Log.d("token mrw1", token);
                      //Toast.makeText(AuthActivity.this, msg, Toast.LENGTH_SHORT).show();

                      // [START update_DB]
                      StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                              new Response.Listener<String>() {
                                  @Override
                                  public void onResponse(String response) {

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
                              params.put("employee_id",id);
                              params.put("employee_token", token);
                              return params;
                          }
                      };;
                      Volley.newRequestQueue(AuthActivity.this).add(stringRequest);
                      // [END update_DB]
                  }
              });
      // [END retrieve_current_token]


  }

}
