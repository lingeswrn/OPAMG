package in.opamg.app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import global.Variables;

public class SplashScreen extends AppCompatActivity {
    SharedPreferences prefs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        prefs = getSharedPreferences(Variables.MY_PREFS_NAME, MODE_PRIVATE);
        String id = prefs.getString(Variables.SESSION_ID, "");
        Log.e("id", id);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                String id = prefs.getString(Variables.SESSION_ID, "");
                if( id.equalsIgnoreCase("0") || id.equalsIgnoreCase("") ){
                    Intent intent = new Intent(SplashScreen.this, LoginActivity.class);
                    startActivity(intent);
                    SplashScreen.this.finish();
                }else {
                    Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                    startActivity(intent);
                    SplashScreen.this.finish();
                }
            }
        }, 1500);
    }
}
