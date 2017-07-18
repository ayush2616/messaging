package com.ayush.shivman.ourmessaging;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;


/**
 * Created by ayush on 18/7/17.
 */

public class FirstStart extends Activity {
    FirebaseAuth mAuth;
    String mVerificationId;
    ProgressDialog pd;
    PhoneAuthProvider.ForceResendingToken mResendToken;
    EditText edNumber,edCode;
    Button btnSendNumber,btnSendCode,btnResend;
     final String TAG="firstart";
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth=FirebaseAuth.getInstance();
        setContentView(R.layout.firststart_activity);
        edNumber=(EditText)findViewById(R.id.ed_mobileNumber);
        edCode=(EditText)findViewById(R.id.ed_verifyCode);
        btnSendNumber=(Button)findViewById(R.id.sendVerification);
        btnResend=(Button)findViewById(R.id.btn_resend);
        btnSendCode=(Button)findViewById(R.id.btn_verifyCode);
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                Log.d(TAG, "onVerificationCompleted:" + credential);
                signInWithPhoneAuthCredential(credential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                Log.w(TAG, "onVerificationFailed", e);

                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                    // ...
                    Toast.makeText(FirstStart.this, "Failed to sign in", Toast.LENGTH_SHORT).show();
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                    // ...
                }
                Toast.makeText(FirstStart.this, "Please Try Again..", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCodeSent(String verificationId,
                                   PhoneAuthProvider.ForceResendingToken token) {
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.
                Log.d(TAG, "onCodeSent:" + verificationId);

                // Save verification ID and resending token so we can use them later
                String mVerificationId = verificationId;
                PhoneAuthProvider.ForceResendingToken mResendToken = token;

                // ...

            }
        };
        btnSendNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendCode();
            }
        });
    }
    void sendCode()
    {
        String phoneNumber=edNumber.getText().toString();
        if(phoneNumber==null || phoneNumber.isEmpty()|| phoneNumber.length()<10)
        {
            Toast.makeText(this, "Enter correct mobile number", Toast.LENGTH_SHORT).show();
            return;
        }
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,
                60,
                TimeUnit.SECONDS,
                this,
                mCallbacks);
        btnSendNumber.setEnabled(false);
        findViewById(R.id.parentLayout).setVisibility(View.VISIBLE);

    }
    private void resendVerificationCode(String phoneNumber,
                                        PhoneAuthProvider.ForceResendingToken token) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks,         // OnVerificationStateChangedCallbacks
                token);             // ForceResendingToken from callbacks
    }
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");

                            FirebaseUser user = task.getResult().getUser();
                            String number=user.getPhoneNumber();
                            SharedPreferences.Editor editor=getSharedPreferences("com.ayush.shivman.ourmessaging.SECRET", Context.MODE_PRIVATE).edit();
                            editor.putString("number",number);
                            editor.apply();
                            finish();
                            startActivity(new Intent(getApplicationContext(),MainActivity.class));
                            // ...
                        } else {
                            // Sign in failed, display a message and update the UI
                            Toast.makeText(FirstStart.this, "Failed to sign in", Toast.LENGTH_SHORT).show();
                            findViewById(R.id.parentLayout).setVisibility(View.GONE);
                            btnSendNumber.setEnabled(true);
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                            }
                        }
                    }
                });
    }
}
