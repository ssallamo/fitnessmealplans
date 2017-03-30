package nz.ac.cornell.fitnessmealplans.Profile;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import nz.ac.cornell.fitnessmealplans.DB.DaoUser;
import nz.ac.cornell.fitnessmealplans.MainActivity;
import nz.ac.cornell.fitnessmealplans.Models.User;
import nz.ac.cornell.fitnessmealplans.R;

/**
 * Sets User information.- update
 * logout
 * Created by HJS on 2016-05-13.
 */
public class ProfileActivity extends AppCompatActivity implements View.OnTouchListener{

    private String userID=null;

    //private TextView tvUserID;
    private EditText etAge, etHeightD, etHeightF, etWeightD, etWeightF, etCalorie;
    private RadioButton rbFemale, rbMale;
    private Spinner spWeight, spHeight, spExercise, spType;

    String[] type = {"Normal", "Vegetarian", "Diet"};
    String[] height = {"cm", "feet"};
    String[] weight = {"kg", "lbs"};
    String[] exercise = {"Little or no Exercise","Working Out:3-5days/week  ", "Working Out:6-7days/week"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Intent intent = this.getIntent();
        setUserID(intent.getExtras().getString("userID"));
        setTitle("Setting < " + getUserID() + " >");

        //tvUserID.setText(getUserID());
        //tvUserID = (TextView) findViewById(R.id.tvUserID);
        spType = (Spinner) findViewById(R.id.spTypeID);
        etAge = (EditText) findViewById(R.id.etAge);
        etHeightD = (EditText) findViewById(R.id.etHeightD);
        etHeightF = (EditText) findViewById(R.id.etHeightF);
        spHeight = (Spinner) findViewById(R.id.spHeight);
        etWeightD = (EditText) findViewById(R.id.etWeightD);
        etWeightF = (EditText) findViewById(R.id.etWeightF);
        spWeight = (Spinner) findViewById(R.id.spWeight);
        rbFemale = (RadioButton) findViewById(R.id.rbFemale);
        rbMale = (RadioButton) findViewById(R.id.rbMale);
        spExercise = (Spinner) findViewById(R.id.spExercise);
        etCalorie = (EditText) findViewById(R.id.etCalories);
        //Touch Event handling
        etAge.setOnTouchListener(this);
        etHeightD.setOnTouchListener(this);
        etHeightF.setOnTouchListener(this);
        etWeightD.setOnTouchListener(this);
        etWeightF.setOnTouchListener(this);

        //type normal, vege, diet
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, type);
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spType.setAdapter(adapter);

