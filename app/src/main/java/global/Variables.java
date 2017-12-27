package global;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Linesh on 5/5/2017.
 */

public class Variables {
    SharedPreferences prefs;
    //public static String API_URL = "http://192.168.0.103/alok/PHP/API/index.php?todo=";
    //public static String API_URL = "http://alokmishra.co.in/sona/API/index.php?todo=";
    public static String API_URL = "http://opamg.in/sona/API/index.php?todo=";
    public static String MY_PREFS_NAME = "OPAMG";
    public static String PROJECT_ID;
    //TODO
    public static String LOGIN = "login";
    public static String PROJECTS = "projects";
    public static String EQUIPMENTS = "equipments";
    public static String LAYERS = "layers";
    public static String MEASUREMENT = "measurement";
    public static String BOOKLEVEL = "booklevel";

    //ACTION
    public static String LIST = "list";
    public static String ADD_PROJECT = "addProject";

    //SESSION
    public static String SESSION_ID = "id";
    public static String SESSION_NAME = "name";
    public static String SESSION_MOBILE = "mobile";
    public static String SESSION_EMAIL = "email_id";


}
