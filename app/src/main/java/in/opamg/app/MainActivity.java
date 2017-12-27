package in.opamg.app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import org.json.JSONArray;

import global.Variables;
import in.opamg.app.DatabaseConnection.DatabaseHandler;

public class MainActivity extends AppCompatActivity {
    Button levelBook, takeSurvey;
    DatabaseHandler db;
    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new DatabaseHandler(this);
        db.createBookLevelTable();
        prefs = getSharedPreferences(Variables.MY_PREFS_NAME, MODE_PRIVATE);
        String id = prefs.getString(Variables.SESSION_ID, "");


        levelBook = (Button) findViewById(R.id.levelBook);
        takeSurvey = (Button) findViewById(R.id.takeSurvey);

        levelBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, BookLevel.class);
                startActivity(intent);
            }
        });

        takeSurvey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ProjectsActivity.class);
                startActivity(intent);
            }
        });
    }
}
