package com.example.sishi;

import static android.content.Context.FINGERPRINT_SERVICE;
import static android.content.Context.KEYGUARD_SERVICE;

import android.app.KeyguardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.util.Log;

public class FingerPrintUtil {
    private static final String TAG = "FingerPrintUtil";
    public static final int RESULT_OK = 1;
    public static final int RESULT_NO_HARDWARE = 2;
    public static final int RESULT_KEYGUARD_NOT_SECURE = 3;
    public static final int RESULT_NO_DATA = 4;

    public static int checkSupport(Context context) {
        Log.d(TAG, "checkSupport: ");
        int ret;
        FingerprintManager fingerprintManager = (FingerprintManager) context.getSystemService(FINGERPRINT_SERVICE);
        KeyguardManager keyguardManager = (KeyguardManager) context.getSystemService(KEYGUARD_SERVICE);
        if (!fingerprintManager.isHardwareDetected()) {
            ret = RESULT_NO_HARDWARE;
            Log.d(TAG, "checkSupport: !fingerprintManager.isHardwareDetected()");
        } else if (!keyguardManager.isKeyguardSecure()) {
            ret = RESULT_KEYGUARD_NOT_SECURE;
            Log.d(TAG, "checkSupport:!keyguardManager.isKeyguardSecure()");
        } else if (!fingerprintManager.hasEnrolledFingerprints()) {
            ret = RESULT_NO_DATA;
            Log.d(TAG, "!fingerprintManager.hasEnrolledFingerprints()");
        } else {
            Log.d(TAG, "checkSupport: true");
            ret = RESULT_OK;
        }
        return ret;
    }

    //todo 完善各大厂商指纹
    public static void startFingerPrint(Context context) {
        Log.d(TAG, "startFingerPrint: " + Build.BRAND);
        String pkgName = "";
        String clsName = "";
        switch (Build.BRAND) {
            case BRAND.SONY:
                pkgName = "com.android.settings";
                clsName = "com.android.settings.Settings$FingerprintEnrollSuggestionActivity";
                break;
            case BRAND.OPPO:
            case BRAND.REALME:
                pkgName = "com.android.settings";
                clsName = "com.coloros.settings.feature.fingerprint.ColorFingerprintSettings";
                break;
            default:
                pkgName = "com.android.settings";
                clsName = "com.android.settings.Settings";
                break;
        }
        Intent intent = new Intent();
        ComponentName componentName = new ComponentName(pkgName, clsName);
        intent.setComponent(componentName);
        intent.setAction(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        try {
            context.startActivity(intent);
        } catch (Exception e) {
            Log.e(TAG, "jumpToFingerPrint: ", e);
        }
    }

    private static class BRAND {
        public static final String SONY = "sony";
        public static final String OPPO = "oppo";
        public static final String REALME = "Realme";
    }
}
