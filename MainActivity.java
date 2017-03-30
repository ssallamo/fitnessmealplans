package nz.ac.cornell.fitnessmealplans;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import nz.ac.cornell.fitnessmealplans.DB.DaoUser;

/**
 * The Front page of this application
 * Login
 * Created by HJS on 2016-05-08.
 */
public class MainActivity extends AppCompatActivity {

    private EditText etId, etPw;
    SharedPreferences userSetting;
    SharedPreferences.Editor savedId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etId = (EditText) findViewById(R.id.etID);
        etPw = (EditText)findViewById(R.id.etPassword);

        //User ID preference
        userSetting = getSharedPreferences("setting", 0);
        savedId = userSetting.edit();
        if(!userSetting.getString("ID", "").equals("")) {
            Intent intent = new Intent(this, LoginActivity.class);
            intent.putExtra("userId", userSetting.getString("ID", ""));
            startActivity(intent);
        }
    }

    /**
     * btn Login event
     * @param v
     */
    public void btnLogin(View v){

        String rtnPw = null;
        String strPw = etPw.getText().toString();
        String strId = etId.getText().toString();

        Log.d("id", "["+etId.getText().toString()+"]");
        Log.d("pw", "["+etPw.getText().toString()+"]");

        if((etId.getText().toString().trim().length())<1){
            Toast.makeText(this, "Please Enter Your ID properly." , Toast.LENGTH_SHORT).show();
        }
        else  if(etPw.getText().toString().trim().length()<4){
            Toast.makeText(this, "Please Enter Your Pw properly." , Toast.LENGTH_SHORT).show();
        }
        else{
            String dbPw = matchingUser(strId, strPw);
            //Password matching check
            if(dbPw.equals(strPw)){
                Toast.makeText(this, "WELCOME " + strId, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, StartActivity.class);
                intent.putExtra("userID", strId);
                startActivity(intent);
            }
            else if(dbPw.equals("NON")){
                Toast.makeText(this, "The userID was NOT FOUND." , Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(this, "The Password is NOT COLLECT." , Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * btn Create ID event
     * Links Create User activity
     * @param v
     */
    public void btnCreateId(View v){
        Intent intent = new Intent(this, CreateUserActivity.class);
        startActivity(intent);
    }

    /**
     * Calls DAO getPassword method
     * @param id user id
     * @param pw user password
     * @return password from Database
     */
    private String matchingUser(String id, String pw) {
        String password = null;
        DaoUser databaseAccess = DaoUser.getInstance(this);
        databaseAccess.open();
        password = databaseAccess.getPassword(id);
        databaseAccess.close();
        return password;
    }
}
