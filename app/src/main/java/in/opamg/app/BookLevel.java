package in.opamg.app;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.method.SingleLineTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
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

import java.util.logging.Level;

import global.Variables;
import in.opamg.app.DatabaseConnection.DatabaseHandler;

public class BookLevel extends AppCompatActivity {
    SharedPreferences prefs;
    DatabaseHandler db;
    Spinner ls_layer, rs_layer;
    Button next, add_more;
    String id;
    Double lastLSOffset, lastRSOffset, lastChainage;
    ImageView backBtn, syncBtn;
    String[] layers = { "CANAL BED CENTER", "CANAL BED-1", "CANAL BED-2", "CANAL BED EDGE", "SECTION SLOPE",  "BERM START", "BERM END", "IN SIDE SLOPE", "DOWEL TOP START", "DOWEL TOP END", "LEFT BANK TOP START", "LEFT BANK TOP END", "OUTER SLOPE", "NSL-1", "NSL-2"};
    EditText chainage;
    ProgressDialog pd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_level);

        prefs = getSharedPreferences(Variables.MY_PREFS_NAME, MODE_PRIVATE);
        id = prefs.getString(Variables.SESSION_ID, "");
        db = new DatabaseHandler(this);
        db.createBookLevelTable();

        ls_layer = (Spinner) findViewById(R.id.ls_layer);
        rs_layer = (Spinner) findViewById(R.id.rs_layer);
        next = (Button) findViewById(R.id.next);
        add_more = (Button) findViewById(R.id.add_more);
        backBtn = (ImageView) findViewById(R.id.backBtn);
        syncBtn = (ImageView) findViewById(R.id.syncBtn);
        final EditText bs_reading = (EditText) findViewById(R.id.bs_reading);
        final EditText fs_reading = (EditText) findViewById(R.id.fs_reading);
        final EditText remarks = (EditText) findViewById(R.id.remarks);
        chainage = (EditText) findViewById(R.id.chainage);
        final EditText ls_offset = (EditText) findViewById(R.id.ls_offset);
        final EditText ls_is_reading = (EditText) findViewById(R.id.ls_is_reading);
        final EditText ls_reduce_level = (EditText) findViewById(R.id.ls_reduce_level);
        final EditText rs_offset = (EditText) findViewById(R.id.rs_offset);
        final EditText rs_is_reading = (EditText) findViewById(R.id.rs_is_reading);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        //Creating the ArrayAdapter instance having the country list
        ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item, layers);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        ls_layer.setAdapter(aa);
        rs_layer.setAdapter(aa);

        final DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        synWithServer();
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        break;
                }
            }
        };

        syncBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isOnline()) {
                    JSONArray all = db.getAllBookLevel(id);
                    Log.e("allSync", String.valueOf(all));
                    if( all.length() > 0 ){
                        AlertDialog.Builder builder = new AlertDialog.Builder(BookLevel.this);
                        builder.setMessage("Are you sure? If yes, all data delete from here. Data will store in Server.").setPositiveButton("Yes", dialogClickListener)
                                .setNegativeButton("No", dialogClickListener).show();
                    }else {
                        Toast.makeText(BookLevel.this, "No Data Found", Toast.LENGTH_LONG).show();
                    }
                }else {
                    Toast.makeText(BookLevel.this, "No Internet Connection", Toast.LENGTH_LONG).show();
                }
            }
        });

        add_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chainage.setText("0.00");
                lastLSOffset = 0.00;
                lastRSOffset = 0.00;
            }
        });

        initiateValues();
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String get_bs_reading = bs_reading.getText().toString();
                String get_fs_reading = fs_reading.getText().toString();
                String get_remarks = remarks.getText().toString();
                String get_chainage = chainage.getText().toString();
                String get_ls_offset = ls_offset.getText().toString();
                String get_ls_layer = ls_layer.getSelectedItem().toString();
                String get_ls_is_reading = ls_is_reading.getText().toString();
                String get_ls_reduce_level = ls_reduce_level.getText().toString();
                String get_rs_offset = rs_offset.getText().toString();
                String get_rs_layer = rs_layer.getSelectedItem().toString();
                String get_rs_is_reading = rs_is_reading.getText().toString();

                if( get_bs_reading.equalsIgnoreCase("")){
                    bs_reading.requestFocus();
                    bs_reading.setError(Html.fromHtml("<font color='#FFFFFF'>Please Enter Valid value</font>"));
                }else if( get_chainage.equalsIgnoreCase("")){
                    chainage.requestFocus();
                    chainage.setError(Html.fromHtml("<font color='#FFFFFF'>Please Enter Valid value</font>"));
                }else {
                    Log.e("get_chainage", get_chainage);
                    Log.e("lastChainage", String.valueOf(lastChainage));

                    if ((Double.valueOf(get_chainage) + 0.0) == lastChainage){
                        lastLSOffset = Double.valueOf(lastLSOffset) + Double.valueOf(get_ls_offset);
                        lastRSOffset = Double.valueOf(lastRSOffset )+ Double.valueOf(get_rs_offset);
                        Log.e("enter", "if");
                    }else {
                        Log.e("enter", "else");
                        lastLSOffset = 0.00;
                        lastRSOffset = 0.00;
                        lastLSOffset = Double.valueOf(lastLSOffset) + Double.valueOf(get_ls_offset);
                        lastRSOffset = Double.valueOf(lastRSOffset) + Double.valueOf(get_rs_offset);
                    }

                    long val = db.addBookLevel(id, get_bs_reading, get_fs_reading,  get_remarks, get_ls_offset, String.valueOf(lastLSOffset),get_ls_layer,
                            get_ls_is_reading, get_ls_reduce_level, get_chainage, get_rs_offset, String.valueOf(lastRSOffset), get_rs_layer, get_rs_is_reading);
                    if(val > 0){
                        bs_reading.setText("");
                        fs_reading.setText("");
                        remarks.setText("");
                        ls_offset.setText("");
                        ls_is_reading.setText("");
                        ls_reduce_level.setText("");
                        rs_offset.setText("");
                        rs_is_reading.setText("");
                        Toast.makeText(BookLevel.this, "Added successfully!",
                                Toast.LENGTH_LONG).show();
                        initiateValues();
                    }else{
                        Toast.makeText(BookLevel.this, "Something went wrong!",
                                Toast.LENGTH_LONG).show();
                    }
                    Log.e("val", String.valueOf(val));
                }

            }
        });
    }
    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
    public void synWithServer(){
        final JSONArray allBooksLevel = db.getAllBookLevel(id);
        Log.d(String.valueOf(allBooksLevel), "synWithServer: ");


        Log.e("Input", String.valueOf(allBooksLevel));
        RequestQueue queue = Volley.newRequestQueue(BookLevel.this);

        JSONObject param = new JSONObject();

        try {
            param.put("data", allBooksLevel);
            pd = new ProgressDialog(BookLevel.this);
            pd.setMessage("Storing Data to server...");
            pd.setCancelable(false);
            pd.show();
        } catch (JSONException ex) {

        }

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST, Variables.API_URL + Variables.BOOKLEVEL, param,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObj) {
                        pd.dismiss();

                        if (jsonObj != null) {

                            try {
                                String code = jsonObj.getString("code");

                                if (code.equalsIgnoreCase("200")) {
//                                    for( int i = 0; i < allBooksLevel.length(); i++ ){
                                        db.deleteBooklevelById( id );
//                                    }
                                    Toast.makeText(BookLevel.this, "Updated to server successfully!", Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(BookLevel.this, MainActivity.class);
                                    startActivity(intent);
                                    pd.dismiss();
                                }
                                else
                                {
                                    Toast.makeText(BookLevel.this, "somthing", Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                Toast.makeText(BookLevel.this, "No Data Found", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(BookLevel.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                });
        queue.add(request);
    }

    public void initiateValues(){
        JSONArray allvalue = db.getAllBookLevel(id);
        Log.e("allvalue", String.valueOf(allvalue));
        JSONObject lastVal = null;
        try {
            lastVal = allvalue.getJSONObject(allvalue.length() - 1);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (allvalue.length() > 0 ){
            Log.e("enter", "if");
            try {
                chainage.setText(lastVal.getString("ch"));
                lastLSOffset = Double.valueOf(lastVal.getString("ls_offset"));
                lastRSOffset = Double.valueOf(lastVal.getString("rs_offset"));
                lastChainage = Double.valueOf(lastVal.getString("ch"));

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else {
            Log.e("enter", "else");
            chainage.setText("0.00");
            lastLSOffset = 0.00;
            lastRSOffset = 0.00;
            lastChainage = 0.00;
        }
        Log.e("lastVal", String.valueOf(lastVal));
    }
}
