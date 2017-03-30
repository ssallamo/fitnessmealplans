package nz.ac.cornell.fitnessmealplans.DB;

import nz.ac.cornell.fitnessmealplans.Models.User;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DaoUser {

    private SQLiteOpenHelper openHelper;
    private SQLiteDatabase database;
    private static DaoUser instance;


    /**
     * Private constructor to aboid object creation from outside classes.
     * @param context
     */
    private DaoUser(Context context) {
        this.openHelper = new DatabaseOpenHelper(context);
    }

    /**
     * Return a singleton instance of DatabaseAccess.
     * @param context the Context
     * @return the instance of DabaseAccess
     */
    public static DaoUser getInstance(Context context) {
        if (instance == null) {
            instance = new DaoUser(context);
        }
        return instance;
    }

    /**
     * Open the database connection.
     */
    public void open() {
        try {
            this.database = openHelper.getWritableDatabase();
        } catch (SQLiteException e) {
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

    /**
     * Return user Password if there is collect matching user
     * @param userId
     * @return userPw or NON
     */
    public String getPassword(String userId) {
        String resultMsg = null;
        Cursor cursor = null;
        if (getUserRawCount(userId) == 0) {
            resultMsg = "NON";
        }
        else {
            try {
                cursor = database.rawQuery("SELECT UserPw FROM User WHERE UserID='" + userId + "'", null);
                if (cursor != null && cursor.getCount() != 0) {
                    cursor.moveToFirst();
                    resultMsg = cursor.getString(0);
                    Log.d("UserID ", userId);
                    Log.d("UserPw ", resultMsg);
                }
            } catch (SQLiteException e) {
                e.printStackTrace();
                Log.d("SQLException", e.getMessage());
                resultMsg = null;
            }
        }
        return resultMsg;
    }

    /**
     * uses at Create user activity
     * New user saves into Database
     * @param aUser user information
     * @return true or false
     */
    public boolean insertUser(User aUser)
    {
        Log.d("USER_ID" , aUser.getUserID());
        Log.d("USER_PW" , aUser.getUserPW());
        Log.d("CODE_ID" , aUser.getCodeID());

        boolean result = true;
        // Checking db before inserting
        if (getUserRawCount(aUser.getUserID()) > 0) {
            result = false;
        }
        else {
            ContentValues cv = new ContentValues();
            try {
                cv.put("UserID", aUser.getUserID());
                cv.put("UserPw", aUser.getUserPW());
                cv.put("CodeID", aUser.getCodeID());
                cv.put("Age", String.valueOf(aUser.getAge()));
                cv.put("Height", aUser.getHeight());
                cv.put("HeightCD", aUser.getHeightCD());
                cv.put("Weight", aUser.getWeight());
                cv.put("WeightCD", aUser.getWeightCD());
                cv.put("GenderCD", aUser.getGenderCD());
                cv.put("ExerciseCD", aUser.getExerciseCD());
                cv.put("DailyCalorie", aUser.getDailyCalorie());
                database.insert("User", null, cv);
            } catch (SQLiteException e) {
                e.printStackTrace();
                Log.d("Exception", e.getMessage());
                result = false;
            }
        }
        return result;
    }

    /**
     * Delete User Information
     * @param aUser
     * @return true or false
     */
    public boolean deleteUser(User aUser)
    {
        Log.d("USER_ID" , aUser.getUserID());
        Log.d("USER_PW" , aUser.getUserPW());
        Log.d("CODE_ID" , aUser.getCodeID());

        boolean result = true;
        // Checking db before inserting
        if (getUserRawCount(aUser.getUserID()) < 1) {
            result = false;
        }
        else {
            try {
                database.delete("User", "userID = ?", new String[] { aUser.getUserID() });
            } catch (SQLiteException e) {
                e.printStackTrace();
                Log.d("SQLException", e.getMessage());
                result = false;
            }
        }
        return result;
    }

    /**
     * Selects one User Information from Database
     * @param userId
     * @return User
     */
    public User getSelectOneUser(String userId){

        User aUser = null;
        Cursor cursor = null;
        if (getUserRawCount(userId) < 1){
            Log.d("getSelectOneUser : ", userId + "was not found.");
        }
        else{
            try{
                String query = String.format("SELECT UserID,CodeID,Age,Height,HeightCD,Weight,WeightCD,GenderCD,ExerciseCD,DailyCalorie" +
                        " From User Where UserID ='" + userId + "'");
                Log.d("selectUser : ", query);

                cursor = database.rawQuery(query, null);
                if (cursor!=null && cursor.getCount()!=0) {
                    cursor.moveToFirst();
                    //assign to aUser
                    aUser = new User(cursor.getString(0), "", cursor.getString(1),
                            cursor.getInt(2), cursor.getDouble(3), cursor.getString(4),
                            cursor.getDouble(5), cursor.getString(6), cursor.getString(7), cursor.getString(8),
                            cursor.getInt(9));
                }
            }
            catch (SQLiteException e) {
                e.printStackTrace();
                Log.d("SQLException", e.getMessage());
            }
        }
        return aUser;
    }

    /**
     * When user update their new setting
     * @param aUser
     * @return
     */
    public boolean setUpdateUser(User aUser) {
        boolean result = false;
        Cursor cursor = null;
        if (getUserRawCount(aUser.getUserID()) < 1){
            Log.d("getSelectOneUser : ", aUser.getUserID() + "was not found.");
            result = false;
        }
        else{
            try{
                String query = String.format("UPDATE User " +
                        "SET CodeID ='"+ aUser.getCodeID() +
                        "', " + "Age = " + aUser.getAge() +
                        " , " + "Height = " + aUser.getHeight() +
                        " , " + "HeightCD = '" + aUser.getHeightCD() +
                        "', " + "Weight = " + aUser.getWeight() +
                        " , " + "WeightCD = '" + aUser.getWeightCD() +
                        "', " + "GenderCD = '" + aUser.getGenderCD() +
                        "', " + "ExerciseCD = '" + aUser.getExerciseCD() +
                        "', " + "DailyCalorie = " + aUser.getDailyCalorie() +
                        " WHERE UserId ='" + aUser.getUserID() + "';");
                Log.d("updateUser : ", query);
                database.execSQL(query);
                result = true;
            }
            catch (SQLiteException e) {
                e.printStackTrace();
                Log.d("SQLException", e.getMessage());
                result = false;
            }
        }
        return result;
    }

    /**
     * Return user Password if there is collect matching user
     * @param userId
     * @return userPw or NON
     */
    public String getUserCodeID(String userId) {

        String resultMsg = null;
        Cursor cursor = null;
        try {
            cursor = database.rawQuery("SELECT CodeID FROM User WHERE UserID='" + userId + "'", null);
            if (cursor != null && cursor.getCount() != 0) {
                cursor.moveToFirst();
                resultMsg = cursor.getString(0);
                Log.d("UserID ", userId);
                Log.d("CodeID ", resultMsg);
            }
        } catch (SQLiteException e) {
            e.printStackTrace();
            Log.d("SQLException", e.getMessage());
            resultMsg = null;
        }
        return resultMsg;
    }

    /**
     * for searing user count.
     * if 0, new user, 1, registered user
     * @param userId
     * @return
     */
    public int getUserRawCount(String userId){

        Cursor cursor = null;
        int resultCnt=0;
        try {
            cursor = database.rawQuery("SELECT count(*) FROM User WHERE UserID='" + userId + "'", null);
            if (cursor != null && cursor.getCount() != 0) {
                cursor.moveToFirst();
                resultCnt = Integer.parseInt(cursor.getString(0));
                Log.d("count(*) ", cursor.getString(0));
            }
        }
        catch (SQLiteException e) {
            e.printStackTrace();
            Log.d("SQLException", e.getMessage());
        }
        return resultCnt;
    }





}
