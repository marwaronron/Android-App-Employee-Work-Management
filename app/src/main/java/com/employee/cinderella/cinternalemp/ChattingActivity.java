package com.employee.cinderella.cinternalemp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.employee.cinderella.cinternalemp.adapter.ChatMessageAdapter;
import com.employee.cinderella.cinternalemp.model.ChatMessage;
import com.employee.cinderella.cinternalemp.utils.WSadressIP;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.data.model.User;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static com.employee.cinderella.cinternalemp.AuthActivity.MyPREFERENCES;

public class ChattingActivity extends AppCompatActivity {

    private static final String TAG = "ChattingActivity";

    public static final String ANONYMOUS = "anonymous";
    public static final int DEFAULT_MSG_LENGTH_LIMIT = 120;

    private ListView mMessageListView;
    private ChatMessageAdapter mMessageAdapter;
    private ProgressBar mProgressBar;
    private ImageButton mPhotoPickerButton;
    private EditText mMessageEditText;
    private Button mSendButton;

    private String mUsername;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mMessagesDatabaseReference;
    private ChildEventListener mChildEventListener;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private FirebaseStorage mFirebaseStorage;
    private StorageReference mChatPhotosStorageReference;


    public static final int RC_SIGN_IN = 1;

    private static final int RC_PHOTO_PICKER =  2;

