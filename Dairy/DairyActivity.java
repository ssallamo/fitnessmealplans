package nz.ac.cornell.fitnessmealplans.Dairy;


import android.app.Activity;
import android.app.ActivityGroup;
import android.app.LocalActivityManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;

import java.util.ArrayList;

public class DairyActivity extends ActivityGroup {

    private String userID = null;
    private String todayDate = null;
    private String selectedDate = null;

    public static DairyActivity group;
    private ArrayList<View> history;
    public static String flag;
    private ArrayList<String> mIdList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_dairy);
        /*if (mIdList == null) mIdList = new ArrayList<String>();
        startChildActivity("MenuListActivity", new Intent(this,MenuListActivity.class));*/
    }

    @Override
    protected void onResume() {
        super.onResume();
        /*if (mIdList == null) mIdList = new ArrayList<String>();
        startChildActivity("MenuListActivity", new Intent(this,MenuListActivity.class));*/
        Intent intent = this.getIntent();
        setUserID(intent.getExtras().getString("userID"));
        setTodayDate(intent.getExtras().getString("todayDate"));
        setSelectedDate(getParent().getIntent().getStringExtra("selectedDate"));

        /*if (mIdList == null)*/ mIdList = new ArrayList<String>();
        Intent chIntent = new Intent(this,MenuListActivity.class);
        chIntent.putExtra("userID", getUserID());
        chIntent.putExtra("selectedDate", getSelectedDate());
        startChildActivity("MenuListActivity", chIntent);

        getParent().getIntent().putExtra("selectedDate", getTodayDate());
    }

    @Override
    public void finishFromChild(Activity child) {
        LocalActivityManager manager = getLocalActivityManager();
        int index = mIdList.size()-1;

        if (index < 1) {
            finish();
            return;
        }

        manager.destroyActivity(mIdList.get(index), true);
        mIdList.remove(index);
        index--;
        String lastId = mIdList.get(index);
        Log.e("backpress lastID", lastId+", "+ index);
        Intent lastIntent = manager.getActivity(lastId).getIntent();
        Window newWindow = manager.startActivity(lastId, lastIntent);
        setContentView(newWindow.getDecorView());
    }

    public void startChildActivity(String Id, Intent intent) {
        Window window = getLocalActivityManager().startActivity(Id,intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        if (window != null) {
            mIdList.add(Id);
            setContentView(window.getDecorView());
        }
    }

    @Override
    public void onBackPressed () {
        int length = mIdList.size();
        Log.e("backpress length", ""+length);
        if ( length > 1) {
            Activity current = getLocalActivityManager().getActivity(mIdList.get(length-1));
            current.finish();
        }
    }

    public String getUserID() { return userID; }
    public void setUserID(String userID) { this.userID = userID; }

    public String getTodayDate() { return todayDate; }
    public void setTodayDate(String todayDate) { this.todayDate = todayDate; }

    public String getSelectedDate() { return selectedDate; }
    public void setSelectedDate(String selectedDate) { this.selectedDate = selectedDate; }

}
