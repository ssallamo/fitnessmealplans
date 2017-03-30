package nz.ac.cornell.fitnessmealplans;

import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.TabHost;
import java.text.SimpleDateFormat;
import java.util.Date;
import nz.ac.cornell.fitnessmealplans.Calendar.CalendarActivity;
import nz.ac.cornell.fitnessmealplans.Dairy.DairyActivity;
import nz.ac.cornell.fitnessmealplans.Profile.ProfileActivity;
import nz.ac.cornell.fitnessmealplans.Recipe.RecipeActivity;

/**
 * Has 4 Tabs - TabHostActivity
 * Created by AJY on 2016-05-09.
 */
public class StartActivity extends TabActivity {

    public static TabHost tabHost;
    private String userID = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        Intent intent = this.getIntent();
        setUserID(intent.getExtras().getString("userID"));
        Resources res = getResources();

        tabHost = (TabHost) findViewById(android.R.id.tabhost);
        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                for(int i=0;i<tabHost.getTabWidget().getChildCount();i++)
                {
                    tabHost.getTabWidget().getChildAt(i).setBackgroundColor(Color.parseColor("#a8457c")); //unselected
                    tabHost.getTabWidget().getChildAt(i).setPadding(2,2,2,2);
                }
                tabHost.getTabWidget().getChildAt(tabHost.getCurrentTab()).setBackgroundColor(Color.parseColor("#881261")); // selected
            }
        });

        // 4 Tabs Setting
        TabHost.TabSpec tabSpec;

        tabSpec = tabHost.newTabSpec("CALENDAR");
        tabSpec.setIndicator("", res.getDrawable(R.drawable.calendar));
        Intent intentCalendar = new Intent(this, CalendarActivity.class);
        intentCalendar.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        tabSpec.setContent(intentCalendar.putExtra("userID", getUserID()));
        tabSpec.setContent(intentCalendar.putExtra("todayDate", getToday()));
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("DAIRY");
        tabSpec.setIndicator("",res.getDrawable(R.drawable.plan));
        Intent intentDairy = new Intent(this, DairyActivity.class);
        tabSpec.setContent(intentDairy.putExtra("userID", getUserID()));
        tabSpec.setContent(intentDairy.putExtra("todayDate", getToday()));
        this.getIntent().putExtra("selectedDate", getToday());
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("RECIPE");
        tabSpec.setIndicator("", res.getDrawable(R.drawable.recipe));
        Intent intentRecipe = new Intent(this, RecipeActivity.class);
        tabSpec.setContent(intentRecipe.putExtra("userID", getUserID()));
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("PROFILE");
        tabSpec.setIndicator("", res.getDrawable(R.drawable.user));
        Intent intentProfile = new Intent(this, ProfileActivity.class);
        tabSpec.setContent(intentProfile.putExtra("userID", getUserID()));
        tabHost.addTab(tabSpec);
    }

    /**
     * get current date format to YYYYMMDD
     * @return
     */
    public String getToday(){
        SimpleDateFormat dtFormat = new SimpleDateFormat("yyyyMMdd");
        String date = dtFormat.format(new Date());
        Log.d("getToday", date);
        return date;
    }

    /**
     * Tabs move to another tab
     * @param tabsIndex
     */
    public void switchTab(int tabsIndex){
        tabHost.setCurrentTab(tabsIndex);
    }

    public String getUserID() { return userID; }
    public void setUserID(String userID) { this.userID = userID; }

}