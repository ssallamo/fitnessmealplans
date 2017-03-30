package nz.ac.cornell.fitnessmealplans.Calendar;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TabHost;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Calendar;

import nz.ac.cornell.fitnessmealplans.DB.DaoMealPlan;
import nz.ac.cornell.fitnessmealplans.Models.DayInfo;
import nz.ac.cornell.fitnessmealplans.R;
import nz.ac.cornell.fitnessmealplans.StartActivity;

/**
 * Shows monthly plans
 * Created by HJS on 2016-05-08.
 */
public class CalendarActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, View.OnClickListener {
    private String userID = null;

    private int pDaysIndex = 0;
    private int nDaysIndex = 0;
    public static int SUNDAY = 1;

    private TextView tvCalendarTitle;
    private GridView gvCalandarView;
    private Button btnPrevious, btnNext;

    private ArrayList<DayInfo> arrListDays;
    private CalendarAdapter calendarAdapter;

    private Calendar thisMonthCalendar;
    private TabHost TabHostWindow;

    private ArrayList<String> mIdList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        //gets userID
        Intent intent = getIntent();
        setUserID(intent.getExtras().getString("userID"));
        setTitle("Monthly Plans");

        // Create Calandar
        // previous, next Button
        btnPrevious = (Button) findViewById(R.id.btnPrevious);
        btnNext = (Button) findViewById(R.id.btnNext);
        tvCalendarTitle = (TextView) findViewById(R.id.tvCalendarTitle);
        gvCalandarView = (GridView) findViewById(R.id.gvCalandarView);
        btnPrevious.setOnClickListener(this);
        btnNext.setOnClickListener(this);

