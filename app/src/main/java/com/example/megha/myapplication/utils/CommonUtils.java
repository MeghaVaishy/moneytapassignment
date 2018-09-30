package com.example.megha.myapplication.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class CommonUtils {

    private static volatile boolean sLastConnectionHealthy = true;
    private static volatile long sLastConnectionFetchTime = 0L;
    private static final int MIN_PING_INTERVAL = 1 * 60 * 1000;
    private static volatile boolean bShowTost = false;

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        boolean result = activeNetworkInfo != null && activeNetworkInfo.isConnected();
        return result && !wasInternetFailed();
    }

    public static boolean wasInternetFailed() {
        if (System.currentTimeMillis() - sLastConnectionFetchTime <= MIN_PING_INTERVAL) {
            return !sLastConnectionHealthy;
        }
        return false;
    }

    public static void showProgress(ProgressDialog progressDialog, String msg) {
        if (progressDialog != null) {
            progressDialog.setMessage(msg);
            progressDialog.show();
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
        }
    }

    public static void hideProgress(ProgressDialog progressDialog) {
        if (progressDialog != null) {
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        }
    }
}
