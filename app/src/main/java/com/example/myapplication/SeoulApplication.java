package com.example.myapplication;

import android.app.Application;
import android.content.Context;
import android.os.SystemClock;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

import java.util.concurrent.TimeUnit;

public class SeoulApplication extends Application {
    private static Context appContext;
    private static GoogleSignInClient googlesigninclient;

    public static Context getAppContext() {
        return appContext;
    }
    public void setAppContext(Context mAppContext) {
        this.appContext = mAppContext;
    }

    public GoogleSignInClient setGoogleClient(){

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.server_client_id))
                .requestEmail()
                .build();

        return GoogleSignIn.getClient(SeoulApplication.getAppContext(), gso);
    }
    public static GoogleSignInClient getGooglesigninclient() {
        return googlesigninclient;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //스플래시를 위한
        SystemClock.sleep(TimeUnit.SECONDS.toMillis(2));

        //context 생성
        this.setAppContext(getApplicationContext());

        //구글 로그인 클라이언트 생성
        googlesigninclient = setGoogleClient();
    }


}
