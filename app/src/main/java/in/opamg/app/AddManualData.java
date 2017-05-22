package in.opamg.app;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import global.CookieGetSet;
import global.Variables;
import in.opamg.app.DatabaseConnection.DatabaseHandler;
import in.opamg.app.Models.Layers;

public class AddManualData extends AppCompatActivity {
    DatabaseHandler db;
    Button add, done;
    EditText chainage, offset, is_reading, remarks;
    AutoCompleteTextView search_layers;
    Double getChainage, getOffset, getISReadings;
    String getStrChainage, getStrOffset, getStrISReadings, getRemarks, getLayer, projectId;
    ListView lstText;
    ArrayList<CookieGetSet> cookieList = new ArrayList<CookieGetSet>();
    CookieListing AdapterCookieList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_manual_data);
        db = new DatabaseHandler(this);
        projectId = Variables.PROJECT_ID;

        add = (Button) findViewById(R.id.add);
        done = (Button) findViewById(R.id.done);

        chainage = (EditText) findViewById(R.id.chainage);
        offset = (EditText) findViewById(R.id.offset);
        is_reading = (EditText) findViewById(R.id.is_reading);
        remarks = (EditText) findViewById(R.id.remarks);
        search_layers = (AutoCompleteTextView) findViewById(R.id.search_layers);
        lstText = (ListView) findViewById(R.id.lstText);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getStrChainage = chainage.getText().toString();
                getStrOffset = offset.getText().toString();
                getStrISReadings = is_reading.getText().toString();
                getRemarks = remarks.getText().toString();
                getLayer = search_layers.getText().toString();

                if(getStrChainage.equalsIgnoreCase("")){
                    chainage.requestFocus();
                    chainage.setError(Html.fromHtml("<font color='#FFFFFF'>Enter Valid data</font>"));
                }else if(getStrOffset.equalsIgnoreCase("")){
                    offset.requestFocus();
                    offset.setError(Html.fromHtml("<font color='#FFFFFF'>Enter Valid data</font>"));
                }else if(getStrISReadings.equalsIgnoreCase("")){
                    is_reading.requestFocus();
                    is_reading.setError(Html.fromHtml("<font color='#FFFFFF'>Enter Valid data</font>"));
                }else if(getLayer.equalsIgnoreCase("")){
                    search_layers.requestFocus();
                    search_layers.setError(Html.fromHtml("<font color='#FFFFFF'>Enter Valid data</font>"));
                }else {
                    String[] LayerArray = getLayer.split("--");
                    long id = db.addCookie(projectId, getStrChainage, getStrOffset, getStrISReadings, getRemarks, LayerArray[0]);
                    if (id > 0){
                        chainage.setText("");
                        offset.setText("");
                        is_reading.setText("");
                        remarks.setText("");
                        search_layers.setText("");
                        Toast.makeText(AddManualData.this, "Added Successfully!", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(AddManualData.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        try {
            getOfflineLayers();
            //db.dropTable("cookie");
            getCookiedata();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getCookiedata() {
        db.createCookieTable();
        JSONArray allCookies = db.getAllCookie( projectId );
        Log.e("allCookies", String.valueOf(allCookies));

        Gson gson = new Gson();
        JSONArray json = allCookies;
        cookieList = gson.fromJson(String.valueOf(allCookies), new TypeToken<List<CookieGetSet>>(){}.getType());
        AdapterCookieList = new CookieListing(cookieList);
        lstText.setAdapter(AdapterCookieList);
    }


    public void getOfflineLayers() throws JSONException {

        List<Layers> lay = db.getAllLayers();
        String[] languages = new String[100];
        JSONArray equiArray = new JSONArray();
        int count = 0;
        for (Layers cn : lay) {
            languages[count] = cn.get_code();
            count++;
            if(languages.length == count){
                layerSetAdapter(languages);
            }
        }
    }

    private void layerSetAdapter(String[] languages) {
        ArrayAdapter adapter = new
                ArrayAdapter(this,android.R.layout.simple_list_item_1,languages);

        search_layers.setAdapter(adapter);
        search_layers.setThreshold(1);
    }

    public class CookieListing extends BaseAdapter {
        ArrayList<CookieGetSet> projecctList = new ArrayList<CookieGetSet>();
        @Override
        public int getCount() {
            return cookieList.size();
            //return 10;
        }
        public CookieListing(ArrayList<CookieGetSet> taskList) {
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

                LayoutInflater inflater = (LayoutInflater) AddManualData.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                inflater = LayoutInflater.from(AddManualData.this);
                convertView = inflater.inflate(R.layout.cookie_list, null);
                holder = new ViewHolder();

                holder.listISReadings      = (TextView) convertView.findViewById(R.id.listISReadings);
                holder.listChainage      = (TextView) convertView.findViewById(R.id.listChainage);
                holder.listOffset      = (TextView) convertView.findViewById(R.id.listOffset);
                holder.listLayer      = (TextView) convertView.findViewById(R.id.listLayer);
                holder.delete      = (ImageView) convertView.findViewById(R.id.delete);


                convertView.setTag(holder);
            } else {

                holder = (ViewHolder) convertView.getTag();
            }
            final CookieGetSet list = this.projecctList.get(position);


            holder.listISReadings.setText(list.getIs_reading());
            holder.listChainage.setText(list.getChainage());
            holder.listOffset.setText(list.getOffset());
            holder.listLayer.setText(list.getLayer());

            holder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    getCookiedata();
                    Log.e("id", list.getId());
                }
            });
            return convertView;
        }

        public class ViewHolder {
            TextView listISReadings, listChainage, listOffset, listLayer;
            ImageView delete;
        }
    }
}
