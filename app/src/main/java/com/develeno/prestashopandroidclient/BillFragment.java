package com.develeno.prestashopandroidclient;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import java.text.DecimalFormat;

public class BillFragment extends Fragment {
    private TextView amountPayable;
    private TextView discount;
    private View discountLayout;
    private TextView promoText;
    private ViewGroup rootView;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.rootView = (ViewGroup) inflater.inflate(R.layout.frag_bill, container, false);
        TextView totalAmount = this.rootView.findViewById(R.id.total);
        TextView deleverycharges = this.rootView.findViewById(R.id.deliverycharges);
        this.amountPayable = this.rootView.findViewById(R.id.amount_payable);
        this.discount = this.rootView.findViewById(R.id.discount);
        this.discountLayout = this.rootView.findViewById(R.id.discount_layout);
        this.promoText = this.rootView.findViewById(R.id.promoText);
        View shareLayout = this.rootView.findViewById(R.id.share_layout);
        if (Data.getCurrentOrder().isDiscountProvided()) {
            updateShareText();
        }
        /*shareLayout.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                Toast.makeText(BillFragment.this.getContext(), "Sharing...", 0).show();
                CheckoutActivity activity = (CheckoutActivity) BillFragment.this.getActivity();
                ShareDialog shareDialog = new ShareDialog((Activity) BillFragment.this.getActivity());
                shareDialog.registerCallback(activity.getCallbackManager(), new FacebookCallback<Result>() {
                    public void onSuccess(Result result) {
                        Toast.makeText(BillFragment.this.getContext(), "Congrats! You received 5% Discount", 0).show();
                        Data.getCurrentOrder().provideDiscount(5.0d);
                        BillFragment.this.updateShareText();
                    }

                    public void onCancel() {
                        Toast.makeText(BillFragment.this.getContext(), "You cancelled Sharing", 0).show();
                    }

                    public void onError(FacebookException e) {
                        Toast.makeText(BillFragment.this.getContext(), e.getMessage(), 0).show();
                    }
                });
                if (ShareDialog.canShow(ShareLinkContent.class)) {
                    shareDialog.show(((Builder) new Builder().setContentTitle("Download VegetableShoppy App").setContentDescription("Get Vegetables, fruits, and many other daily needs with free home delivery anywhere in Jabalpur").setContentUrl(Uri.parse("https://play.google.com/store/apps/details?id=com.develeno.prestashopandroidclient&hl=en"))).build());
                }
            }
        });*/
        double total = Cart.getTotal();
        totalAmount.setText("₹" + new DecimalFormat("#.##").format(total));
        if (total < 199.0d) {
            deleverycharges.setText("₹15");
            this.amountPayable.setText("₹" + Data.getCurrentOrder().getTotalAmountPayable());
        } else {
            deleverycharges.setText("No Charges");
            this.amountPayable.setText("₹" + Data.getCurrentOrder().getTotalAmountPayable());
        }
        Data.getCurrentOrder().setItems(Cart.getItems());
        return this.rootView;
    }

    /* access modifiers changed from: private */
    public void updateShareText() {
        this.discount.setText("₹" + Data.getCurrentOrder().getDiscount());
        this.amountPayable.setText("₹" + Data.getCurrentOrder().getTotalAmountPayable());
        this.discountLayout.setVisibility(0);
        this.promoText.setText("Thanks for Sharing");
        ((TextView) this.rootView.findViewById(R.id.subtext)).setText("You received a 5% discount");
    }
}
