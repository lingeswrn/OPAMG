package in.opamg.app;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.DataPointInterface;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.OnDataPointTapListener;
import com.jjoe64.graphview.series.Series;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import global.CookieGetSet;
import global.Variables;
import global.UTM2LatLon;
import in.opamg.app.DatabaseConnection.DatabaseHandler;
import in.opamg.app.Models.Layers;
import in.opamg.app.Models.Measurement;

public class AddManualData extends AppCompatActivity {
    DatabaseHandler db;
    Button add, done;
    EditText chainage, offset, is_reading, remarks, search_layers;
//    AutoCompleteTextView search_layers;
    Double getChainage, getOffset, getISReadings, getTBM_RL;
    String getStrChainage, getStrOffset, getStrISReadings, getRemarks, getLayer, projectId, measurementId, mappingCh;
    ListView lstText;
    ArrayList<CookieGetSet> cookieList = new ArrayList<CookieGetSet>();
    CookieListing AdapterCookieList;
    JSONArray allCookies = new JSONArray();
    ImageView backBtn;
    int previousDataLength;
    String tempTBL = null;
    String LastIds = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_manual_data);
        db = new DatabaseHandler(this);
        projectId = Variables.PROJECT_ID;

        measurementId = getIntent().getStringExtra("measurementId");
        mappingCh = getIntent().getStringExtra("mappingCh");

        add = (Button) findViewById(R.id.add);
        done = (Button) findViewById(R.id.done);
        backBtn = (ImageView) findViewById(R.id.backBtn);

        chainage = (EditText) findViewById(R.id.chainage);
        offset = (EditText) findViewById(R.id.offset);
        is_reading = (EditText) findViewById(R.id.is_reading);
        remarks = (EditText) findViewById(R.id.remarks);
        search_layers = (EditText) findViewById(R.id.search_layers);
        lstText = (ListView) findViewById(R.id.lstText);
        final UTM2LatLon test = new UTM2LatLon();

        GraphView graph = (GraphView) findViewById(R.id.graph);
        LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(new DataPoint[] {
//                new DataPoint(0, 1),
//                new DataPoint(1, 5),
//                new DataPoint(2, 3),
//                new DataPoint(3, 2),
//                new DataPoint(4, 6),

                new DataPoint(0, 1),
                new DataPoint(1, 1),
                new DataPoint(2, 3),
                new DataPoint(3, 2),
                new DataPoint(4, -1),
                new DataPoint(5, -1),
                new DataPoint(6, -1),
                new DataPoint(7, 2),
                new DataPoint(8, 5),
                new DataPoint(9, 4),
                new DataPoint(10, 1)
        });


        series.setTitle("Random Curve 1");
        series.setColor(R.color.colorAccent);
        series.setDrawDataPoints(true);
//        series.setDataPointsRadius(10);
//        series.setThickness(8);

        series.setOnDataPointTapListener(new OnDataPointTapListener() {
            @Override
            public void onTap(Series series, DataPointInterface dataPoint) {
//                Toast.makeText(AddManualData.this, "Series1: On Data Point clicked: "+dataPoint, Toast.LENGTH_SHORT).show();
                String point = String.valueOf(dataPoint);
                String layer = null;
                Log.e("val", String.valueOf(dataPoint));
                if(point.equalsIgnoreCase("[1.0/1.0]")){
                    layer = "NGL";
                }else if(point.equalsIgnoreCase("[2.0/3.0]")){
                    layer = "SPOIL BANK TOP";
                }else if(point.equalsIgnoreCase("[3.0/2.0]")){
                    layer = "BERM";
                }else if(point.equalsIgnoreCase("[4.0/-1.0]")){
                    layer = "CB";
                }else if(point.equalsIgnoreCase("[5.0/-1.0]")){
                    layer = "CL";
                }else if(point.equalsIgnoreCase("[6.0/-1.0]")){
                    layer = "CB";
                }else if(point.equalsIgnoreCase("[7.0/2.0]")){
                    layer = "BERM";
                }else if(point.equalsIgnoreCase("[8.0/5.0]")){
                    layer = "DOWEL";
                }else if(point.equalsIgnoreCase("[9.0/4.0]")){
                    layer = "SERVICE ROAD";
                }else if(point.equalsIgnoreCase("[10.0/1.0]")){
                    layer = "NGL";
                }

                search_layers.setText(layer);
            }
        });