        //height feet or cm
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, height);
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spHeight.setAdapter(adapter);

        //weight kg or lbs
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, weight);
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spWeight.setAdapter(adapter);

        //exercise
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, exercise);
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spExercise.setAdapter(adapter);

        //Fetch User information from the db and Display on screen
        if(getUserID()!=null){
            User auser = new User();
            auser = selectAUser(getUserID());
            if(auser!=null) setDisplay(auser);
        }
    }

    /**
     * Calls DAO select user information
     * @param userId
     * @return user
     */
    public User selectAUser(String userId){
        User aUser = new User();
        DaoUser databaseAccess = DaoUser.getInstance(this);
        databaseAccess.open();
        aUser = databaseAccess.getSelectOneUser(userId);
        databaseAccess.close();
        return aUser;
    }

    /**
     * Updates User information
     * @param v
     */
    public void btnSave(View v) {

        String userId = getUserID();

        //userType
        String codeID = "" + (this.spType.getSelectedItemPosition()+1);

        //age
        EditText etAge = (EditText) findViewById(R.id.etAge);
        if(etAge.getText().toString().equals("")){
            Toast.makeText(this, "Please Enger Your Age.." , Toast.LENGTH_SHORT).show();
            etAge.requestFocus();
            return;
        }
        int age = Integer.parseInt(etAge.getText().toString());

        //Height
        EditText etHeightd = (EditText) findViewById(R.id.etHeightD);
        EditText etHeightf = (EditText) findViewById(R.id.etHeightF);
        if(etHeightd.getText().toString().equals("")){
            Toast.makeText(this, "Please Enter Your Height properly.." , Toast.LENGTH_SHORT).show();
            etHeightd.requestFocus();
            return;
        }
        if(etHeightf.getText().toString().equals("")){
            Toast.makeText(this, "Please Enter Your Height properly.." , Toast.LENGTH_SHORT).show();
            etHeightf.requestFocus();
            return;
        }
        int heightd = Integer.parseInt(etHeightd.getText().toString());
        int heightf = Integer.parseInt(etHeightf.getText().toString());
        Double height = heightd + (double)(heightf*0.1);
        String heightCD = "0" + this.spHeight.getSelectedItemPosition();

        //Weight
        EditText etWeightd = (EditText) findViewById(R.id.etWeightD);
        EditText etWeightf = (EditText) findViewById(R.id.etWeightF);
        if(etWeightd.getText().toString().equals("")){
            Toast.makeText(this, "Please Enter Your Height properly.." , Toast.LENGTH_SHORT).show();
            etWeightd.requestFocus();
            return;
        }
        if(etWeightf.getText().toString().equals("")){
            Toast.makeText(this, "Please Enter Your Height properly.." , Toast.LENGTH_SHORT).show();
            etWeightf.requestFocus();
            return;
        }
        int weightd = Integer.parseInt(etWeightd.getText().toString());
        int weightf = Integer.parseInt(etWeightf.getText().toString());
        Double weight = weightd + (double)(weightf*0.1);
        String weightCD = "0" + this.spWeight.getSelectedItemPosition();

        //gender
        RadioGroup rg = (RadioGroup) findViewById(R.id.genderRG);
        RadioButton rd = (RadioButton) findViewById(rg.getCheckedRadioButtonId());
        String genderCD = (rd.getText().equals("Female") ? "0" : "1");

        //exerciseCD
        String exerciseCD = "0" + this.spExercise.getSelectedItemPosition();

        //to calculate
        int dailyCalorie = calDailyCalorie(age, height, heightCD, weight, weightCD, genderCD, exerciseCD);

        EditText etCalorie = (EditText) findViewById(R.id.etCalories);
        etCalorie.setText(String.valueOf(dailyCalorie));

        User aUser = new User(userId,"",codeID,age,height,heightCD,weight,weightCD,genderCD,exerciseCD,dailyCalorie);

        if(updateUser(aUser))
            Toast.makeText(this, "Updating completed.." , Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(this, "Updating failed.." , Toast.LENGTH_SHORT).show();
    }

    /**
     * Calls DAO update user information
     * @param aUser
     * @return true and false
     */
    public boolean updateUser(User aUser){
        boolean result=false;
        DaoUser databaseAccess = DaoUser.getInstance(this);
        databaseAccess.open();
        result = databaseAccess.setUpdateUser(aUser);
        databaseAccess.close();
        return result;
    }

    /**
     * Calculates Daily needed Calories
     * @param age
     * @param height
     * @param heightCD
     * @param weight
     * @param weightCD
     * @param gender
     * @param exerciseCD
     * @return needed calories
     */
    public int calDailyCalorie(int age, double height, String heightCD, double weight, String weightCD, String gender, String exerciseCD ) {

        /**
         W = weight in kilograms (weight (lbs)/2.2) =weight in kg
         H = height in centimeters (inches x 2.54) =height in cm
         A = age in years
         Men: BMR=66.47+ (13.75 x W) + (5.0 x H) - (6.75 x A)
         Women: BMR=665.09 + (9.56 x W) + (1.84 x H) - (4.67 x A)
         cxersice ==00->*1.3 , 01->*1.5, 02->*1.7
         */

        // HEIGHT_CD -> 00:cm 01:feet
        double cmHeight  =(heightCD.equals("01"))?(height*30.587):height;
        // WEIGHT_CD -> 00:kg 01:lbs
        double  kgWeight =(weightCD.equals("01"))?(weight/2.2):weight;
        double dailyCalorie = 0.0;
        if(gender.equals("0")){//female
            // Women: BMR=665.09 + (9.56 x W) + (1.84 x H) - (4.67 x A)
            dailyCalorie = 665.09 + (9.56 * kgWeight) + (1.84 * cmHeight) - (4.67 * age);
        }
        else{ //male
            //Men: BMR=66.47+ (13.75 x W) + (5.0 x H) - (6.75 x A)
            dailyCalorie = 66.47 + (13.75 * kgWeight) + (5.0 * cmHeight) - (6.75 * age);
        }

        if(exerciseCD.equals("00")) dailyCalorie=(dailyCalorie*1.3);
        else if(exerciseCD.equals("01")) dailyCalorie=(dailyCalorie*1.5);
        else if(exerciseCD.equals("02")) dailyCalorie=(dailyCalorie*1.7);
        else dailyCalorie=0;

        Log.d("daily calorie ", String.valueOf(dailyCalorie) + " h :" + String.valueOf(cmHeight) + " K : " + String.valueOf(kgWeight) );
        return (int)dailyCalorie;
    }

    /**
     * Displays on Screen with usrs personal setting information
     * @param aUser
     */
    public void setDisplay(User aUser)
    {
        //tvUserID.setText(aUser.getUserID());
        spType.setSelection((Integer.parseInt(aUser.getCodeID())-1));

        etAge.setText(String.valueOf(aUser.getAge()));

        double tmpHeight;
        int tmpHeightd, tmpHeightf;
        tmpHeight = aUser.getHeight();
        tmpHeightd = (int)tmpHeight;
        tmpHeightf = (int)(Math.round((tmpHeight-(double)tmpHeightd)*10));
        etHeightD.setText(String.valueOf(tmpHeightd));
        etHeightF.setText(String.valueOf(tmpHeightf));
        spHeight.setSelection(Integer.parseInt(aUser.getHeightCD()));

        double tmpWeight;
        int tmpWeightd, tmpWeightf;
        tmpWeight = aUser.getWeight();
        tmpWeightd = (int)tmpWeight;
        tmpWeightf = (int)(Math.round((tmpWeight-(double)tmpWeightd)*10));
        etWeightD.setText(String.valueOf(tmpWeightd));
        etWeightF.setText(String.valueOf(tmpWeightf));
        spWeight.setSelection(Integer.parseInt(aUser.getWeightCD()));

        if(aUser.getGenderCD().equals("1")){
            rbFemale.setChecked(false);
            rbMale.setChecked(true);
        }else{
            rbFemale.setChecked(true);
            rbMale.setChecked(false);
        }
        spExercise.setSelection(Integer.parseInt(aUser.getExerciseCD()));
        etCalorie.setText(String.valueOf(aUser.getDailyCalorie()));

    }

    /**
     * user Logout
     * @param v
     */
    public void btnLogOut(View v){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public String getUserID() { return userID; }
    public void setUserID(String userID) { this.userID = userID; }

    /**
     * When textEdit touch, rest data
     * @param v
     * @param motionEvent
     * @return
     */
    @Override
    public boolean onTouch(View v, MotionEvent motionEvent) {
        if((v.getId())==(R.id.etAge))
            etAge.setText("");
        else if((v.getId())==(R.id.etHeightD))
            etHeightD.setText("");
        else if((v.getId())==(R.id.etHeightF))
            etHeightF.setText("");
        else if((v.getId())==(R.id.etWeightD))
            etWeightD.setText("");
        else if((v.getId())==(R.id.etWeightF))
            etWeightF.setText("");
        return false;
    }
}