    String full_name , department_name , emp_id, emp_position , emp_email , emp_phonework, img;
    Button goToProfiles;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatting);

        //SET TOP BAR
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar5);
        mToolbar.setTitle("Cinderella Chat");
        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChattingActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        //get shared preferences
        SharedPreferences preferences = ChattingActivity.this.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        full_name = preferences.getString(AuthActivity.Name, "xxxl");
        department_name = preferences.getString(AuthActivity.Department_Name, "Undefined");
        emp_id = preferences.getString("emp_id", "1");
        emp_position = preferences.getString(AuthActivity.Position, "Account Manager");
        emp_email = preferences.getString(AuthActivity.Email, "ac@cinderella.com");
        emp_phonework = preferences.getString(AuthActivity.Workphone, "676312991");
        img = preferences.getString("img", "0");

        mUsername = ANONYMOUS;

        //set firebase component
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mMessagesDatabaseReference = mFirebaseDatabase.getReference().child("messages");
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseStorage = FirebaseStorage.getInstance();
        mChatPhotosStorageReference = mFirebaseStorage.getReference().child("chat_photos");


        // Initialize references to views
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        mMessageListView = (ListView) findViewById(R.id.messageListView);
        mPhotoPickerButton = (ImageButton) findViewById(R.id.photoPickerButton);
        mMessageEditText = (EditText) findViewById(R.id.messageEditText);
        mSendButton = (Button) findViewById(R.id.sendButton);
        goToProfiles = findViewById(R.id.button12);
        goToProfiles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChattingActivity.this, CinderellaProfilesActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
            }
        });





        // Initialize message ListView and its adapter
        List<ChatMessage> friendlyMessages = new ArrayList<>();
        mMessageAdapter = new ChatMessageAdapter(this, R.layout.chat_message_row, friendlyMessages);
        mMessageListView.setAdapter(mMessageAdapter);

        // Initialize progress bar
        mProgressBar.setVisibility(ProgressBar.INVISIBLE);

        // ImagePickerButton shows an image picker to upload a image for a message
        mPhotoPickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/jpeg");
                intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                startActivityForResult(Intent.createChooser(intent, "Complete action using"), RC_PHOTO_PICKER);
            }
        });

        // Enable Send button when there's text to send
        mMessageEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().trim().length() > 0) {
                    mSendButton.setEnabled(true);
                } else {
                    mSendButton.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        mMessageEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(DEFAULT_MSG_LENGTH_LIMIT)});

        // Send button sends a message and clears the EditText
        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: Send messages on click
                DateFormat df = new SimpleDateFormat("EEE, d MMM, HH:mm");
                String date = df.format(Calendar.getInstance().getTime());
                ChatMessage friendlyMessage = new ChatMessage(mMessageEditText.getText().toString(), mUsername, null,date);
                mMessagesDatabaseReference.push().setValue(friendlyMessage);
                // Clear input box
                mMessageEditText.setText("");
            }
        });

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user != null){
                    // is signed already
                    //Toast.makeText(MainActivity.this, "You're now signed in. Welcome to FriendlyChat.", Toast.LENGTH_SHORT).show();
                    onSignedInInitialize(full_name); //HERE assign name from shared preferences

                }else{
                    //not signed the user
                    onSignedOutCleanup();
                    SignInOrSignUp(emp_id);
                }

            }
        };
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RC_SIGN_IN){
            if(resultCode == RESULT_OK){
                Toast.makeText(ChattingActivity.this, "Signed in!", Toast.LENGTH_SHORT).show();
            }else if ( resultCode == RESULT_CANCELED){
                Toast.makeText(ChattingActivity.this, "Sign in canceled", Toast.LENGTH_SHORT).show();
                finish();
            }

        }else if(requestCode == RC_PHOTO_PICKER && resultCode == RESULT_OK){
            DateFormat df = new SimpleDateFormat("EEE, d MMM, HH:mm");
            String date = df.format(Calendar.getInstance().getTime());
            Uri selectedImageUri = data.getData();


            StorageReference photoRef = mChatPhotosStorageReference.child(selectedImageUri.getLastPathSegment());
            UploadTask uploadTask = photoRef.putFile(selectedImageUri);

            Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }

                    // Continue with the task to get the download URL
                    Log.v("ok","%%% 1 // "+photoRef.getDownloadUrl().toString());
                    Log.v("ok","%%% 3 // "+task.getResult().toString());
                    Log.v("ok","%%% 4 // "+task.getResult().getStorage().toString());
                    Log.v("ok","%%% 5 // "+task.getResult().getStorage().getDownloadUrl().toString());
                    Log.v("ok","%%% 6 // "+task.getResult().getStorage().getMetadata().toString());
                    Log.v("ok","%%% 7 // "+task.getResult().getStorage().getName());
                    Log.v("ok","%%% 8 // "+task.getResult().getStorage().getBucket());
                    Log.v("ok","%%% 9 // "+photoRef.getPath());
                    return photoRef.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        Log.v("ok","%%% 2 // "+downloadUri.toString());
                        ChatMessage friendlyMessage = new  ChatMessage(
                                null, mUsername,downloadUri.toString(),date);
                        mMessagesDatabaseReference.push().setValue(friendlyMessage);
                    } else {
                        // Handle failures
                        // ...
                    }
                }
            });
          /*  photoRef.putFile(selectedImageUri)
                    .addOnSuccessListener(this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            Log.v("hi","image %%%%%%%%");


                            Task<Uri> uri = taskSnapshot.getStorage().getDownloadUrl();
                            String downloadUrl = taskSnapshot.getMetadata().getReference().getDownloadUrl().toString();

                            Task<Uri> first_uri = photoRef.getDownloadUrl();
                            String first_string = first_uri.toString();

                            ChatMessage friendlyMessage = new  ChatMessage(
                                    null, mUsername,first_string,date);
                            mMessagesDatabaseReference.push().setValue(friendlyMessage);


                        }
                    });

                    photoRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {

                            Log.d(TAG, "onSuccess: uri= "+ uri.toString());
                            ChatMessage friendlyMessage = new  ChatMessage(
                                    null, mUsername,uri.toString(),date);
                            mMessagesDatabaseReference.push().setValue(friendlyMessage);
                        }
                    });*/


        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mAuthStateListener != null){
            mFirebaseAuth.removeAuthStateListener(mAuthStateListener);
        }
        detachDatabaseReadListener();
        mMessageAdapter.clear();
    }

    @Override
    protected void onResume(){
        super.onResume();
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }

    private void onSignedInInitialize(String username){
        mUsername = username;
        attachDatabaseReadListener();
    }

    private void onSignedOutCleanup(){
        mUsername = ANONYMOUS;
        mMessageAdapter.clear();
        detachDatabaseReadListener();
    }

    private void attachDatabaseReadListener(){
        if(mChildEventListener == null){
            mChildEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    ChatMessage friendlyMessage = dataSnapshot.getValue(ChatMessage.class);
                    mMessageAdapter.add(friendlyMessage);
                }
                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) { }
                @Override
                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) { }
                @Override
                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) { }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) { }
            };
            mMessagesDatabaseReference.addChildEventListener(mChildEventListener);
        }
    }

    private void detachDatabaseReadListener(){
        if(mChildEventListener != null){
            mMessagesDatabaseReference.removeEventListener(mChildEventListener);
            mChildEventListener = null;
        }
    }

    public void SignInOrSignUp(String emp_id){
        final String   URL =  "http://"+ WSadressIP.WSIP+":5000/cinternal/findEmployeeChatAccount";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONArray array = new JSONArray(response);
                            if(array.length() > 0){
                                JSONObject product = array.getJSONObject(0);

                                //2: GET EMPLOYEE DATA
                                String email = product.getString("email");
                                String password = product.getString("password");

                                mFirebaseAuth.signInWithEmailAndPassword(email, password)
                                        .addOnCompleteListener(ChattingActivity.this, new OnCompleteListener<AuthResult>() {
                                            @Override
                                            public void onComplete(@NonNull Task<AuthResult> task) {
                                                if (task.isSuccessful()) {
                                                    // Sign in success, update UI with the signed-in user's information
                                                    Log.d("ok", "signInWithEmail:success");
                                                    FirebaseUser user = mFirebaseAuth.getCurrentUser();
                                                    Intent intent = new Intent(ChattingActivity.this, MainActivity.class);
                                                    startActivity(intent);
                                                    finish();
                                                    //updateUI(user);
                                                } else {
                                                    // If sign in fails, display a message to the user.
                                                    Log.w("ok", "signInWithEmail:failure", task.getException());
                                                    Toast.makeText(ChattingActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                                                    // updateUI(null);

                                                }

                                            }
                                        });
                            }else{
                                char[] chars1 = "ABCDEF012GHIJKL345MNOPQR678STUVWXYZ9".toCharArray();
                                StringBuilder sb1 = new StringBuilder();
                                Random random1 = new Random();
                                for (int i = 0; i < 7; i++)
                                {
                                    char c1 = chars1[random1.nextInt(chars1.length)];
                                    sb1.append(c1);
                                }
                                String password = sb1.toString();
                                Log.d("pwd","%%%%%%%%%%% password= "+password);

                                mFirebaseAuth.createUserWithEmailAndPassword(emp_email, password)
                                        .addOnCompleteListener(ChattingActivity.this, new OnCompleteListener<AuthResult>() {
                                            @Override
                                            public void onComplete(@NonNull Task<AuthResult> task) {


                                                if (!task.isSuccessful()) {
                                                    Toast.makeText(ChattingActivity.this, "Authentication failed. please contact It team" + task.getException(),
                                                            Toast.LENGTH_SHORT).show();
                                                } else {

                                                    Toast.makeText(ChattingActivity.this, "Welcome to Cinderella Chat" , Toast.LENGTH_SHORT).show();
                                                    startActivity(new Intent(ChattingActivity.this, ChattingActivity.class));
                                                    finish();
                                                }
                                            }
                                        });
                                //TO DO: add in database the account
                                InsertNewChatAccount(emp_email,password);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Toast.makeText(getApplicationContext(),"This Account doesn\'t existe. Please verify with your admin", Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Content-Type","application/x-www-form-urlencoded");
                params.put("employee_id",emp_id);
                return params;
            }
        };;
        Volley.newRequestQueue(ChattingActivity.this).add(stringRequest);
    }

    public void InsertNewChatAccount(String emp_email, String password){
        final String   URL =  "http://"+ WSadressIP.WSIP+":5000/cinternal/addNewEmployeeChatAccount";

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
                params.put("employee_id",emp_id);
                params.put("employee_email",emp_email);
                params.put("employee_password",password);
                return params;
            }
        };;
        Volley.newRequestQueue(ChattingActivity.this).add(stringRequest);
    }

}
