package com.virupawadegmail.sdhmancharpune;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by amit on 30/9/16.
 */
public class Signup extends AppCompatActivity{
    private EditText name,email,password,phone;
    private Button signup,login;
    public static String phoneno="";
    private FirebaseAuth auth;
    public static final String url="https://blog-641ac.firebaseio.com/";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        Firebase.setAndroidContext(this);
        setTitle("Signup");
        auth = FirebaseAuth.getInstance();
        name=(EditText)findViewById(R.id.nm);
        email=(EditText)findViewById(R.id.el);
        password=(EditText)findViewById(R.id.pswd);
        phone=(EditText)findViewById(R.id.phn);
        signup=(Button)findViewById(R.id.sup);

        signup.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                signup();
            }
        });
    }

    public void signup(){
        if(isInternetOn()) {
            if (!validate()) {
                onSignupFailed();
                return;
            }
            final String mail = email.getText().toString();
            String cpassword = password.getText().toString();
            phoneno = phone.getText().toString();
            final String cname = name.getText().toString().trim();
            // progressBar.setVisibility(View.VISIBLE);
            final Firebase ref = new Firebase(url);
            //final Firebase userRef=ref.child("Firebaseuser");
            // final Firebaseuser usr = new Firebaseuser();
            // usr.setName(cname);
            // usr.setEmail(mail);
            // usr.setPhone(phoneno);
            // usr.setRefferal(refer);

            final Map<String,Object> userdata=new HashMap<String, Object>();
            userdata.put("name",cname);
            userdata.put("email",mail);
            userdata.put("phone",phoneno);

            auth.createUserWithEmailAndPassword(mail, cpassword).addOnCompleteListener(Signup.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    Toast.makeText(Signup.this, "createUserWithEmail:onComplete:" + task.isSuccessful(), Toast.LENGTH_SHORT).show();
                    //progressBar.setVisibility(View.GONE);
                    if (!task.isSuccessful()) {
                        Toast.makeText(Signup.this, "Authentication failed." + task.getException(),
                                Toast.LENGTH_SHORT).show();
                    } else {
                        //final ProgressDialog progressDialog = new ProgressDialog(Signup.this,
                        //   R.style.AppTheme_Dark_Dialog);
                        Firebase userRef=ref.child(phoneno);
                        userRef.updateChildren(userdata);
                        //  ref.child("Firebaseuser").updateChildren(userdata);
                                /*ref.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        for(DataSnapshot postSnapshot:dataSnapshot.getChildren()){
                                            Firebaseuser usr=postSnapshot.getValue(Firebaseuser.class);
                                            String string="Name:"+usr.getName()+"\nEmail:"+usr.getEmail()+"\nPhone:"+usr.getPhone()+"\nReferal:"+usr.getRefferal()+"\n \n";
                                        }
                                    }
                                    @Override
                                    public void onCancelled(FirebaseError firebaseError) {
                                        System.out.println("The read failed"+firebaseError.getMessage());
                                    }
                                });*/

                        startActivity(new Intent(Signup.this, MainActivity.class));

                        finish();
                    }
                }
            });

        /*else {
            Intent i1 = new Intent(Signup.this, Signup.class);
            startActivity(i1);
            alert.showAlertDialog(Signup.this, "Signup failed..", "Follow instructions", false);
            finish();
        }*/
        }else{
            final ProgressDialog progressDialog = new ProgressDialog(Signup.this,
                    R.style.AppTheme_Dark_Dialog);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Please connect to a network...");
            progressDialog.show();
            timerDelayRemoveDialog(5000, progressDialog);
        }
    }
    public static String getProfile( ){
        return phoneno;
    }
    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "Signup failed", Toast.LENGTH_LONG).show();

        //_signupButton.setEnabled(true);
    }

    public boolean validate(){
        boolean valid=true;
        String  mail = email.getText().toString();
        String cpassword = password.getText().toString();
        String phoneno = phone.getText().toString();
        String cname=name.getText().toString().trim();
        if(cname.isEmpty()||cname.length()<4){
            name.setError("Lenth must be atleast 4");
            valid=false;
        }else{
            name.setError(null);
        }
        if(mail.isEmpty()||!android.util.Patterns.EMAIL_ADDRESS.matcher(mail).matches()){
            email.setError("Email must be valid");
            valid=false;
        }else{
            email.setError(null);
        }
        if(cpassword.length()<7){
            password.setError("minimum length 7");
            valid=false;
        }else{
            password.setError(null);
        }
        if(phoneno.length()<8||phoneno.length()>12){
            phone.setError("Enter correct number");
            valid=false;
        }else{
            phone.setError(null);
        }
        return valid;

    }
    public final boolean isInternetOn() {

        // get Connectivity Manager object to check connection
        ConnectivityManager connec =
                (ConnectivityManager)getSystemService(getBaseContext().CONNECTIVITY_SERVICE);

        // Check for network connections
        if ( connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTED ||
                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED ) {

            // if connected with internet

            Toast.makeText(this, " Connected ", Toast.LENGTH_LONG).show();
            return true;

        } else if (
                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.DISCONNECTED ||
                        connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.DISCONNECTED  ) {


            return false;
        }
        return false;
    }
    public void timerDelayRemoveDialog(long time, final Dialog d){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                d.dismiss();
            }
        }, time);
    }
}
