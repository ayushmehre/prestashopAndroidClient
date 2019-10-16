package com.develeno.prestashopandroidclient;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;

public class ManageAccount extends AppCompatActivity {
    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_account);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setTitle("Your Account");
        ArrayList<MoreListItem> items = new ArrayList<>();
        items.add(new MoreListItem("Account Details", "Edit Name and Password", R.drawable.help_icon, new Intent(this, EditAccountDetailsActivity.class)));
        items.add(new MoreListItem("Addresses", "Your addresses", R.drawable.contact_us_icon, new Intent(this, AddressesActivity.class)));
        items.add(new MoreListItem("Order History", "List of Previous orders", R.drawable.feedback_icon, new Intent(this, WishListActivity.class)));
        items.add(new MoreListItem("My Wallet", "Your online wallet", R.drawable.feedback_icon, new Intent(this, WalletActivity.class)));
        items.add(new MoreListItem("Logout", "logout from your account", R.drawable.share_icon, new OnClickListener() {
            public void onClick(View view) {
                ManageAccount.this.logout();
            }
        }));
        ((ListView) findViewById(R.id.listView)).setAdapter(new MoreListAdapter(this, items));
    }

    /* access modifiers changed from: private */
    public void logout() {
        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setTitle("Logging out");
        dialog.setMessage("Please Wait");
        dialog.show();
        Data.setCurrentUser(null, this);
        Intent intent = new Intent(this, SplashScreen.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
//        try {
//            if (FacebookSdk.isInitialized()) {
//                LoginManager.getInstance().logOut();
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        finish();
        MainActivity.finishActivty();
        startActivity(new Intent(this, SplashScreen.class));
    }
}
