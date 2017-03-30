package nz.ac.cornell.fitnessmealplans.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

import nz.ac.cornell.fitnessmealplans.Models.Category;
import nz.ac.cornell.fitnessmealplans.Models.Menu;
import nz.ac.cornell.fitnessmealplans.Models.Preference;

public class DaoPreference {

    private SQLiteOpenHelper openHelper;
    private SQLiteDatabase database;
    private static DaoPreference instance;


    /**
     * Private constructor to aboid object creation from outside classes.
     *
     * @param context
     */
    private DaoPreference(Context context) {
        this.openHelper = new DatabaseOpenHelper(context);
    }

    /**
     * Return a singleton instance of DatabaseAccess.
     *
     * @param context the Context
     * @return the instance of DabaseAccess
     */
    public static DaoPreference getInstance(Context context) {
        if (instance == null) {
            instance = new DaoPreference(context);
        }
        return instance;
    }

    /**
     * Open the database connection.
     */
    public void open() {
        try {
            this.database = openHelper.getWritableDatabase();
        } catch (SQLException e) {
            close();
        }
    }

    /**
     * Close the database connection.
     */
    public void close() {
        if (database != null) {
            this.database.close();
        }
    }

    public ArrayList<Preference> getPreference() {
        ArrayList<Preference> list = new ArrayList<Preference>();
        Cursor cursor = database.rawQuery("SELECT * FROM Preference", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(new Preference(cursor.getString(0), cursor.getString(1), NulltoString(cursor.getString(2))));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    private String NulltoString(String data) {
        if(data == null) {
            data = "";
        }
        return data;
    }
}

