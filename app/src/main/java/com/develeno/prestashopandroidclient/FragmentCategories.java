package com.develeno.prestashopandroidclient;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class FragmentCategories extends Fragment {
    /* access modifiers changed from: private */
    public CategoriesAdapter categoriesAdapter;
    /* access modifiers changed from: private */
    public ProgressDialog dialog;
    private RelativeLayout errorLayout;
    private GridView gridView;
    private ProgressBar progressBar;
    private ArrayList<Category> newCategories = new ArrayList<>();

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.frag_categories, container, false);
        this.dialog = new ProgressDialog(getContext());
        this.dialog.setTitle("Loading Data");
        this.dialog.setMessage(Data.getLoadingText());
        this.dialog.setCanceledOnTouchOutside(false);
        this.dialog.show();
        this.gridView = rootView.findViewById(R.id.gridView);
        this.errorLayout = rootView.findViewById(R.id.error_layout);
        this.progressBar = rootView.findViewById(R.id.progressBar);
        if (Data.getCategories().size() == 0) {
            fetchCategories();
        } else {
            displayCategories(Data.getCategories());
        }
        this.errorLayout.findViewById(R.id.button).setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                FragmentCategories.this.fetchCategories();
            }
        });
        return rootView;
    }

    /* access modifiers changed from: private */
    public void fetchCategories() {
        this.progressBar.setVisibility(View.VISIBLE);
        this.errorLayout.setVisibility(View.GONE);
        if (Data.getCategories().size() == 0) {
            WebService.fetchAllCategories(new OnBackgroundTaskCompleted() {
                public void getResult(Object result) {
                    FragmentCategories.this.fetchStockInfos();
                    FragmentCategories.this.displayCategories((ArrayList) result);
                    //createCategoriesInFirebase((ArrayList<Category>) result);
                    if (result != null) {
                        fetchAllCategoriesInFirebase((ArrayList<Category>) result);
                    }
                }

                public void refresh(Object result) {
                }
            });
        } else {
            displayCategories(Data.getCategories());
        }
    }

    private void createCategoriesInFirebase(ArrayList<Category> result) {
        for (Category category : result) {
            Map<String, String> map = new HashMap<>();
            map.put("img_link", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRB28hGVMiQ44C686Q-Dsv7-xdlLjZI8I4KDhY5s2WxeK0wj0E7&s");
            FirebaseFirestore.getInstance().collection("categories")
                    .document(category.getLabel().toLowerCase()).set(map);
        }
    }

    private void fetchAllCategoriesInFirebase(final ArrayList<Category> result) {
        //Fetching categories
        FirebaseFirestore.getInstance().collection("categories")
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (Category category : result) {
                    boolean exists = false;
                    for (DocumentSnapshot snapshot : queryDocumentSnapshots) {
                        if (snapshot.getId().equalsIgnoreCase(category.getLabel())) {
                            exists = true;
                        }
                    }
                    if (!exists) {
                        newCategories.add(category);
                    }
                }

                //Creating new Categories
                createCategoriesInFirebase(newCategories);
            }
        });

    }

    /* access modifiers changed from: private */
    public void displayCategories(ArrayList<Category> result) {
        this.progressBar.setVisibility(View.GONE);
        if (result != null) {
            this.errorLayout.setVisibility(View.GONE);
            Data.setCategories(filterCategories(result));
            this.categoriesAdapter = new CategoriesAdapter(getActivity(), Data.getCategories(), false);
            this.gridView.setAdapter(this.categoriesAdapter);
            this.gridView.setVisibility(View.VISIBLE);
            return;
        }
        this.gridView.setVisibility(View.GONE);
        this.errorLayout.setVisibility(View.VISIBLE);


    }

    private ArrayList<Category> filterCategories(ArrayList<Category> categories) {
        String[] blacklisted = {"http://www.organicdolchi.com/api/categories/1", "http://www.organicdolchi.com/api/categories/2"};
        ArrayList<Category> filteredCategories = new ArrayList<>();
        Iterator it = categories.iterator();
        while (it.hasNext()) {
            Category category = (Category) it.next();
            if (category != null) {
                boolean isBlackListed = false;
                int length = blacklisted.length;
                int i = 0;
                while (true) {
                    if (i >= length) {
                        break;
                    }
                    String categoryURL = category.getURL();
                    if (categoryURL != null && categoryURL.contentEquals(blacklisted[i])) {
                        isBlackListed = true;
                        break;
                    }
                    i++;
                }
                if (!isBlackListed) {
                    filteredCategories.add(category);
                }
            }
        }
        return filteredCategories;
    }

    public void fetchAllProducts() {
        WebService.fetchAllProducts(new OnBackgroundTaskCompleted() {
            public void getResult(Object result) {
                if (result != null) {
                    Data.setProducts((ArrayList) result);
                    FragmentCategories.this.categoriesAdapter.notifyDataSetChanged();
                    Data.getWishlist(FragmentCategories.this.getContext());
                    ((MainActivity) FragmentCategories.this.getActivity()).getListener().getResponse(null);
                    FragmentCategories.this.dialog.dismiss();
                    return;
                }
                FragmentCategories.this.displayCategories(null);
                Toast.makeText(FragmentCategories.this.getContext(), "Failed to load products", Toast.LENGTH_SHORT).show();
            }

            public void refresh(Object result) {
            }
        });
    }

    /* access modifiers changed from: private */
    public void fetchStockInfos() {
        WebService.fetchAllStocksAvailableInfo(new OnBackgroundTaskCompleted() {
            public void getResult(Object result) {
                Data.setStockInfos((ArrayList) result);
                FragmentCategories.this.fetchAllProducts();
            }

            public void refresh(Object result) {
            }
        });
    }
}
