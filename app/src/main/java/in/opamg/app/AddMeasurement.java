package in.opamg.app;

import android.location.Location;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import in.opamg.app.DatabaseConnection.DatabaseHandler;
import in.opamg.app.Models.Equipments;
import in.opamg.app.Models.Layers;
import in.opamg.app.R;

import static android.R.id.list;

public class AddMeasurement extends AppCompatActivity {
    private int Zone;
    private char Letter;
    private double Easting, Northing;
    Double Latitude;
    Double Longitude;
    EditText latDms, longDMS, northing, easting, zone, mapping_ch, measurement_ch, gps_offset_length, n_offset, e_offset;
    EditText l_section_offset, x_section_offset, search_layers, equipmentId, last_calibration_service_center, expiry_date, least_count;
    EditText owner, dgps, el, height_of_instrument, adj_rl, tbm_rl, bs_angle, fs_angle;
    Spinner equipment;
    Button save;
    RadioGroup radioGroup;
    RadioButton radioButton;
    LinearLayout thirdCount;
    DatabaseHandler db;
    AutoCompleteTextView autoComplete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_measurement);
        db = new DatabaseHandler(this);

        Latitude = Double.valueOf(getIntent().getStringExtra("Latitude"));
        Longitude = Double.valueOf(getIntent().getStringExtra("Longitude"));

        latDms      = (EditText) findViewById(R.id.latDMS);
        longDMS     = (EditText) findViewById(R.id.longDMS);
        northing    = (EditText) findViewById(R.id.northing);
        northing    = (EditText) findViewById(R.id.northing);
        easting     = (EditText) findViewById(R.id.easting);
        zone        = (EditText) findViewById(R.id.zone);
        mapping_ch        = (EditText) findViewById(R.id.mapping_ch);
        measurement_ch        = (EditText) findViewById(R.id.measurement_ch);
        gps_offset_length        = (EditText) findViewById(R.id.gps_offset_length);
        n_offset        = (EditText) findViewById(R.id.n_offset);
        e_offset        = (EditText) findViewById(R.id.e_offset);
        l_section_offset        = (EditText) findViewById(R.id.l_section_offset);
        x_section_offset        = (EditText) findViewById(R.id.x_section_offset);
        search_layers        = (EditText) findViewById(R.id.search_layers);
        last_calibration_service_center        = (EditText) findViewById(R.id.last_calibration_service_center);
        expiry_date        = (EditText) findViewById(R.id.expiry_date);
        least_count        = (EditText) findViewById(R.id.least_count);
        owner        = (EditText) findViewById(R.id.owner);
        dgps        = (EditText) findViewById(R.id.dgps);
        el        = (EditText) findViewById(R.id.el);
        height_of_instrument        = (EditText) findViewById(R.id.height_of_instrument);
        adj_rl        = (EditText) findViewById(R.id.adj_rl);
        tbm_rl        = (EditText) findViewById(R.id.tbm_rl);
        bs_angle        = (EditText) findViewById(R.id.bs_angle);
        fs_angle        = (EditText) findViewById(R.id.fs_angle);
        equipmentId        = (EditText) findViewById(R.id.equipmentId);

        equipment = (Spinner) findViewById(R.id.equipment);
        save = (Button) findViewById(R.id.save);
        thirdCount = (LinearLayout) findViewById(R.id.thirdCount);
        radioGroup = (RadioGroup) findViewById(R.id.siteCount);
        autoComplete=(AutoCompleteTextView)findViewById(R.id.search_layers);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                // get selected radio button from radioGroup
                int selectedId = radioGroup.getCheckedRadioButtonId();
                // find the radiobutton by returned id
                radioButton = (RadioButton) findViewById(selectedId);
                String type = (String) radioButton.getText();
                Toast.makeText(AddMeasurement.this,radioButton.getText(), Toast.LENGTH_SHORT).show();

                if( type.equalsIgnoreCase("3")){
                    thirdCount.setVisibility(View.VISIBLE);
                }else{
                    thirdCount.setVisibility(View.GONE);
                }
            }
        });

        equipment.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String modelNumber = equipment.getSelectedItem().toString();
                //db.getSingleEquipments(modelNumber);
                List<Equipments> layerSingle = db.getSingleEquipments(modelNumber);
                Log.e("Model", modelNumber);
                for (Equipments cn : layerSingle) {
                    Log.e("Model", ""+cn.get_id());
                    equipmentId.setText(cn.get_id());
                    last_calibration_service_center.setText(cn.get_last_calibration_service_center());
                    expiry_date.setText(cn.get_expiry_date());
                    least_count.setText(cn.get_least_count());
                    owner.setText(cn.get_owner());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }


        });
        convertToDegree(Latitude, Longitude);
        zoneNorthingEasting(Latitude, Longitude);

        try {
            getOfflineEquipments();
            getOfflineLayers();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void zoneNorthingEasting(double Lat, double Lon){
        Zone= (int) Math.floor(Lon/6+31);

        Log.e("Zone", String.valueOf(Zone));
        zone.setText(String.valueOf(Zone));
        if (Lat<-72)
            Letter='C';
        else if (Lat<-64)
            Letter='D';
        else if (Lat<-56)
            Letter='E';
        else if (Lat<-48)
            Letter='F';
        else if (Lat<-40)
            Letter='G';
        else if (Lat<-32)
            Letter='H';
        else if (Lat<-24)
            Letter='J';
        else if (Lat<-16)
            Letter='K';
        else if (Lat<-8)
            Letter='L';
        else if (Lat<0)
            Letter='M';
        else if (Lat<8)
            Letter='N';
        else if (Lat<16)
            Letter='P';
        else if (Lat<24)
            Letter='Q';
        else if (Lat<32)
            Letter='R';
        else if (Lat<40)
            Letter='S';
        else if (Lat<48)
            Letter='T';
        else if (Lat<56)
            Letter='U';
        else if (Lat<64)
            Letter='V';
        else if (Lat<72)
            Letter='W';
        else
            Letter='X';
        Easting=0.5*Math.log((1+Math.cos(Lat*Math.PI/180)*Math.sin(Lon*Math.PI/180-(6*Zone-183)*Math.PI/180))/(1-Math.cos(Lat*Math.PI/180)*Math.sin(Lon*Math.PI/180-(6*Zone-183)*Math.PI/180)))*0.9996*6399593.62/Math.pow((1+Math.pow(0.0820944379, 2)*Math.pow(Math.cos(Lat*Math.PI/180), 2)), 0.5)*(1+ Math.pow(0.0820944379,2)/2*Math.pow((0.5*Math.log((1+Math.cos(Lat*Math.PI/180)*Math.sin(Lon*Math.PI/180-(6*Zone-183)*Math.PI/180))/(1-Math.cos(Lat*Math.PI/180)*Math.sin(Lon*Math.PI/180-(6*Zone-183)*Math.PI/180)))),2)*Math.pow(Math.cos(Lat*Math.PI/180),2)/3)+500000;
        Easting=Math.round(Easting*100)*0.01;


        Log.e("Easting", String.valueOf(Easting));
        easting.setText(String.valueOf(Easting));

        Northing = (Math.atan(Math.tan(Lat*Math.PI/180)/Math.cos((Lon*Math.PI/180-(6*Zone -183)*Math.PI/180)))-Lat*Math.PI/180)*0.9996*6399593.625/Math.sqrt(1+0.006739496742*Math.pow(Math.cos(Lat*Math.PI/180),2))*(1+0.006739496742/2*Math.pow(0.5*Math.log((1+Math.cos(Lat*Math.PI/180)*Math.sin((Lon*Math.PI/180-(6*Zone -183)*Math.PI/180)))/(1-Math.cos(Lat*Math.PI/180)*Math.sin((Lon*Math.PI/180-(6*Zone -183)*Math.PI/180)))),2)*Math.pow(Math.cos(Lat*Math.PI/180),2))+0.9996*6399593.625*(Lat*Math.PI/180-0.005054622556*(Lat*Math.PI/180+Math.sin(2*Lat*Math.PI/180)/2)+4.258201531e-05*(3*(Lat*Math.PI/180+Math.sin(2*Lat*Math.PI/180)/2)+Math.sin(2*Lat*Math.PI/180)*Math.pow(Math.cos(Lat*Math.PI/180),2))/4-1.674057895e-07*(5*(3*(Lat*Math.PI/180+Math.sin(2*Lat*Math.PI/180)/2)+Math.sin(2*Lat*Math.PI/180)*Math.pow(Math.cos(Lat*Math.PI/180),2))/4+Math.sin(2*Lat*Math.PI/180)*Math.pow(Math.cos(Lat*Math.PI/180),2)*Math.pow(Math.cos(Lat*Math.PI/180),2))/3);
        if (Letter<'M')
            Northing = Northing + 10000000;


        Northing=Math.round(Northing*100)*0.01;
        Log.e("Northing", String.valueOf(Northing));
        northing.setText(String.valueOf(Northing));

    }

    public void convertToDegree(double latitudee, double longitudee){
        StringBuilder builder = new StringBuilder();

        if (latitudee < 0) {
            builder.append("S ");
        } else {
            builder.append("N ");
        }

        String latitudeDegrees = Location.convert(Math.abs(latitudee), Location.FORMAT_SECONDS);
        String[] latitudeSplit = latitudeDegrees.split(":");

        latDms.setText( latitudeSplit[0] +","+ latitudeSplit[1] +","+ latitudeSplit[2] );

        builder.append(latitudeSplit[0]);
        builder.append("°");
        builder.append(latitudeSplit[1]);
        builder.append("'");
        builder.append(latitudeSplit[2]);
        builder.append("\"");

        builder.append(" ");

        if (longitudee < 0) {
            builder.append("W ");
        } else {
            builder.append("E ");
        }

        String longitudeDegrees = Location.convert(Math.abs(longitudee), Location.FORMAT_SECONDS);
        String[] longitudeSplit = longitudeDegrees.split(":");

        longDMS.setText( longitudeSplit[0] +","+ longitudeSplit[1] +","+ longitudeSplit[2] );
        builder.append(longitudeSplit[0]);
        builder.append("°");
        builder.append(longitudeSplit[1]);
        builder.append("'");
        builder.append(longitudeSplit[2]);
        builder.append("\"");

        Log.e("eee",builder.toString());
    }

    public void getOfflineLayers() throws JSONException {

        List<Layers> lay = db.getAllLayers();
        String[] languages = new String[100];
        languages[0] = "text";
        JSONArray equiArray = new JSONArray();
        int count = 0;
        for (Layers cn : lay) {
            Log.e("eee", cn.get_code());
            languages[count] = cn.get_code();
            count++;
        }
        ArrayAdapter adapter = new
                ArrayAdapter(this,android.R.layout.simple_list_item_1,languages);

        autoComplete.setAdapter(adapter);
        autoComplete.setThreshold(1);
    }

    public void getOfflineEquipments() throws JSONException {

        List<Equipments> eqi = db.getAllEquipments();
        List<String> list = new ArrayList<String>();

        for (Equipments cn : eqi) {
            list.add(cn.get_model_number());
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        equipment.setAdapter(dataAdapter);
    }
}