// set manual X bounds
        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMinX(0);
        graph.getViewport().setMaxX(10);

// set manual Y bounds
        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setMinY(-2);
        graph.getViewport().setMaxY(6);
        graph.getGridLabelRenderer().setGridStyle(GridLabelRenderer.GridStyle.NONE);

        GridLabelRenderer gridLabel = graph.getGridLabelRenderer();
        gridLabel.setHorizontalLabelsVisible(false);
        gridLabel.setVerticalLabelsVisible(false);
        graph.addSeries(series);

        chainage.setText(mappingCh);
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
                        //chainage.setText("");
                        offset.setText("");
                        is_reading.setText("");
                        remarks.setText("");
                        search_layers.setText("");
                        Toast.makeText(AddManualData.this, "Added Successfully!", Toast.LENGTH_SHORT).show();
                        getCookiedata();
                    }else{
                        Toast.makeText(AddManualData.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONArray allCookiee = db.getAllCookie(Variables.PROJECT_ID);
                if (allCookiee.length() > 0 ){
                    JSONArray nextPrev = db.getPrevNextData(measurementId, projectId);
                    JSONObject previosDataJSON = new JSONObject();
                    JSONObject nextDataJSON = new JSONObject();
                    previousDataLength = 1;
                    Log.e("nextPrev", String.valueOf(nextPrev));
                    try {
                        previosDataJSON = nextPrev.getJSONObject(0);
                        nextDataJSON = nextPrev.getJSONObject(1);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    Double getEasting = null;
                    Double getMappingCh = null;
                    Double getNorthing = null;
                    Double angleRedians = null;

                    try {
                        getEasting = Double.valueOf(nextDataJSON.getString("utm_easting"));
                        getMappingCh = Double.valueOf(nextDataJSON.getString("mapping_ch"));
                        getNorthing = Double.valueOf(nextDataJSON.getString("utm_northing"));
                        angleRedians = Double.valueOf(nextDataJSON.getString("utm_northing"));
                        tempTBL = nextDataJSON.getString("tbm_rl");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Double manualLastEasting, manualLastNorthing, temp ,temp1, temp2, temp3, first_half, temp4;
                    try {
                        temp = Double.parseDouble(allCookiee.getJSONObject( allCookiee.length() - 1 ).getString("chainage")) - Double.parseDouble(previosDataJSON.getString("mapping_ch"));
                        Log.e("getEasting", String.valueOf(getEasting));
                        Log.e("temp", String.valueOf(temp));

                        temp1 = getEasting * temp;
                        temp2 = Double.parseDouble(allCookiee.getJSONObject( allCookiee.length() - 1 ).getString("chainage")) * Double.parseDouble(previosDataJSON.getString("utm_easting"));
                        temp3 = getMappingCh - temp2;
                        first_half = temp1 + temp3;
                        temp4 = getMappingCh - Double.parseDouble(previosDataJSON.getString("mapping_ch"));
                        manualLastEasting = first_half / temp4;
                        //Log.e("manualLastEasting", String.valueOf(manualLastEasting));

                        temp = Double.parseDouble(allCookiee.getJSONObject( allCookiee.length() - 1 ).getString("chainage")) - Double.parseDouble(previosDataJSON.getString("mapping_ch"));
                        temp1 = getNorthing * temp;
                        temp2 = Double.parseDouble(allCookiee.getJSONObject( allCookiee.length() - 1 ).getString("chainage")) * Double.parseDouble(previosDataJSON.getString("utm_northing"));
                        temp3 = getMappingCh - temp2;
                        first_half = temp1 + temp3;
                        temp4 = getMappingCh - Double.parseDouble(previosDataJSON.getString("mapping_ch"));
                        manualLastNorthing = first_half / temp4;
                        //Log.e("manualLastNorthing", String.valueOf(manualLastNorthing));

                        if( manualLastEasting > 0 && manualLastNorthing > 0 ){
                            Double t, t1,t3;
                            t = getNorthing - manualLastNorthing;
                            t1 = getEasting - manualLastEasting;
                            t3 = t / t1;
                            angleRedians = Math.atan(t3);
                        }else {
                            angleRedians = 0.000;
                        }

                        for( int i = 0; i < allCookiee.length(); i++){
                            Double man_cs_offset_easting, man_cs_offset_northing, man_bsoffset, man_isoffset, man_fsoffset;
                            Double ManbsOffsetMean, ManfsOffsetMean, ManisOffsetMean, manrisePlus, manfallMinus, manchByAutoLevel, mancheckedReduceLevel;
                            Double manreduceLevel, manheightOfInstrument, manavgHeightOfInstrument;
                            Double tempEasting;
//                            JSONArray intermediatesite = new JSONArray();
                            ArrayList<String> intermediatesite = new ArrayList<String>();

                            JSONArray backsite = new JSONArray();
                            JSONArray foresite = new JSONArray();
                            JSONArray tempArray = new JSONArray();
                            tempArray.put("0.00");
                            tempArray.put("0.00");

                            //tempEasting = ();
                            temp = Double.parseDouble(allCookiee.getJSONObject(i).getString("chainage")) - Double.parseDouble(previosDataJSON.getString("mapping_ch"));
                            temp1 = getEasting * temp;
                            //temp2 = Double.parseDouble(allCookiee.getJSONObject(i).getString("chainage")) * Double.parseDouble(previosDataJSON.getString("utm_easting"));
                            temp3 = (getMappingCh - Double.parseDouble(allCookiee.getJSONObject(i).getString("chainage"))) * Double.parseDouble(previosDataJSON.getString("utm_easting")) ;
                            first_half = temp1 + temp3;
                            temp4 = getMappingCh - Double.parseDouble(previosDataJSON.getString("mapping_ch"));
                            manualLastEasting = first_half / temp4;

                            temp = Double.parseDouble(allCookiee.getJSONObject(i).getString("chainage")) - Double.parseDouble(previosDataJSON.getString("mapping_ch"));
                            temp1 = getNorthing * temp;
                            //temp2 = Double.parseDouble(allCookiee.getJSONObject(i).getString("chainage")) * Double.parseDouble(previosDataJSON.getString("utm_northing"));
                            temp3 = (getMappingCh - Double.parseDouble(allCookiee.getJSONObject(i).getString("chainage"))) * Double.parseDouble(previosDataJSON.getString("utm_northing"));
                            first_half = temp1 + temp3;
                            temp4 = getMappingCh - Double.parseDouble(previosDataJSON.getString("mapping_ch"));
                            manualLastNorthing = first_half / temp4;
                            Log.e("manualLastNorthing", String.valueOf(manualLastNorthing));

                            intermediatesite.add(allCookiee.getJSONObject(i).getString("is_reading"));
                            backsite.put("");
                            foresite.put("");

                            // Easting
                            if( angleRedians > 0 ){
                                temp = Math.sin(angleRedians) * Double.parseDouble(allCookiee.getJSONObject(i).getString("offset"));
                                man_cs_offset_easting = manualLastEasting + temp;
                            }else{
                                temp = Math.sin(angleRedians) * Double.parseDouble(allCookiee.getJSONObject(i).getString("offset"));
                                man_cs_offset_easting = manualLastEasting - temp;
                            }
                            Log.e("man_cs_offset_easting", String.valueOf(man_cs_offset_easting));

                            // Northing
                            if( angleRedians > 0 ){
                                temp = Math.cos(angleRedians) * Double.parseDouble(allCookiee.getJSONObject(i).getString("offset"));
                                man_cs_offset_northing = manualLastNorthing - temp;
                            }else{
                                temp = Math.cos(angleRedians) * Double.parseDouble(allCookiee.getJSONObject(i).getString("offset"));
                                man_cs_offset_northing = manualLastNorthing + temp;
                            }
                            Log.e("man_cs_offset_northing", String.valueOf(man_cs_offset_northing));

                            /***** Calculation for fliping points **********/
                            Double man_cs_offset_eastingForCal, man_cs_offset_northingForCal;
                            // Easting
                            if( angleRedians > 0 ){
                                temp = Math.sin(angleRedians) * (-1 * Double.parseDouble(allCookiee.getJSONObject(i).getString("offset")));
                                man_cs_offset_eastingForCal = manualLastEasting + temp;
                            }else{
                                temp = Math.sin(angleRedians) * (-1 * Double.parseDouble(allCookiee.getJSONObject(i).getString("offset")));
                                man_cs_offset_eastingForCal = manualLastEasting - temp;
                            }
                            Log.e("man_cs_eastingForCal", String.valueOf(man_cs_offset_eastingForCal));

                            // Northing
                            if( angleRedians > 0 ){
                                temp = Math.cos(angleRedians) * (-1 *Double.parseDouble(allCookiee.getJSONObject(i).getString("offset")));
                                man_cs_offset_northingForCal = manualLastNorthing - temp;
                            }else{
                                temp = Math.cos(angleRedians) * (-1 *Double.parseDouble(allCookiee.getJSONObject(i).getString("offset")));
                                man_cs_offset_northingForCal = manualLastNorthing + temp;
                            }
                            Log.e("man_cet_northingForCal", String.valueOf(man_cs_offset_northingForCal));
                            //AddMeasurement mes = new AddMeasurement();
                            man_bsoffset = 0.000;
                            man_isoffset = calSiteOffset(intermediatesite);
                            man_fsoffset = 0.000;

                            JSONArray bsSumMean = tempArray;
                            JSONArray isSumMean = calSiteOffsetSumMean(intermediatesite);
                            JSONArray fsSumMean = tempArray;

                            ManbsOffsetMean = 0.000;
                            ManfsOffsetMean = 0.000;
                            ManisOffsetMean = 0.000;
                            try {
                                ManbsOffsetMean = Double.valueOf(bsSumMean.getString(1));
                                ManisOffsetMean = Double.valueOf(isSumMean.getString(1));
                                ManfsOffsetMean = Double.valueOf(fsSumMean.getString(1));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            JSONArray bsRiseFall = calRiseFall(ManbsOffsetMean, ManfsOffsetMean);
                            manrisePlus = 0.000;
                            manfallMinus = 0.000;
                            try {
                                manrisePlus = bsRiseFall.getDouble(0);
                                manfallMinus = bsRiseFall.getDouble(1);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            Double manprevious_ch_by_auto_level = Double.parseDouble(previosDataJSON.getString("ch_by_auto_level"));
                            Double manprevious_hight_of_instrument = Double.parseDouble(previosDataJSON.getString("hight_of_instrument"));
                            Double manprevious_utm_easting = Double.valueOf(previosDataJSON.getString("utm_easting"));
                            Double manprevious_utm_northing = Double.valueOf(previosDataJSON.getString("utm_northing"));
                            Double manprevious_measurment_ch = Double.valueOf(previosDataJSON.getString("measurment_ch"));

                            manchByAutoLevel = calChByAutoLevel(manprevious_ch_by_auto_level, man_bsoffset, man_fsoffset);
                            mancheckedReduceLevel = calCheckedReduceLevel(manprevious_hight_of_instrument, ManisOffsetMean, ManfsOffsetMean);
                            manreduceLevel = calReduceLevel(manprevious_hight_of_instrument, ManbsOffsetMean, ManfsOffsetMean);

                            manheightOfInstrument = calheightOfInstrument( mancheckedReduceLevel, ManbsOffsetMean, ManfsOffsetMean);
                            manavgHeightOfInstrument = manheightOfInstrument - mancheckedReduceLevel;

                            //String tempTBL = tbm_rl.getText().toString();
                            Double manTBM_RL;

                            if( tempTBL.equals("") ){
                                manTBM_RL = 0.000;
                            }else {
                                manTBM_RL = Double.valueOf(tempTBL);
                            }
                            String n = String.valueOf(manprevious_utm_northing);
                            String e = String.valueOf(manprevious_utm_easting);

                            Double manadjustmentError = calAdjustmentError( manTBM_RL, manreduceLevel);
                            Double mann_offset = Double.valueOf(calNOffset(man_cs_offset_northing, n));
                            Double mane_offset = Double.valueOf(calEOffset(man_cs_offset_easting, e));

                            String nOffset = String.valueOf(mann_offset);
                            String eOffset = String.valueOf(mane_offset);
                            String Pgps_offset = calGPSOffsetLength( man_cs_offset_northing, man_cs_offset_easting, previosDataJSON.getString("utm_northing"), previosDataJSON.getString("utm_easting"), nOffset, eOffset);
                            Double mangps_offset_length = Double.valueOf(Pgps_offset);

                            String Pmapping_ch = calMappingCh( nOffset, Pgps_offset, previosDataJSON.getString("mapping_ch"));
                            Double manch = Double.valueOf(allCookiee.getJSONObject(i).getString("chainage"));

                            String manlSection = allCookiee.getJSONObject(i).getString("chainage");
                            String manxSection = allCookiee.getJSONObject(i).getString("offset");
                            String manRemarks = allCookiee.getJSONObject(i).getString("remarks");

                            Double manMeasurementCH = calMeasurementCH( manprevious_measurment_ch , manxSection, manlSection);
                            int id = Integer.parseInt(Variables.PROJECT_ID);
                            int equ = Integer.parseInt(previosDataJSON.getString("equipement_id"));
//                            String layer = previosDataJSON.getString("layer_code");
                            Log.e("single", String.valueOf(allCookiee.getJSONObject(i)));
                            String layer = allCookiee.getJSONObject(i).getString("layer");
                            String manel = "";
                            Log.e("layer", layer);
                            //JSONArray latlng = UTMtoGeog(String.valueOf(man_cs_offset_easting), String.valueOf(man_cs_offset_northing),previosDataJSON.getString("utm_zone") );
                            String str = previosDataJSON.getString("utm_zone") + "P "+ man_cs_offset_eastingForCal + " " + man_cs_offset_northingForCal;
                            double[] result = test.convertUTMToLatLong(str);
                            Log.e("lat", String.valueOf(result[0]));
                            Log.e("lng", String.valueOf(result[1]));
                            String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());

                            int lastId = db.addMeasurement(new Measurement(id, equ, layer,String.valueOf(result[0]), String.valueOf(result[1]), previosDataJSON.getString("utm_zone"), String.valueOf(man_cs_offset_easting),
                                    String.valueOf(man_cs_offset_northing), String.valueOf(angleRedians), String.valueOf(man_cs_offset_easting), String.valueOf(man_cs_offset_northing),
                                    "", String.valueOf(manch),String.valueOf(manchByAutoLevel),String.valueOf(manMeasurementCH), String.valueOf(mangps_offset_length),
                                    String.valueOf(man_bsoffset), String.valueOf(man_isoffset), String.valueOf(man_fsoffset),
                                    String.valueOf(mann_offset), String.valueOf(mane_offset), manlSection, manxSection, String.valueOf(manrisePlus), String.valueOf(manfallMinus),
                                    String.valueOf(manavgHeightOfInstrument), String.valueOf(manheightOfInstrument),
                                    String.valueOf(manreduceLevel), String.valueOf(mancheckedReduceLevel), String.valueOf(manRemarks), "", String.valueOf(manadjustmentError), "",
                                    "", "", "", "", "", "", "", "C", 1, currentDateTimeString));


                            Object[] intermediatesiteArray = intermediatesite.toArray();
                            String concatBackSite = backsite.getString(0);
                            String concatInterSite = (String) intermediatesiteArray[0];
                            String concatForeSite = foresite.getString(0);

                            int readingId = db.addStaffReadings(lastId, concatBackSite, concatInterSite, concatForeSite);

                            //db.addParentChild(measurementId, lastId);
                            LastIds += lastId + ",";
                            //if (lastId > 0 && readingId > 0){
                            if (allCookiee.length() - 1 == i){
                                if(LastIds.endsWith(",")){
                                    LastIds = LastIds.substring(0, LastIds.length() - 1);
                                }
                                db.updateChild(measurementId, LastIds);
                                //Log.e("getPrentChild", String.valueOf(db.getPrentChild(measurementId)));
                                db.deleteCookieByProjectId(String.valueOf(id));
                                Intent intent = new Intent(AddManualData.this, MapActivity.class);
                                startActivity(intent);
                                AddManualData.this.finish();
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else{
                    Toast.makeText(AddManualData.this, "No data found!",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
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

        allCookies = db.getAllCookie( projectId );
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
//        ArrayAdapter adapter = new
//                ArrayAdapter(this,android.R.layout.simple_list_item_1,languages);
//
//        search_layers.setAdapter(adapter);
//        search_layers.setThreshold(1);
    }

    public class CookieListing extends BaseAdapter {
        ArrayList<CookieGetSet> projecctList = new ArrayList<CookieGetSet>();
        @Override
        public int getCount() {
            return cookieList.size();
            //return 3;
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
                    db.deleteCookie( list.getId() );
                    getCookiedata();
                }
            });
            return convertView;
        }

        public class ViewHolder {
            TextView listISReadings, listChainage, listOffset, listLayer;
            ImageView delete;
        }
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
        //String tempTBL = tbm_rl.getText().toString();

        if( tempTBL.equals("") ){
            returnValue = 0.000;
            getTBM_RL = returnValue;
        }else {
            getTBM_RL = Double.valueOf(tempTBL);
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

    public String calNOffset(Double getNorthings, String utm_northings) {
        Double returnValue = 0.000;

        if( getNorthings > 0 ){
            returnValue = getNorthings - Double.parseDouble(utm_northings);
        }else {
            returnValue = 0.000;
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

    public String calMappingCh(String pn_offsets, String pgps_offsets, String mapping_chs) {
        Double returnValue = 0.000;
        returnValue = Double.parseDouble(pgps_offsets) + Double.parseDouble(mapping_chs);
        return String.valueOf(returnValue);
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

    /*private void Declarations() {
        Item = 0.0;//Default
        k0 = 0.9996;//scale on central meridian
        //a = DatumEqRad[Item];//equatorial radius, meters.
        a = 6378137.0;//equatorial radius, meters.
        //f = 1/DatumFlat[Item];//polar flattening.
        f = 1/298.2572236;//polar flattening.
        b = a*(1-f);//polar axis.
        e = Math.sqrt(1 - b*b/a*a);//eccentricity
        drad = Math.PI/180;//Convert degrees to radians)
        latd = 0;//latitude in degrees
        int phi = 0;//latitude (north +, south -), but uses phi in reference
        e0 = e/Math.sqrt(1 - e*e);//e prime in reference
        double tempPhi = Math.sin(phi);
        //N = a/Math.sqrt(1-Math.pow(e* tempPhi),2);
        T = Math.pow(Math.tan(phi),2);
        C = Math.pow(e*Math.cos(phi),2);
        lng = 0;//Longitude (e = +, w = -) - can't use long - reserved word
        lng0 = 0;//longitude of central meridian
        lngd = 0;//longitude in degrees
        M = 0;//M requires calculation
        x = 0;//x coordinate
        y = 0;//y coordinate
        k = 1;//local scale
        utmz = 30;//utm zone
        zcm = 0;//zone central meridian
        DigraphLetrsE = "ABCDEFGHJKLMNPQRSTUVWXYZ";
        DigraphLetrsN = "ABCDEFGHJKLMNPQRSTUV";
        OOZok = false;
    }
*/
    /*public JSONArray UTMtoGeog(String getEasting, String getNorthing,String getZone){
        Log.e("getEasting",getEasting);
        Log.e("getNorthing",getNorthing);
        Log.e("getZone",getZone);
        //Convert UTM Coordinates to Geographic
        Declarations();
        k0 = 0.9996;
        b = a*(1-f);//polar axis.
        e = Math.sqrt(1 - (b/a)*(b/a));//eccentricity
        e0 = e/Math.sqrt(1 - e*e);//Called e prime in reference
        Double esq = (1 - (b/a)*(b/a));//e squared for use in expansions
        Double e0sq = e*e/(1-e*e);// e0 squared - always even powers
        Double x = Double.valueOf(getEasting);
        if (x<160000 || x>840000){Log.e("error", "Outside permissible range of easting values \n Results may be unreliable \n Use with caution");}
        Double y = Double.valueOf(getNorthing);

        if (y<0){Log.e("error", "Negative values not allowed \n Results may be unreliable \n Use with caution");}
        if (y>10000000){Log.e("error","Northing may not exceed 10,000,000 \n Results may be unreliable \n Use with caution");}
        utmz = Integer.parseInt(getZone);
        zcm = 3 + 6*(utmz-1) - 180;//Central meridian of zone
        Double e1 = (1 - Math.sqrt(1 - e*e))/(1 + Math.sqrt(1 - e*e));//Called e1 in USGS PP 1395 also
        Double M0 = 0.00;//In case origin other than zero lat - not needed for standard UTM
        Double M = M0 + y/k0;//Arc length along standard meridian.
        //if (document.getElementById("SHemBox").checked === true){M=M0+(y-10000000)/k;}
        Double mu = M/(a*(1 - esq*(1/4 + esq*(3/64 + 5*esq/256))));
        Double phi1 = mu + e1*(3/2 - 27*e1*e1/32)*Math.sin(2*mu) + e1*e1*(21/16 -55*e1*e1/32)*Math.sin(4*mu);//Footprint Latitude
        phi1 = phi1 + e1*e1*e1*(Math.sin(6*mu)*151/96 + e1*Math.sin(8*mu)*1097/512);
        Double C1 = e0sq*Math.pow(Math.cos(phi1),2);
        Double T1 = Math.pow(Math.tan(phi1),2);
        Double N1 = a/Math.sqrt(1-Math.pow(e*Math.sin(phi1),2));
        Double R1 = N1*(1-e*e)/(1-Math.pow(e*Math.sin(phi1),2));
        Double D = (x-500000)/(N1*k0);
        Double phi = (D*D)*(1/2 - D*D*(5 + 3*T1 + 10*C1 - 4*C1*C1 - 9*e0sq)/24);
        phi = phi + Math.pow(D,6)*(61 + 90*T1 + 298*C1 + 45*T1*T1 -252*e0sq - 3*C1*C1)/720;
        phi = phi1 - (N1*Math.tan(phi1)/R1)*phi;

//Output Latitude
        Double latFinal = Math.floor(1000000*phi/drad)/1000000;
        //Double  latFinal = 1000000*phi/drad/1000000;

//Longitude
        Double lng = D*(1 + D*D*((-1 -2*T1 -C1)/6 + D*D*(5 - 2*C1 + 28*T1 - 3*C1*C1 +8*e0sq + 24*T1*T1)/120))/Math.cos(phi1);
        Double lngd = zcm+lng/drad;

//Output Longitude
        Double lngFinal = Math.floor(1000000*lngd)/1000000;
        //Double lngFinal =1000000*lngd/1000000;
        JSONArray output = new JSONArray();
        output.put(latFinal);
        output.put(lngFinal);
        Log.e("output", String.valueOf(output));
        return output;
    }//End UTM to Geog*/


}
