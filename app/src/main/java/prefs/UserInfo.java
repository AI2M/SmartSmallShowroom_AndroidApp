package prefs;

import android.content.Context;
import android.content.SharedPreferences;

import java.lang.reflect.Array;

/**
 * Created by tongchaitonsau on 10/10/2017 AD.
 */

public class UserInfo {
    private static final String TAG = UserSession.class.getSimpleName();
    private static final String PREF_NAME = "userinfo";
    private static final String KEY_ID = "id";
    private static final String KEY_PASSWORD = "password";




    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    Context ctx;

    public UserInfo(Context ctx) {
        this.ctx = ctx;
        prefs = ctx.getSharedPreferences(PREF_NAME, ctx.MODE_PRIVATE);
        editor = prefs.edit();
    }

    public void setPassword(String password){
        editor.putString(KEY_PASSWORD, password);
        editor.apply();
    }

    public void setId(String id){
        editor.putString(KEY_ID, id);
        editor.apply();
    }

    public void clearUserInfo(){
        editor.clear();
        editor.commit();
    }



    public String getKeyId(){return prefs.getString(KEY_ID, "");}
    public String getKeyPassword(){return prefs.getString(KEY_PASSWORD, "");}
}
