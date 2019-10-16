package com.develeno.prestashopandroidclient;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import org.apache.http.protocol.HTTP;

public class FragmentMore extends Fragment {
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.frag_more, container, false);
        ArrayList<MoreListItem> items = new ArrayList<>();
        items.add(new MoreListItem("My WishList", "List of your favourite Products", (int) R.drawable.share_icon, new Intent(getActivity(), WishListActivity.class)));
        if (Data.getCurrentUser(getActivity()) == null) {
            items.add(new MoreListItem("My Account", "Sign in to your account", (int) R.drawable.help_icon, new Intent(getActivity(), SignInActivity.class)));
        } else {
            items.add(new MoreListItem("My Account", "Manage your account", (int) R.drawable.help_icon, new Intent(getActivity(), ManageAccount.class)));
        }
        Intent mailIntent = new Intent("android.intent.action.SEND");
        mailIntent.setType("plain/text");
        mailIntent.putExtra("android.intent.extra.EMAIL", "vegetableshoppy@gmail.com");
        String name = "";
        Customer currentUser = Data.getCurrentUser(getContext());
        if (currentUser != null) {
            name = currentUser.getFirstName() + " " + currentUser.getLastName();
        }
        mailIntent.putExtra("android.intent.extra.SUBJECT", "CUSTOMER FEEDBACK: " + name);
        Intent mailIntent2 = new Intent("android.intent.action.SEND");
        mailIntent2.setType("plain/text");
        mailIntent2.putExtra("android.intent.extra.EMAIL", Uri.fromParts("mailto", "abc@gmail.com", null));
        mailIntent2.putExtra("android.intent.extra.SUBJECT", "CUSTOMER QUERY: " + name);
        items.add(new MoreListItem("Help", "Need any help?", (int) R.drawable.help_icon, mailIntent));
        items.add(new MoreListItem("Contact Us", "Feel free to contact us", (int) R.drawable.contact_us_icon, mailIntent2));
        items.add(new MoreListItem("Feedback", "We would love to hear from you", (int) R.drawable.feedback_icon, mailIntent));
        Intent sharingIntent = new Intent("android.intent.action.SEND");
        sharingIntent.setType(HTTP.PLAIN_TEXT_TYPE);
        String shareBody = "Download the app: " + Uri.parse("market://details?id=" + getActivity().getPackageName());
        sharingIntent.putExtra("android.intent.extra.SUBJECT", "Download VegetableShoppy");
        sharingIntent.putExtra("android.intent.extra.TEXT", shareBody);
        items.add(new MoreListItem("Share", "Let your friends also enjoy home delivery", (int) R.drawable.share_icon, sharingIntent));
        items.add(new MoreListItem("Rate Us", "Rate our app at play store", (int) R.drawable.rate_us_icon, new Intent("android.intent.action.VIEW", Uri.parse("market://details?id=" + getActivity().getPackageName()))));
        items.add(new MoreListItem("About us", "Get familiar with who we are", (int) R.drawable.about_us_icon, new Intent(getContext(), AboutActivity.class)));
        ((ListView) rootView.findViewById(R.id.listview)).setAdapter(new MoreListAdapter(getActivity(), items));
        return rootView;
    }
}
