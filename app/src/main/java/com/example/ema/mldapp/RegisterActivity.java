package com.example.ema.mldapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseError;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private TextView email,pass,confirmPass,firstName,lastName,userName;
    private TextView validateTxt,validatePass,validateNoEmpties;
    private final String TAG = "RegisterActivity.class";
    private DatabaseReference mDatabase;
    private boolean isUnique;
    private String emailfd;
    private String emailtxt,passtxt,userNametxt,firstNametxt,lastNametxt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        //isUnique = true;
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        validatePass = findViewById(R.id.lblPasswordValid);
        validateTxt = findViewById(R.id.lblUsernameValid);
        validateNoEmpties = findViewById(R.id.lblFieldsEmpty);
        validateNoEmpties.setText("");
        validateTxt.setText("");
        validatePass.setText("");
        email = findViewById(R.id.txtEmail);
        pass = findViewById(R.id.txtPass);
        confirmPass = findViewById(R.id.txtCPass);
        firstName = findViewById(R.id.txtName);
        lastName = findViewById(R.id.txtSurname);
        userName = findViewById(R.id.txtUser);
        userName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                final String uname = userName.getText().toString();

                //check if a user with same username exists or if it has forbidden chars
                if(!hasFocus){
                    if(uname.length() < 4)
                        validateTxt.setText("Username at least 4 chars");
                    else if(uname.indexOf('.') != -1 || uname.indexOf('/') != -1 ||
                    uname.indexOf('$') != -1 || uname.indexOf('#') != -1 || uname.indexOf('[') != -1 || uname.indexOf(']') != -1) {
                        validateTxt.setText("Can't contain ./#$[]");
                    }
                    else{
                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                        DatabaseReference usersRef = ref.child("users");
                        usersRef.orderByKey().equalTo(uname).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull  DataSnapshot dataSnapshot) {
                                if(dataSnapshot.exists())
                                    validateTxt.setText("Username already taken");
                                else
                                    validateTxt.setText("");
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                Log.d("RegisterActivity", "Database error retrieving data");
                            }
                        });


                    }
                }
            }
        });

        pass.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                if(pass.getText().toString().length() < 10){
                    validatePass.setText("Password must contain at least 10 chars");
                }

                else validatePass.setText("");
            }}
        });

//        confirmPass.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                if(hasFocus){
//                if(pass.getText().toString().equals("")){
//                    return;
//                }
//            }
//            }
//        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }

    public void onClick(View v) {
        if(v.getId() == R.id.btnRegister)
        {

            emailtxt = email.getText().toString();
            passtxt = pass.getText().toString();
            userNametxt = userName.getText().toString();
            firstNametxt = firstName.getText().toString();
            lastNametxt = lastName.getText().toString();

            register(emailtxt,passtxt,userNametxt,firstNametxt,lastNametxt);
        }
    }

    private boolean noFieldsEmpty() {
        if(!email.getText().toString().equals("") && !userName.getText().toString().equals("") && !firstName.getText().toString().equals("") &&
                !lastName.getText().toString().equals("") && !confirmPass.getText().toString().equals("") && !pass.getText().toString().equals(""))
        {
            validateNoEmpties.setText("");
            return true;
        }
        else{
            validateNoEmpties.setText("There should be no empty fields");
            return false;
        }
    }

    private boolean isValidated(){
        if(validateTxt.getText().toString().equals("") && pwdConfirmValid() && noFieldsEmpty())
            return true;
        return false;
    }

    private void register(final String email, String password, final String userName, final String firstName, final String lastName){
        if(isValidated()){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            final FirebaseUser user = mAuth.getCurrentUser();
                            if(user!=null){
                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(userName)
                                    .build();

                            user.updateProfile(profileUpdates)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(RegisterActivity.this,user.getDisplayName(),Toast.LENGTH_LONG).show();
                                                Log.d(TAG, "Usr profile updated.");
                                            }
                                        }
                                    });

                            writeNewUser(userName,email,firstName,lastName);
                            startActivity(new Intent(RegisterActivity.this,LogInActivity.class));
                        }} else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        }
    }

    //    private void sendVerificationEmail()
//    {
//        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//
//        user.sendEmailVerification()
//                .addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        if (task.isSuccessful()) {
//                            // email sent
//                            startActivity(new Intent(RegisterActivity.this, ProfileActivity.class));
//                            finish();
//                        }
//                        else
//                        {
//                            // email not sent, so display message and restart the activity or do whatever you wish to do
//                            //restart this activity
//                            overridePendingTransition(0, 0);
//                            finish();
//                            overridePendingTransition(0, 0);
//                            startActivity(getIntent());
//
//                        }
//                    }
//                });
//    }

    private boolean pwdConfirmValid(){
        if(pass.getText().toString().equals(confirmPass.getText().toString()))
        return true;

        return false;
    }

    private void writeNewUser(String username, String email, String firstName, String lastName) {
        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);

        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        String formattedDate = df.format(c);
        User u = new User(email,firstName,lastName,username,null,formattedDate,"Just joined",0);
        mDatabase.child("users").child(username).setValue(u);
    }
}
