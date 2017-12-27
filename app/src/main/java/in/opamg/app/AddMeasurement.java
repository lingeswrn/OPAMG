package in.opamg.app;

import android.content.Intent;
import android.location.Location;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import global.Variables;
import in.opamg.app.DatabaseConnection.DatabaseHandler;
import in.opamg.app.Models.Equipments;
import in.opamg.app.Models.Layers;
import in.opamg.app.Models.Measurement;


public class AddMeasurement extends AppCompatActivity {
    private int Zone;
    private char Letter;
    private double Easting, Northing;
    Double Latitude;
    Double Longitude;
    EditText latDms, longDMS, northing, easting, zone, mapping_ch, measurement_ch, gps_offset_length, n_offset, e_offset;
    EditText l_section_offset, x_section_offset, search_layers, equipmentId, last_calibration_service_center, expiry_date, least_count;
    EditText owner, dgps, el, height_of_instrument, adj_rl, tbm_rl, bs_angle, fs_angle, remarks;
    EditText back_site, autoComplete1, intermediate_site,forward_site, back_site_1, intermediate_site_1, forward_site_1, back_site_2, intermediate_site_2, forward_site_2;
    Spinner equipment;
    Button save, addManual;
    RadioGroup radioGroup;
    RadioButton radioButton;
    LinearLayout thirdCount;
    DatabaseHandler db;
    AutoCompleteTextView autoComplete;
    LinearLayout linTitle, linEqui, linAddManual;
    int ProjectId, getEquipmentId;
    String type;
    int previousDataLength;
    Double getMappingCh, getMeasurementCh, getGPSOffsetLength, getNOffset, getEOffset, getNorthing, getEasting;
    String getLayer, getBackSite, getIntermediateSite, getForwardSite, getBackSite1, getIntermediateSite1, getForwardSite1, getBackSite2, getIntermediateSite2, getForwardSite2;
    JSONArray previousData = new JSONArray();
    JSONObject previosDataJSON = new JSONObject();
    Double bsoffset, isoffset, fsoffset, bsOffsetMean, fsOffsetMean, isOffsetMean, bsOffsetSum, isOffsetSum, fsOffsetSum, risePlus, fallMinus;
    Double previous_ch_by_auto_level, previous_hight_of_instrument, previous_measurment_ch, chByAutoLevel, checkedReduceLevel, getTBM_RL, reduceLevel, heightOfInstrument, avgHeightOfInstrument, adjustmentError, angleRedians;
    Double previous_utm_easting, previous_utm_northing, cs_offset_easting, cs_offset_northing;
    String getLSection, getXSection, getLatDMS, getLongDMS, getZone, getEL, getRemarks, getADJRl, getBSAngle, getFSAngle;
    ImageView backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_measurement);
        db = new DatabaseHandler(this);

        Latitude = Double.valueOf(getIntent().getStringExtra("Latitude"));
        Longitude = Double.valueOf(getIntent().getStringExtra("Longitude"));
        ProjectId = Integer.parseInt(Variables.PROJECT_ID);

        linTitle = (LinearLayout) findViewById(R.id.linTitle);
        linEqui = (LinearLayout) findViewById(R.id.linEqui);
        linAddManual = (LinearLayout) findViewById(R.id.linAddManual);
        backBtn = (ImageView) findViewById(R.id.backBtn);


        latDms      = (EditText) findViewById(R.id.latDMS);
        longDMS     = (EditText) findViewById(R.id.longDMS);
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
        remarks        = (EditText) findViewById(R.id.remarks);
        equipmentId        = (EditText) findViewById(R.id.equipmentId);
        back_site        = (EditText) findViewById(R.id.back_site);
        intermediate_site        = (EditText) findViewById(R.id.intermediate_site);
        forward_site        = (EditText) findViewById(R.id.forward_site);
        back_site_1        = (EditText) findViewById(R.id.back_site_1);
        intermediate_site_1        = (EditText) findViewById(R.id.intermediate_site_1);
        forward_site_1        = (EditText) findViewById(R.id.forward_site_1);
        back_site_2        = (EditText) findViewById(R.id.back_site_2);
        intermediate_site_2        = (EditText) findViewById(R.id.intermediate_site_2);
        forward_site_2        = (EditText) findViewById(R.id.forward_site_2);

        equipment = (Spinner) findViewById(R.id.equipment);
        save = (Button) findViewById(R.id.save);
        addManual = (Button) findViewById(R.id.addManual);
        thirdCount = (LinearLayout) findViewById(R.id.thirdCount);
        radioGroup = (RadioGroup) findViewById(R.id.siteCount);
        autoComplete =(AutoCompleteTextView)findViewById(R.id.search_layers);

        int selectedId = radioGroup.getCheckedRadioButtonId();
        // find the radiobutton by returned id
        radioButton = (RadioButton) findViewById(selectedId);
        type = (String) radioButton.getText();

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                // get selected radio button from radioGroup
                int selectedId = radioGroup.getCheckedRadioButtonId();
                // find the radiobutton by returned id
                radioButton = (RadioButton) findViewById(selectedId);
                type = (String) radioButton.getText();
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
                for (Equipments cn : layerSingle) {
                    equipmentId.setText(cn.get_id());
                    last_calibration_service_center.setText(cn.get_last_calibration_service_center());
                    expiry_date.setText(cn.get_expiry_date());
                    least_count.setText(cn.get_least_count());
                    owner.setText(cn.get_owner());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        linTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linEqui.setVisibility(View.VISIBLE);
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLatDMS          = latDms.getText().toString();
                getLongDMS          = longDMS.getText().toString();
                getNorthing          = Double.valueOf(northing.getText().toString());
                getEasting          = Double.valueOf(easting.getText().toString());
                getEquipmentId       =  Integer.parseInt(equipmentId.getText().toString());
                getZone              =  zone.getText().toString();
                getMappingCh         = Double.valueOf(mapping_ch.getText().toString());
                getMeasurementCh     = Double.valueOf(measurement_ch.getText().toString());
                getGPSOffsetLength   = Double.valueOf(gps_offset_length.getText().toString());
                getNOffset           = Double.valueOf(n_offset.getText().toString());
                getEOffset           = Double.valueOf(e_offset.getText().toString());
                getLayer             = autoComplete.getText().toString();
                getBackSite          = back_site.getText().toString();
                getIntermediateSite  = intermediate_site.getText().toString();
                getForwardSite       = forward_site.getText().toString();
                getBackSite1          = back_site_1.getText().toString();
                getIntermediateSite1  = intermediate_site_1.getText().toString();
                getForwardSite1       = forward_site_1.getText().toString();
                getBackSite2          = back_site_2.getText().toString();
                getIntermediateSite2  = intermediate_site_2.getText().toString();
                getForwardSite2       = forward_site_2.getText().toString();
                getLSection            = l_section_offset.getText().toString();
                getXSection            = x_section_offset.getText().toString();
                getEL                  = el.getText().toString();
                getRemarks             = remarks.getText().toString();
                getADJRl             = adj_rl.getText().toString();
                getBSAngle           = bs_angle.getText().toString();
                getFSAngle           = fs_angle.getText().toString();

                cs_offset_easting = getEasting;
                cs_offset_northing = getNorthing;

                if (getMappingCh.equals("")){
                    mapping_ch.requestFocus();
                    mapping_ch.setError(Html.fromHtml("<font color='#FFFFFF'>Enter Valid data</font>"));
                }else if (getMeasurementCh.equals("")){
                    measurement_ch.requestFocus();
                    measurement_ch.setError(Html.fromHtml("<font color='#FFFFFF'>Enter Valid data</font>"));
                }else if (getGPSOffsetLength.equals("")){
                    gps_offset_length.requestFocus();
                    gps_offset_length.setError(Html.fromHtml("<font color='#FFFFFF'>Enter Valid data</font>"));
                }else if (getNOffset.equals("")){
                    n_offset.requestFocus();
                    n_offset.setError(Html.fromHtml("<font color='#FFFFFF'>Enter Valid data</font>"));
                }else if (getEOffset.equals("")){
                    e_offset.requestFocus();
                    e_offset.setError(Html.fromHtml("<font color='#FFFFFF'>Enter Valid data</font>"));
                }else if (getLSection.equals("")){
                    l_section_offset.requestFocus();
                    l_section_offset.setError(Html.fromHtml("<font color='#FFFFFF'>Enter Valid data</font>"));
                }else if (getXSection.equals("")){
                    x_section_offset.requestFocus();
                    x_section_offset.setError(Html.fromHtml("<font color='#FFFFFF'>Enter Valid data</font>"));
                }else if (getLayer.equals("")){
                    autoComplete.requestFocus();
                    autoComplete.setError(Html.fromHtml("<font color='#FFFFFF'>Enter Valid data</font>"));
                }else {
                    calCoreCal();
                }
            }
        });
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                AddMeasurement.this.finish();
            }
        });

        addManual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AddMeasurement.this, AddManualData.class);
                startActivity(i);
                AddMeasurement.this.finish();
            }
        });

        l_section_offset.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                getLSection            = l_section_offset.getText().toString();
                getXSection            = x_section_offset.getText().toString();
                try {
                    if( previousDataLength != 0 )
                        previous_measurment_ch = Double.valueOf(previosDataJSON.getString("measurment_ch"));
                    else
                        previous_measurment_ch = 0.000;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if ( getXSection.equalsIgnoreCase(""))
                    getXSection = "0.000";
                if ( getLSection.equalsIgnoreCase(""))
                    getLSection = "0.000";

                getMeasurementCh = calMeasurementCH(previous_measurment_ch, getXSection, getLSection);
                measurement_ch.setText(String.valueOf(getMeasurementCh));
            }
        });

        x_section_offset.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                getLSection            = l_section_offset.getText().toString();
                getXSection            = x_section_offset.getText().toString();
                try {
                    if( previousDataLength != 0 )
                        previous_measurment_ch = Double.valueOf(previosDataJSON.getString("measurment_ch"));
                    else
                        previous_measurment_ch = 0.000;
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if ( getXSection.equalsIgnoreCase(""))
                    getXSection = "0.000";
                if ( getLSection.equalsIgnoreCase(""))
                    getLSection = "0.000";
                getMeasurementCh = calMeasurementCH(previous_measurment_ch, getXSection, getLSection);
                measurement_ch.setText(String.valueOf(getMeasurementCh));
            }
        });
        convertToDegree(Latitude, Longitude);
        zoneNorthingEasting(Latitude, Longitude);
        //db.deleteTable("measurement");
        //db.createMeasurementTables();

        previousData = db.getMeasurementByProjectId( ProjectId );

        if( previousData.length() > 0){
            previousDataLength = previousData.length();
            getNorthing          = Double.valueOf(northing.getText().toString());
            getEasting          = Double.valueOf(easting.getText().toString());
            try {
                previosDataJSON = previousData.getJSONObject(0);
                String Pn_offset = calNOffset( getNorthing, previosDataJSON.getString("utm_northing"));
                String Pe_offset = calEOffset( getEasting, previosDataJSON.getString("utm_easting"));
                String Pgps_offset = calGPSOffsetLength( getNorthing, getEasting, previosDataJSON.getString("utm_northing"), previosDataJSON.getString("utm_easting"), Pn_offset, Pe_offset);
                String Pmapping_ch = calMappingCh( Pn_offset, Pgps_offset, previosDataJSON.getString("mapping_ch"));

                mapping_ch.setText(Pmapping_ch);
                measurement_ch.setText("0.000");
                gps_offset_length.setText(Pgps_offset);
                n_offset.setText(Pn_offset);
                e_offset.setText(Pe_offset);
                l_section_offset.setText(Pmapping_ch);
                //linAddManual.setVisibility(View.VISIBLE);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else {
            previousDataLength = 0;
            mapping_ch.setText("0.000");
            measurement_ch.setText("0.000");
            gps_offset_length.setText("0.000");
            n_offset.setText("0.000");
            e_offset.setText("0.000");
            l_section_offset.setText("0.000");
        }

        Log.e("Previous", String.valueOf(previosDataJSON));

        try {
            getOfflineEquipments();
            getOfflineLayers();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public String calMappingCh(String pn_offsets, String pgps_offsets, String mapping_chs) {
        Double returnValue = 0.000;
        returnValue = Double.parseDouble(pgps_offsets) + Double.parseDouble(mapping_chs);
        return String.valueOf(returnValue);
    }

    public String calGPSOffsetLength(Double Northings, Double Eastings, String utm_northings, String utm_eastings, String pn_offsets, String pe_offsets) {
        Double returnValue = 0.000;

        if( Northings != 0 || Eastings != 0 ){
            Double temp, tempPow, temp1, tempPow1, tempCombine, tempFinal;
            temp = Eastings - Double.parseDouble(utm_eastings);
            tempPow = Math.pow(temp, 2);
            temp1 = Northings - Double.parseDouble(utm_northings);
            tempPow1 = Math.pow(temp1, 2);

            tempCombine = tempPow + tempPow1;
            returnValue = Math.pow(tempCombine, 0.5);
        }

        return String.valueOf(returnValue);
    }

    public String calEOffset(Double getEastings, String utm_eastings) {
        Double returnValue = 0.000;

        if( getEastings > 0 ){
            returnValue = getEastings - Double.parseDouble(utm_eastings);
        }else {
            returnValue = 0.000;
        }
        return String.valueOf(returnValue);
    }

    public String calNOffset(Double getNorthings, String utm_northings) {
        Double returnValue = 0.000;

        if( getNorthings > 0 ){
            returnValue = getNorthings - Double.parseDouble(utm_northings);
        }else {
            returnValue = 0.000;
        }
        return String.valueOf(returnValue);

    }

    private void calCoreCal() {
        /*JSONArray backsite = new JSONArray();
        JSONArray intermediatesite = new JSONArray();
        JSONArray foresite = new JSONArray();*/
//        String[] backsite = new String[] {};
        ArrayList<String> backsite = new ArrayList<String>();
        ArrayList<String> intermediatesite = new ArrayList<String>();
        ArrayList<String> foresite = new ArrayList<String>();

        if (!getBackSite.equalsIgnoreCase(""))
            backsite.add(getBackSite);
        if (!getBackSite1.equalsIgnoreCase(""))
            backsite.add(getBackSite1);
        if (!getBackSite2.equalsIgnoreCase(""))
            backsite.add(getBackSite2);

        if (!getIntermediateSite.equalsIgnoreCase(""))
            intermediatesite.add(getIntermediateSite);
        if (!getIntermediateSite1.equalsIgnoreCase(""))
            intermediatesite.add(getIntermediateSite1);
        if (!getIntermediateSite2.equalsIgnoreCase(""))
            intermediatesite.add(getIntermediateSite2);

        if (!getForwardSite.equalsIgnoreCase(""))
            foresite.add(getForwardSite);
        if (!getForwardSite1.equalsIgnoreCase(""))
            foresite.add(getForwardSite1);
        if (!getForwardSite2.equalsIgnoreCase(""))
            foresite.add(getForwardSite2);

        bsoffset = calSiteOffset(backsite);
        isoffset = calSiteOffset(intermediatesite);
        fsoffset = calSiteOffset(foresite);

        JSONArray bsSumMean = calSiteOffsetSumMean(backsite);
        JSONArray isSumMean = calSiteOffsetSumMean(intermediatesite);
        JSONArray fsSumMean = calSiteOffsetSumMean(foresite);

        bsOffsetMean = 0.000;
        fsOffsetMean = 0.000;
        isOffsetMean = 0.000;
        try {
            bsOffsetSum = Double.valueOf(bsSumMean.getString(0));
            bsOffsetMean = Double.valueOf(bsSumMean.getString(1));

            isOffsetSum = Double.valueOf(isSumMean.getString(0));
            isOffsetMean = Double.valueOf(isSumMean.getString(1));

            fsOffsetSum = Double.valueOf(fsSumMean.getString(0));
            fsOffsetMean = Double.valueOf(fsSumMean.getString(1));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JSONArray bsRiseFall = calRiseFall(bsOffsetMean, fsOffsetMean);
        risePlus = 0.000;
        fallMinus = 0.000;
        try {
            risePlus = bsRiseFall.getDouble(0);
            fallMinus = bsRiseFall.getDouble(1);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        previous_ch_by_auto_level = 0.000;
        previous_hight_of_instrument = 0.000;
        previous_utm_easting = 0.000;
        previous_utm_northing = 0.000;
        try {
            if(previousDataLength > 0) {
                previous_ch_by_auto_level = Double.valueOf(previosDataJSON.getString("ch_by_auto_level"));
                previous_hight_of_instrument = Double.valueOf(previosDataJSON.getString("hight_of_instrument"));
                previous_utm_easting = Double.valueOf(previosDataJSON.getString("utm_easting"));
                previous_utm_northing = Double.valueOf(previosDataJSON.getString("utm_northing"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        chByAutoLevel = calChByAutoLevel(previous_ch_by_auto_level, bsoffset, fsoffset);
        checkedReduceLevel = calCheckedReduceLevel(previous_hight_of_instrument, isOffsetMean, fsOffsetMean);


        if( previousDataLength ==  0 ){
            reduceLevel = checkedReduceLevel;
        }else {
            reduceLevel = calReduceLevel(previous_hight_of_instrument, isOffsetMean, fsOffsetMean);
        }



        heightOfInstrument = calheightOfInstrument( checkedReduceLevel, bsOffsetMean, isOffsetMean);
        height_of_instrument.setText(String.valueOf(heightOfInstrument));
        avgHeightOfInstrument = heightOfInstrument - checkedReduceLevel;
        adjustmentError = calAdjustmentError( getTBM_RL, reduceLevel);

        angleRedians = calAngleRedians( getNorthing, getEasting, previous_utm_northing, previous_utm_easting);

        String layer [] = getLayer.split ("--");
        getLayer = layer[0];


        String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());

// textView is the TextView view that should display it
        Log.e("currentDateTimeString",currentDateTimeString);
        int lastId = db.addMeasurement(new Measurement(ProjectId, getEquipmentId, getLayer,
                String.valueOf(Latitude), String.valueOf(Longitude), getZone, String.valueOf(getEasting),
                String.valueOf(getNorthing), String.valueOf(angleRedians), String.valueOf(cs_offset_easting), String.valueOf(cs_offset_northing),
                getEL, String.valueOf(getMappingCh),String.valueOf(chByAutoLevel),String.valueOf(getMeasurementCh), String.valueOf(getGPSOffsetLength), String.valueOf(bsoffset), String.valueOf(isoffset), String.valueOf(fsoffset),
                String.valueOf(getNOffset), String.valueOf(getEOffset), getLSection, getXSection, String.valueOf(risePlus), String.valueOf(fallMinus), String.valueOf(avgHeightOfInstrument), String.valueOf(heightOfInstrument),
                String.valueOf(reduceLevel), String.valueOf(checkedReduceLevel), String.valueOf(getRemarks), String.valueOf(getADJRl), String.valueOf(adjustmentError), String.valueOf(getTBM_RL), String.valueOf(getBSAngle), String.valueOf(getFSAngle),
                "", "", "", "", "", "P", 1, currentDateTimeString));

        Log.e("id", String.valueOf(lastId));
        String[] latDMSArray = getLatDMS.split(",");
        int coordinate1 = db.addCoOrdinates(lastId, "lat", latDMSArray[0], latDMSArray[1], latDMSArray[2]);

        String[] longDMSArray = getLongDMS.split(",");
        int coordinate2 = db.addCoOrdinates(lastId, "lng", longDMSArray[0], longDMSArray[1], longDMSArray[2]);

        String concatBackSite = "", concatInterSite = "", concatForeSite = "";
        Object[] backsiteArray = backsite.toArray();
        Object[] intermediatesiteArray = intermediatesite.toArray();
        Object[] foresiteArray = foresite.toArray();
        if( backsiteArray.length > 1 ||  intermediatesiteArray.length > 1 ||  foresiteArray.length > 1 ){

            if(  backsiteArray.length > 1)
                concatBackSite = backsiteArray[0] + "," + backsiteArray[1] + "," + backsiteArray[2];
            else
                concatBackSite = ",,,";
            if( intermediatesiteArray.length > 1 )
                concatInterSite = intermediatesiteArray[0] + "," + intermediatesiteArray[1] + "," + intermediatesiteArray[2];
            else
                concatInterSite = ",,,";

            if( foresiteArray.length > 1)
                concatForeSite = foresiteArray[0] + "," + foresiteArray[1] + "," + foresiteArray[2];
            else
                concatForeSite = ",,,";

        }else {
            concatBackSite = "";
            concatInterSite = "";
            concatForeSite = "";

            if( backsiteArray.length > 0 ) {
                concatBackSite = (String) backsiteArray[0];
                concatInterSite = "";
                concatForeSite = "";
            }

            if( intermediatesiteArray.length > 0 ) {
                concatBackSite = "";
                concatInterSite = (String) intermediatesiteArray[0];
                concatForeSite = "";
            }

            if( foresiteArray.length > 0 ) {
                concatBackSite = "";
                concatInterSite = "";
                concatForeSite = (String) foresiteArray[0];
            }
        }
        int readingId = db.addStaffReadings(lastId, concatBackSite, concatInterSite, concatForeSite);

        if( lastId > 0 && coordinate1 > 0 && coordinate2 > 0 && readingId > 0){
            Toast.makeText(AddMeasurement.this, "Added Successfully!", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(AddMeasurement.this, MapActivity.class);
            startActivity(i);
            AddMeasurement.this.finish();
        }else {
            Toast.makeText(AddMeasurement.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
        }
    }

    private String concat(JSONArray dataInput){
        String returnValue = null;
        try {
            returnValue = dataInput.getString(0) + "," + dataInput.getString(1) + "," + dataInput.getString(2);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return returnValue;
    }

    private Double calAngleRedians(Double getNorthings, Double getEastings, Double previous_utm_northings, Double previous_utm_eastings) {
        Double returnValue = 0.000, temp, temp1, temp2;

        temp = getNorthings - previous_utm_northings;
        temp1 = getEastings - previous_utm_eastings;
        temp2 = temp / temp1;
        returnValue = Math.atan(temp2);

        return Double.parseDouble(String.format("%.3f", returnValue));
    }

    public Double calSiteOffset(ArrayList<String> backsite) {
        Double returnValue = 0.000;
        Object[] mStringArray = backsite.toArray();
        if(previousDataLength == 0){
            if(mStringArray.length > 1){
                if( Double.valueOf((String) mStringArray[0]) > 0 && Double.valueOf((String) mStringArray[2]) > 0){
                    returnValue = Double.parseDouble((String) mStringArray[0]) - Double.parseDouble((String) mStringArray[2]);
                    returnValue = returnValue * 100;
                }else {
                    returnValue = 0.000;
                }
            }else {
                if(mStringArray.length > 0) {
                    returnValue = Double.valueOf((String) mStringArray[0]);
                }else {
                    returnValue = 0.000;
                }
            }
        }else {
            if(mStringArray.length > 1){
                returnValue = Double.parseDouble((String) mStringArray[0]) - Double.parseDouble((String) mStringArray[2]);
                returnValue = returnValue * 100;
            }else {
                if(mStringArray.length > 0) {
                    returnValue = Double.valueOf((String) mStringArray[0]);
                }else {
                    returnValue = 0.000;
                }
            }
        }
        return Double.parseDouble(String.format("%.3f", returnValue));
    }

    public JSONArray calSiteOffsetSumMean(ArrayList<String> dataInput){
        JSONArray returnValue = new JSONArray();
        Double sum = 0.000;
        Double mean = 0.000;
        Object[] mStringArray = dataInput.toArray();
        if(mStringArray.length > 1){
            for (int i = 0; i < mStringArray.length; i++){
                sum = sum + Double.parseDouble((String) mStringArray[i]);
            }
            mean = sum / mStringArray.length;
        }else {
            if(mStringArray.length > 0){
                sum = Double.parseDouble((String) mStringArray[0]);
                mean = Double.parseDouble((String) mStringArray[0]);
            }else {
                sum = 0.000;
                mean = 0.000;
            }
        }
        try {
            returnValue.put(Double.parseDouble(String.format("%.3f", sum)));
            returnValue.put(Double.parseDouble(String.format("%.3f", mean)));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return returnValue;
    }

    public JSONArray calRiseFall(Double bsOffsetMean, Double fsOffsetMean) {
        JSONArray returnValue = new JSONArray();
        Double rise = 0.000;
        Double fall = 0.000;

        //CALCULATE RISE
        if( bsOffsetMean > fsOffsetMean ){
            Log.e("bs", String.valueOf(bsOffsetMean));
            Log.e("fs", String.valueOf(fsOffsetMean));
            rise = bsOffsetMean - fsOffsetMean;
        }else {
            rise = 0.000;
        }

        // CALCULATE FALL
        if ( bsOffsetMean < fsOffsetMean ){
            fall = fsOffsetMean - bsOffsetMean;
        }else {
            fall = 0.000;
        }

        returnValue.put(rise);
        returnValue.put(fall);

        return returnValue;
    }

    public Double calChByAutoLevel(Double ch_by_auto_level, Double bsoffset, Double fsoffset) {
        Double returnValue = 0.000;
        if( previousDataLength == 0 ){
            returnValue = 0.000;
        }else{
            returnValue = ch_by_auto_level + bsoffset + fsoffset;
        }
        return Double.parseDouble(String.format("%.3f", returnValue));
    }

    public Double calCheckedReduceLevel(Double hight_of_instrument, Double isOffsetMean, Double fsOffsetMean) {
        Double returnValue = 0.000, temp, returnFinal = 0.000;
        String tempTBL = tbm_rl.getText().toString();

        if( tempTBL.equals("") ){
            returnValue = 0.000;
            getTBM_RL = returnValue;
        }else {
            getTBM_RL = Double.valueOf(tbm_rl.getText().toString());
            returnValue = getTBM_RL;
        }

        if(previousDataLength == 0){
            returnFinal = returnValue;
        }else {
            if( returnValue > 0 ){
                returnFinal = returnValue;
            }else {
                temp = isOffsetMean + fsOffsetMean;
                returnFinal = hight_of_instrument - temp;
            }
        }

        return Double.parseDouble(String.format("%.3f", returnValue));
    }

    public Double calReduceLevel(Double hight_of_instrument, Double isOffsetMean, Double fsOffsetMean) {
        Double returnValue = 0.000, temp;
        temp = isOffsetMean + fsOffsetMean;
        returnValue = hight_of_instrument - temp;
        return Double.parseDouble(String.format("%.3f", returnValue));
    }

    public Double calheightOfInstrument(Double checkedReduceLevel, Double bsOffsetMean, Double isOffsetMean) {
        Double returnValue =  0.000;
        returnValue = checkedReduceLevel + bsOffsetMean + isOffsetMean;

        return Double.parseDouble(String.format("%.3f", returnValue));
    }

    public Double calAdjustmentError(Double getTBM_rl, Double reduceLevel) {
        Double returnValue = 0.000;

        if( getTBM_rl > 0 ){
            returnValue = reduceLevel - getTBM_rl;
        }else {
            returnValue = 0.000;
        }
        return Double.parseDouble(String.format("%.3f", returnValue));
    }

    public Double calMeasurementCH(Double previous_measurment_chs, String getXSections, String getLSections){
        Double returnValue = 0.000;
        Double temp = Double.valueOf(getXSections);
        Double temp1 = Double.valueOf(getLSections);

        if( previousDataLength != 0 ){
            if( temp > 0 )
                returnValue = previous_measurment_chs;
            else
                returnValue = previous_measurment_chs + temp1;
        }
        return Double.parseDouble(String.format("%.3f", returnValue));
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
