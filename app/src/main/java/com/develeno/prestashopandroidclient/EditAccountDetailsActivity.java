package com.develeno.prestashopandroidclient;

import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import org.apache.http.HttpHeaders;

public class EditAccountDetailsActivity extends AppCompatActivity {
    private Customer currentUser;
    private EditText email;
    private RadioButton female;
    private EditText firstname;
    /* access modifiers changed from: private */
    public boolean isSomethingChanged;
    private EditText lastname;
    private RadioButton male;
    private TextView saveButton;
    private Toolbar toolbar;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_account_details);
        this.toolbar = findViewById(R.id.toolbar);
        this.firstname = findViewById(R.id.firstname);
        this.lastname = findViewById(R.id.lastname);
        this.email = findViewById(R.id.email);
        this.male = findViewById(R.id.male);
        this.female = findViewById(R.id.female);
        this.saveButton = findViewById(R.id.saveButton);
        setSupportActionBar(this.toolbar);
        getSupportActionBar().setTitle("Edit Your Details");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.currentUser = Data.getCurrentUser(this);
        this.firstname.setText(this.currentUser.getFirstName());
        this.lastname.setText(this.currentUser.getLastName() + "");
        this.email.setText(this.currentUser.getEmail() + "");
        if (this.currentUser.isMale().booleanValue()) {
            this.male.setSelected(true);
            this.female.setSelected(false);
        } else {
            this.male.setSelected(false);
            this.female.setSelected(true);
        }
        TextWatcher watcher = new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                EditAccountDetailsActivity.this.isSomethingChanged = true;
            }

            public void afterTextChanged(Editable editable) {
            }
        };
        this.firstname.addTextChangedListener(watcher);
        this.lastname.addTextChangedListener(watcher);
        this.email.addTextChangedListener(watcher);
        OnClickListener listener = new OnClickListener() {
            public void onClick(View view) {
                EditAccountDetailsActivity.this.isSomethingChanged = true;
                Toast.makeText(EditAccountDetailsActivity.this, "Changed", 0).show();
            }
        };
        this.male.setOnClickListener(listener);
        this.female.setOnClickListener(listener);
        this.saveButton.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                EditAccountDetailsActivity.this.saveChanges();
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit_account_details, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() != R.id.save) {
            return super.onOptionsItemSelected(item);
        }
        saveChanges();
        return true;
    }

    public void onBackPressed() {
        if (this.isSomethingChanged) {
            Builder builder = new Builder(this);
            builder.setTitle(HttpHeaders.WARNING);
            builder.setMessage("Proceed without saving changes?");
            builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    EditAccountDetailsActivity.this.saveChanges();
                }
            });
            builder.setNegativeButton("Go Back", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    EditAccountDetailsActivity.super.onBackPressed();
                }
            });
            builder.show();
            return;
        }
        super.onBackPressed();
    }

    /* access modifiers changed from: private */
    public void saveChanges() {
        if (checkAndAssignFields()) {
            final ProgressDialog dialog = new ProgressDialog(this);
            dialog.setMessage("Please Wait");
            dialog.setTitle("Saving");
            dialog.show();
            WebService.saveUser(this.currentUser, this, new OnBackgroundTaskCompleted() {
                public void getResult(Object result) {
                    dialog.dismiss();
                    if (((Boolean) result).booleanValue()) {
                        Toast.makeText(EditAccountDetailsActivity.this, "Successfully Changed", 0).show();
                    } else {
                        Toast.makeText(EditAccountDetailsActivity.this, "Failed to save Changes", 0).show();
                    }
                }

                public void refresh(Object result) {
                }
            });
        }
    }

    private boolean checkAndAssignFields() {
        int genderValue;
        String firstnameString = this.firstname.getText().toString().trim();
        String lastnameString = this.lastname.getText().toString().trim();
        String emailString = this.email.getText().toString().trim();
        if (this.male.isSelected()) {
            genderValue = 1;
        } else {
            genderValue = 0;
        }
        if (firstnameString.length() == 0) {
            this.firstname.setError("First name can't be empty");
            return false;
        } else if (lastnameString.length() == 0) {
            this.lastname.setError("First name can't be empty");
            return false;
        } else if (emailString.length() == 0) {
            this.email.setError("First name can't be empty");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(emailString).matches()) {
            this.email.setError("Invalid Email");
            return false;
        } else {
            this.currentUser.setFirstName(firstnameString);
            this.currentUser.setLastName(lastnameString);
            this.currentUser.setEmail(emailString);
            this.currentUser.setGender(genderValue);
            return true;
        }
    }
}
