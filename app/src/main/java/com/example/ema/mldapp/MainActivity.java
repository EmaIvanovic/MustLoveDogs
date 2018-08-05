package com.example.ema.mldapp;

import android.content.Intent;
import android.nfc.Tag;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener
{
    private FirebaseAuth mAuth;
    static final int RC_SIGN_IN = 1;
    DatabaseReference rootRef;
    private static final String TAG = "MainActivity";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rootRef = FirebaseDatabase.getInstance().getReference();

    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth = FirebaseAuth.getInstance();
        if(mAuth.getCurrentUser() != null)
            proceedToHome();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_signin_email:
                emailSignIn();
                break;
            case R.id.button_register_email:
                emailRegister();
                break;

        }
    }
    private void emailSignIn() {
        Intent intent = new Intent(this, LogInActivity.class);
        startActivity(intent);
    }

    private void emailRegister() {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    private void proceedToHome() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }

//    private void googleSignIn() {
//        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
//        startActivityForResult(signInIntent, RC_SIGN_IN);
//    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
//        if (requestCode == RC_SIGN_IN) {
//            // The Task returned from this call is always completed, no need to attach
//            // a listener.
//            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
//            handleSignInResult(task);
//        }
//    }
//
//    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
//        try {
//            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
//
//            // Signed in successfully, show authenticated UI.
//            updateUI(account);
//        } catch (ApiException e) {
//            // The ApiException status code indicates the detailed failure reason.
//            // Please refer to the GoogleSignInStatusCodes class reference for more information.
//            //Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
//            updateUI(null);
//        }
//    }
}
