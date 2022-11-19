package com.example.sishi;

import static com.example.sishi.FingerPrintUtil.checkSupport;
import static com.example.sishi.FingerPrintUtil.startFingerPrint;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import android.app.KeyguardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.security.Signature;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "wewekfc";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    @Override
    protected void onResume() {
        super.onResume();

    }

    public void check(View view) {
        int result = checkSupport(this);
        if (result == FingerPrintUtil.RESULT_NO_HARDWARE) {
            Toast.makeText(this, "no hardware", Toast.LENGTH_SHORT).show();
        } else if (result == FingerPrintUtil.RESULT_KEYGUARD_NOT_SECURE || result == FingerPrintUtil.RESULT_NO_DATA) {
            Toast.makeText(this, "jump to set", Toast.LENGTH_SHORT).show();
            startFingerPrint(this);
        } else {
            BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder().setTitle("titile").setDescription("desc").setNegativeButtonText("取消").build();
            BiometricPrompt biometricPrompt = new BiometricPrompt(this, ContextCompat.getMainExecutor(this), new BiometricPrompt.AuthenticationCallback() {
                @Override
                public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                    super.onAuthenticationError(errorCode, errString);
                    Toast.makeText(MainActivity.this, errString, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                    super.onAuthenticationSucceeded(result);
                    Toast.makeText(MainActivity.this, "success", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onAuthenticationFailed() {
                    super.onAuthenticationFailed();
                    Toast.makeText(MainActivity.this, "onAuthenticationFailed", Toast.LENGTH_SHORT).show();
                }
            });
            biometricPrompt.authenticate(promptInfo);
        }
    }


}