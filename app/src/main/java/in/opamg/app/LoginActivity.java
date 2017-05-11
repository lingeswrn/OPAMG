package in.opamg.app;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import global.*;


public class LoginActivity extends AppCompatActivity {
    Button login;
    EditText empId, password;
    TextView forgetPassword;
    String getEmployeeId, getPassword;
    Intent i;
    ProgressDialog pd;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        login           = (Button) findViewById(R.id.login);

        empId           = (EditText) findViewById(R.id.empId);
        password        = (EditText) findViewById(R.id.password);

        forgetPassword  = (TextView) findViewById(R.id.forgetPassword);

        ColorStateList colorStateList = ColorStateList.valueOf(ContextCompat.getColor(this,R.color.colorCommon));
        empId.setBackgroundTintList(colorStateList);
        password.setBackgroundTintList(colorStateList);
        //LOGIN FUNCTION
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getEmployeeId   = empId.getText().toString();
                getPassword     = password.getText().toString();

                if( getEmployeeId.equalsIgnoreCase("")){
                    empId.requestFocus();
                    empId.setError(Html.fromHtml("<font color='#FFFFFF'>Please Enter Valid Emp. Id</font>"));
                }else if( getPassword.equalsIgnoreCase("")){
                    password.requestFocus();
                    password.setError(Html.fromHtml("<font color='#FFFFFF'>Please Enter Valid Password</font>"));
                }else{
                    RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("username", getEmployeeId);
                    params.put("password", getPassword);

                    JSONObject data = new JSONObject(params);
                    JSONObject param = new JSONObject();
                    try {
                        param.put("data", data);
                        pd = new ProgressDialog(LoginActivity.this);
                        pd.setMessage("Please Wait...");
                        pd.setCancelable(false);
                        pd.show();

                    } catch (JSONException ex) {

                    }

                    JsonObjectRequest request = new JsonObjectRequest(
                            Request.Method.POST, Variables.API_URL + Variables.LOGIN, param,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject jsonObj) {
                                    pd.dismiss();
                                    if (jsonObj != null) {
                                        Log.e("response", jsonObj.toString());

                                        //CODE
                                        String code = null;
                                        try {
                                            code = jsonObj.getString("code");
                                            if(code.equalsIgnoreCase("200")) {
                                                SharedPreferences.Editor editor = getSharedPreferences(Variables.MY_PREFS_NAME, MODE_PRIVATE).edit();
                                                JSONArray response = jsonObj.getJSONArray("data");
                                                JSONObject data = response.getJSONObject(0);

                                                String id   = data.getString("id");
                                                String name = data.getString("name");
                                                String mobile = data.getString("mobile");
                                                String email_id = data.getString("email_id");

                                                editor.putString(Variables.SESSION_ID, id);
                                                editor.putString(Variables.SESSION_NAME, name);
                                                editor.putString(Variables.SESSION_MOBILE, mobile);
                                                editor.putString(Variables.SESSION_EMAIL, email_id);

                                                editor.commit();
                                                i = new Intent(LoginActivity.this, ProjectsActivity.class);
                                                startActivity(i);
                                            }else {
                                                Toast.makeText(LoginActivity.this, "Login Error!", Toast.LENGTH_SHORT).show();
                                                AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(LoginActivity.this);

                                                dlgAlert.setMessage("wrong password or username");
                                                dlgAlert.setTitle("Error Message...");
                                                dlgAlert.setPositiveButton("OK", null);
                                                dlgAlert.setCancelable(true);
                                                dlgAlert.create().show();

                                                dlgAlert.setPositiveButton("Ok",
                                                        new DialogInterface.OnClickListener() {
                                                            public void onClick(DialogInterface dialog, int which) {

                                                            }
                                                        });
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                        Log.e("code", code);
                                    }
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("Error", error.toString());
                        }
                    });
                    queue.add(request);
                }
            }
        });


    }
}
