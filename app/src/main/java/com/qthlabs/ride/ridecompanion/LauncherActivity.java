package com.qthlabs.ride.ridecompanion;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.Session;
import com.facebook.SessionLoginBehavior;
import com.facebook.SessionState;

public class LauncherActivity extends Activity {

    private TextView fbStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);
        // Check for an open session
        facebookSessionCheck(this);
    }

    protected void facebookSessionCheck(final Activity _self){

        Session session = null;
        //already authenticated in another Activity, so reuse session:
        session = Session.getActiveSession();
        //app resumed/rotated, so reopen session:
        if (session == null) {
            session = Session.openActiveSessionFromCache(getApplicationContext());
        }

        if (session == null) {
            session = new Session(getApplicationContext());

            //access_token from SDK 2.0 (async API), stored in SharedPreferences
            SharedPreferences sharedPref = _self.getPreferences(Context.MODE_PRIVATE);
            String AccessTokenString = sharedPref.getString("FB_Access_token", null);
            if (AccessTokenString != null) {
                AccessToken access_token = AccessToken.createFromExistingAccessToken(AccessTokenString, null, null, null, null);
                session.open(access_token, new Session.StatusCallback(){
                    @Override
                    public void call(Session session, SessionState state, Exception exception) {
                        if (session.isOpened()) {
                            SharedPreferences sharedPref = _self.getPreferences(Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPref.edit();
                            editor.putString("FB_Access_token", session.getAccessToken());
                            editor.apply();
                        } else {jumpToLogin(_self); return;}
                    }
                });
            } else { jumpToLogin(_self); return; }
        }

        //user previously logged in (app restarted), or SSO
        if (!session.isOpened()) {
            session.openForRead(new Session.OpenRequest(_self).setLoginBehavior(SessionLoginBehavior.SSO_WITH_FALLBACK));
        }
        if (session == null ) { jumpToLogin(_self); return;}
        // LOGGED IN!!
        Session.setActiveSession(session);
        jumpToMain(_self);
    }

    private void jumpToLogin(final Activity _self){
        Intent intent = new Intent(_self, LoginActivity.class);
        startActivity(intent);
    }
    private void jumpToMain(final Activity _self){
        Intent intent = new Intent(_self,MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Session.getActiveSession()
                .onActivityResult(this, requestCode, resultCode, data);
    }
}
