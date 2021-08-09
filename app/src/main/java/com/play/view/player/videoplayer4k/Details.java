package com.play.view.player.videoplayer4k;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;
import android.util.Log;
import android.widget.TextView;

public class Details extends AppCompatActivity {
    String insert_query, select_query, delete_query;
    Cursor c;
    //DataBaseManager db;
    String filenamedb, vidid;
    TextView textView, textView2, textView3, textView4, textView5, textView6;
    String dbfilename, dbimage, dbduraton, dbtitle, dbalbum, dbartish, dbsize, dbresol, delete, deletefilename;
    SharedPreferences mSharedPrefs = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        final Intent i = getIntent();
        Bundle extras = i.getExtras();
        textView = findViewById(R.id.dbdata);
        textView2 = findViewById(R.id.dbdata2);
        textView3 = findViewById(R.id.dbdata3);
        textView4 = findViewById(R.id.dbdata4);
        textView5 = findViewById(R.id.dbdata5);
        textView6 = findViewById(R.id.dbdata6);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("vidid", "");
        editor.commit();

        if (extras != null) {
            vidid = extras.getString("vidid");

        }

        /*
        db = new DataBaseManager(getApplicationContext());
        try {
            db.createDataBase();
        } catch (IOException e) {
            e.printStackTrace();
        }
        */

        select_query = "select * from videos where title='" + vidid + "'";
        Log.e("select query", select_query);
      /*
        c = db.selectQuery(select_query);
        if (c.getCount() > 0) {
            if (c.moveToFirst() && c != null) {
                do {
                    dbfilename = c.getString(c.getColumnIndex("filename"));
                    dbimage = c.getString(c.getColumnIndex("image"));
                    dbduraton = c.getString(c.getColumnIndex("duration"));
                    dbtitle = c.getString(c.getColumnIndex("title"));
                    dbalbum = c.getString(c.getColumnIndex("album"));
                    dbartish = c.getString(c.getColumnIndex("artist"));
                    dbsize = c.getString(c.getColumnIndex("size"));
                    dbresol = c.getString(c.getColumnIndex("resol"));
                }
                    while (c.moveToNext()) ;
                }
            }

            textView.setText(dbfilename);
        textView2.setText(dbduraton);
        textView3.setText(dbtitle);
        textView4.setText(dbalbum);
        textView5.setText(dbartish);
        textView6.setText(dbsize);

        }
        */

    }
}
