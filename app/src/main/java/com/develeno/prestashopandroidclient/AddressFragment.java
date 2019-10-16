package com.develeno.prestashopandroidclient;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.ArrayList;

public class AddressFragment extends Fragment {
    private AddressAdapter addressAdapter;
    private ArrayList<Address> addresses;
    private ListView listView;
    private View noAddressLayout;
    private ProgressBar progressBar;
    private boolean selectable;
    private SwipeRefreshLayout swipe;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.activity_addresses, container, false);
        Bundle args = getArguments();
        if (args != null) {
            this.selectable = args.getBoolean("selectable", false);
        } else {
            rootView.findViewById(R.id.title).setVisibility(View.GONE);
        }
        this.noAddressLayout = rootView.findViewById(R.id.noAddressLayout);
        this.listView = rootView.findViewById(R.id.listView);
        this.swipe = rootView.findViewById(R.id.swipe_refresh_layout);
        this.noAddressLayout.setVisibility(View.GONE);
        this.progressBar = rootView.findViewById(R.id.progressBar);
        this.addresses = new ArrayList<>();
        this.addressAdapter = new AddressAdapter(this.addresses, this.selectable, (AppCompatActivity) getActivity());
        this.listView.setAdapter(this.addressAdapter);
        this.swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            public void onRefresh() {
                AddressFragment.this.fetchAddresses();
            }
        });
        rootView.findViewById(R.id.add_address).setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                AddressFragment.this.addAddress();
            }
        });
        return rootView;
    }

    /* access modifiers changed from: private */
    public void fetchAddresses() {
        if (Data.getAddresses() == null) {
            this.swipe.setRefreshing(true);
            WebService.fetchAllAddresses(new OnBackgroundTaskCompleted() {
                public void getResult(Object result) {
                    if (result != null) {
                        AddressFragment.this.onGotAddress((ArrayList) result);
                    } else {
                        Toast.makeText(AddressFragment.this.getContext(), "Failed to load address", Toast.LENGTH_SHORT).show();
                    }
                }

                public void refresh(Object result) {
                }
            }, getActivity());
            return;
        }
        onGotAddress(Data.getAddresses());
    }

    /* access modifiers changed from: private */
    public void onGotAddress(ArrayList<Address> result) {
        this.progressBar.setVisibility(View.GONE);
        this.swipe.setRefreshing(false);
        Data.setAddresses(result);
        dataSetChanged();
        if (this.addresses.size() > 0) {
            CheckoutActivity.isAddressSelected = true;
            this.noAddressLayout.setVisibility(View.GONE);
            return;
        }
        this.noAddressLayout.setVisibility(View.GONE);
    }

    public void onResume() {
        super.onResume();
        this.progressBar.setVisibility(View.GONE);
        fetchAddresses();
    }

    /* access modifiers changed from: private */
    public void dataSetChanged() {
        this.addresses = Data.getAddresses();
        this.addressAdapter = new AddressAdapter(this.addresses, this.selectable, (AppCompatActivity) getActivity());
        this.listView.setAdapter(this.addressAdapter);
        if (this.addresses.size() > 0) {
            this.noAddressLayout.setVisibility(View.GONE);
        }
    }

    public void addAddress() {
        Builder builder = new Builder(getActivity());
        View view = getActivity().getLayoutInflater().inflate(R.layout.new_address, null);
        builder.setView(view);
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
        final EditText title = view.findViewById(R.id.title);
        final EditText line1 = view.findViewById(R.id.line1);
        final EditText line2 = view.findViewById(R.id.line2);
        final Spinner spinner = view.findViewById(R.id.spinner);
        view.findViewById(R.id.button).setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                final ProgressDialog dialog = new ProgressDialog(AddressFragment.this.getActivity());
                dialog.setTitle("Saving");
                dialog.setMessage("Please Wait");
                dialog.show();
                String titleString = title.getText().toString().trim();
                String line1String = line1.getText().toString().trim();
                String line2String = line2.getText().toString().trim();
                if (titleString.isEmpty()) {
                    title.setError("Name can't be Empty");
                    dialog.dismiss();
                } else if (line1String.isEmpty()) {
                    line1.setError("Line 1 can't be Empty");
                    dialog.dismiss();
                } else {
                    final Address address = new Address();
                    address.setTitle(titleString);
                    address.setLine1(line1String);
                    address.setLine2(line2String + " - " + spinner.getSelectedItem().toString());
                    address.setPinCode("482002");
                    WebService.addNewAddress(address, AddressFragment.this.getContext(), new OnBackgroundTaskCompleted() {
                        public void getResult(Object result) {
                            dialog.dismiss();
                            if (((Boolean) result).booleanValue()) {
                                AddressFragment.this.dataSetChanged();
                                Toast.makeText(AddressFragment.this.getActivity(), "Successfully Added", Toast.LENGTH_SHORT).show();
                                alertDialog.dismiss();
                                Data.getCurrentOrder().setAddress(address);
                                return;
                            }
                            Toast.makeText(AddressFragment.this.getActivity(), "Failed to add address", Toast.LENGTH_SHORT).show();
                        }

                        public void refresh(Object result) {
                        }
                    });
                }
            }
        });
    }
}
