package com.develeno.prestashopandroidclient;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import org.json.JSONObject;

public class SignInActivity extends AppCompatActivity {
//    private CallbackManager callbackManager;
    /* access modifiers changed from: private */
    public EditText email;
    /* access modifiers changed from: private */
    public boolean isViaFB;
    /* access modifiers changed from: private */
    public JSONObject jsonObject;
//    private LoginButton loginButton;
    /* access modifiers changed from: private */
    public EditText password;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        Toolbar toolbar = findViewById(R.id.toolbar);
        this.email = findViewById(R.id.email);
        this.password = findViewById(R.id.password);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Sign In");
//        this.loginButton = (LoginButton) findViewById(R.id.login_button);
//        this.loginButton.setReadPermissions(Arrays.asList(new String[]{"public_profile, email, user_birthday, user_friends"}));
//        this.callbackManager = Factory.create();
//        this.loginButton.registerCallback(this.callbackManager, new FacebookCallback<LoginResult>() {
//            public void onSuccess(LoginResult loginResult) {
//                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphJSONObjectCallback() {
//                    public void onCompleted(JSONObject object, GraphResponse response) {
//                        try {
//                            SignInActivity.this.jsonObject = object;
//                            SignInActivity.this.email.setText(SignInActivity.this.jsonObject.getString("email"));
//                            SignInActivity.this.password.setText("password");
//                            SignInActivity.this.isViaFB = true;
//                            SignInActivity.this.SignIn(null);
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                });
//                Bundle parameters = new Bundle();
//                parameters.putString(GraphRequest.FIELDS_PARAM, "id,name,email,gender,birthday");
//                request.setParameters(parameters);
//                request.executeAsync();
//            }
//
//            public void onCancel() {
//                Toast.makeText(SignInActivity.this, "cancel", 0).show();
//            }
//
//            public void onError(FacebookException exception) {
//                Toast.makeText(SignInActivity.this, exception.getMessage(), 0).show();
//            }
//        });
    }

    /* access modifiers changed from: protected */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        this.callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    public void SignIn(View view) {
        String emailString = this.email.getText().toString();
        String passwordString = this.password.getText().toString();
        if (emailString.isEmpty()) {
            this.email.setError("Cannot be empty");
        } else if (passwordString.isEmpty()) {
            this.password.setError("Cannot be empty");
        } else {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Please Wait");
            progressDialog.setTitle("Signing In");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
            WebService.signIn(emailString, passwordString, new OnBackgroundTaskCompleted() {
                public void getResult(Object result) {
                    progressDialog.dismiss();
                    if (result instanceof Integer) {
                        if (((Integer) result).intValue() == 545610) {
                            Toast.makeText(SignInActivity.this, "Null Response Error", Toast.LENGTH_SHORT).show();
                            return;
                        } else if (((Integer) result).intValue() == 6535465) {
                            Toast.makeText(SignInActivity.this, "Invalid Email", Toast.LENGTH_SHORT).show();
                            if (SignInActivity.this.isViaFB) {
                                SignInActivity.this.signUp(null);
                                return;
                            }
                            return;
                        }
                    }
                    if (result != null) {
                        SignInActivity.this.proceed((Customer) Parser.parse(result + "", Parser.PARSE_CUSTOMER));
                        return;
                    }
                    Toast.makeText(SignInActivity.this, "Wrong Password", Toast.LENGTH_SHORT).show();
                }

                public void refresh(Object result) {
                }
            });
        }
    }

    /* access modifiers changed from: private */
    public void proceed(Customer customer) {
        Data.setCurrentUser(customer, getApplicationContext());
        if (getIntent().getBooleanExtra("exitToCheckOutPage", false)) {
            startActivity(new Intent(this, CheckoutActivity.class));
        } else {
            startActivity(new Intent(this, MainActivity.class));
        }
        Toast.makeText(this, "Successful", Toast.LENGTH_SHORT).show();
        finish();
    }

    public void signUp(View view) {
        Intent intent = new Intent(this, SignUpActivity.class);
        if (this.isViaFB) {
            try {
                intent.putExtra("isViaFB", true);
                intent.putExtra("email", this.jsonObject.getString("email"));
                intent.putExtra("fname", this.jsonObject.getString("name").split(" ")[0]);
                intent.putExtra("lname", this.jsonObject.getString("name").split(" ")[1]);
                intent.putExtra("ismale", this.jsonObject.getString("gender").equalsIgnoreCase("male"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        startActivity(intent);
    }
}
