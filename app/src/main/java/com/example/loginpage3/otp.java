package com.example.loginpage3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

import com.example.loginpage3.Model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class otp extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference users;

    String verificationCodeBySystem;
    Button btnVerify;
    EditText txtOtp;
    TextView CountTime;
    ProgressBar progressBar;
    CountDownTimer CountDownTimer;
    int counter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);

        btnVerify = findViewById(R.id.btnVerify);
        txtOtp = findViewById(R.id.txtOtp);
        progressBar = findViewById(R.id.progressBar);
        CountTime = findViewById(R.id.CountTime);

        database = FirebaseDatabase.getInstance();
        users = database.getReference("Users");

        //progressBar.setVisibility(View.VISIBLE);

        /*CountDownTimer = new CountDownTimer(30000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                CountTime.setText(String.valueOf(counter));
                counter++;
            }

            @Override
            public void onFinish() {
                Toast.makeText(otp.this, "Try Again Later!", Toast.LENGTH_SHORT).show();
                System.exit(0);
            }
        }.start();*/
        final String username = getIntent().getStringExtra("username");

        final DatabaseReference databaseReference = database.getReference("Users");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren())
                    if(dataSnapshot.child("username").getValue(String.class).contains(username)) {
                        String phoneNumber = dataSnapshot.child("phoneNo").getValue().toString();
                        sendVerificationCodeToUser(phoneNumber);
                    }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });



    }

    private void sendVerificationCodeToUser(String phoneNumber) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+91" + phoneNumber,        // Phone number to verify
                30,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                TaskExecutors.MAIN_THREAD,              // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);

            verificationCodeBySystem = s;
        }

        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

            String code = phoneAuthCredential.getSmsCode();
            if(code!=null){
                verifyCode(code);
            }

        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            System.exit(0);
        }

        private void verifyCode(String codeByUser){

            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationCodeBySystem, codeByUser);
            signInByUser(credential);
        }
    };

    private void signInByUser(PhoneAuthCredential credential) {

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(otp.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()){
                            //Toast.makeText(otp.this, "Sign In Successful", Toast.LENGTH_SHORT).show();
                            Intent s = new Intent(getApplicationContext(), dashboardActivity.class);
                            startActivity(s);
                        }
                        else
                            System.exit(0);

                    }
                });
    }
}