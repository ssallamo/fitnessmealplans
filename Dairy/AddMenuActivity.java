package nz.ac.cornell.fitnessmealplans.Dairy;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;

import nz.ac.cornell.fitnessmealplans.DB.DaoCategory;
import nz.ac.cornell.fitnessmealplans.DB.DaoPreference;
import nz.ac.cornell.fitnessmealplans.Models.Category;
import nz.ac.cornell.fitnessmealplans.Models.Menu;
import nz.ac.cornell.fitnessmealplans.Models.Preference;
import nz.ac.cornell.fitnessmealplans.R;

public class AddMenuActivity extends AppCompatActivity {

    private String userID = null;
    private String selectedDate = null;
    private String menuId = null;
    private EditText tvName, tvCalory, tvAmount;
    private Button btnAddMyMenu;
    private Spinner spCategory, spType;
    private ArrayList<Category> categoryList;
    private ArrayList<Preference> codeList;
    private String[] category, code;
    private String categoryId, codeId;
    private Menu menu;
    private String flag = "add";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_menu);

        Intent intent = this.getIntent();
        setUserID(intent.getExtras().getString("userID"));
        setSelectedDate(intent.getExtras().getString("selectedDate"));
        menuId = intent.getExtras().getString("menuId");

        tvName = (EditText) findViewById(R.id.tvName);
        tvAmount = (EditText) findViewById(R.id.tvAmount);
        spType = (Spinner) findViewById(R.id.spType);
        spCategory = (Spinner) findViewById(R.id.spCategory);
        tvCalory = (EditText) findViewById(R.id.tvCalory);
        btnAddMyMenu = (Button) findViewById(R.id.btnAddMyMenu);

        getSpinners();
        makeSpinnerOption();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, category);
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spCategory.setAdapter(adapter);

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, code);
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spType.setAdapter(adapter);

        if(!menuId.equals("")) {
            setTitle("Edit My Menu");
            flag = "update";
            loadData();
            tvName.setText(menu.getMenuName());
            tvAmount.setText(menu.getAmount());
            tvCalory.setText(String.valueOf(menu.getCalories()));
            spType.setSelection(Integer.parseInt(menu.getCodeId())-1);
            spCategory.setSelection(Integer.parseInt(menu.getCategoryId())-1);
        } else {
            setTitle("Add My Menu");
        }

        switch(flag) {
            case "add" :
                btnAddMyMenu.setText("Add My Menu");
                break;
            case "update" :
                btnAddMyMenu.setText("Edit My Menu");
                break;
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        DairyActivity parent = ((DairyActivity) getParent());
        parent.onBackPressed();
    }

    public void onAddMyMenuClicked(View v) {
        SaveDataThread savedata = new SaveDataThread();
        savedata.execute();
        /*getCategoryId();
        getCodeId();
        saveData();*/
        /*DairyActivity parentActivity = ((DairyActivity) getParent());
        parentActivity.onBackPressed();*/

    }

    private void saveData() {
        /*DatabaseAccess databaseAccess = DatabaseAccess.getInstance(this);
        databaseAccess.open();
        databaseAccess.setPersonalMenu(tvName.getText().toString(), tvAmount.getText().toString(),
                                        Double.parseDouble(tvCalory.getText().toString()), codeId, categoryId, "jiyoon");
        databaseAccess.close();*/
        DaoCategory databaseAccess = DaoCategory.getInstance(this);
        databaseAccess.open();
        if(flag.equals("add")) {
            String menuID = databaseAccess.getPersonalMenuId();
            menuID = makeNewId(menuID);
            databaseAccess.setPersonalMenu(menuID, tvName.getText().toString(), tvAmount.getText().toString(),
                    Double.parseDouble(tvCalory.getText().toString()), codeId, categoryId, getUserID());
        } else if(flag.equals("update")) {
            databaseAccess.updatePersonalMenu(menuId, tvName.getText().toString(), tvAmount.getText().toString(),
                    Double.parseDouble(tvCalory.getText().toString()), codeId, categoryId, getUserID());
        }
        databaseAccess.close();
    }

    private void getSpinners() {
        /*DatabaseAccess databaseAccess = DatabaseAccess.getInstance(this);
        databaseAccess.open();
        categoryList = databaseAccess.getSimpleCategory();
        codeList = databaseAccess.getPreference();
        databaseAccess.close();*/
        getCategorySpinner();
        getPreferenceSpinner();
    }

    /*
    Dao를 분리해서 함수도 두 개로 쪼갬
    getSpinner -> getCategorySpinner, getPreferenceSpinner
     */

    private void getCategorySpinner() {
        DaoCategory databaseAccess = DaoCategory.getInstance(this);
        databaseAccess.open();
        categoryList = databaseAccess.getSimpleCategory();
        databaseAccess.close();
    }

    private void getPreferenceSpinner() {
        DaoPreference databaseAccess = DaoPreference.getInstance(this);
        databaseAccess.open();
        codeList = databaseAccess.getPreference();
        databaseAccess.close();
    }

    private void makeSpinnerOption() {
        category = new String[categoryList.size()];
        for(int i=0; i<categoryList.size(); i++) {
            category[i] = categoryList.get(i).getCategoryName();
        }

        code = new String[codeList.size()];
        for(int i=0; i<codeList.size(); i++) {
            code[i] = codeList.get(i).getCodeName();
        }
    }

    private void getCategoryId() {
        categoryId = categoryList.get(this.spCategory.getSelectedItemPosition()).getCategoryId();
    }

    private void getCodeId() {
        codeId = codeList.get(this.spType.getSelectedItemPosition()).getCodeId();
    }

    private String makeNewId(String menuID) {
        if(menuID != null) {
            menuID = menuID.substring(1);
            int index = Integer.parseInt(menuID);
            index++;
            menuID = "p" + String.valueOf(index);
        } else {
            menuID = "p1";
        }
        return menuID;
    }

    private void loadData() {
        DaoCategory databaseAccess = DaoCategory.getInstance(this);
        databaseAccess.open();
        menu = databaseAccess.getChosenPersonalMenu(menuId);
        databaseAccess.close();
    }

    public String getUserID() { return userID; }
    public void setUserID(String userID) { this.userID = userID; }

    public String getSelectedDate() { return selectedDate; }
    public void setSelectedDate(String selectedDate) { this.selectedDate = selectedDate; }

    class SaveDataThread extends AsyncTask<Void, Void, Void> {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = ProgressDialog.show(getParent(), "Fitness Meal Plan", "Saving Your Menu");
        }

        @Override
        protected Void doInBackground(Void... params) {
            getCategoryId();
            getCodeId();
            saveData();
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
            /*getCategoryId();
            getCodeId();
            saveData();*/
            /*DairyActivity parentActivity = ((DairyActivity) getParent());
            parentActivity.onBackPressed();*/
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            dialog.dismiss();
            DairyActivity parentActivity = ((DairyActivity) getParent());
            parentActivity.onBackPressed();
        }
    }
}
