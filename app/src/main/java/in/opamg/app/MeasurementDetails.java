package in.opamg.app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import in.opamg.app.DatabaseConnection.DatabaseHandler;

public class MeasurementDetails extends AppCompatActivity {
    String measurementId;
    String getMappingch;
    JSONArray fullData = new JSONArray();
    JSONObject all = null;
    TextView date, project_name, empId, lattitude, longitude, northing;
    DatabaseHandler db;
    ImageView backBtn, addManual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_measurement_details);
        db = new DatabaseHandler(this);

        backBtn = (ImageView) findViewById(R.id.backBtn);
        addManual = (ImageView) findViewById(R.id.addManual);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                MeasurementDetails.this.finish();
            }
        });



        measurementId = getIntent().getStringExtra("measurementId");
        all = db.getAllMeasurementByProjectId(measurementId);
        Log.e("all", String.valueOf(all));
        date = (TextView) findViewById(R.id.date);
        project_name = (TextView) findViewById(R.id.project_name);
        empId = (TextView) findViewById(R.id.empId);
        lattitude = (TextView) findViewById(R.id.lattitude);
        longitude = (TextView) findViewById(R.id.longitude);
        northing = (TextView) findViewById(R.id.northing);
        TextView easting = (TextView) findViewById(R.id.easting);
        TextView el = (TextView) findViewById(R.id.el);
        TextView zone = (TextView) findViewById(R.id.zone);
        TextView markPoint = (TextView) findViewById(R.id.markPoint);
        TextView mapping_ch = (TextView) findViewById(R.id.mapping_ch);
        TextView x_section_offset = (TextView) findViewById(R.id.x_section_offset);
        TextView back_site = (TextView) findViewById(R.id.back_site);
        TextView intermediate_site = (TextView) findViewById(R.id.intermediate_site);
        TextView forward_site = (TextView) findViewById(R.id.forward_site);
        TextView checked_reduce_level = (TextView) findViewById(R.id.checked_reduce_level);
        TextView layer_code = (TextView) findViewById(R.id.layer_code);
        TextView category = (TextView) findViewById(R.id.category);
        TextView description = (TextView) findViewById(R.id.description);
        TextView remarks = (TextView) findViewById(R.id.remarks);
        TextView dup_lattitude = (TextView) findViewById(R.id.dup_lattitude);
        TextView dup_longitude = (TextView) findViewById(R.id.dup_longitude);
        TextView dup_zone = (TextView) findViewById(R.id.dup_zone);
        TextView lat_degree = (TextView) findViewById(R.id.lat_degree);
        TextView lat_minites = (TextView) findViewById(R.id.lat_minites);
        TextView lat_sec = (TextView) findViewById(R.id.lat_sec);
        TextView lng_degree = (TextView) findViewById(R.id.lng_degree);
        TextView lng_minites = (TextView) findViewById(R.id.lng_minites);
        TextView lng_sec = (TextView) findViewById(R.id.lng_sec);
        TextView dup_northing = (TextView) findViewById(R.id.dup_northing);
        TextView dup_easting = (TextView) findViewById(R.id.dup_easting);
        TextView dup_el = (TextView) findViewById(R.id.dup_el);
        TextView dup_layer_code = (TextView) findViewById(R.id.dup_layer_code);

        try {
            JSONArray projectDetails = all.getJSONArray("projectDetails");
            JSONObject projectObj = projectDetails.getJSONObject(0);
            JSONArray staffReadingsDetails = all.getJSONArray("staff_readings");
            JSONObject staffObj = staffReadingsDetails.getJSONObject(0);
            JSONArray layerDetails = all.getJSONArray("layerDetails");
            JSONObject layerObj = layerDetails.getJSONObject(0);
            JSONArray coordinatesDetails = all.getJSONArray("coordinates");
            JSONObject latCoorObj = coordinatesDetails.getJSONObject(0);
            JSONObject lngCoorObj = coordinatesDetails.getJSONObject(1);

            String getDate = all.getString("created_date");
            String getProjectname = projectObj.getString("project_name");
            String getEmpId = projectObj.getString("user_id");
            String getLattitude = all.getString("lattitude");
            String getLongitude = all.getString("longitude");
            String getNorthing = all.getString("utm_northing");
            String getEasting = all.getString("utm_easting");
            String getEL = all.getString("el");
            String getZone = all.getString("utm_zone");
            getMappingch = all.getString("mapping_ch");
            String getXSection = all.getString("x_section_offset");
            String getBS = staffObj.getString("back_site");
            String getIS = staffObj.getString("intermediate_site");
            String getFS = staffObj.getString("forward_site");
            String getReducedLevel = all.getString("checked_reduce_level");
            String getLayercode = all.getString("layer_code");
            String getCategory = layerObj.getString("category");
            String getDescription = layerObj.getString("description");
            String getRemarks = all.getString("remarks");
            String getLatD = latCoorObj.getString("deg");
            String getLatM = latCoorObj.getString("min");
            String getLatS = latCoorObj.getString("sec");
            String getLngD = lngCoorObj.getString("deg");
            String getLngM = lngCoorObj.getString("min");
            String getLngS = lngCoorObj.getString("sec");

            date.setText(getDate);
            project_name.setText(getProjectname);
            empId.setText(getEmpId);
            lattitude.setText(getLattitude);
            longitude.setText(getLongitude);
            northing.setText(getNorthing);
            easting.setText(getEasting);
            el.setText(getEL);
            zone.setText(getZone);
            markPoint.setText("1");
            mapping_ch.setText(getMappingch);
            x_section_offset.setText(getXSection);
            back_site.setText(getBS);
            intermediate_site.setText(getIS);
            forward_site.setText(getFS);
            checked_reduce_level.setText(getReducedLevel);
            layer_code.setText(getLayercode);
            category.setText(getCategory);
            description.setText(getDescription);
            remarks.setText(getRemarks);

            lat_minites.setText(getLatD);
            lat_degree.setText(getLatM);
            lat_sec.setText(getLatS);
            lng_minites.setText(getLngD);
            lng_degree.setText(getLngM);
            lng_sec.setText(getLngS);

            dup_lattitude.setText(getLattitude);
            dup_longitude.setText(getLongitude);
            dup_zone.setText(getZone);
            dup_northing.setText(getNorthing);
            dup_easting.setText(getEasting);
            dup_el.setText(getEL);
            dup_layer_code.setText(getLayercode);

        } catch (JSONException e) {
            Log.e("err", String.valueOf(e));
            e.printStackTrace();
        }

        addManual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MeasurementDetails.this, AddManualData.class);
                i.putExtra("mappingCh", getMappingch );
                i.putExtra("measurementId", measurementId );
                startActivity(i);
            }
        });
    }
}