        gvCalandarView.setOnItemClickListener(this);
        arrListDays = new ArrayList<DayInfo>();
        TabHostWindow = (TabHost) findViewById(android.R.id.tabhost);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Create this month instance
        thisMonthCalendar = Calendar.getInstance();
        thisMonthCalendar.set(Calendar.DAY_OF_MONTH, 1);
        getCalendar(thisMonthCalendar);
    }

    /**
     * Setting up the calendar.
     * @param calendar the shown month Calendar
     */
    private void getCalendar(Calendar calendar) {
        int lastMonthStartDay;
        int dayOfMonth;
        int thisMonthLastDay;

        //Seleteted month get year and month
        String tmpYm = String.valueOf(thisMonthCalendar.get(Calendar.YEAR))+ String.format("%02d", thisMonthCalendar.get(Calendar.MONTH) + 1);
        arrListDays.clear();

        // find the day what is the starting day of this month.
        // in case of Sunday starting day is changing index to 1
        dayOfMonth = calendar.get(Calendar.DAY_OF_WEEK);
        thisMonthLastDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        calendar.add(Calendar.MONTH, -1);
        // get the last date of last month
        lastMonthStartDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        calendar.add(Calendar.MONTH, 1);
        if (dayOfMonth == SUNDAY) dayOfMonth += 7;
        lastMonthStartDay -= (dayOfMonth - 1) - 1;
        setTvCalendarTitle(thisMonthCalendar.get(Calendar.MONTH), thisMonthCalendar.get(Calendar.YEAR));

        pDaysIndex = dayOfMonth;
        nDaysIndex = lastMonthStartDay;
        //Setting daily up
        DayInfo day;
        for (int i = 0; i < dayOfMonth - 1; i++) {
            int date = lastMonthStartDay + i;
            day = new DayInfo();
            day.setDate(Integer.toString(date)); //date
            day.setbMonth(false); //invalid
            day.setHavingPlan(false); //havingPlan
            arrListDays.add(day);
        }

        for (int i = 1; i <= thisMonthLastDay; i++) {
            day = new DayInfo();
            day.setDate(Integer.toString(i));
            day.setbMonth(true);
            day.setHavingPlan(getDBmealPlans(userID, tmpYm, i));
            day.setUpDown(getDailyCal(userID, tmpYm, i)); //over_calorie or less_Calorie
            arrListDays.add(day);
        }
        for (int i = 1; i < 42 - (thisMonthLastDay + dayOfMonth - 1) + 1; i++) {
            day = new DayInfo();
            day.setDate(Integer.toString(i));
            day.setbMonth(false);
            day.setHavingPlan(false);
            arrListDays.add(day);
        }
        initCalendarAdapter();//Calendar init
    }

    /**
     * Return Last Calendar Object
     * @param calendar
     * @return LastMonthCalendar
     */
    private Calendar getLastMonth(Calendar calendar) {
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), 1);
        calendar.add(Calendar.MONTH, -1);
        setTvCalendarTitle(thisMonthCalendar.get(Calendar.MONTH), thisMonthCalendar.get(Calendar.YEAR));
        return calendar;
    }

    /**
     * retun next month Calendar Object
     * @param calendar
     * @return NextMonthCalendar
     */
    private Calendar getNextMonth(Calendar calendar) {
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), 1);
        calendar.add(Calendar.MONTH, +1);
        setTvCalendarTitle(thisMonthCalendar.get(Calendar.MONTH), thisMonthCalendar.get(Calendar.YEAR));
        return calendar;
    }

    /**
     * when date click , event
     * moves to dairy tab
     * @param parent
     * @param v
     * @param position
     * @param arg3
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View v, int position, long arg3) {
        String year = String.format("%4d", thisMonthCalendar.get(Calendar.YEAR));
        String month = String.format("%02d", thisMonthCalendar.get(Calendar.MONTH) + 1);
        TextView tmpStr = (TextView) v.findViewById(R.id.tvDate);
        String date = String.format("%02d", Integer.parseInt(tmpStr.getText().toString()));
        setMoveToTab(1, year+month+date );//go to dairyActivity
    }

    /**
     * click previous, next month
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnPrevious:
                thisMonthCalendar = getLastMonth(thisMonthCalendar);
                getCalendar(thisMonthCalendar);
                break;
            case R.id.btnNext:
                thisMonthCalendar = getNextMonth(thisMonthCalendar);
                getCalendar(thisMonthCalendar);
                break;
        }
    }

    /**
     * Draw calendar
     */
    private void initCalendarAdapter() {
        calendarAdapter = new CalendarAdapter(this, R.layout.day, arrListDays);
        gvCalandarView.setAdapter(calendarAdapter);
    }

    /**
     * setting Calendar Tittle TextView
     * @param month
     * @param year
     */
    public void setTvCalendarTitle(int month, int year) {
        // Calendar tittle setting
        String tmpTitle = null;
        switch (month + 1) {
            case 1:  tmpTitle = "Jan. " + year; break;
            case 2:  tmpTitle = "Feb. " + year; break;
            case 3:  tmpTitle = "Mar. " + year; break;
            case 4:  tmpTitle = "Apr. " + year; break;
            case 5:  tmpTitle = "May. " + year; break;
            case 6:  tmpTitle = "Jun. " + year; break;
            case 7:  tmpTitle = "Jul. " + year; break;
            case 8:  tmpTitle = "Aug. " + year; break;
            case 9:  tmpTitle = "Sep. " + year; break;
            case 10: tmpTitle = "Oct. " + year; break;
            case 11: tmpTitle = "Nov. " + year; break;
            case 12: tmpTitle = "Dec. " + year; break;
        }
        tvCalendarTitle.setText(tmpTitle);
    }

    /**
     * Fetch the meal plans of the day
     * @param userId
     * @param yyyymm
     * @param date
     * @return
     */
    public boolean getDBmealPlans(String userId, String yyyymm, int date) {
        boolean result = false;
        String tmpYmd = yyyymm + String.format("%02d", date);
        DaoMealPlan databaseAccess = DaoMealPlan.getInstance(this);
        databaseAccess.open();
        if (databaseAccess.getSelectMealPlan(tmpYmd, userId) > 0) result = true;
        else result = false;
        databaseAccess.close();
        return result;
    }

    /**
     * fetch Daily needed calorie and planed calorie
     * @param userId
     * @param yyyymm
     * @param date
     * @return
     */
    public boolean getDailyCal(String userId, String yyyymm, int date) {
        boolean result = false;
        String tmpYmd = yyyymm + String.format("%02d", date);
        DaoMealPlan databaseAccess = DaoMealPlan.getInstance(this);
        databaseAccess.open();
        if (databaseAccess.getSelectDairyCalorie(tmpYmd, userId) > 0) result = true;
        else result = false;
        databaseAccess.close();
        return result;
    }

    /**
     * when day click , move from the monthly tab to dairy tab
     * @param index
     * @param selectedDt
     */
    public void setMoveToTab(int index, String selectedDt){
        StartActivity startActivity = (StartActivity) this.getParent();
        startActivity.getIntent().putExtra("selectedDate", selectedDt);
        startActivity.switchTab(index);
    }

    public String getUserID() { return userID; }
    public void setUserID(String userID) { this.userID = userID; }
}
