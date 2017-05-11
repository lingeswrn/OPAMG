package in.opamg.app.DatabaseConnection;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

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

    public static String getTableProjects() {
        return TABLE_PROJECTS;
    }

    // Projects table name
    private static final String TABLE_PROJECTS = "projects";

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
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_PROJECTS + "("
                + KEY_ID + " INTEGER," + KEY_FROM + " TEXT,"
                + KEY_TO + " TEXT, " + KEY_PROJECT_NAME +" TEXT,"
                + KEY_COMPANY_NAME + " TEXT, " + KEY_WORK_ORDER_NUMBER +" TEXT,"
                + KEY_SCOPE_WORK + " TEXT, "+ KEY_REMARKS +" TEXT,"
                + KEY_USER_ID + " TEXT, " + KEY_STATUS + "TEXT,"
                + KEY_CREATED_DATE + "TEXT, "+ KEY_UPDATED_DATE +" TEXT)";
        db.execSQL(CREATE_CONTACTS_TABLE);
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


    // Adding new contact
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
}
