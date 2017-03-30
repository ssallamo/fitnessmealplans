package nz.ac.cornell.fitnessmealplans.Dairy;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

import nz.ac.cornell.fitnessmealplans.DB.DaoCategory;
import nz.ac.cornell.fitnessmealplans.DB.DaoMealPlan;
import nz.ac.cornell.fitnessmealplans.DB.DaoMenu;
import nz.ac.cornell.fitnessmealplans.Models.MealPlan;
import nz.ac.cornell.fitnessmealplans.Models.Menu;
import nz.ac.cornell.fitnessmealplans.R;

public class MenuListActivity extends AppCompatActivity implements View.OnClickListener {

    private String userID = null;
    private String selectedDate = null;

    private LinearLayout layoutBreakfast, layoutLunch, layoutDinner;
    //private TextView tvDayTitle;
    private EditText tvBreakfast1, tvBreakfast2, tvBreakfast3
            , tvLunch1, tvLunch2, tvLunch3
            , tvDinner1, tvDinner2, tvDinner3, tvTotalCalories;
    private ImageView ivBreakfast, ivLunch, ivDinner;
    private ImageButton btnDeleteFirstBreakfast, btnDeleteSecondBreakfast, btnDeleteThirdBreakfast,
                btnDeleteFirstLunch, btnDeleteSecondLunch, btnDeleteThirdLunch,
                btnDeleteFirstDinner, btnDeleteSecondDinner, btnDeleteThirdDinner;
    private String breakfast, lunch, dinner;
    private MealPlan mealPlan;
    private String[] arrBreakfastId, arrLunchId, arrDinnerId, arrBreakfastName, arrLunchName,arrDinnerName;
    private String breakfastCategory, lunchCategory, dinnerCategory;
    private double[] arrCalories;
    private double caloires = 0;
    //private String date = "Today";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_list);

        /*Intent intent = this.getIntent();
        setUserID(intent.getExtras().getString("userID"));
        setSelectedDate(getParent().getIntent().getStringExtra("selectedDate"));*/

        Intent intent = this.getIntent();
        setUserID(intent.getExtras().getString("userID"));
        setSelectedDate(intent.getExtras().getString("selectedDate"));

        String[] date = new String[3];
        date[0] = getSelectedDate().substring(6);
        date[1] = getSelectedDate().substring(4,6);
        date[2] = getSelectedDate().substring(0,4);

        String today = date[0] +"/" + date[1] + "/" + date[2];

        if(!getToday().equals(getSelectedDate())){
            //tvDayTitle.setText("20"+getSelectedDate()+ " Meal Plan");
            setTitle(today+ " Meal Plans");
        } else {
            setTitle("Today Meal Plan");
        }
        //setTitle(date+ " Meal Plan");

        layoutBreakfast = (LinearLayout) findViewById(R.id.layoutBreakfast);
        layoutLunch = (LinearLayout) findViewById(R.id.layoutLunch);
        layoutDinner = (LinearLayout) findViewById(R.id.layoutDinner);

        ivBreakfast = (ImageView) findViewById(R.id.ivBreakfast);
        ivLunch = (ImageView) findViewById(R.id.ivLunch);
        ivDinner = (ImageView) findViewById(R.id.ivDinner);
        tvBreakfast1 = (EditText) findViewById(R.id.tvBreakfast1);
        tvBreakfast2 = (EditText) findViewById(R.id.tvBreakfast2);
        tvBreakfast3 = (EditText) findViewById(R.id.tvBreakfast3);
        tvLunch1 = (EditText) findViewById(R.id.tvLunch1);
        tvLunch2 = (EditText) findViewById(R.id.tvLunch2);
        tvLunch3 = (EditText) findViewById(R.id.tvLunch3);
        tvDinner1 = (EditText) findViewById(R.id.tvDinner1);
        tvDinner2 = (EditText) findViewById(R.id.tvDinner2);
        tvDinner3 = (EditText) findViewById(R.id.tvDinner3);
        tvTotalCalories = (EditText) findViewById(R.id.tvTotalCalories);
        btnDeleteFirstBreakfast = (ImageButton) findViewById(R.id.btnDeleteFirstBreakfast);
        btnDeleteSecondBreakfast= (ImageButton) findViewById(R.id.btnDeleteSecondBreakfast);
        btnDeleteThirdBreakfast = (ImageButton) findViewById(R.id.btnDeleteThirdBreakfast);
        btnDeleteFirstLunch = (ImageButton) findViewById(R.id.btnDeleteFirstLunch);
        btnDeleteSecondLunch= (ImageButton) findViewById(R.id.btnDeleteSecondLunch);
        btnDeleteThirdLunch = (ImageButton) findViewById(R.id.btnDeleteThirdLunch);
        btnDeleteFirstDinner = (ImageButton) findViewById(R.id.btnDeleteFirstDinner);
        btnDeleteSecondDinner = (ImageButton) findViewById(R.id.btnDeleteSecondDinner);
        btnDeleteThirdDinner = (ImageButton) findViewById(R.id.btnDeleteThirdDinner);

