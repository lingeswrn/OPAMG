package in.opamg.app.DatabaseConnection;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.opamg.app.Models.Equipments;
import in.opamg.app.Models.Layers;
import in.opamg.app.Models.Measurement;
import in.opamg.app.Models.Project;

/**
 * Created by Linesh on 5/10/2017.
 */

public class DatabaseHandler extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 2   ;

    // Database Name
    private static final String DATABASE_NAME = "OPAMG";

    // Projects table name
    private static final String TABLE_PROJECTS = "projects";
    private static final String TABLE_EQUIPMENTS = "equipments_list";

    public static String getTableLayers() {
        return TABLE_LAYERS;
    }

    private static final String TABLE_LAYERS = "layers";

    public static String getTableProjects() {
        return TABLE_PROJECTS;
    }

    public static String getTableEquipments() {
        return TABLE_EQUIPMENTS;
    }

    // Projects Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_FROM = "_from";
    private static final String KEY_TO = "_to";
    private static final String KEY_PROJECT_NAME = "project_name";
    private static final String KEY_COMPANY_NAME = "company_name";
    private static final String KEY_WORK_ORDER_NUMBER = "work_order_number";
    private static final String KEY_SCOPE_WORK = "scope_wrk";
    private static final String KEY_REMARKS = "remarks";
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_STATUS = "status";
    private static final String KEY_CREATED_DATE = "created_date";
    private static final String KEY_UPDATED_DATE = "updated_date";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_PROJECT_TABLE = "CREATE TABLE " + TABLE_PROJECTS + "("
                + KEY_ID + " INTEGER," + KEY_FROM + " TEXT,"
                + KEY_TO + " TEXT, " + KEY_PROJECT_NAME +" TEXT,"
                + KEY_COMPANY_NAME + " TEXT, " + KEY_WORK_ORDER_NUMBER +" TEXT,"
                + KEY_SCOPE_WORK + " TEXT, "+ KEY_REMARKS +" TEXT,"
                + KEY_USER_ID + " TEXT, " + KEY_STATUS + "TEXT,"
                + KEY_CREATED_DATE + "TEXT, "+ KEY_UPDATED_DATE +" TEXT)";
        Log.e("Query", CREATE_PROJECT_TABLE);
        db.execSQL(CREATE_PROJECT_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PROJECTS);

        // Create tables again
        onCreate(db);
    }

    public void deleteTable(String tableProjects){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from "+ tableProjects);
    }

    public void dropTable(String tableProjects){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE "+ tableProjects);
    }

    // Adding new Project
    public void addProject(Project project) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID, project.get_id());
        values.put(KEY_FROM, project.get_from());
        values.put(KEY_TO, project.get_to());
        values.put(KEY_PROJECT_NAME, project.get_project_name());
        values.put(KEY_COMPANY_NAME, project.get_company_name());
        values.put(KEY_WORK_ORDER_NUMBER, project.get_work_order_number());
        values.put(KEY_SCOPE_WORK, project.get_scope_wrk());
        values.put(KEY_REMARKS, project.get_remarks());
        values.put(KEY_USER_ID, project.get_user_id());
        //values.put(KEY_STATUS, project.get_status());
        //values.put(KEY_CREATED_DATE, project.get_created_date());
        //values.put(KEY_UPDATED_DATE, project.get_updated_date());


        // Inserting Row
        db.insert(TABLE_PROJECTS, null, values);
        db.close(); // Closing database connection
    }

    public void addParentChild(String measurementId, int lastId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("parentId", measurementId);
        values.put("childId", lastId);
        db.insert("parentchild", null, values);
        db.close();
    }

    // Adding new Equipment
    public void addEquipment(Equipments equipments) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("id", equipments.get_id());
        values.put("model_number", equipments.get_model_number());
        values.put("last_calibration_service_center", equipments.get_last_calibration_service_center());
        values.put("expiry_date", equipments.get_expiry_date());
        values.put("least_count", equipments.get_least_count());
        values.put("owner", equipments.get_owner());
        values.put("status", equipments.get_status());
        values.put("created_date", equipments.get_created_date());


        // Inserting Row
        db.insert(TABLE_EQUIPMENTS, null, values);
        db.close(); // Closing database connection
    }

    //Adding new GPS Co ordinates
    public int addCoOrdinates(int lastId, String type, String s, String s1, String s2){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("measurement_id", lastId);
        values.put("type", type);
        values.put("deg", s);
        values.put("min", s1);
        values.put("sec", s2);

        // Inserting Row
        int co = (int) db.insert("gps_coordinates", null, values);
        db.close(); // Closing database connection
        return co;
    }

    //Adding Staff reading
    public int addStaffReadings(int lastId, String s, String s1, String s2){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("measurement_id", lastId);
        values.put("back_site", s);
        values.put("intermediate_site", s1);
        values.put("forward_site", s2);
        Log.e("values", String.valueOf(values));
        // Inserting Row
        int staffId = (int) db.insert("staff_readings", null, values);
        db.close(); // Closing database connection
        return staffId;
    }

    // Adding new Layers
    public void addLayers(Layers layers) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("code", layers.get_code());
        values.put("description", layers.get_description());
        values.put("category", layers.get_category());
        // Inserting Row
        db.insert(TABLE_LAYERS, null, values);
        db.close(); // Closing database connection
    }

    // Getting All Contacts
    public List<Project> getAllProjects() {
        List<Project> projectList = new ArrayList<Project>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_PROJECTS;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Project project = new Project();
                project.set_id(cursor.getString(0));
                project.set_from(cursor.getString(1));
                project.set_to(cursor.getString(2));
                project.set_project_name(cursor.getString(3));
                project.set_company_name(cursor.getString(4));
                project.set_work_order_number(cursor.getString(5));
                project.set_scope_wrk(cursor.getString(6));
                project.set_remarks(cursor.getString(7));
                project.set_user_id(cursor.getString(8));
                project.set_status(cursor.getString(9));
                project.set_created_date(cursor.getString(10));
                project.set_updated_date(cursor.getString(11));

                projectList.add(project);
            } while (cursor.moveToNext());
        }

        // return contact list
        return projectList;
    }

    public void createEquipmentTable(){
        SQLiteDatabase db = this.getWritableDatabase();
        String CREATE_EQUIPMENT_TABLE = "CREATE TABLE IF NOT EXISTS "+ TABLE_EQUIPMENTS +" ( id INTEGER, model_number TEXT, last_calibration_service_center TEXT, expiry_date TEXT, least_count TEXT, owner TEXT, status TEXT, created_date TEXT )";
        db.execSQL(CREATE_EQUIPMENT_TABLE);
    }

    public List<Equipments> getAllEquipments() {
        List<Equipments> equipmentList = new ArrayList<Equipments>();
        // Select All Query
        String selectQuery = "SELECT model_number FROM " + TABLE_EQUIPMENTS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Equipments equipments = new Equipments();
                equipments.set_model_number(cursor.getString(0));

                equipmentList.add(equipments);
            } while (cursor.moveToNext());
        }

        // return contact list
        return equipmentList;
    }

    public List<Equipments> getSingleEquipments(String modelNumber) {
        List<Equipments> equipmentList = new ArrayList<Equipments>();

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_EQUIPMENTS, new String[] { "id", "model_number", "last_calibration_service_center",
                        "expiry_date", "least_count", "owner", "status", "created_date" }, "model_number" + "=?",
                new String[] { String.valueOf(modelNumber) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        if (cursor.moveToFirst()) {
            do {
                Equipments equipments = new Equipments();
                equipments.set_id(cursor.getString(0));
                equipments.set_model_number(cursor.getString(1));
                equipments.set_last_calibration_service_center(cursor.getString(2));
                equipments.set_expiry_date(cursor.getString(3));
                equipments.set_least_count(cursor.getString(4));
                equipments.set_owner(cursor.getString(5));
                equipments.set_status(cursor.getString(6));
                equipments.set_created_date(cursor.getString(7));

                equipmentList.add(equipments);
            } while (cursor.moveToNext());
        }
        return equipmentList;
    }

    public List<Layers> getAllLayers() {
        List<Layers> layers = new ArrayList<Layers>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_LAYERS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Layers lay = new Layers();
                lay.set_code(cursor.getString(0));
                lay.set_description(cursor.getString(1));

                layers.add(lay);
            } while (cursor.moveToNext());
        }

        // return contact list
        return layers;
    }

    public void createLayersTable(){
        SQLiteDatabase db = this.getWritableDatabase();
        String CREATE_LAYER_TABLE = "CREATE TABLE IF NOT EXISTS "+ TABLE_LAYERS +" ( code TEXT, description TEXT, category TEXT )";
        db.execSQL(CREATE_LAYER_TABLE);
    }

    public void createMeasurementTables(){
        SQLiteDatabase db = this.getWritableDatabase();
        String CREATE_STAFF_READINGS = "CREATE TABLE IF NOT EXISTS staff_readings ( id INTEGER PRIMARY KEY, measurement_id INT, back_site TEXT, intermediate_site TEXT, forward_site TEXT)";
        db.execSQL(CREATE_STAFF_READINGS);

        String CREATE_GPS_COORDINATES = "CREATE TABLE IF NOT EXISTS gps_coordinates ( id INTEGER PRIMARY KEY, measurement_id INT, type TEXT, deg TEXT, min TEXT, sec TEXT)";
        db.execSQL(CREATE_GPS_COORDINATES);

        String CREATE_MEASUREMENT = "CREATE TABLE IF NOT EXISTS measurement ( id INTEGER PRIMARY KEY, project_id INT, equipement_id INT, layer_code TEXT, lattitude TEXT, longitude TEXT, utm_zone TEXT, utm_easting TEXT, utm_northing TEXT, angle_redians TEXT, cs_offset_e TEXT, cs_offset_n TEXT, el TEXT, mapping_ch TEXT, ch_by_auto_level TEXT, measurment_ch TEXT, gps_offset_length TEXT, bs_offset TEXT, is_offset TEXT, fs_offset TEXT, n_offset TEXT, e_offset TEXT, l_section_offset TEXT, x_section_offset TEXT, rise_plus TEXT, fall_minus TEXT, avg_hight_of_instrument_from_gl TEXT, hight_of_instrument TEXT, calculated_reduce_rl TEXT, checked_reduce_level TEXT, remarks TEXT, adj_rl TEXT, adjustment_error TEXT, tbm_rl TEXT, bs_angle TEXT, fs_angle TEXT, close_photograph TEXT, location_photograph TEXT, screen_shot TEXT, other_photograph TEXT, child TEXT, isparent TEXT, status INT, created_date TEXT )";
        db.execSQL(CREATE_MEASUREMENT);

        String CREATE_PARENT_TABLE = "CREATE TABLE IF NOT EXISTS parentchild ( id INTEGER PRIMARY KEY, parentId TEXT, childId TEXT )";
        db.execSQL(CREATE_PARENT_TABLE);
    }

    public void createCookieTable(){
        SQLiteDatabase db = this.getWritableDatabase();
        String CREATE_STAFF_READINGS = "CREATE TABLE IF NOT EXISTS cookie ( id INTEGER PRIMARY KEY, project_id TEXT, chainage TEXT, offset TEXT, is_reading TEXT, remarks TEXT, layer TEXT)";
        db.execSQL(CREATE_STAFF_READINGS);
    }

    public JSONArray getMeasurementByProjectId(int projectId ){
        String selectQuery= "SELECT * FROM measurement WHERE project_id = " + projectId + " AND isparent = 'P' ORDER BY id DESC LIMIT 1";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        JSONArray previousArray = new JSONArray();
        if(cursor.moveToFirst()) {
            JSONObject prev = new JSONObject();
            try {

                prev.put("id", cursor.getString(0));
                prev.put("project_id", cursor.getString(1));
                prev.put("equipement_id", cursor.getString(2));
                prev.put("layer_code", cursor.getString(3));
                prev.put("lattitude", cursor.getString(4));
                prev.put("longitude", cursor.getString(5));
                prev.put("utm_zone", cursor.getString(6));
                prev.put("utm_easting", cursor.getString(7));
                prev.put("utm_northing", cursor.getString(8));
                prev.put("angle_redians", cursor.getString(9));
                prev.put("cs_offset_e", cursor.getString(10));
                prev.put("cs_offset_n", cursor.getString(11));
                prev.put("el", cursor.getString(12));
                prev.put("mapping_ch", cursor.getString(13));
                prev.put("ch_by_auto_level", cursor.getString(14));
                prev.put("measurment_ch", cursor.getString(15));
                prev.put("gps_offset_length", cursor.getString(16));
                prev.put("bs_offset", cursor.getString(17));
                prev.put("is_offset", cursor.getString(18));
                prev.put("fs_offset", cursor.getString(19));
                prev.put("n_offset", cursor.getString(20));
                prev.put("e_offset", cursor.getString(21));
                prev.put("l_section_offset", cursor.getString(22));
                prev.put("x_section_offset", cursor.getString(23));
                prev.put("rise_plus", cursor.getString(24));
                prev.put("fall_minus", cursor.getString(25));
                prev.put("avg_hight_of_instrument_from_gl", cursor.getString(26));
                prev.put("hight_of_instrument", cursor.getString(27));
                prev.put("calculated_reduce_rl", cursor.getString(28));
                prev.put("checked_reduce_level", cursor.getString(29));
                prev.put("remarks", cursor.getString(30));
                prev.put("adj_rl", cursor.getString(31));
                prev.put("adjustment_error", cursor.getString(32));
                prev.put("tbm_rl", cursor.getString(33));
                prev.put("bs_angle", cursor.getString(34));
                prev.put("fs_angle", cursor.getString(35));
                prev.put("close_photograph", cursor.getString(36));
                prev.put("location_photograph", cursor.getString(37));
                prev.put("screen_shot", cursor.getString(38));
                prev.put("other_photograph", cursor.getString(39));
                prev.put("child", cursor.getString(40));
                prev.put("isparent", cursor.getString(41));
                prev.put("status", cursor.getString(42));
                prev.put("created_date", cursor.getString(43));
                previousArray.put(prev);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        cursor.close();
        return previousArray;
    }
    // Adding new Measurement
    public int addMeasurement(Measurement measurement) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        Log.e("proj", String.valueOf(measurement.get_project_id()));
        values.put("project_id", measurement.get_project_id());
        values.put("equipement_id", measurement.get_equipement_id());
        values.put("layer_code", measurement.get_layer_code());
        values.put("lattitude", measurement.get_lattitude());
        values.put("longitude", measurement.get_longitude());
        values.put("utm_zone", measurement.get_utm_zone());
        values.put("utm_easting", measurement.get_utm_easting());
        values.put("utm_northing", measurement.get_utm_northing());
        values.put("angle_redians", measurement.get_angle_redians());
        values.put("cs_offset_e", measurement.get_cs_offset_e());
        values.put("cs_offset_n", measurement.get_cs_offset_n());
        values.put("el", measurement.get_el());
        values.put("mapping_ch", measurement.get_mapping_ch());
        values.put("ch_by_auto_level", measurement.get_ch_by_auto_level());
        values.put("measurment_ch", measurement.get_measurment_ch());
        values.put("gps_offset_length", measurement.get_gps_offset_length());
        values.put("bs_offset", measurement.get_bs_offset());
        values.put("is_offset", measurement.get_is_offset());
        values.put("fs_offset", measurement.get_fs_offset());
        values.put("n_offset", measurement.get_n_offset());
        values.put("e_offset", measurement.get_e_offset());
        values.put("l_section_offset", measurement.get_l_section_offset());
        values.put("x_section_offset", measurement.get_x_section_offset());
        values.put("rise_plus", measurement.get_rise_plus());
        values.put("fall_minus", measurement.get_fall_minus());
        values.put("avg_hight_of_instrument_from_gl", measurement.get_avg_hight_of_instrument_from_gl());
        values.put("hight_of_instrument", measurement.get_hight_of_instrument());
        values.put("calculated_reduce_rl", measurement.get_calculated_reduce_rl());
        values.put("checked_reduce_level", measurement.get_checked_reduce_level());
        values.put("remarks", measurement.get_remarks());
        values.put("adj_rl", measurement.get_adj_rl());
        values.put("adjustment_error", measurement.get_adjustment_error());
        values.put("tbm_rl", measurement.get_tbm_rl());
        values.put("bs_angle", measurement.get_bs_angle());
        values.put("fs_angle", measurement.get_fs_angle());
        values.put("close_photograph", measurement.get_close_photograph());
        values.put("location_photograph", measurement.get_close_photograph());
        values.put("screen_shot", measurement.get_screen_shot());
        values.put("other_photograph", measurement.get_other_photograph());
        values.put("child", measurement.get_child());
        values.put("isparent", measurement.get_isparent());
        values.put("status", measurement.get_status());
        values.put("created_date", measurement.get_created_date());
        // Inserting Row
        int id = (int) db.insert("measurement", null, values);
        db.close(); // Closing database connection
        return id;
    }
    // Adding Cookies
    public long addCookie(String projectId, String getStrChainage, String getStrOffset, String getStrISReadings, String getRemarks, String getLayer) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("project_id", projectId);
        values.put("chainage", getStrChainage);
        values.put("offset", getStrOffset);
        values.put("is_reading", getStrISReadings);
        values.put("remarks", getRemarks);
        values.put("layer", getLayer);
        // Inserting Row
        long latId = db.insert("cookie", null, values);
        db.close(); // Closing database connection

        return latId;
    }

    public JSONArray getAllCookie(String projectId ) {
        String selectQuery = "SELECT * FROM cookie WHERE project_id = " + projectId;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        JSONArray cookieArray = new JSONArray();
        if (cursor.moveToFirst()) {
            do {
                JSONObject cookie = new JSONObject();
                try {
                    cookie.put("id", cursor.getString(0));
                    cookie.put("project_id", cursor.getString(1));
                    cookie.put("chainage", cursor.getString(2));
                    cookie.put("offset", cursor.getString(3));
                    cookie.put("is_reading", cursor.getString(4));
                    cookie.put("remarks", cursor.getString(5));
                    cookie.put("layer", cursor.getString(6));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                cookieArray.put(cookie);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return cookieArray;
    }

    public void deleteCookie(String cookieeId){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from cookie where id = "+cookieeId);
    }

    public JSONArray getLatLng(String projectId ) {

        String selectQuery = "SELECT lattitude, longitude, layer_code, id FROM measurement WHERE project_id = " + projectId;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        JSONArray latlngArray = new JSONArray();
        if (cursor.moveToFirst()) {
            do {
                JSONObject latlng = new JSONObject();
                try {
                    latlng.put("latitude", cursor.getString(0));
                    latlng.put("longitude", cursor.getString(1));
                    latlng.put("layer_code", cursor.getString(2));
                    latlng.put("id", cursor.getString(3));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                latlngArray.put(latlng);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return latlngArray;
    }

    public JSONObject getAllMeasurementByProjectId(String id){
        String selectQuery = "SELECT * FROM  `measurement` WHERE id = " + id;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        JSONObject allMeasurementObj = new JSONObject();
        JSONArray allMeasurementsArray = new JSONArray();
        if (cursor.moveToFirst()) {
            do {

                JSONArray coordinatesArray = new JSONArray();
                JSONArray projectArray = new JSONArray();
                JSONArray layerArray = new JSONArray();
                JSONArray staffReadingArray = new JSONArray();
                try {
                    allMeasurementObj.put("id", cursor.getString(0));
                    allMeasurementObj.put("project_id", cursor.getString(1));
                    allMeasurementObj.put("equipement_id", cursor.getString(2));
                    allMeasurementObj.put("layer_code", cursor.getString(3));
                    allMeasurementObj.put("lattitude", cursor.getString(4));
                    allMeasurementObj.put("longitude", cursor.getString(5));
                    allMeasurementObj.put("utm_zone", cursor.getString(6));
                    allMeasurementObj.put("utm_easting", cursor.getString(7));
                    allMeasurementObj.put("utm_northing", cursor.getString(8));
                    allMeasurementObj.put("angle_redians", cursor.getString(9));
                    allMeasurementObj.put("cs_offset_e", cursor.getString(10));
                    allMeasurementObj.put("cs_offset_n", cursor.getString(11));
                    allMeasurementObj.put("el", cursor.getString(12));
                    allMeasurementObj.put("mapping_ch", cursor.getString(13));
                    allMeasurementObj.put("ch_by_auto_level", cursor.getString(14));
                    allMeasurementObj.put("measurment_ch", cursor.getString(15));
                    allMeasurementObj.put("gps_offset_length", cursor.getString(16));
                    allMeasurementObj.put("bs_offset", cursor.getString(17));
                    allMeasurementObj.put("is_offset", cursor.getString(18));
                    allMeasurementObj.put("fs_offset", cursor.getString(19));
                    allMeasurementObj.put("n_offset", cursor.getString(20));
                    allMeasurementObj.put("e_offset", cursor.getString(21));
                    allMeasurementObj.put("l_section_offset", cursor.getString(22));
                    allMeasurementObj.put("x_section_offset", cursor.getString(23));
                    allMeasurementObj.put("rise_plus", cursor.getString(24));
                    allMeasurementObj.put("fall_minus", cursor.getString(25));
                    allMeasurementObj.put("avg_hight_of_instrument_from_gl", cursor.getString(26));
                    allMeasurementObj.put("hight_of_instrument", cursor.getString(27));
                    allMeasurementObj.put("calculated_reduce_rl", cursor.getString(28));
                    allMeasurementObj.put("checked_reduce_level", cursor.getString(29));
                    allMeasurementObj.put("remarks", cursor.getString(30));
                    allMeasurementObj.put("adj_rl", cursor.getString(31));
                    allMeasurementObj.put("adjustment_error", cursor.getString(32));
                    allMeasurementObj.put("tbm_rl", cursor.getString(33));
                    allMeasurementObj.put("bs_angle", cursor.getString(34));
                    allMeasurementObj.put("fs_angle", cursor.getString(35));
                    allMeasurementObj.put("close_photograph", cursor.getString(36));
                    allMeasurementObj.put("location_photograph", cursor.getString(37));
                    allMeasurementObj.put("screen_shot", cursor.getString(38));
                    allMeasurementObj.put("other_photograph", cursor.getString(39));
                    allMeasurementObj.put("child", cursor.getString(40));
                    allMeasurementObj.put("isparent", cursor.getString(41));
                    allMeasurementObj.put("status", cursor.getString(42));
                    allMeasurementObj.put("created_date", cursor.getString(43));
                    allMeasurementObj.put("staff_readings", getAllStaffreadings(cursor.getString(0)));

                    coordinatesArray = getAlLCoOrdinates( cursor.getString(0) );
                    projectArray = getEmpIdbyProjectId( cursor.getString(1) );
                    layerArray = getLayersbyCode( cursor.getString(3) );

                    allMeasurementObj.put("coordinates", coordinatesArray);
                    allMeasurementObj.put("projectDetails", projectArray);
                    allMeasurementObj.put("layerDetails", layerArray);
                    allMeasurementsArray.put(allMeasurementObj);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                //if( coordinatesArray.length() > 0 )

            } while (cursor.moveToNext());
        }
        cursor.close();
        return allMeasurementObj;
    }

    public JSONArray getAlLCoOrdinates( String measurementId){
        String selectQuery = "SELECT * FROM gps_coordinates WHERE measurement_id = " + measurementId;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        JSONArray latlngArray = new JSONArray();
        if (cursor.moveToFirst()) {
            do {
                JSONObject latlng = new JSONObject();
                try {
                    latlng.put("type", cursor.getString(2));
                    latlng.put("deg", cursor.getString(3));
                    latlng.put("min", cursor.getString(4));
                    latlng.put("sec", cursor.getString(5));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                latlngArray.put(latlng);
            } while (cursor.moveToNext());
        }else{
            JSONObject latlng = new JSONObject();
            JSONObject latlng1 = new JSONObject();
            try {
                latlng.put("type", "lat");
                latlng.put("deg", "");
                latlng.put("min", "");
                latlng.put("sec", "");
                latlng1.put("type", "lng");
                latlng1.put("deg", "");
                latlng1.put("min", "");
                latlng1.put("sec", "");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            latlngArray.put(latlng);
            latlngArray.put(latlng1);
        }

        cursor.close();
        return latlngArray;
    }

    public JSONArray getEmpIdbyProjectId(String id){
        String selectQuery = "SELECT  project_name, user_id FROM " + TABLE_PROJECTS + " WHERE id = " + id;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        JSONArray allMeasurementsArray = new JSONArray();
        if (cursor.moveToFirst()) {
            do {
                JSONObject projects = new JSONObject();
                try {
                    projects.put("project_name", cursor.getString(0));
                    projects.put("user_id", cursor.getString(1));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                allMeasurementsArray.put( projects );

            } while (cursor.moveToNext());
        }
        cursor.close();
        return allMeasurementsArray;
    }

    public JSONArray getLayersbyCode(String id){
        String selectQuery = "SELECT  * FROM " + TABLE_LAYERS + " WHERE code LIKE '" + id + "%'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        JSONArray allMeasurementsArray = new JSONArray();
        if (cursor.moveToFirst()) {
            do {
                JSONObject layers = new JSONObject();
                try {
                    layers.put("code", cursor.getString(0));
                    layers.put("description", cursor.getString(1));
                    layers.put("category", cursor.getString(2));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                allMeasurementsArray.put( layers );

            } while (cursor.moveToNext());
        }
        cursor.close();
        return allMeasurementsArray;
    }

    public void deleteMeasurement(String projectId){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from measurement WHERE project_id ="+ projectId);
    }

    public void deleteCookieByProjectId(String projectId){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from cookie WHERE project_id ="+ projectId);
    }

    public JSONArray getAllMeasurementCount(String projectId){
        String selectQuery = "SELECT id FROM  `measurement` WHERE project_id = " + projectId;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        JSONArray allMeasurementsArray = new JSONArray();
        if (cursor.moveToFirst()) {
            do {
                allMeasurementsArray.put( cursor.getString(0));
                //allMeasurementsArray.put(getAllStaffreadings(cursor.getString(0)));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return allMeasurementsArray;
    }

    public JSONArray getAllStaffreadings(String id){
        String selectQuery = "SELECT * FROM  `staff_readings` WHERE measurement_id = " + id;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        JSONArray allMeasurementsArray = new JSONArray();
        if (cursor.moveToFirst()) {
            do {
                JSONObject sites = new JSONObject();
                try {
                    sites.put("back_site", cursor.getString(2));
                    sites.put("intermediate_site", cursor.getString(3));
                    sites.put("forward_site", cursor.getString(4));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                allMeasurementsArray.put( sites );

            } while (cursor.moveToNext());
        }
        cursor.close();
        return allMeasurementsArray;
    }

    public void deleteMeasurementById(String id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from measurement WHERE id ="+ id);
        db.execSQL("delete from staff_readings WHERE measurement_id ="+ id);
        db.execSQL("delete from gps_coordinates WHERE measurement_id ="+ id);
    }

    public JSONArray getMeasurementByProjectIdForManual(String projectId ){
        String selectQuery= "SELECT * FROM measurement WHERE project_id = " + projectId + " AND isparent = 'P'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        JSONArray previousArray = new JSONArray();
        if(cursor.moveToFirst()) {
            do {
                JSONObject prev = new JSONObject();
                try {

                    prev.put("id", cursor.getString(0));
                    prev.put("project_id", cursor.getString(1));
                    prev.put("equipement_id", cursor.getString(2));
                    prev.put("layer_code", cursor.getString(3));
                    prev.put("lattitude", cursor.getString(4));
                    prev.put("longitude", cursor.getString(5));
                    prev.put("utm_zone", cursor.getString(6));
                    prev.put("utm_easting", cursor.getString(7));
                    prev.put("utm_northing", cursor.getString(8));
                    prev.put("angle_redians", cursor.getString(9));
                    prev.put("cs_offset_e", cursor.getString(10));
                    prev.put("cs_offset_n", cursor.getString(11));
                    prev.put("el", cursor.getString(12));
                    prev.put("mapping_ch", cursor.getString(13));
                    prev.put("ch_by_auto_level", cursor.getString(14));
                    prev.put("measurment_ch", cursor.getString(15));
                    prev.put("gps_offset_length", cursor.getString(16));
                    prev.put("bs_offset", cursor.getString(17));
                    prev.put("is_offset", cursor.getString(18));
                    prev.put("fs_offset", cursor.getString(19));
                    prev.put("n_offset", cursor.getString(20));
                    prev.put("e_offset", cursor.getString(21));
                    prev.put("l_section_offset", cursor.getString(22));
                    prev.put("x_section_offset", cursor.getString(23));
                    prev.put("rise_plus", cursor.getString(24));
                    prev.put("fall_minus", cursor.getString(25));
                    prev.put("avg_hight_of_instrument_from_gl", cursor.getString(26));
                    prev.put("hight_of_instrument", cursor.getString(27));
                    prev.put("calculated_reduce_rl", cursor.getString(28));
                    prev.put("checked_reduce_level", cursor.getString(29));
                    prev.put("remarks", cursor.getString(30));
                    prev.put("adj_rl", cursor.getString(31));
                    prev.put("adjustment_error", cursor.getString(32));
                    prev.put("tbm_rl", cursor.getString(33));
                    prev.put("bs_angle", cursor.getString(34));
                    prev.put("fs_angle", cursor.getString(35));
                    prev.put("close_photograph", cursor.getString(36));
                    prev.put("location_photograph", cursor.getString(37));
                    prev.put("screen_shot", cursor.getString(38));
                    prev.put("other_photograph", cursor.getString(39));
                    prev.put("child", cursor.getString(40));
                    prev.put("isparent", cursor.getString(41));
                    prev.put("status", cursor.getString(42));
                    prev.put("created_date", cursor.getString(43));
                    previousArray.put(prev);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
        return previousArray;
    }

    public JSONArray getPrevNextData(String mesId, String projId){
        JSONArray finalarray = new JSONArray();
        JSONObject currentData = this.getAllMeasurementByProjectId(mesId);
        JSONArray prevArray = this.getMeasurementByProjectIdForManual(projId);
        int index = 0;
        for(int i=0; i < prevArray.length(); i++) {
            try {
                JSONObject jObject = prevArray.getJSONObject(i);
                if(jObject.getString("id").equalsIgnoreCase(mesId)){
                    index = i;
                    break;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        try {
            finalarray.put(currentData);

            if( prevArray.length() - 1 == index ){
                JSONObject nextData = new JSONObject();
                finalarray.put(nextData);
            }else {
                JSONObject nextData = prevArray.getJSONObject(index + 1);
                finalarray.put(nextData);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return finalarray;
    }

    public void updateChild(String lastId, String lastIds) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("child", lastIds);

        db.update("measurement", values, "id = ?",
                new String[] { String.valueOf(lastId) });

    }

    public JSONArray getPrentChild(String measurementId){
        String selectQuery = "SELECT childId FROM parentchild WHERE parentId = '" + measurementId + "'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        String parentchild = "";
        JSONArray parentChildArray = new JSONArray();
        if (cursor.moveToFirst()) {
            do {
                parentchild += cursor.getString(0) + ",";
            } while (cursor.moveToNext());
            if(parentchild.endsWith(",")){
                parentchild = parentchild.substring(0, parentchild.length() - 1);
            }
        }
        parentChildArray.put(parentchild);
        cursor.close();
        return parentChildArray;
    }
}
