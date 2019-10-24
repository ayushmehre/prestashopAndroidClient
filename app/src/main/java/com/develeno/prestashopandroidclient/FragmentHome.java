package com.develeno.prestashopandroidclient;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class FragmentHome extends Fragment {
    boolean areProductsFetched;
    /* access modifiers changed from: private */
    public ArrayList<SliderOffer> offers = new ArrayList<>();
    private View recentlayout;
    private ViewGroup rootView;
    private int size;
    private View topCategories;
    private View trendingLayout;
    private View wishlistLayout;

    private class RemindTask extends TimerTask {
        /* access modifiers changed from: private */
        public final ViewPager pager;

        public RemindTask(ViewPager pager2) {
            this.pager = pager2;
        }

        public void run() {
            try {
                FragmentHome.this.getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        FragmentHome.this.changeBanner(RemindTask.this.pager);
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void fetchOffers(final ViewGroup rootView2) {
       /* ParseQuery.getQuery("SliderOffers").findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> objects, ParseException e) {
                rootView2.findViewById(R.id.progressBar4).setVisibility(View.GONE);
                if (e == null) {
                    FragmentHome.this.offers = new ArrayList();
                    for (ParseObject obj : objects) {
                        FragmentHome.this.offers.add(new SliderOffer(obj));
                    }
                    FragmentHome.this.createPager();
                }
            }
        });*/

        for (int i = 0; i < 3; i++) {
            offers.add(new SliderOffer("Diwali Offer", "10% Flat Discount", "Purchase any items from store worth Rs.500 or more and get flat 10% discount"));
        }
        createPager();
        rootView.findViewById(R.id.progressBar4).setVisibility(View.GONE);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.rootView = (ViewGroup) inflater.inflate(R.layout.frag_home, container, false);
        this.topCategories = this.rootView.findViewById(R.id.top_categories_layout);
        this.recentlayout = this.rootView.findViewById(R.id.recent_layout);
        this.trendingLayout = this.rootView.findViewById(R.id.trending_layout);
        this.wishlistLayout = this.rootView.findViewById(R.id.wishlist_layout);
        final View howitworks = this.rootView.findViewById(R.id.howitworks);
        View clear = this.rootView.findViewById(R.id.clear);
        View recenltyViewAll = this.rootView.findViewById(R.id.recently_view_all);
        View trendingViewAll = this.rootView.findViewById(R.id.trending_view_all);
        View topCategoryViewAll = this.rootView.findViewById(R.id.top_categories_View_all);
        View wishlistViewAll = this.rootView.findViewById(R.id.wishlist_view_all);
        View signInLayout = this.rootView.findViewById(R.id.signinLayout);
        View signInButton = this.rootView.findViewById(R.id.signinButton);
        View signUpButton = this.rootView.findViewById(R.id.signupbutton);
        this.topCategories.setVisibility(View.GONE);
        this.recentlayout.setVisibility(View.GONE);
        this.trendingLayout.setVisibility(View.GONE);
        this.wishlistLayout.setVisibility(View.GONE);
        if (Data.getOffers().size() > 0) {
            createPager();
        } else {
            fetchOffers(this.rootView);
        }
        final MainActivity activity = (MainActivity) getActivity();
        activity.setOnCategoriesAndProductsLoadedListener(new OnGotResponse() {
            public void getResponse(String response) {
                FragmentHome.this.areProductsFetched = true;
                FragmentHome.this.refreshTopCategories();
                FragmentHome.this.refreshTrendingProducts();
                FragmentHome.this.refreshRecentProducts();
                FragmentHome.this.refreshWishListProducts();
            }
        });
        if (Data.getCurrentUser(getContext()) != null) {
            signInLayout.setVisibility(View.GONE);
        }
        if (PreferenceManager.getDefaultSharedPreferences(getContext()).getBoolean("tut_hide", false)) {
            howitworks.setVisibility(View.GONE);
        }
        clear.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                howitworks.setVisibility(View.GONE);
                PreferenceManager.getDefaultSharedPreferences(FragmentHome.this.getContext()).edit().putBoolean("tut_hide", true).apply();
            }
        });
        topCategoryViewAll.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                ((MainActivity) FragmentHome.this.getActivity()).setCurrentItem(1);
            }
        });
        wishlistViewAll.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                FragmentHome.this.startActivity(new Intent(FragmentHome.this.getContext(), WishListActivity.class));
            }
        });
        recenltyViewAll.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                ProductsActivity.setProductsToView(Data.getRecentProducts(FragmentHome.this.getContext()));
                Intent intent = new Intent(activity, ProductsActivity.class);
                intent.putExtra("title", "Recently Viewed");
                FragmentHome.this.startActivity(intent);
            }
        });
        trendingViewAll.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                ProductsActivity.setProductsToView(Data.getProducts());
                Intent intent = new Intent(activity, ProductsActivity.class);
                intent.putExtra("title", "Trending Products");
                FragmentHome.this.startActivity(intent);
            }
        });
        signInButton.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                FragmentHome.this.startActivity(new Intent(FragmentHome.this.getContext(), SignInActivity.class));
            }
        });
        signUpButton.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                FragmentHome.this.startActivity(new Intent(FragmentHome.this.getContext(), SignUpActivity.class));
            }
        });
        return this.rootView;
    }

    /* access modifiers changed from: private */
    public void refreshWishListProducts() {
        ArrayList<Product> products = Data.getWishlist(getContext());
        new LinearLayoutAdapter((LinearLayout) this.rootView.findViewById(R.id.wishlist_LL), new ProductListAdapter((AppCompatActivity) getActivity(), products, ProductListAdapter.GRID), getContext()).createViews();
        if (products.size() > 0) {
            this.wishlistLayout.setVisibility(View.VISIBLE);
        }
    }

    /* access modifiers changed from: private */
    public void refreshRecentProducts() {
        ArrayList<Product> products = Data.getRecentProducts(getContext());
        new LinearLayoutAdapter((LinearLayout) this.rootView.findViewById(R.id.recent), new ProductListAdapter((AppCompatActivity) getActivity(), products, ProductListAdapter.GRID), getContext()).createViews();
        if (products.size() > 0) {
            this.recentlayout.setVisibility(View.VISIBLE);
        }
    }

    /* access modifiers changed from: private */
    public void refreshTrendingProducts() {
        ArrayList<Product> products = Data.getProducts();
        new LinearLayoutAdapter((LinearLayout) this.rootView.findViewById(R.id.trending_products), new ProductListAdapter((AppCompatActivity) getActivity(), products, ProductListAdapter.GRID), getContext()).createViews();
        if (products.size() > 0) {
            this.trendingLayout.setVisibility(View.VISIBLE);
        }
    }

    /* access modifiers changed from: private */
    public void refreshTopCategories() {
        ArrayList<Category> categories = Data.getCategories();
        LinearLayoutAdapter linearadapter = new LinearLayoutAdapter((LinearLayout) this.rootView.findViewById(R.id.top_categories_LL), new CategoriesAdapter(getActivity(), categories, false), getContext());
        linearadapter.setWidth(200.0f);
        linearadapter.createViews();
        if (categories.size() > 0) {
            this.topCategories.setVisibility(View.VISIBLE);
        }
    }

    public void onResume() {
        super.onResume();
        if (this.areProductsFetched) {
            refreshRecentProducts();
            refreshWishListProducts();
        }
    }

    /* access modifiers changed from: private */
    public void createPager() {
        ViewPager pager = this.rootView.findViewById(R.id.pager);
        pager.setAdapter(new BannerAdapter(getChildFragmentManager(), this.offers));
        enableAutoScroll(pager, 1500);
        LinearLayout indicator = this.rootView.findViewById(R.id.indicator);
        this.size = this.offers.size();
        new IndicatorAdapter(this.size, indicator, getActivity(), pager);
    }

    class SliderOffer {
        private String desc;
        private String heading;
        private String title;

        public SliderOffer(Object obj) {
            this.title = "Title";//obj.getString("title");
            this.heading = "Heading"; //obj.getString("heading");
            this.desc = "Description";// obj.getString("description");
        }

        public SliderOffer(String title, String heading, String desc) {
            this.title = title;
            this.heading = heading;
            this.desc = desc;
        }

        public String getTitle() {
            return this.title;
        }

        public void setTitle(String title2) {
            this.title = title2;
        }

        public String getHeading() {
            return this.heading;
        }

        public void setHeading(String heading2) {
            this.heading = heading2;
        }

        public String getDesc() {
            return this.desc;
        }

        public void setDesc(String desc2) {
            this.desc = desc2;
        }
    }

    private void enableAutoScroll(ViewPager pager, int interval) {
        new Timer().scheduleAtFixedRate(new RemindTask(pager), 0, (long) interval);
    }

    /* access modifiers changed from: private */
    public void changeBanner(ViewPager pager) {
        int currentItem = pager.getCurrentItem();
        if (currentItem + 1 == this.size) {
            pager.setCurrentItem(0);
        } else {
            pager.setCurrentItem(currentItem + 1);
        }
    }
}
