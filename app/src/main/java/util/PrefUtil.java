package util;

import android.content.Context;

public class PrefUtil {

    private Context context;
    private final String PREFS_KEY = "PREFS";

    public PrefUtil(Context context) {
        this.context = context;
    }


    public String getAccessToken() {
        return context.getSharedPreferences(PREFS_KEY,Context.MODE_PRIVATE).getString("access_token","");
    }

    public void setAccessToken(String access_token) {
        context.getSharedPreferences(PREFS_KEY,Context.MODE_PRIVATE).edit().putString("access_token",access_token).commit();
    }
}
