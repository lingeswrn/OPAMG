package in.opamg.app;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import global.ProjectGetSet;
import global.Variables;
import in.opamg.app.DatabaseConnection.DatabaseHandler;
import in.opamg.app.Models.*;

public class ProjectsActivity extends AppCompatActivity {
    SharedPreferences prefs;
    ProjectListing AdapterAddressList;
    ListView listProject;
    ProgressDialog pd;
    ArrayList<ProjectGetSet> taskList = new ArrayList<ProjectGetSet>();
    ImageView addProject, logout;
    DatabaseHandler db;
    private static final int PERMISSION_REQUEST_CODE = 1337;
    private View view;
    private LocationManager locationManager;
    private String provider;
    boolean gps_enabled = false;
    boolean network_enabled = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_projects);
        db = new DatabaseHandler(this);

        prefs = getSharedPreferences(Variables.MY_PREFS_NAME, MODE_PRIVATE);

        listProject = (ListView) findViewById(R.id.projects);
        addProject = (ImageView) findViewById(R.id.addProject);
        logout = (ImageView) findViewById(R.id.logout);

        String id = prefs.getString(Variables.SESSION_ID, "");
        String name = prefs.getString(Variables.SESSION_NAME, "");
        String mobile = prefs.getString(Variables.SESSION_MOBILE, "");
        String email = prefs.getString(Variables.SESSION_EMAIL, "");

        db.createMeasurementTables();


        addProject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isOnline()) {
                    Intent i = new Intent(ProjectsActivity.this, AddProject.class);
                    startActivity(i);
                }else {
                    Toast.makeText(ProjectsActivity.this, "No Internet Connection", Toast.LENGTH_LONG).show();
                }
            }

        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("id", "0");
                editor.commit();

                Intent i = new Intent(ProjectsActivity.this, LoginActivity.class);
                startActivity(i);
                ProjectsActivity.this.finish();
            }
        });
        checkPermission();
    }

    public void checkPermission(){
        int result = ContextCompat.checkSelfPermission(ProjectsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION);
        if ( result == -1 ){
            ActivityCompat.requestPermissions(ProjectsActivity.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},PERMISSION_REQUEST_CODE);
        }else {
            callAll();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    callAll();

                } else {
                    Toast toast = Toast.makeText(ProjectsActivity.this, "Location Permission needed", Toast.LENGTH_LONG);
                    toast.show();
                    checkPermission();
                }
                break;
        }
    }

    public void callAll(){
        if(isOnline()){
            Log.e("Online", String.valueOf(isOnline()));
            getProjectList();

            db.createEquipmentTable();
            db.createLayersTable();
            db.createCookieTable();

            getEquipmentList();
            getLayersList();
        }else {
            try {
                getOfflineProjects();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.e("Offline", String.valueOf(isOnline()));
            Toast.makeText(ProjectsActivity.this, "No Internet", Toast.LENGTH_LONG).show();
        }
    }

    private void getEquipmentList() {
        RequestQueue queue = Volley.newRequestQueue(ProjectsActivity.this);
        Map<String, String> params = new HashMap<String, String>();
        params.put("action", Variables.LIST);
        JSONObject data = new JSONObject(params);

        JSONObject param = new JSONObject();
        try {
            param.put("data", data);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST, Variables.API_URL + Variables.EQUIPMENTS, param,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObj) {


                        if (jsonObj != null) {

                            try {
                                String code = jsonObj.getString("code");
                                Log.e("code", code);

                                String responce = jsonObj.getString("data");
                                JSONArray result = jsonObj.getJSONArray("data");
                                Log.e("result", "" + result);

                                if (code.equalsIgnoreCase("200")) {
                                    db.deleteTable(db.getTableEquipments());
                                    for (int i = 0; i < result.length(); i++)
                                    {
                                        JSONObject object = result.getJSONObject(i);
                                        Log.e("datat", String.valueOf(object));
                                        String getId = object.getString("id");
                                        String getModelNumber = object.getString("model_number");
                                        String getLastCalibration = object.getString("last_calibration_service_center");
                                        String getExpiryDate = object.getString("expiry_date");
                                        String getLeastCount = object.getString("least_count");
                                        String getOwner = object.getString("owner");
                                        String getStatus = object.getString("status");
                                        String getCreatedDate = object.getString("created_date");

                                        db.addEquipment(new Equipments(getId, getModelNumber, getLastCalibration, getExpiryDate, getLeastCount, getOwner, getStatus, getCreatedDate));
                                    }
                                }
                                else
                                {
                                    Toast.makeText(ProjectsActivity.this, responce, Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                Toast.makeText(ProjectsActivity.this, "No Data Found", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ProjectsActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                });
        queue.add(request);
    }

    public void getOfflineProjects() throws JSONException {

        List<Project> projects = db.getAllProjects();

        JSONArray projArray = new JSONArray();
        for (Project cn : projects) {
            JSONObject proj = new JSONObject();
            proj.put("id", cn.get_id());
            proj.put("project_name", cn.get_project_name());
            projArray.put(proj);
        }
        Log.e("projArray", String.valueOf(projArray));
        Gson gson = new Gson();
        JSONArray json = projArray;
        taskList = gson.fromJson(String.valueOf(json), new TypeToken<List<ProjectGetSet>>(){}.getType());
        AdapterAddressList = new ProjectListing(taskList);
        listProject.setAdapter(AdapterAddressList);
    }


    private void getProjectList() {
        String id = prefs.getString(Variables.SESSION_ID, "");
        String name = prefs.getString(Variables.SESSION_NAME, "");

        RequestQueue queue = Volley.newRequestQueue(ProjectsActivity.this);
        Map<String, String> params = new HashMap<String, String>();
        params.put("name", name);
        params.put("action", Variables.LIST);
        params.put("id",id);
        JSONObject data = new JSONObject(params);

        JSONObject param = new JSONObject();

        try {
            param.put("data", data);
            pd = new ProgressDialog(ProjectsActivity.this);
            pd.setMessage("Fetching Projects...");
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

                                String responce = jsonObj.getString("data");
                                JSONArray result = jsonObj.getJSONArray("data");

                                if (code.equalsIgnoreCase("200")) {
                                    db.deleteTable(db.getTableProjects());
                                    for (int i = 0; i < result.length(); i++)
                                    {
                                        JSONObject object = result.getJSONObject(i);
                                        String getId = object.getString("id");
                                        String getFrom = object.getString("_from");
                                        String getTo = object.getString("_to");
                                        String getProject = object.getString("project_name");
                                        String getCompany = object.getString("company_name");
                                        String getOrder = object.getString("work_order_number");
                                        String getScope = object.getString("scope_wrk");
                                        String getRemarks = object.getString("remarks");
                                        String getUserId = object.getString("user_id");
                                        String getStatus = object.getString("status");
                                        String getCreatedDate = object.getString("created_date");
                                        String getUpdatedDate = object.getString("updated_date");

                                        db.addProject(new Project(getId, getFrom, getTo, getProject, getCompany, getOrder, getScope, getRemarks, getUserId, getStatus, getCreatedDate, getUpdatedDate));
                                    }
                                    getOfflineProjects();
                                }
                                else
                                {
                                    Toast.makeText(ProjectsActivity.this, responce, Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                Toast.makeText(ProjectsActivity.this, "No Data Found", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ProjectsActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                });
        queue.add(request);
    }

    private void getLayersList() {

        RequestQueue queue = Volley.newRequestQueue(ProjectsActivity.this);
        Map<String, String> params = new HashMap<String, String>();

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET, Variables.API_URL + Variables.LAYERS, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObj) {
                        pd.dismiss();

                        if (jsonObj != null) {

                            try {
                                String code = jsonObj.getString("code");

                                String responce = jsonObj.getString("data");
                                JSONArray result = jsonObj.getJSONArray("data");

                                if (code.equalsIgnoreCase("200")) {
                                    db.deleteTable(db.getTableLayers());
                                    for (int i = 0; i < result.length(); i++)
                                    {
                                        JSONObject object = result.getJSONObject(i);
                                        String getCode = object.getString("code");
                                        String getDesc = object.getString("description");
                                        String getCategory = object.getString("category");

                                        db.addLayers(new Layers(getCode, getDesc, getCategory));
                                    }
                                }
                                else
                                {
                                    Toast.makeText(ProjectsActivity.this, responce, Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                Toast.makeText(ProjectsActivity.this, "No Data Found", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ProjectsActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                });
        queue.add(request);
    }

    public class ProjectListing extends BaseAdapter {
        ArrayList<ProjectGetSet> projecctList = new ArrayList<ProjectGetSet>();
        @Override
        public int getCount() {
            return taskList.size();
            //return 10;
        }
        public ProjectListing(ArrayList<ProjectGetSet> taskList) {
            this.projecctList = taskList;
        }
        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            ViewHolder holder;
            if (convertView == null) {

                LayoutInflater inflater = (LayoutInflater) ProjectsActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                inflater = LayoutInflater.from(ProjectsActivity.this);
                convertView = inflater.inflate(R.layout.project_list, null);
                holder = new ViewHolder();

                holder.name      = (TextView) convertView.findViewById(R.id.projectName);
                holder.linProject = (LinearLayout) convertView.findViewById(R.id.projectList);


                convertView.setTag(holder);
            } else {

                holder = (ViewHolder) convertView.getTag();
            }
            final ProjectGetSet list = this.projecctList.get(position);


            holder.name.setText(list.getName());
            holder.linProject.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(ProjectsActivity.this, MapActivity.class);
                    Variables.PROJECT_ID = list.getId();
                    i.putExtra("PROJECT_ID", list.getId());
                    startActivity(i);
                }
            });
            return convertView;
        }

        public class ViewHolder {
            LinearLayout linProject;
            TextView name;
        }
    }
    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}
