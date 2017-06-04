package in.opamg.app;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import global.ProjectGetSet;
import global.Variables;

public class AddProject extends AppCompatActivity {
    EditText company_name, project_name, order_name, scope_of_work, from, to, remarks;
    Button btnAddProject;
    SharedPreferences prefs;
    ProgressDialog pd;
    ImageView backBtn;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_project);

        company_name = (EditText) findViewById(R.id.company_name);
        project_name = (EditText) findViewById(R.id.project_name);
        order_name = (EditText) findViewById(R.id.order_name);
        scope_of_work = (EditText) findViewById(R.id.scope_of_work);
        from = (EditText) findViewById(R.id.from);
        to = (EditText) findViewById(R.id.to);
        remarks = (EditText) findViewById(R.id.remarks);
        btnAddProject = (Button) findViewById(R.id.btnAddProject);
        backBtn = (ImageView) findViewById(R.id.backBtn);

        prefs = getSharedPreferences(Variables.MY_PREFS_NAME, MODE_PRIVATE);

        ColorStateList colorStateList = ColorStateList.valueOf(ContextCompat.getColor(this,R.color.colorCommon));
        company_name.setBackgroundTintList(colorStateList);
        project_name.setBackgroundTintList(colorStateList);
        order_name.setBackgroundTintList(colorStateList);
        scope_of_work.setBackgroundTintList(colorStateList);
        from.setBackgroundTintList(colorStateList);
        to.setBackgroundTintList(colorStateList);
        remarks.setBackgroundTintList(colorStateList);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btnAddProject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String getCompanyName = company_name.getText().toString();
                String getProjectName = project_name.getText().toString();
                String getOrderNumber = order_name.getText().toString();
                String getScope = scope_of_work.getText().toString();
                String getFrom = from.getText().toString();
                String getTo = to.getText().toString();
                String getRemarks = remarks.getText().toString();

                if( getCompanyName.equalsIgnoreCase("")){
                    company_name.requestFocus();
                    company_name.setError(Html.fromHtml("<font color='#FFFFFF'>Please Enter Valid company name</font>"));
                }else if( getProjectName.equalsIgnoreCase("")){
                    project_name.requestFocus();
                    project_name.setError(Html.fromHtml("<font color='#FFFFFF'>Please Enter Valid Project Name</font>"));
                }else if( getOrderNumber.equalsIgnoreCase("")){
                    order_name.requestFocus();
                    order_name.setError(Html.fromHtml("<font color='#FFFFFF'>Please Enter Valid Order Number</font>"));
                }else if( getScope.equalsIgnoreCase("")){
                    scope_of_work.requestFocus();
                    scope_of_work.setError(Html.fromHtml("<font color='#FFFFFF'>Please Enter Valid Data</font>"));
                }else if( getFrom.equalsIgnoreCase("")){
                    from.requestFocus();
                    from.setError(Html.fromHtml("<font color='#FFFFFF'>Please Enter Valid Data</font>"));
                }else if( getTo.equalsIgnoreCase("")){
                    to.requestFocus();
                    to.setError(Html.fromHtml("<font color='#FFFFFF'>Please Enter Valid Data</font>"));
                }else if( getRemarks.equalsIgnoreCase("")){
                    remarks.requestFocus();
                    remarks.setError(Html.fromHtml("<font color='#FFFFFF'>Please Enter Valid Data</font>"));
                }else{
                    String id = prefs.getString(Variables.SESSION_ID, "");

                    RequestQueue queue = Volley.newRequestQueue(AddProject.this);
                    Map<String, String> params = new HashMap<String, String>();

                    params.put("action", Variables.ADD_PROJECT);
                    params.put("id",id);
                    params.put("pro_name",getProjectName);
                    params.put("pro_from",getFrom);
                    params.put("pro_to",getTo);
                    params.put("pro_company_name",getCompanyName);
                    params.put("work_order_number",getOrderNumber);
                    params.put("scope_of_wrk_id",getScope);
                    params.put("remarks",getRemarks);

                    JSONObject data = new JSONObject(params);

                    JSONObject param = new JSONObject();

                    try {
                        param.put("data", data);
                        pd = new ProgressDialog(AddProject.this);
                        pd.setMessage("Please wait...");
                        pd.setCancelable(false);
                        pd.show();
                    } catch (JSONException ex) {

                    }

                    JsonObjectRequest request = new JsonObjectRequest(
                            Request.Method.POST, Variables.API_URL + Variables.PROJECTS, param,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject jsonObj) {
                                    pd.dismiss();

                                    if (jsonObj != null) {

                                        try {
                                            String code = jsonObj.getString("code");
                                            Log.e("code", code);


                                            if (code.equalsIgnoreCase("200")) {
                                                Intent i = new Intent(AddProject.this, ProjectsActivity.class);
                                                startActivity(i);
                                                Toast.makeText(AddProject.this, "Project Added!", Toast.LENGTH_SHORT).show();
                                            }
                                            else
                                            {
                                                Toast.makeText(AddProject.this, "Project Already Exists!", Toast.LENGTH_SHORT).show();
                                            }
                                        } catch (JSONException e) {
                                            Toast.makeText(AddProject.this, "No Data Found", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(AddProject.this, error.toString(), Toast.LENGTH_LONG).show();
                                }
                            });
                    queue.add(request);
                }
            }
        });
    }
}
