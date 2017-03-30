package nz.ac.cornell.fitnessmealplans.DB;

import nz.ac.cornell.fitnessmealplans.Models.MealPlan;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class DaoMealPlan {
    private SQLiteOpenHelper openHelper;
    private SQLiteDatabase database;
    private static DaoMealPlan instance;


    /**
     * Private constructor to aboid object creation from outside classes.
     *
     * @param context
     */
    private DaoMealPlan(Context context) {
        this.openHelper = new DatabaseOpenHelper(context);
    }

    /**
     * Return a singleton instance of DatabaseAccess.
     *
     * @param context the Context
     * @return the instance of DabaseAccess
     */
    public static DaoMealPlan getInstance(Context context) {
        if (instance == null) {
            instance = new DaoMealPlan(context);
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

    public MealPlan getMealPlan(String planId, String userId) {

        MealPlan mealPlan = null;
        Cursor cursor = null;
        if (getSelectMealPlan(planId, userId) == 0) {
            mealPlan = new MealPlan(planId, userId, "00", "", "", "", 0.0);
        }
        else {
            try {
                cursor = database.rawQuery("SELECT * FROM MealPlan WHERE planId='" + planId + "' AND userId = '" + userId + "'", null);
                if (cursor != null && cursor.getCount() != 0) {
                    cursor.moveToFirst();
                    mealPlan = new MealPlan(cursor.getString(0), cursor.getString(1), NulltoString(cursor.getString(2)),
                            NulltoString(cursor.getString(3)), NulltoString(cursor.getString(4)), NulltoString(cursor.getString(5)), cursor.getDouble(6));
                }
            } catch (SQLiteException e) {
                e.printStackTrace();
                Log.d("SQLException", e.getMessage());
            }
        }

        return mealPlan;
    }

    public void setMealPlan(String planId, String userId, String flag, String time, double calories) {
        Log.d("planId", planId);
        Log.d("userId", userId);
        Log.d("flag", flag);
        Log.d("time", time);
        /*
         D/planId: 160527
         D/userId: Amanda
         D/flag: lunch
         D/time: y001
         */

        ContentValues cv = new ContentValues();

        if (getSelectMealPlan(planId, userId) == 0) {
            //insert
            try {
                cv.put("planId", planId);
                cv.put("UserId", userId);
                cv.put("codeID", "00");
                cv.put("calories", calories);
                cv.put(flag, time);
                database.insert("MealPlan", null, cv);
            } catch (SQLiteException e) {
                e.printStackTrace();
                Log.d("Exception", e.getMessage());
            }
        }
        else{
            //update
            try {
                String query = String.format("UPDATE MealPlan " +
                        " SET " + flag + " = '" + time +"', calories =" + calories +" " +
                        " WHERE planId ='" + planId + "' AND UserId ='" + userId + "';");
                Log.d("updateMealPlan : ", query);
                database.execSQL(query);

            } catch (SQLiteException e) {
                e.printStackTrace();
                Log.d("Exception", e.getMessage());
            }
        }
    }

    private String NulltoString(String data) {
        if(data == null) {
            data = "";
        }
        return data;
    }

    public int getSelectMealPlan(String planId, String userId){
        Cursor cursor = null;
        int resultCnt=0;
        try {
            cursor = database.rawQuery("SELECT count(*) FROM MealPlan WHERE planId='" + planId + "' AND userId = '" + userId + "'", null);
            if (cursor != null && cursor.getCount() != 0) {
                cursor.moveToFirst();
                resultCnt = Integer.parseInt(cursor.getString(0));
                Log.d("MealPlanCount(*) ", cursor.getString(0));
            }
        }
        catch (SQLiteException e) {
            e.printStackTrace();
            Log.d("SQLException", e.getMessage());
        }
        cursor.close();
        return resultCnt;
    }


    public int getSelectDairyCalorie(String planId, String userId){
        Cursor cursor = null;
        int resultCnt=0;
        try {

            cursor = database.rawQuery("SELECT (A.Calories-B.Dailycalorie) FROM MEALPLAN A, USER B WHERE A.PlanId ='" + planId + "' AND A.UserId = '" + userId + "' AND A.Userid = B.UserId", null);
           // cursor = database.rawQuery("SELECT count(*) FROM MealPlan WHERE planId='" + planId + "' AND userId = '" + userId + "'", null);
            if (cursor != null && cursor.getCount() != 0) {
                cursor.moveToFirst();
                resultCnt = Integer.parseInt(cursor.getString(0));
                Log.d("A.Calo-B.Dailycal", cursor.getString(0));
            }
        }
        catch (SQLiteException e) {
            e.printStackTrace();
            Log.d("SQLException", e.getMessage());
        }
        cursor.close();
        return resultCnt;
    }

    public void setDeleteMealPlan(String planId, String userId) {
        database.delete("MealPlan", "planId = ? AND userId = ?", new String[] {planId, userId});
        database.close();
    }


}
