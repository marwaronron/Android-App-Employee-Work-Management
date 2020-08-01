package com.employee.cinderella.cinternalemp;

import android.content.Context;
import android.content.SharedPreferences;


import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;

import androidx.drawerlayout.widget.DrawerLayout;

import android.os.Bundle;

import android.view.MenuItem;
import android.widget.TextView;

import com.employee.cinderella.cinternalemp.fragment.CalendarFragment;
import com.employee.cinderella.cinternalemp.fragment.ChatFragment;
import com.employee.cinderella.cinternalemp.fragment.MeetingsFragment;
import com.employee.cinderella.cinternalemp.fragment.MyTasksFragment;
import com.employee.cinderella.cinternalemp.fragment.OrdersFragment;
import com.employee.cinderella.cinternalemp.fragment.PollFragment;
import com.employee.cinderella.cinternalemp.fragment.ProfileFragment;
import com.employee.cinderella.cinternalemp.fragment.ProjectsFragment;
import com.employee.cinderella.cinternalemp.fragment.RequestFragment;

import com.employee.cinderella.cinternalemp.fragment.TodayFragment;
import com.google.android.material.navigation.NavigationView;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener  {

    private DrawerLayout drawer;

    private static final String URL_getAllEmployees = "http://192.168.1.119:5000/cinternal/getAllEmployees";
    TextView full_name;


   /* private Socket socket;




   private com.github.nkzawa.socketio.client.Socket mSocket;
    {
        try {
            Log.d("ok", "instance initializer: initializing socket");

            mSocket = IO.socket("http://"+ WSadressIP.WSIP+":5000");

        }catch (URISyntaxException e){

            e.printStackTrace();

            Log.d("not ok", "instance initializer: "+e.getMessage());

        }
    }

    String CHANNEL_ID = "1234";*/


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
      /*  try {
            getPublicIPAddress(MainActivity.this);

            SharedPreferences pref = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
            Log.v("notif","************  "+ pref.getString(EXTERNAL_IP,"000") );
        } catch (Exception e) {
            e.printStackTrace();
        }*/

        //SET SHARED PREFERENCES
        SharedPreferences preferences = this.getSharedPreferences(AuthActivity.MyPREFERENCES, Context.MODE_PRIVATE);
        String id =  preferences.getString("emp_id", "xxxl");

        //SET MENU
        drawer = findViewById(R.id.drawer_layout);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar ,R.string.navigation_drawer_open ,R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();



        //Toast.makeText(Home.this, OKOK , Toast.LENGTH_SHORT).show();
        if(savedInstanceState == null) {
            if(this.getIntent().getStringExtra("goToToday") != null){
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new TodayFragment()).commit();
                navigationView.setCheckedItem(R.id.nav_today);
            }else
            if(this.getIntent().getStringExtra("goToCalendar") != null){
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new CalendarFragment()).commit();
                navigationView.setCheckedItem(R.id.nav_Calendar);
            }else
            if(this.getIntent().getStringExtra("goToProjects") != null){
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ProjectsFragment()).commit();
                navigationView.setCheckedItem(R.id.nav_Projects);
            }else
            if(this.getIntent().getStringExtra("goToMyTasks") != null){
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new MyTasksFragment()).commit();
                navigationView.setCheckedItem(R.id.nav_mytasks);
            }else
            if(this.getIntent().getStringExtra("goToOrders") != null){


                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new OrdersFragment()).commit();
                navigationView.setCheckedItem(R.id.nav_Orders);
            }else
            if(this.getIntent().getStringExtra("goToPoll") != null){
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new PollFragment()).commit();
                navigationView.setCheckedItem(R.id.nav_Poll);
            }else
            if(this.getIntent().getStringExtra("goToReuest") != null){
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new RequestFragment()).commit();
                navigationView.setCheckedItem(R.id.nav_Request);
            }else  if(this.getIntent().getStringExtra("goToProfile") != null){
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ProfileFragment()).commit();
                navigationView.setCheckedItem(R.id.nav_Profile);
            }else  if(this.getIntent().getStringExtra("goToChat") != null){
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ChatFragment()).commit();
                navigationView.setCheckedItem(R.id.nav_Chat);
            }else  if(this.getIntent().getStringExtra("goToMeetings") != null){
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new MeetingsFragment()).commit();
                navigationView.setCheckedItem(R.id.nav_Meetings);
            }else if (this.getIntent().getStringExtra("redirectToPolls") != null){
                //this for redirect to poll fragment (from poll adapter after employee confirm his vote
                if(this.getIntent().getStringExtra("redirectToPolls").equals("redirectToPolls")){
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new PollFragment()).commit();
                    getSupportActionBar().setTitle("Polls");
                    navigationView.setCheckedItem(R.id.nav_Poll);
                }
            }else if (this.getIntent().getStringExtra("redirectToRequests") != null){
                //this for redirect to poll fragment (from poll adapter after employee confirm his vote
                if(this.getIntent().getStringExtra("redirectToRequests").equals("redirectToRequests")){
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new RequestFragment()).commit();
                    getSupportActionBar().setTitle("My Requests");
                    navigationView.setCheckedItem(R.id.nav_Request);
                }

            } else if (this.getIntent().getStringExtra("redirectToOrders") != null){
                //this for redirect to poll fragment (from poll adapter after employee confirm his vote
                if(this.getIntent().getStringExtra("redirectToOrders").equals("redirectToOrders")){
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new OrdersFragment()).commit();
                    getSupportActionBar().setTitle("Orders");
                    navigationView.setCheckedItem(R.id.nav_Orders);
                }
            } else if (this.getIntent().getStringExtra("redirectToMeetings") != null){
                //this for redirect to poll fragment (from poll adapter after employee confirm his vote
                if(this.getIntent().getStringExtra("redirectToMeetings").equals("redirectToMeetings")){
                   getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new MeetingsFragment()).commit();
                    getSupportActionBar().setTitle("Meetings");
                   navigationView.setCheckedItem(R.id.nav_Meetings);
                }
            } else if (this.getIntent().getStringExtra("redirectToTasks") != null){
                if(this.getIntent().getStringExtra("redirectToTasks").equals("redirectToTasks")){
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new MyTasksFragment()).commit();
                    getSupportActionBar().setTitle("My Tasks");
                    navigationView.setCheckedItem(R.id.nav_mytasks);
                }
            }else{
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new TodayFragment()).commit();
                navigationView.setCheckedItem(R.id.nav_today);


            }


        }

        /////////////////////////////////////////////////// SOCKET
      /*  createNotificationChannel();
       mSocket.on("foo", onNewMessage);
        mSocket.connect();*/

        ///////// Redirection :




    }
  /*private Emitter.Listener onNewMessage = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            Log.d("call", "call: OnNewMessage called");

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    String data = (String) args[0];
                    Log.d("print", "A new department was created "+ data);
                    addNotification("A new department was created ",data);

                }
            });
        }
    };*/





    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        //copy and paste the same case for all the rest of the menu
        switch (menuItem.getItemId()){
            case R.id.nav_today:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new TodayFragment()).commit();
               getSupportActionBar().setTitle("Today's Tasks");
                break;
            case R.id.nav_Calendar:
                // startActivity(new Intent(getApplicationContext(), MarwaFitness.class));
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new CalendarFragment()).commit();

                getSupportActionBar().setTitle("Calendar");

                break;
            case R.id.nav_Projects:
                // startActivity(new Intent(getApplicationContext(), MarwaAdd.class));
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ProjectsFragment()).commit();
                getSupportActionBar().setTitle("Department Projects");
                break;
            case R.id.nav_mytasks:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new MyTasksFragment()).commit();
                getSupportActionBar().setTitle("My Tasks");
                break;
            case R.id.nav_Orders:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new OrdersFragment()).commit();
                getSupportActionBar().setTitle("Clients Orders");
                break;
            case R.id.nav_Poll:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new PollFragment()).commit();
                getSupportActionBar().setTitle("Polls");
                break;
            case R.id.nav_Request:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new RequestFragment()).commit();
                getSupportActionBar().setTitle("My Requests");
                break;
            case R.id.nav_Profile:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ProfileFragment()).commit();
                getSupportActionBar().setTitle("My Profile");
                break;
            case R.id.nav_Chat:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ChatFragment()).commit();
                getSupportActionBar().setTitle("Chat");
                break;
            case R.id.nav_Meetings:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new MeetingsFragment()).commit();
                getSupportActionBar().setTitle("Meetings");
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);



        }else{
            super.onBackPressed();
        }

    }
  /*  private void addNotification(String title,String data) {

        // Create an explicit intent for an Activity in your app
        Intent intent = new Intent(MainActivity.this, EditProfileActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(MainActivity.this, 0, intent, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(MainActivity.this, CHANNEL_ID)
                .setSmallIcon(R.drawable.notifications)
                .setContentTitle(title)
                .setContentText(data)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                // Set the intent that will fire when the user taps the notification
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);
        //////////
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(MainActivity.this);

        // notificationId is a unique int for each notification that you must define
        notificationManager.notify(1, builder.build());
    }
    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.cdescription);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = MainActivity.this.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }*/

    @Override
    public void onPause() {
        super.onPause();
        overridePendingTransition(0, 0);
    }
}
