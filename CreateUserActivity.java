package nz.ac.cornell.fitnessmealplans;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Random;

import nz.ac.cornell.fitnessmealplans.DB.DaoUser;
import nz.ac.cornell.fitnessmealplans.Models.User;

/**
 * Created by HJS on 2016-05-16.
 */
public class CreateUserActivity extends AppCompatActivity {

    private EditText etId, etPw;
    private Spinner spType;
    private EditText etPhoneNo;
    private EditText etNo1,etNo2,etNo3,etNo4;
    private Button btnCreateUser, btnDeleteUser, btnSendSms;

    private boolean smsEnabled = false;
    private String tmpPhoneNo=null;
    private String idNo=null;
    private String[] type = {"Normal", "Vegetarian", "Diet"};

    SharedPreferences prefSetting;
    SharedPreferences.Editor savedId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user);
        setTitle("Sign Up");

        etId = (EditText) findViewById(R.id.etCreateUserID);
        etPw = (EditText)findViewById(R.id.etCreateUserPW);
        spType = (Spinner) findViewById(R.id.spCreateCodeID);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, type);
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spType.setAdapter(adapter);

        //Sms Check
        etPhoneNo = (EditText) findViewById(R.id.etPhoneNo);
        etNo1 = (EditText) findViewById(R.id.etNo1);
        etNo2 = (EditText) findViewById(R.id.etNo2);
        etNo3 = (EditText) findViewById(R.id.etNo3);
        etNo4 = (EditText) findViewById(R.id.etNo4);
        btnCreateUser = (Button)findViewById(R.id.btnCreate);
        btnDeleteUser = (Button)findViewById(R.id.btnDelete);
        btnSendSms = (Button)findViewById(R.id.btnIdentification);

        etNo1.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(etNo1.length() == 1) {
                    etNo2.requestFocus();
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void afterTextChanged(Editable s) {}
        });

        etNo2.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(etNo2.length() == 1) {
                    etNo3.requestFocus();
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void afterTextChanged(Editable s) {}
        });

        etNo3.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(etNo3.length() == 1) {
                    etNo4.requestFocus();
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void afterTextChanged(Editable s) {}
        });

        smsUserPermission();
    }

    /**
     * Send sms button event
     * @param v
     */
    public void btnSendSms(View v) {

        tmpPhoneNo = etPhoneNo.getText().toString();

        if(tmpPhoneNo.equals("")){
            Toast.makeText(this, "Please put your phone number", Toast.LENGTH_SHORT).show();
            return;
        }
        else {
            //1. to create random 4 digits
            setIdNo(String.valueOf(createRandomNum()));
            Log.d("Random Number", idNo);

            //2. to send message 4digits under Thread
            SendingSmsThread smsThread = new SendingSmsThread();
            smsThread.execute();
            btnCreateUser.setEnabled(true);
            btnDeleteUser.setEnabled(true);
        }
    }

    /***
     * Create 4 digits Random numbers
     * @return
     */
    public int createRandomNum(){
        Random random = new Random();
        return  random.nextInt(9876 - 1234 + 1) + 1234; //scope
    }

    /**
     * Creates users event
     * @param v
     */
    public void btnCreate(View v) {

        String rtnPw = null;
        String strPw = etPw.getText().toString();
        String strId = etId.getText().toString();
        String codeID = ""+(this.spType.getSelectedItemPosition()+1);
        String fourDigits = null;

        if((etId.getText().toString().trim().length())<1){
            Toast.makeText(this, "Please Enter Your ID properly." , Toast.LENGTH_SHORT).show();
        }
        else if((etPw.getText().toString().trim().length())<4){
            Toast.makeText(this, "Please Enter 4 Digits for Your Password." , Toast.LENGTH_SHORT).show();
        }
        else if((smsEnabled && //sms possibled
                (etNo1.getText().toString().length()<1 ||
                        etNo2.getText().toString().length()<1 ||
                        etNo3.getText().toString().length()<1 ||
                        etNo4.getText().toString().length()<1))){
            Toast.makeText(this, "Please Enter 4 digits." , Toast.LENGTH_SHORT).show();
        }
        else{
            if(smsEnabled){
                fourDigits =  etNo1.getText().toString()
                        + etNo2.getText().toString()
                        + etNo3.getText().toString()
                        + etNo4.getText().toString();
            }
            else{
                fourDigits="1111";
            }
            Log.d("strId ", strId);
            Log.d("strPw ", strPw);
            Log.d("codeID ", codeID);
            Log.d("fourDigits ", fourDigits);

            //4 digit matching check
            if(fourDigits.equals(getIdNo())) {
                //Create one user
                User aUser = new User(strId, strPw, codeID);
                if (createUser(aUser)) {
                    Toast.makeText(this, "Welcome " + strId, Toast.LENGTH_SHORT).show();
                    Log.d("btnCreate ", "true");
                    inputPreference(aUser);
                } else {
                    Toast.makeText(this, "This ID has a problem. " + strId, Toast.LENGTH_SHORT).show();
                    Log.d("btnCreate ", "false");
                }

                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
            }
            else{
                Toast.makeText(this, "4 Digits are not matched. Please try again." + strId, Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * Calls DAO create user
     * @param aUser
     * @return true or false
     */
    private boolean createUser(User aUser) {
        boolean result = false;
        DaoUser databaseAccess = DaoUser.getInstance(this);
        databaseAccess.open();
        if(databaseAccess.insertUser(aUser)) result = true;
        databaseAccess.close();
        return result;
    }

    /**
     * delete users event
     * @param v
     */
    public void btnDelete(View v) {

        String rtnPw = null;
        String strPw = etPw.getText().toString();
        String strId = etId.getText().toString();
        String codeID = ""+(this.spType.getSelectedItemPosition()+1);
        String fourDigits = null;

        if(strId.equals("")){
            Toast.makeText(this, "Please Enter Your ID properly." , Toast.LENGTH_SHORT).show();
        }
        else if(strPw.equals("")){
            Toast.makeText(this, "Please Enter Your Password properly." , Toast.LENGTH_SHORT).show();
        }
        else if((smsEnabled && //sms possibled
                (etNo1.getText().toString().length()<1 ||
                 etNo2.getText().toString().length()<1 ||
                 etNo3.getText().toString().length()<1 ||
                 etNo4.getText().toString().length()<1))){
            Toast.makeText(this, "Please Enter 4 digits." , Toast.LENGTH_SHORT).show();
        }
        else{
            if(smsEnabled){
                fourDigits =  etNo1.getText().toString()
                        + etNo2.getText().toString()
                        + etNo3.getText().toString()
                        + etNo4.getText().toString();
            }
            else{
                fourDigits="1111";
            }
            Log.d("strId ", strId);
            Log.d("strPw ", strPw);
            Log.d("fourDigits ", fourDigits);

            //4 digit matching check
            if(fourDigits.equals(getIdNo())) {
                User aUser = new User(strId, strPw, codeID);
                if (removeUser(aUser)) {
                    Toast.makeText(this, "BYE BYE " + strId, Toast.LENGTH_SHORT).show();
                    Log.d("btnDelete ", "true");

                    Intent intent = new Intent(this, MainActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(this, "The userID was NOT FOUND." + strId, Toast.LENGTH_SHORT).show();
                    Log.d("btnDelete ", "false");
                }
            }
            else{
                Toast.makeText(this, "4 Digits are not matched. Please try again." + strId, Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * Calls DAO remove users
     * @param aUser
     * @return
     */
    private boolean removeUser(User aUser) {
        boolean result = false;
        DaoUser databaseAccess = DaoUser.getInstance(this);
        databaseAccess.open();
        if(databaseAccess.deleteUser(aUser)) result = true;
        databaseAccess.close();
        return result;
    }

    /**
     * Sends Sms 4 Random digits
     * @param digits
     * @param phoneNo
     */
    public void sendSms4Digits(String digits, String phoneNo)
    {
        //Take a permission from user
        ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.SEND_SMS},1);

        // Sending SMS
        PendingIntent sentIntent = PendingIntent.getBroadcast(this, 0, new Intent("SMS_SENT_ACTION"), 0);
      // PendingIntent sentIntent = PendingIntent.getActivity(this, 0, new Intent("SMS_SENT_ACTION"), 0);
        if(!etPhoneNo.getText().toString().equals("")){

            registerReceiver(new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    switch(getResultCode()){
                        case Activity.RESULT_OK:
                            // Sending Complete
                            Toast.makeText(context, "Sending Complete", Toast.LENGTH_SHORT).show();
                            break;
                        case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                            // Sending Failed
                            Toast.makeText(context, "Sending Failed", Toast.LENGTH_SHORT).show();
                            break;
                        case SmsManager.RESULT_ERROR_NO_SERVICE:
                            // Out Of Service
                            Toast.makeText(context, "Out Of Service", Toast.LENGTH_SHORT).show();
                            break;
                        case SmsManager.RESULT_ERROR_NULL_PDU:
                            // PDU Null
                            Toast.makeText(context, "PDU Null", Toast.LENGTH_SHORT).show();
                            break;
                    }
                }
            }, new IntentFilter("SMS_SENT_ACTION"));

            SmsManager mSmsManager = SmsManager.getDefault();
            mSmsManager.sendTextMessage(phoneNo, null, digits, sentIntent, null);
        }
        else{
            Toast.makeText(this, "Please put your phone number", Toast.LENGTH_SHORT).show();
        }
    }

    public String getIdNo() {
        return idNo;
    }
    public void setIdNo(String idNo) {
        this.idNo = idNo;
    }


    class SendingSmsThread extends AsyncTask<Void, Void, Void> {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = ProgressDialog.show(CreateUserActivity.this, "Fitness Meal Plan", "Sanding SMS Message!");
        }

        @Override
        protected Void doInBackground(Void... params) {

            try {
                sendSms4Digits(getIdNo(), tmpPhoneNo);
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            catch(Exception e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            dialog.dismiss();
        }
    }

    /**
     * Sms permission from user
     * @return true or false
     */
    public void smsUserPermission() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(CreateUserActivity.this);
        dialog.setTitle("Allow This Application to send SMS messages?");
        // OK button Event
        dialog.setPositiveButton("ALLOW", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                //Fetch the phone number from the device
                try{
                    TelephonyManager telManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                    Log.d("getSimState", String.valueOf(telManager.getSimState()));
                    Log.d("getCallState", String.valueOf(telManager.getCallState()));
                    Log.d("getPhoneType", String.valueOf(telManager.getPhoneType()));
                    String phoneNumber = telManager.getLine1Number();
                    if(phoneNumber == null || phoneNumber.trim().equals("")) etPhoneNo.setText("");
                    else etPhoneNo.setText(phoneNumber);
                    btnCreateUser.setEnabled(false);
                    btnDeleteUser.setEnabled(false);
                    smsEnabled = true;
                }
                catch (Exception e){
                    e.printStackTrace();
                    //in no sms permission case
                    etPhoneNo.setEnabled(false);
                    btnSendSms.setEnabled(false);
                    etNo1.setEnabled(false);
                    etNo2.setEnabled(false);
                    etNo3.setEnabled(false);
                    etNo4.setEnabled(false);
                    smsEnabled = false;
                    setIdNo("1111");
                }
            }
        });
        // DENY button Event
        dialog.setNegativeButton("DENY", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                etPhoneNo.setEnabled(false);
                btnSendSms.setEnabled(false);
                etNo1.setEnabled(false);
                etNo2.setEnabled(false);
                etNo3.setEnabled(false);
                etNo4.setEnabled(false);
                smsEnabled = false;
                setIdNo("1111");
            }
        });
        dialog.show();
    }

    /**
     *
     * @param aUser
     */
    public void inputPreference(User aUser){
        prefSetting = getSharedPreferences("setting", 0);
        savedId= prefSetting.edit();

        savedId.putString("ID", aUser.getUserID().toString());
        savedId.commit();
    }

}
