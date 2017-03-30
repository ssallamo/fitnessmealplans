package nz.ac.cornell.fitnessmealplans.Dairy;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.allen.expandablelistview.BaseSwipeMenuExpandableListAdapter;
import com.allen.expandablelistview.SwipeMenuExpandableCreator;
import com.allen.expandablelistview.SwipeMenuExpandableListAdapter;
import com.allen.expandablelistview.SwipeMenuExpandableListView;
import com.allen.expandablelistview.SwipeMenuExpandableListView.OnMenuItemClickListenerForExpandable;
import com.baoyz.swipemenulistview.ContentViewWrapper;
import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuItem;

import nz.ac.cornell.fitnessmealplans.DB.DaoCategory;
import nz.ac.cornell.fitnessmealplans.DB.DaoMealPlan;
import nz.ac.cornell.fitnessmealplans.DB.DatabaseOpenHelper;
import nz.ac.cornell.fitnessmealplans.Models.Category;
import nz.ac.cornell.fitnessmealplans.Models.MealPlan;
import nz.ac.cornell.fitnessmealplans.Models.Menu;
import nz.ac.cornell.fitnessmealplans.R;


public class ChooseMealActivity extends AppCompatActivity {

    private String userID = null;
    private String selectedDate = null;

    private ArrayList<Category> categoryList = new ArrayList<Category>();
    private AppAdapter mAdapter;
    private SwipeMenuExpandableListView lvMenu;
    private MealPlan mealPlan;
    private String breakfast, lunch, dinner;
    private String[] arrBreakfast, arrLunch, arrDinner;
    private double calories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_meal);
        setTitle("Menu List");

        /*Intent intent = this.getIntent();
        setUserID(intent.getExtras().getString("userID"));
        setSelectedDate(intent.getExtras().getString("selectedDate"));

        loadData();

        lvMenu = (SwipeMenuExpandableListView) findViewById(R.id.lvMenu);
        mAdapter = new AppAdapter(ChooseMealActivity.this, categoryList);
        lvMenu.setAdapter(mAdapter);


        //Set OnChildClickListener
        lvMenu.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                Category category = categoryList.get(groupPosition);
                ArrayList<Menu> tempMenuList = category.getMenuList();
                Menu menu = tempMenuList.get(childPosition);
                modifyMealPlan(menu);

                DairyActivity parentActivity = ((DairyActivity) getParent());
                parentActivity.onBackPressed();
                return false;
            }
        });

        //Create and set a SwipeMenuExpandableCreator
        //Define the group and child creator function
        SwipeMenuExpandableCreator creator = new SwipeMenuExpandableCreator() {
            @Override
            public void createGroup(SwipeMenu menu) {
            }

            @Override
            public void createChild(SwipeMenu menu) {
                // Create different menus depending on the view type

                createMenu(menu);
            }

            private void createMenu(SwipeMenu menu) {
                SwipeMenuItem item1 = new SwipeMenuItem(getApplicationContext());
                item1.setBackground(new ColorDrawable(Color.rgb(0xE5, 0xE0, 0x3F)));
                item1.setWidth(dp2px(45));
                item1.setIcon(R.drawable.ic_edit);
                menu.addMenuItem(item1);
                SwipeMenuItem item2 = new SwipeMenuItem(getApplicationContext());
                item2.setBackground(new ColorDrawable(Color.rgb(0xF9, 0x3F, 0x25)));
                item2.setWidth(dp2px(45));
                item2.setIcon(R.drawable.ic_delete);
                menu.addMenuItem(item2);
            }
        };
        lvMenu.setMenuCreator(creator);

        lvMenu.setOnMenuItemClickListener(new OnMenuItemClickListenerForExpandable() {
            @Override
            public boolean onMenuItemClick(int groupPostion, int childPostion, SwipeMenu menu, int index) {
                Category category = categoryList.get(groupPostion);
                ArrayList<Menu> menuList = category.getMenuList();
                Menu chosenMenu = menuList.get(childPostion);
                switch (index) {
                    case 0:
                        Intent intent = new Intent(getParent(), AddMenuActivity.class);
                        intent.putExtra("userID", getUserID());
                        intent.putExtra("selectedDate", getSelectedDate());
                        intent.putExtra("menuId",chosenMenu.getMenuId());

                        DairyActivity parentActivity = (DairyActivity)getParent();
                        parentActivity.startChildActivity("AddMenuActivity", intent);
                        break;
                    case 1:
                        Toast.makeText(ChooseMealActivity.this, "delete", Toast.LENGTH_SHORT).show();
                        deletePersonalMenu(chosenMenu.getMenuId());
                        mAdapter.notifyDataSetChanged();
                        break;
                }
                return false;
            }
        });*/
    }

    OnClickListener l = new OnClickListener() {
        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            mAdapter.notifyDataSetChanged(true);
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = this.getIntent();
        setUserID(intent.getExtras().getString("userID"));
        setSelectedDate(intent.getExtras().getString("selectedDate"));
        calories = intent.getExtras().getDouble("calories");

        loadData();

        lvMenu = (SwipeMenuExpandableListView) findViewById(R.id.lvMenu);
        mAdapter = new AppAdapter(ChooseMealActivity.this, categoryList);
        lvMenu.setAdapter(mAdapter);


        //Set OnChildClickListener
        lvMenu.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                Category category = categoryList.get(groupPosition);
                ArrayList<Menu> tempMenuList = category.getMenuList();
                Menu menu = tempMenuList.get(childPosition);
                modifyMealPlan(menu);

                DairyActivity parentActivity = ((DairyActivity) getParent());
                parentActivity.onBackPressed();
                return false;
            }
        });

        //Create and set a SwipeMenuExpandableCreator
        //Define the group and child creator function
        SwipeMenuExpandableCreator creator = new SwipeMenuExpandableCreator() {
            @Override
            public void createGroup(SwipeMenu menu) {
            }

            @Override
            public void createChild(SwipeMenu menu) {
                // Create different menus depending on the view type

                createMenu(menu);
            }

            private void createMenu(SwipeMenu menu) {
                SwipeMenuItem item1 = new SwipeMenuItem(getApplicationContext());
                item1.setBackground(new ColorDrawable(Color.rgb(0xE5, 0xE0, 0x3F)));
                item1.setWidth(dp2px(45));
                item1.setIcon(R.drawable.ic_edit);
                menu.addMenuItem(item1);
                SwipeMenuItem item2 = new SwipeMenuItem(getApplicationContext());
                item2.setBackground(new ColorDrawable(Color.rgb(0xF9, 0x3F, 0x25)));
                item2.setWidth(dp2px(45));
                item2.setIcon(R.drawable.ic_delete);
                menu.addMenuItem(item2);
            }
        };
        lvMenu.setMenuCreator(creator);

        lvMenu.setOnMenuItemClickListener(new OnMenuItemClickListenerForExpandable() {
            @Override
            public boolean onMenuItemClick(int groupPostion, int childPostion, SwipeMenu menu, int index) {
                Category category = categoryList.get(groupPostion);
                ArrayList<Menu> menuList = category.getMenuList();
                Menu chosenMenu = menuList.get(childPostion);
                switch (index) {
                    case 0:
                        Intent intent = new Intent(getParent(), AddMenuActivity.class);
                        intent.putExtra("userID", getUserID());
                        intent.putExtra("selectedDate", getSelectedDate());
                        intent.putExtra("menuId",chosenMenu.getMenuId());

                        DairyActivity parentActivity = (DairyActivity)getParent();
                        parentActivity.startChildActivity("AddMenuActivity", intent);
                        break;
                    case 1:
                        deletePersonalMenu(chosenMenu.getMenuId());
                        //mAdapter.notifyDataSetChanged();
                        onResume();
                        break;
                }
                return false;
            }
        });
    }

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }

    private void loadData() {
        DaoCategory databaseAccess = DaoCategory.getInstance(this);
        databaseAccess.open();
        categoryList = databaseAccess.getCategory();
        loadPersonalData(databaseAccess);
        databaseAccess.close();
    }

    private void loadPersonalData(DaoCategory databaseAccess) {
        ArrayList<Menu> menuList = new ArrayList<Menu>();
        menuList = databaseAccess.getPersonalMenu(getUserID());
        if(menuList != null) {
            categoryList.add(new Category("personal", "My Menu", menuList));
        }
    }

    private void modifyMealPlan(Menu menu) {
        String menuId = menu.getMenuId();
        calories = calories + menu.getCalories();
        loadMealPlanData();
        getData(menuId);
    }

    private void loadMealPlanData() {
        DaoMealPlan databaseAccess = DaoMealPlan.getInstance(this);
        databaseAccess.open();
        mealPlan = databaseAccess.getMealPlan(getSelectedDate(), getUserID());
        databaseAccess.close();
    }

    private void getData(String menuId) {
        switch (DairyActivity.flag) {
            case "breakfast" :
                breakfast = mealPlan.getBreakfast();
                arrBreakfast = (breakfast.equals("")) ? null : breakfast.split(",");
                if(arrBreakfast == null) {
                    breakfast += menuId;
                } else {
                    boolean addFlag = true;
                    if(arrBreakfast.length<3) {
                        for(int i=0;i<arrBreakfast.length;i++) {
                            if(menuId.equals(arrBreakfast[i])){
                                addFlag=false;
                            }
                        }
                        if(addFlag){
                            breakfast += ","+menuId;
                        } else {
                            Toast.makeText(this, "You already chose the same menu", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                updateMealPlan(DairyActivity.flag);
                break;
            case "lunch" :
                lunch = mealPlan.getLunch();
                arrLunch = (lunch.equals("")) ? null :lunch.split(",");
                if(arrLunch== null) {
                    lunch += menuId;
                } else {
                    boolean addFlag = true;
                    if(arrLunch.length<3) {
                        for(int i=0;i<arrLunch.length;i++) {
                            if(menuId.equals(arrLunch[i])){
                                addFlag=false;
                            }
                        }
                        if(addFlag){
                            lunch += ","+menuId;
                        } else {
                            Toast.makeText(this, "You already chose the same menu", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                updateMealPlan(DairyActivity.flag);
                break;
            case "dinner" :
                dinner = mealPlan.getDinner();
                arrDinner= (dinner.equals("")) ? null :dinner.split(",");
                if(arrDinner == null) {
                    dinner += menuId;
                } else {
                    boolean addFlag = true;
                    if(arrDinner.length<3) {
                        for(int i=0;i<arrDinner.length;i++) {
                            if(menuId.equals(arrDinner[i])){
                                addFlag=false;
                            }
                        }
                        if(addFlag){
                            dinner += ","+menuId;
                        } else {
                            Toast.makeText(this, "You already chose the same menu", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                updateMealPlan(DairyActivity.flag);
                break;
            default:
                break;
        }
    }

    private void updateMealPlan(String flag) {

        DaoMealPlan databaseAccess = DaoMealPlan.getInstance(this);
        databaseAccess.open();
        switch (flag) {
            case "breakfast" :

                databaseAccess.setMealPlan(getSelectedDate(), getUserID(), flag, breakfast, calories);
                break;
            case "lunch" :
                databaseAccess.setMealPlan(getSelectedDate(), getUserID(), flag, lunch,calories);
                break;
            case "dinner" :
                databaseAccess.setMealPlan(getSelectedDate(), getUserID(), flag, dinner, calories);
                break;
            default:
                break;
        }
        databaseAccess.close();
    }

    private void deletePersonalMenu(String menuId) {
        DaoCategory databaseAccess = DaoCategory.getInstance(this);
        databaseAccess.open();
        databaseAccess.setDeletePersonalMenu(menuId);
        databaseAccess.close();
    }

    @Override
    public void onBackPressed() {
        DairyActivity parent = ((DairyActivity) getParent());
        parent.onBackPressed();
    }

    public void onAddPersonalClicked (View v) {
        Intent intent = new Intent(getParent(), AddMenuActivity.class);
        intent.putExtra("userID", getUserID());
        intent.putExtra("selectedDate", getSelectedDate());
        intent.putExtra("menuId","");
        DairyActivity parentActivity = (DairyActivity)getParent();
        parentActivity.startChildActivity("AddMenuActivity", intent);
    }

    public String getUserID() { return userID; }
    public void setUserID(String userID) { this.userID = userID; }

    public String getSelectedDate() { return selectedDate; }
    public void setSelectedDate(String selectedDate) { this.selectedDate = selectedDate; }
}
