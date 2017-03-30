package nz.ac.cornell.fitnessmealplans;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import nz.ac.cornell.fitnessmealplans.DB.DaoUser;

/**
 * Login through the 4digits pin numbers
 * Created by AJY on 2016-06-02.
 */
public class LoginActivity extends AppCompatActivity {

    private EditText etPIN1, etPIN2, etPIN3, etPIN4;
    private TextView tvMessage;
    private String userId;
    private String pin = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Intent intent = this.getIntent();
        userId = intent.getExtras().getString("userId");

        etPIN1 = (EditText) findViewById(R.id.etPIN1);
        etPIN2 = (EditText) findViewById(R.id.etPIN2);
        etPIN3 = (EditText) findViewById(R.id.etPIN3);
        etPIN4 = (EditText) findViewById(R.id.etPIN4);
        tvMessage = (TextView) findViewById(R.id.tvMessage);

        //the first pin number
        etPIN1.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(etPIN1.length() == 1) {
                    pin += etPIN1.getText();
                    etPIN2.requestFocus(); //focus moves to next
                }
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void afterTextChanged(Editable s) {}
        });

        //the second pin number
        etPIN2.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(etPIN2.length() == 1) {
                    pin += etPIN2.getText();
                    etPIN3.requestFocus();//focus moves to next
                }
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void afterTextChanged(Editable s) {}
        });

        //the third pin number
        etPIN3.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(etPIN3.length() == 1) {
                    pin += etPIN3.getText();
                    etPIN4.requestFocus();
                }
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void afterTextChanged(Editable s) {}
        });

        //the fourth pin number
        etPIN4.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(etPIN4.length() == 1) {
                    pin += etPIN4.getText();
                    checkPin(userId, pin); //when text changed forth pin number check whole pin number
                }
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void afterTextChanged(Editable s) {}
        });

    }

    /**
     * User Id And pin number match with db number
     * @param userId
     * @param pin
     */
    private void checkPin(String userId, String pin) {
        String dbPw = matchingUser(userId, pin);
        //Password matching check
        if(dbPw.equals(pin)) {
            Intent intent = new Intent(this, StartActivity.class);
            intent.putExtra("userID", userId);
            startActivity(intent);
        }
        else if(dbPw.equals("NON")){
            Toast.makeText(this, "The userID was NOT FOUND." , Toast.LENGTH_SHORT).show();
        }
        else{
            etPIN1.setText("");
            etPIN2.setText("");
            etPIN3.setText("");
            etPIN4.setText("");
            tvMessage.setText("You Entered Wrong PIN Number. Try Again");
            etPIN1.requestFocus();
            setPin("");
        }
    }

    /**
     * DAO Call Matching user
     * @param id userid
     * @param pw pin number
     * @return user pw
     */
    private String matchingUser(String id, String pw) {
        String password = null;
        DaoUser databaseAccess = DaoUser.getInstance(this);
        databaseAccess.open();
        password = databaseAccess.getPassword(id);
        databaseAccess.close();
        return password;
    }

    public String getPin() { return pin; }
    public void setPin(String pin) {this.pin = pin;}
}
