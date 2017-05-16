package in.opamg.app.DatabaseConnection;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.opamg.app.Models.Equipments;
import in.opamg.app.Models.Layers;
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

    // Adding new Layers
    public void addLayers(Layers layers) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("code", layers.get_code());
        values.put("description", layers.get_description());
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
        String CREATE_LAYER_TABLE = "CREATE TABLE IF NOT EXISTS "+ TABLE_LAYERS +" ( code TEXT, description TEXT )";
        db.execSQL(CREATE_LAYER_TABLE);
    }

    public void createMeasurementTables(){
        SQLiteDatabase db = this.getWritableDatabase();
        String CREATE_STAFF_READINGS = "CREATE TABLE IF NOT EXISTS staff_readings ( id INT PRIMARY KEY, measurement_id INT, back_site TEXT, intermediate_site TEXT, forward_site TEXT)";
        db.execSQL(CREATE_STAFF_READINGS);

        String CREATE_GPS_COORDINATES = "CREATE TABLE IF NOT EXISTS staff_readings ( id INT PRIMARY KEY, measurement_id INT, type TEXT, deg TEXT, min TEXT, sec TEXT)";
        db.execSQL(CREATE_GPS_COORDINATES);

        String CREATE_MEASUREMENT = "CREATE TABLE IF NOT EXISTS measurement ( id INT PRIMARY KEY, project_id INT, equipement_id INT, layer_code TEXT, lattitude TEXT, longitude TEXT, utm_zone TEXT, utm_easting TEXT, utm_northing TEXT, angle_redians TEXT, cs_offset_e TEXT, cs_offset_n TEXT, el TEXT, mapping_ch TEXT, ch_by_auto_level TEXT, measurment_ch TEXT, gps_offset_length TEXT, bs_offset TEXT, is_offset TEXT, fs_offset TEXT, n_offset TEXT, e_offset TEXT, l_section_offset TEXT, x_section_offset TEXT, rise_plus TEXT, fall_minus TEXT, avg_hight_of_instrument_from_gl TEXT, hight_of_instrument TEXT, calculated_reduce_rl TEXT, checked_reduce_level TEXT, remarks TEXT, adj_rl TEXT, adjustment_error TEXT, tbm_rl TEXT, bs_angle TEXT, fs_angle TEXT, close_photograph TEXT, location_photograph TEXT, screen_shot TEXT, other_photograph TEXT, status INT, created_date TEXT )";
        db.execSQL(CREATE_MEASUREMENT);
    }

    public String getMeasurementByProjectId(int projectId ){
        String selectQuery= "SELECT * FROM measurement WHERE project_id = " + projectId + " ORDER BY id DESC LIMIT 1";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        String str = "";
        if(cursor.moveToFirst())
            str  =  cursor.getString( cursor.getColumnIndex("project_id") );
        Log.e("eeeeee", str);
        Map<String, String> map = new HashMap<String, String>();
        map.put("name", "demo");
        map.put("fname", "fdemo");
        cursor.close();
        return map.toString();
    }
}
