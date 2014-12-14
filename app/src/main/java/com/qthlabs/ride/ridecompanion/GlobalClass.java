package com.qthlabs.ride.ridecompanion;

/**
 * Created by jareval on 04/12/2014.
 */
import android.app.Application;
import org.json.JSONObject;

public class GlobalClass extends Application{

    public JSONObject session;

    public JSONObject getSession() {

        return session;
    }

    public void setSession(JSONObject aSession) {

        session = aSession;

    }

}