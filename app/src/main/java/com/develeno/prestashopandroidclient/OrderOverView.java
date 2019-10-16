package com.develeno.prestashopandroidclient;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import java.text.DecimalFormat;

public class OrderOverView extends Fragment {
    /* access modifiers changed from: private */
    public ProgressDialog dialog;
    private ViewGroup rootView;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.rootView = (ViewGroup) inflater.inflate(R.layout.order_overview_frag, container, false);
        refresh();
        Data.getCurrentOrder().setOnOrderChangeListener(new OnOrderChangeListener() {
            public void onTimeChanged(Order order) {
                OrderOverView.this.refresh();
            }
        });
        ((Button) this.rootView.findViewById(R.id.placeOrder)).setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                if (!CheckoutActivity.isAddressSelected) {
                    Toast.makeText(OrderOverView.this.getContext(), "No Address Found", 0).show();
                    return;
                }
                OrderOverView.this.dialog = new ProgressDialog(OrderOverView.this.getContext());
                OrderOverView.this.dialog.setTitle("Placing Order");
                OrderOverView.this.dialog.setMessage("Please wait");
                OrderOverView.this.dialog.setCanceledOnTouchOutside(false);
                OrderOverView.this.dialog.show();
                OrderOverView.this.createCart();
            }
        });
        View addressLayout = this.rootView.findViewById(R.id.address_layout);
        View billlayout = this.rootView.findViewById(R.id.bill);
        View timelayout = this.rootView.findViewById(R.id.time_layout);
        addressLayout.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                ((CheckoutActivity) OrderOverView.this.getActivity()).getViewPager().setCurrentItem(1);
            }
        });
        timelayout.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                ((CheckoutActivity) OrderOverView.this.getActivity()).getViewPager().setCurrentItem(2);
            }
        });
        billlayout.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                ((CheckoutActivity) OrderOverView.this.getActivity()).getViewPager().setCurrentItem(0);
            }
        });
        return this.rootView;
    }

    /* access modifiers changed from: private */
    public void createCart() {
        WebService.createNewCart(getContext(), new OnBackgroundTaskCompleted() {
            public void getResult(Object result) {
                if (result != null) {
                    OrderOverView.this.placeOrder((CartObject) result);
                    return;
                }
                Toast.makeText(OrderOverView.this.getContext(), "Failed to create cart", 0).show();
                OrderOverView.this.dialog.dismiss();
            }

            public void refresh(Object result) {
            }
        });
    }

    /* access modifiers changed from: private */
    public void placeOrder(CartObject cartObject) {
        Data.getCurrentOrder().setCartId(cartObject.getId());
        WebService.placeOrder(getContext(), new OnBackgroundTaskCompleted() {
            public void getResult(Object result) {
                if (((Boolean) result).booleanValue()) {
                    OrderOverView.this.startActivity(new Intent(OrderOverView.this.getContext(), OrderSuccessFullActivity.class));
                    OrderOverView.this.getActivity().finish();
                    CartActivity.finishActivity();
                    Cart.clear();
                    Data.clearCurrentOrder();
                }
                OrderOverView.this.dialog.dismiss();
            }

            public void refresh(Object result) {
            }
        });
    }

    public void refresh() {
        TextView totalAmount = (TextView) this.rootView.findViewById(R.id.total);
        TextView deleverycharges = (TextView) this.rootView.findViewById(R.id.deliverycharges);
        TextView amountPayable = (TextView) this.rootView.findViewById(R.id.amount_payable);
        TextView line1 = (TextView) this.rootView.findViewById(R.id.line1);
        TextView line2 = (TextView) this.rootView.findViewById(R.id.line2);
        TextView code = (TextView) this.rootView.findViewById(R.id.code);
        TextView timeline = (TextView) this.rootView.findViewById(R.id.line1a);
        Order order = Data.getCurrentOrder();
        double total = Cart.getTotal();
        totalAmount.setText("₹" + new DecimalFormat("#.##").format(total));
        if (total < 199.0d) {
            deleverycharges.setText("₹15");
            amountPayable.setText("₹" + Data.getCurrentOrder().getTotalAmountPayable());
        } else {
            deleverycharges.setText("No Charges");
            amountPayable.setText("₹" + Data.getCurrentOrder().getTotalAmountPayable());
        }
        if (Data.getCurrentOrder().isDiscountProvided()) {
            TextView discount = (TextView) this.rootView.findViewById(R.id.discount);
            View discountLayout = this.rootView.findViewById(R.id.discount_layout);
            discount.setText("₹" + Data.getCurrentOrder().getDiscount());
            discountLayout.setVisibility(0);
        }
        line1.setVisibility(8);
        code.setVisibility(8);
        line2.setVisibility(8);
        Address address = order.getAddress();
        if (address != null) {
            if (!address.getLine1().trim().isEmpty()) {
                line1.setText(address.getLine1());
                line1.setVisibility(0);
            }
            if (!address.getLine2().trim().isEmpty()) {
                line2.setText(address.getLine2());
                line2.setVisibility(0);
            }
            if (!address.getPinCode().trim().isEmpty()) {
                code.setText(address.getPinCode());
                code.setVisibility(0);
            }
        } else {
            line1.setText("No Address added");
            line1.setVisibility(0);
        }
        timeline.setText(order.getTimeslot());
    }
}
