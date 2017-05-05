package in.opamg.app;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity {
    Button login;
    EditText empId, password;
    TextView forgetPassword;
    String getEmployeeId, getPassword;
    Intent i;
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
//                    i = new Intent(LoginActivity.this, Address.class);
//                    startActivity(i);
                }
            }
        });


    }
}