        layoutBreakfast.setBackgroundResource(R.drawable.layout_solid);
        layoutLunch.setBackgroundResource(R.drawable.layout_solid);
        layoutDinner.setBackgroundResource(R.drawable.layout_solid);

        tvBreakfast1.setInputType(0);
        tvBreakfast2.setInputType(0);
        tvBreakfast3.setInputType(0);
        tvLunch1.setInputType(0);
        tvLunch2.setInputType(0);
        tvLunch3.setInputType(0);
        tvDinner1.setInputType(0);
        tvDinner2.setInputType(0);
        tvDinner3.setInputType(0);

        btnDeleteFirstBreakfast.setOnClickListener(this);
        btnDeleteSecondBreakfast.setOnClickListener(this);
        btnDeleteThirdBreakfast.setOnClickListener(this);
        btnDeleteFirstLunch.setOnClickListener(this);
        btnDeleteSecondLunch.setOnClickListener(this);
        btnDeleteThirdLunch.setOnClickListener(this);
        btnDeleteFirstDinner.setOnClickListener(this);
        btnDeleteSecondDinner.setOnClickListener(this);
        btnDeleteThirdDinner.setOnClickListener(this);

        loadData();
        getData();
        changeIdtoName();
        fillData();
        setImage();
    }

    public void onAddBreakfastClicked(View v) {
        DairyActivity.flag = "breakfast";

        if(arrBreakfastId == null || arrBreakfastId.length<3) {
            Intent intent = new Intent(getParent(), ChooseMealActivity.class);
            intent.putExtra("userID", getUserID());
            intent.putExtra("selectedDate", getSelectedDate());
            intent.putExtra("calories", caloires);
            DairyActivity parentActivity = (DairyActivity) getParent();
            parentActivity.startChildActivity("ChooseMealActivity", intent);
        } else {
            Toast.makeText(this, "You already planned 3 menus", Toast.LENGTH_SHORT).show();
        }
    }

    public void onAddLunchClicked(View v) {
        DairyActivity.flag = "lunch";

        if(arrLunchId == null|| arrLunchId.length<3) {
            Intent intent = new Intent(getParent(), ChooseMealActivity.class);
            intent.putExtra("userID", getUserID());
            intent.putExtra("selectedDate", getSelectedDate());
            intent.putExtra("calories", caloires);
            DairyActivity parentActivity = (DairyActivity) getParent();
            parentActivity.startChildActivity("ChooseMealActivity", intent);
        } else {
            Toast.makeText(this, "You already planned 3 menus", Toast.LENGTH_SHORT).show();
        }
    }

    public void onAddDinnerClicked(View v) {
        DairyActivity.flag = "dinner";

        if(arrDinnerId == null || arrDinnerId.length<3) {
            Intent intent = new Intent(getParent(), ChooseMealActivity.class);
            intent.putExtra("userID", getUserID());
            intent.putExtra("selectedDate", getSelectedDate());
            intent.putExtra("calories", caloires);
            DairyActivity parentActivity = (DairyActivity) getParent();
            parentActivity.startChildActivity("ChooseMealActivity", intent);
        } else {
            Toast.makeText(this, "You already planned 3 menus", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadData() {
        /*DatabaseAccess databaseAccess = DatabaseAccess.getInstance(this);
        databaseAccess.open();
        mealPlan = databaseAccess.getMealPlan("160521", "jiyoon");
        databaseAccess.close();*/
        DaoMealPlan databaseAccess = DaoMealPlan.getInstance(this);
        databaseAccess.open();
        mealPlan = databaseAccess.getMealPlan(this.getSelectedDate(), this.getUserID());
        databaseAccess.close();
    }

    private void getData() {
        breakfast = mealPlan.getBreakfast();
        arrBreakfastId = (breakfast.equals("")) ? null : breakfast.split(",");
        lunch = mealPlan.getLunch();
        arrLunchId = (lunch.equals("")) ? null : lunch.split(",");
        dinner = mealPlan.getDinner();
        arrDinnerId = (dinner.equals("")) ? null : dinner.split(",");
    }

    private Menu getMenuName(String menuId) {
        DaoCategory databaseAccess = DaoCategory.getInstance(this);
        databaseAccess.open();
        Menu menu = databaseAccess.getMenuName(menuId);
        databaseAccess.close();
        return menu;
    }

    private void changeIdtoName() {
        Menu menu;
        if(arrBreakfastId!=null) {
            arrBreakfastName = new String[arrBreakfastId.length];
            for(int i=0; i<arrBreakfastId.length; i++) {
                menu = getMenuName(arrBreakfastId[i]);
                arrBreakfastName[i] = menu.getMenuName();
                caloires += menu.getCalories();
                //arrBreakfastName[i] = getMenuName(arrBreakfastId[i]);
            }
        }
        if(arrLunchId!=null) {
            arrLunchName = new  String[arrLunchId.length];
            for(int i=0; i<arrLunchId.length; i++) {
                menu = getMenuName(arrLunchId[i]);
                arrLunchName[i] = menu.getMenuName();
                caloires += menu.getCalories();
                //arrLunchName[i] = getMenuName(arrLunchId[i]);
            }
        }
        if(arrDinnerId!=null) {
            arrDinnerName = new String[arrDinnerId.length];
            for(int i=0; i<arrDinnerId.length; i++) {
                menu = getMenuName(arrDinnerId[i]);
                arrDinnerName[i] = menu.getMenuName();
                caloires += menu.getCalories();
                //arrDinnerName[i] = getMenuName(arrDinnerId[i]);
            }
        }
    }

    private void fillData() {
        if(arrBreakfastName!=null) {
            for(int i=0; i<arrBreakfastName.length; i++) {
                if(i==0) {
                    tvBreakfast1.setText(arrBreakfastName[i]);
                    btnDeleteFirstBreakfast.setVisibility(View.VISIBLE);
                } else if(i==1) {
                    tvBreakfast2.setText(arrBreakfastName[i]);
                    btnDeleteSecondBreakfast.setVisibility(View.VISIBLE);
                } else {
                    tvBreakfast3.setText(arrBreakfastName[i]);
                    btnDeleteThirdBreakfast.setVisibility(View.VISIBLE);
                }
            }
        }

        if(arrLunchName!=null) {
            for(int i=0; i<arrLunchName.length; i++) {
                if(i==0) {
                    tvLunch1.setText(arrLunchName[i]);
                    btnDeleteFirstLunch.setVisibility(View.VISIBLE);
                } else if(i==1) {
                    tvLunch2.setText(arrLunchName[i]);
                    btnDeleteSecondLunch.setVisibility(View.VISIBLE);
                } else {
                    tvLunch3.setText(arrLunchName[i]);
                    btnDeleteThirdLunch.setVisibility(View.VISIBLE);
                }
            }
        }

        if(arrDinnerId!=null) {
            for(int i=0; i<arrDinnerId.length; i++) {
                if(i==0) {
                    tvDinner1.setText(arrDinnerName[i]);
                    btnDeleteFirstDinner.setVisibility(View.VISIBLE);
                } else if(i==1) {
                    tvDinner2.setText(arrDinnerName[i]);
                    btnDeleteSecondDinner.setVisibility(View.VISIBLE);
                } else {
                    tvDinner3.setText(arrDinnerName[i]);
                    btnDeleteThirdDinner.setVisibility(View.VISIBLE);
                }
            }
        }

        tvTotalCalories.setText(String.valueOf(caloires));
    }
    private void setImage() {
        if(arrBreakfastId!=null) {
            breakfastCategory = getMenuCategory(arrBreakfastId[0]);
            switch(breakfastCategory) {
                case "1" :
                    ivBreakfast.setImageResource(R.drawable.fruits);
                    break;
                case "2" :
                    ivBreakfast.setImageResource(R.drawable.vegetables);
                    break;
                case "3" :
                    ivBreakfast.setImageResource(R.drawable.salads);
                    break;
                case "4" :
                    ivBreakfast.setImageResource(R.drawable.chickens);
                    break;
                case "5" :
                    ivBreakfast.setImageResource(R.drawable.eggs);
                    break;
                case "6" :
                    ivBreakfast.setImageResource(R.drawable.seafoods);
                    break;
                case "7" :
                    ivBreakfast.setImageResource(R.drawable.meats);
                    break;
                case "8" :
                    ivBreakfast.setImageResource(R.drawable.sandwiches);
                    break;
                case "9" :
                    ivBreakfast.setImageResource(R.drawable.fastfoods);
                    break;
                case "10" :
                    ivBreakfast.setImageResource(R.drawable.drinks);
                    break;
                case "11" :
                    ivBreakfast.setImageResource(R.drawable.breads);
                    break;
                case "12" :
                    ivBreakfast.setImageResource(R.drawable.beverages);
                    break;
                case "13" :
                    ivBreakfast.setImageResource(R.drawable.cereals);
                    break;
                case "14" :
                    ivBreakfast.setImageResource(R.drawable.pastas);
                    break;
                case "15" :
                    ivBreakfast.setImageResource(R.drawable.icecreams);
                    break;
                case "16" :
                    ivBreakfast.setImageResource(R.drawable.yogurts);
                    break;
                case "17" :
                    ivBreakfast.setImageResource(R.drawable.snacks);
                    break;
                default:
                    break;
            }
        }
        if(arrLunchId != null) {
            lunchCategory = getMenuCategory(arrLunchId[0]);

            switch(lunchCategory) {
                case "1" :
                    ivLunch.setImageResource(R.drawable.fruits);
                    break;
                case "2" :
                    ivLunch.setImageResource(R.drawable.vegetables);
                    break;
                case "3" :
                    ivLunch.setImageResource(R.drawable.salads);
                    break;
                case "4" :
                    ivLunch.setImageResource(R.drawable.chickens);
                    break;
                case "5" :
                    ivLunch.setImageResource(R.drawable.eggs);
                    break;
                case "6" :
                    ivLunch.setImageResource(R.drawable.seafoods);
                    break;
                case "7" :
                    ivLunch.setImageResource(R.drawable.meats);
                    break;
                case "8" :
                    ivLunch.setImageResource(R.drawable.sandwiches);
                    break;
                case "9" :
                    ivLunch.setImageResource(R.drawable.fastfoods);
                    break;
                case "10" :
                    ivLunch.setImageResource(R.drawable.drinks);
                    break;
                case "11" :
                    ivLunch.setImageResource(R.drawable.breads);
                    break;
                case "12" :
                    ivLunch.setImageResource(R.drawable.beverages);
                    break;
                case "13" :
                    ivLunch.setImageResource(R.drawable.cereals);
                    break;
                case "14" :
                    ivLunch.setImageResource(R.drawable.pastas);
                    break;
                case "15" :
                    ivLunch.setImageResource(R.drawable.icecreams);
                    break;
                case "16" :
                    ivLunch.setImageResource(R.drawable.yogurts);
                    break;
                case "17" :
                    ivLunch.setImageResource(R.drawable.snacks);
                    break;
                default:
                    break;
            }
        }
        if(arrDinnerId != null) {
            dinnerCategory = getMenuCategory(arrDinnerId[0]);

            switch(dinnerCategory) {
                case "1" :
                    ivDinner.setImageResource(R.drawable.fruits);
                    break;
                case "2" :
                    ivDinner.setImageResource(R.drawable.vegetables);
                    break;
                case "3" :
                    ivDinner.setImageResource(R.drawable.salads);
                    break;
                case "4" :
                    ivDinner.setImageResource(R.drawable.chickens);
                    break;
                case "5" :
                    ivDinner.setImageResource(R.drawable.eggs);
                    break;
                case "6" :
                    ivDinner.setImageResource(R.drawable.seafoods);
                    break;
                case "7" :
                    ivDinner.setImageResource(R.drawable.meats);
                    break;
                case "8" :
                    ivDinner.setImageResource(R.drawable.sandwiches);
                    break;
                case "9" :
                    ivDinner.setImageResource(R.drawable.fastfoods);
                    break;
                case "10" :
                    ivDinner.setImageResource(R.drawable.drinks);
                    break;
                case "11" :
                    ivDinner.setImageResource(R.drawable.breads);
                    break;
                case "12" :
                    ivDinner.setImageResource(R.drawable.beverages);
                    break;
                case "13" :
                    ivDinner.setImageResource(R.drawable.cereals);
                    break;
                case "14" :
                    ivDinner.setImageResource(R.drawable.pastas);
                    break;
                case "15" :
                    ivDinner.setImageResource(R.drawable.icecreams);
                    break;
                case "16" :
                    ivDinner.setImageResource(R.drawable.yogurts);
                    break;
                case "17" :
                    ivDinner.setImageResource(R.drawable.snacks);
                    break;
                default:
                    break;
            }
        }
    }

    private String getMenuCategory(String menuId) {
        DaoCategory databaseAccess = DaoCategory.getInstance(this);
        databaseAccess.open();
        String menuCategory = databaseAccess.getMenuCategory(menuId);
        databaseAccess.close();
        return menuCategory;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnDeleteFirstBreakfast :
                if(!tvBreakfast1.getText().equals("") || !tvBreakfast1.getText().equals("- First Menu")) {
                    deleteCurrentMeal("breakfast", 0);
                    updateMealPlan("breakfast");
                }
                break;
            case R.id.btnDeleteSecondBreakfast :
                if(!tvBreakfast2.getText().equals("") || !tvBreakfast2.getText().equals("- Second Menu")) {
                    deleteCurrentMeal("breakfast", 1);
                    updateMealPlan("breakfast");
                }
                break;
            case R.id.btnDeleteThirdBreakfast :
                if(!tvBreakfast3.getText().equals("") || !tvBreakfast3.getText().equals("- Third Menu")) {
                    deleteCurrentMeal("breakfast", 2);
                    updateMealPlan("breakfast");
                }
                break;
            case R.id.btnDeleteFirstLunch :
                if(!tvLunch1.getText().equals("") || !tvLunch1.getText().equals("- First Menu")) {
                    deleteCurrentMeal("lunch", 0);
                    updateMealPlan("lunch");
                }
                break;
            case R.id.btnDeleteSecondLunch :
                if(!tvLunch2.getText().equals("") || !tvLunch2.getText().equals("- Second Menu")) {
                    deleteCurrentMeal("lunch", 1);
                    updateMealPlan("lunch");
                }
                break;
            case R.id.btnDeleteThirdLunch :
                if(!tvLunch3.getText().equals("") || !tvLunch3.getText().equals("- Third Menu")) {
                    deleteCurrentMeal("lunch", 2);
                    updateMealPlan("lunch");
                }
                break;
            case R.id.btnDeleteFirstDinner :
                if(!tvDinner1.getText().equals("") || !tvDinner1.getText().equals("- First Menu")) {
                    deleteCurrentMeal("dinner", 0);
                    updateMealPlan("dinner");
                }
                break;
            case R.id.btnDeleteSecondDinner:
                if(!tvDinner2.getText().equals("") || !tvDinner2.getText().equals("- Second Menu")) {
                    deleteCurrentMeal("dinner", 1);
                    updateMealPlan("dinner");
                }
                break;
            case R.id.btnDeleteThirdDinner:
                if(!tvDinner3.getText().equals("") || !tvDinner3.getText().equals("- Third Menu")) {
                    deleteCurrentMeal("dinner", 2);
                    updateMealPlan("dinner");
                }
                break;
            default:
                break;
        }
        if(breakfast.equals("") && lunch.equals("") && dinner.equals("")) {
            deleteMealPlan(getSelectedDate(), getUserID());
        }
    }

    private double submissionCalory(String menuId) {
        DaoCategory databaseAccess = DaoCategory.getInstance(this);
        databaseAccess.open();
        double deletedCalory = databaseAccess.getCalory(menuId);
        databaseAccess.close();
        return deletedCalory;
    }

    private void deleteCurrentMeal(String timeFlag, int index) {
        if(timeFlag.equals("breakfast")) {
            caloires = caloires - submissionCalory(arrBreakfastId[index]);
            breakfast = "";
            arrBreakfastName[index] = "";
            arrBreakfastId[index] = "";
            for(int i=0; i<arrBreakfastId.length-1; i++) {
                if(i<arrBreakfastId.length) {
                    if(arrBreakfastId[i].equals("")) {
                        arrBreakfastId[i] = arrBreakfastId[i+1];
                        arrBreakfastId[i+1] = "";
                        arrBreakfastName[i] = arrBreakfastName[i+1];
                        arrBreakfastName[i+1] = "";
                    }
                }
            }

            for(int i=0; i<arrBreakfastId.length; i++) {
                if(i>0) {
                    if(!arrBreakfastId[i].equals("")) {
                        breakfast += ","+arrBreakfastId[i];
                    }
                } else {
                    breakfast += arrBreakfastId[i];
                }
            }

        } else if(timeFlag.equals("lunch")) {
            caloires = caloires - submissionCalory(arrLunchId[index]);
            lunch = "";
            arrLunchId[index] = "";
            arrLunchName[index] = "";
            for(int i=0; i<arrLunchId.length-1; i++) {
                if(i<arrLunchId.length) {
                    if(arrLunchId[i].equals("")) {
                        arrLunchId[i] = arrLunchId[i+1];
                        arrLunchId[i+1] = "";
                        arrLunchName[i] = arrLunchName[i+1];
                        arrLunchName[i+1] = "";
                    }
                }
            }

            for(int i=0; i<arrLunchId.length; i++) {
                if(i>0) {
                    if(!arrLunchId[i].equals("")) {
                        lunch += ","+arrLunchId[i];
                    }
                } else {
                    lunch += arrLunchId[i];
                }
            }

        } else if(timeFlag.equals("dinner")) {
            caloires = caloires - submissionCalory(arrDinnerId[index]);
            dinner = "";
            arrDinnerId[index] = "";
            arrDinnerName[index] = "";
            for(int i=0; i<arrDinnerId.length-1; i++) {
                if(i<arrDinnerId.length) {
                    if(arrDinnerId[i].equals("")) {
                        arrDinnerId[i] = arrDinnerId[i+1];
                        arrDinnerId[i+1] = "";
                        arrDinnerName[i] = arrDinnerName[i+1];
                        arrDinnerName[i+1] = "";
                    }
                }
            }

            for(int i=0; i<arrDinnerId.length; i++) {
                if(i>0) {
                    if(!arrDinnerId[i].equals("")) {
                        dinner += ","+arrDinnerId[i];
                    }
                } else {
                    dinner += arrDinnerId[i];
                }
            }

        }
    }

    private void updateMealPlan(String flag) {
        DaoMealPlan databaseAccess = DaoMealPlan.getInstance(this);
        databaseAccess.open();
        switch (flag) {
            case "breakfast" :
                databaseAccess.setMealPlan(getSelectedDate(), getUserID(), flag, breakfast, caloires);
                break;
            case "lunch" :
                databaseAccess.setMealPlan(getSelectedDate(), getUserID(), flag, lunch, caloires);
                break;
            case "dinner" :
                databaseAccess.setMealPlan(getSelectedDate(), getUserID(), flag, dinner, caloires);
                break;
            default:
                break;
        }
        databaseAccess.close();
        DairyActivity parentActivity = ((DairyActivity) getParent());
        parentActivity.onResume();
    }

    private void deleteMealPlan(String planId, String userId) {
        DaoMealPlan databaseAccess = DaoMealPlan.getInstance(this);
        databaseAccess.open();
        databaseAccess.setDeleteMealPlan(planId, userId);
        databaseAccess.close();
    }

    public String getToday(){
        SimpleDateFormat dtFormat = new SimpleDateFormat("yyyyMMdd");
        String date = dtFormat.format(new Date());
        return date;
    }

    public String getUserID() { return userID; }
    public void setUserID(String userID) { this.userID = userID; }
    public String getSelectedDate() { return selectedDate; }
    public void setSelectedDate(String selectedDate) { this.selectedDate = selectedDate; }
}
