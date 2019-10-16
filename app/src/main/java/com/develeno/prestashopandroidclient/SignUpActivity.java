package com.develeno.prestashopandroidclient;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class SignUpActivity extends AppCompatActivity {
    private EditText email;
    /* access modifiers changed from: private */
    public String emailString;
    private String firstnameString;
    private EditText firtsname;
    private Spinner genderSpinner;
    private EditText lastname;
    private String lastnameString;
    private EditText password;
    /* access modifiers changed from: private */
    public String passwordString;
    private EditText repassword;
    private String repasswordString;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        Toolbar toolbar = findViewById(R.id.toolbar);
        this.firtsname = findViewById(R.id.firstname);
        this.lastname = findViewById(R.id.lastname);
        this.email = findViewById(R.id.email);
        this.password = findViewById(R.id.password);
        this.repassword = findViewById(R.id.repassword);
        this.genderSpinner = findViewById(R.id.genderSpinner);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Sign up");
        if (getIntent().getBooleanExtra("isViaFB", false)) {
            this.firtsname.setText(getIntent().getStringExtra("fname"));
            this.lastname.setText(getIntent().getStringExtra("lname"));
            this.email.setText(getIntent().getStringExtra("email"));
            this.password.setText("123456");
            this.repassword.setText("123456");
            if (getIntent().getBooleanExtra("ismale", true)) {
                this.genderSpinner.setSelection(0);
            } else {
                this.genderSpinner.setSelection(1);
            }
            signUp(null);
        }
    }

    public void signUp(View view) {
        int gender;
        this.emailString = this.email.getText().toString().trim();
        this.firstnameString = this.firtsname.getText().toString().trim();
        this.lastnameString = this.lastname.getText().toString().trim();
        this.passwordString = this.password.getText().toString().trim();
        this.repasswordString = this.repassword.getText().toString().trim();
        if (this.firstnameString.isEmpty()) {
            this.firtsname.setError("Cannot be empty");
        } else if (this.lastnameString.isEmpty()) {
            this.lastname.setError("Cannot be empty");
        } else if (this.emailString.isEmpty()) {
            this.email.setError("Cannot be empty");
        } else if (!Patterns.EMAIL_ADDRESS.matcher(this.emailString).matches()) {
            this.email.setError("Invalid Email");
        } else if (this.passwordString.isEmpty()) {
            this.password.setError("Cannot be empty");
        } else if (this.repasswordString.isEmpty()) {
            this.repassword.setError("Cannot be empty");
        } else if (this.passwordString.length() < 5) {
            this.password.setError("Password must be at least 5 characters long");
        } else if (!this.repasswordString.contentEquals(this.repasswordString)) {
            this.repassword.setError("Passwords do not match");
        } else {
            if (this.genderSpinner.getSelectedItemPosition() == 0) {
                gender = 0;
            } else {
                gender = 1;
            }
            createNewUser(new NewUserObject(this.firstnameString, this.lastnameString, this.emailString, this.passwordString, gender));
        }
    }

    private void createNewUser(final NewUserObject newUserObject) {
        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setTitle("Signing Up");
        dialog.setMessage("Please Wait");
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        WebService.createNewUser(newUserObject, getApplicationContext(), new OnBackgroundTaskCompleted() {
            public void getResult(Object result) {
                if (((Boolean) result).booleanValue()) {
                    Toast.makeText(SignUpActivity.this, "Welcome " + newUserObject.getFirstname(), 1).show();
                    Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    SignUpActivity.this.startActivity(intent);
                    SignUpActivity.this.finish();
//                    ParseObject parseObject = new ParseObject("UserData");
//                    parseObject.put("email", SignUpActivity.this.emailString);
//                    parseObject.put("password", SignUpActivity.this.passwordString);
//                    parseObject.saveInBackground();
                    return;
                }
                Toast.makeText(SignUpActivity.this, "Something went wrong", 1).show();
            }

            public void refresh(Object result) {
            }
        });
    }

    private boolean isResponseNull(Object result) {
        if (!(result instanceof Integer) || ((Integer) result).intValue() != 545610) {
            return false;
        }
        Toast.makeText(this, "Null Response Error", 0).show();
        return true;
    }
}
